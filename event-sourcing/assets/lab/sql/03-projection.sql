-- ===========================================================================
-- A projection written in SQL (Lesson 05): account balances.
-- Idempotent (last_position guard) and checkpointed in the same transaction.
-- ===========================================================================
CREATE TABLE rm_account_balance (
    account_id     TEXT PRIMARY KEY,
    owner          TEXT,
    balance        NUMERIC NOT NULL DEFAULT 0,
    status         TEXT    NOT NULL DEFAULT 'OPEN',
    last_position  BIGINT  NOT NULL
);

-- Process one batch from the checkpoint. Call repeatedly (it is safe to re-run).
CREATE FUNCTION run_balance_projection(p_batch INT DEFAULT 500)
RETURNS INT LANGUAGE plpgsql AS $$
DECLARE
  v_cp  BIGINT;
  r     events;
  n     INT := 0;
  v_acc TEXT;
BEGIN
  INSERT INTO projection_checkpoints VALUES ('account-balance-v1', 0) ON CONFLICT DO NOTHING;
  SELECT position INTO v_cp FROM projection_checkpoints WHERE name = 'account-balance-v1' FOR UPDATE;

  FOR r IN SELECT * FROM read_all_safe(v_cp, p_batch) LOOP
    IF r.stream_id LIKE 'account-%' THEN
      v_acc := substring(r.stream_id from 9);
      CASE r.event_type
        WHEN 'AccountOpened' THEN
          INSERT INTO rm_account_balance (account_id, owner, last_position)
          VALUES (v_acc, r.data->>'owner', r.global_position)
          ON CONFLICT (account_id) DO NOTHING;
        WHEN 'MoneyDeposited' THEN
          UPDATE rm_account_balance SET balance = balance + (r.data->>'amount')::numeric, last_position = r.global_position
           WHERE account_id = v_acc AND last_position < r.global_position;
        WHEN 'MoneyWithdrawn' THEN
          UPDATE rm_account_balance SET balance = balance - (r.data->>'amount')::numeric, last_position = r.global_position
           WHERE account_id = v_acc AND last_position < r.global_position;
        WHEN 'AccountClosed' THEN
          UPDATE rm_account_balance SET status = 'CLOSED', last_position = r.global_position
           WHERE account_id = v_acc AND last_position < r.global_position;
        ELSE NULL;
      END CASE;
    END IF;
    v_cp := r.global_position;
    n := n + 1;
  END LOOP;

  UPDATE projection_checkpoints SET position = v_cp WHERE name = 'account-balance-v1';
  RETURN n;
END $$;

-- Rebuild from zero (Lesson 06). In production you'd build a v2 table side-by-side and swap.
CREATE FUNCTION rebuild_balance_projection() RETURNS VOID LANGUAGE plpgsql AS $$
BEGIN
  TRUNCATE rm_account_balance;
  UPDATE projection_checkpoints SET position = 0 WHERE name = 'account-balance-v1';
END $$;
