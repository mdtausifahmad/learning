-- ===========================================================================
-- append_events: atomic append with optimistic concurrency (Lesson 04).
--   expected_version = 0  → stream must not exist (NoStream)
--   expected_version = n  → stream must be exactly at n
--   expected_version = -1 → any (no check; use sparingly)
-- events: jsonb array of {"type": "...", "data": {...}, "metadata": {...}, "eventId": "uuid"?}
-- Returns the new stream version.
-- ===========================================================================
CREATE FUNCTION append_events(p_stream TEXT, p_expected BIGINT, p_events JSONB)
RETURNS BIGINT LANGUAGE plpgsql AS $$
DECLARE
  v_current BIGINT;
  v_next    BIGINT;
  e         JSONB;
BEGIN
  SELECT COALESCE(MAX(stream_version), 0) INTO v_current FROM events WHERE stream_id = p_stream;

  IF p_expected <> -1 AND v_current <> p_expected THEN
    RAISE EXCEPTION 'concurrency conflict on %: expected %, actual %', p_stream, p_expected, v_current
      USING ERRCODE = '40001';
  END IF;

  v_next := v_current;
  FOR e IN SELECT * FROM jsonb_array_elements(p_events) LOOP
    v_next := v_next + 1;
    INSERT INTO events (event_id, stream_id, stream_version, event_type, data, metadata)
    VALUES (COALESCE((e->>'eventId')::uuid, gen_random_uuid()), p_stream, v_next,
            e->>'type', e->'data', COALESCE(e->'metadata', '{}'));
  END LOOP;
  RETURN v_next;
  -- Note: two racing callers can both pass the SELECT check; the UNIQUE(stream_id, stream_version)
  -- constraint is what guarantees only one wins (the other gets 23505 unique_violation).
END $$;

-- ===========================================================================
-- read_all_safe: gap-safe global read for subscriptions (Lesson 08).
-- Only returns rows whose writing transaction is older than every in-flight
-- transaction, so a lower position can never become visible "later".
-- ===========================================================================
CREATE FUNCTION read_all_safe(p_after BIGINT, p_limit INT DEFAULT 500)
RETURNS SETOF events LANGUAGE sql STABLE AS $$
  SELECT * FROM events
   WHERE global_position > p_after
     AND tx_id < pg_snapshot_xmin(pg_current_snapshot())
   ORDER BY global_position
   LIMIT p_limit;
$$;
