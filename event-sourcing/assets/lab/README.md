# Local event-sourcing lab

A real PostgreSQL event store you can drive from `psql`. It covers append with
optimistic concurrency, immutability, a checkpointed and idempotent projection,
rebuilds, and the **gap problem** (Lesson 08), all reproducible by hand.

```
append_events(stream, expectedVersion, events[])  ──▶  events  (UNIQUE stream_id+stream_version, tx_id xid8)
                                                         │
                         read_all_safe(after) ◀──────────┘  (tx_id < pg_snapshot_xmin)
                                │
                 run_balance_projection()  ──▶  rm_account_balance + projection_checkpoints
```

## Run (Podman)

```sh
podman compose up -d          # start (schema auto-loaded from sql/)
podman compose down -v        # stop and wipe data
psql "postgres://es:es@localhost:55432/eventstore"     # or:
podman exec -it es-lab-postgres psql -U es -d eventstore
```

## Exercises

**1. Append and the concurrency check (Lesson 04)**
```sql
SELECT append_events('account-42', 0, '[{"type":"AccountOpened","data":{"owner":"Asha"}},
                                        {"type":"MoneyDeposited","data":{"amount":100}}]');   -- → 2
SELECT append_events('account-42', 2, '[{"type":"MoneyWithdrawn","data":{"amount":30}}]');    -- → 3
SELECT append_events('account-42', 2, '[{"type":"MoneyWithdrawn","data":{"amount":30}}]');    -- ERROR: concurrency conflict
```

**2. Immutability.** `UPDATE events SET data='{}' WHERE global_position=1;` is rejected by a trigger.
Corrections are new events.

**3. Projection, checkpoint and replay (Lessons 05–06)**
```sql
SELECT run_balance_projection();       -- processes 3 events
SELECT run_balance_projection();       -- 0: checkpoint persisted, re-run is safe
SELECT * FROM rm_account_balance;      -- balance 70
SELECT rebuild_balance_projection(); SELECT run_balance_projection();   -- same answer from history
```

**4. The gap problem (Lesson 08).** You need two terminals.
```sql
-- Terminal A                                   -- Terminal B
BEGIN;
SELECT append_events('account-7',0,
  '[{"type":"AccountOpened","data":{}}]');      -- gets position N, NOT committed
                                                SELECT append_events('account-8',0,
                                                  '[{"type":"AccountOpened","data":{}}]');  -- position N+1, committed
                                                SELECT global_position FROM events WHERE global_position > N-1;
                                                --   ↑ naive poller sees only N+1 → would checkpoint past N forever
                                                SELECT global_position FROM read_all_safe(N-1);
                                                --   ↑ empty: waits until A finishes
COMMIT;
                                                SELECT global_position FROM read_all_safe(N-1);  -- N, N+1
```

## Where to go next

- Port the Java decider from Lesson 03 to call `append_events` with JDBC, and retry on SQLSTATE `40001`/`23505`.
- Add a `snapshots` writer every 100 events (Lesson 07) and measure load time.
- Publish from `read_all_safe` to Kafka as an outbox relay (Lesson 09).

> The image is `postgres:16`. Pin a digest for anything real.
