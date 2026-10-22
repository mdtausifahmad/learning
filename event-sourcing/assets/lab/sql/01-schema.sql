-- ===========================================================================
-- Event store schema (Lesson 04). PostgreSQL 13+ (uses xid8).
-- ===========================================================================
CREATE TABLE events (
    global_position  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id         UUID        NOT NULL UNIQUE,                 -- idempotency
    stream_id        TEXT        NOT NULL,
    stream_version   BIGINT      NOT NULL,                        -- 1, 2, 3 … per stream
    event_type       TEXT        NOT NULL,
    schema_version   INT         NOT NULL DEFAULT 1,
    data             JSONB       NOT NULL,
    metadata         JSONB       NOT NULL DEFAULT '{}',
    occurred_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    recorded_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    tx_id            XID8        NOT NULL DEFAULT pg_current_xact_id(),  -- gap-safe reads (Lesson 08)
    CONSTRAINT uq_stream_version UNIQUE (stream_id, stream_version)      -- optimistic concurrency
);

CREATE INDEX ix_events_tx ON events (tx_id);

CREATE TABLE snapshots (
    stream_id       TEXT PRIMARY KEY,
    stream_version  BIGINT NOT NULL,
    schema_version  INT    NOT NULL,
    state           JSONB  NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE projection_checkpoints (
    name      TEXT PRIMARY KEY,
    position  BIGINT NOT NULL
);

-- Events are immutable: block UPDATE/DELETE even for the owner.
CREATE FUNCTION forbid_mutation() RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
  RAISE EXCEPTION 'events are immutable (append a compensating event instead)';
END $$;

CREATE TRIGGER events_immutable
  BEFORE UPDATE OR DELETE ON events
  FOR EACH ROW EXECUTE FUNCTION forbid_mutation();
