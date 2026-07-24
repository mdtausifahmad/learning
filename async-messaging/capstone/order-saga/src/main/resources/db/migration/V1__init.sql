-- Capstone schema. Flyway owns this; JPA is set to validate against it.
-- Tables map 1:1 to the entities in the Kotlin scaffold.

-- ------- Order service (Milestone 1) -------
CREATE TABLE orders (
    id           VARCHAR(64) PRIMARY KEY,       -- orderId (also the Kafka message key → per-order ordering, L09)
    customer_id  VARCHAR(64)     NOT NULL,
    amount       NUMERIC(12, 2)  NOT NULL,
    status       VARCHAR(16)     NOT NULL,       -- PENDING | CONFIRMED | CANCELLED
    created_at   TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ     NOT NULL DEFAULT now()
);

-- Transactional outbox (Milestone 1 / Lesson 14): written in the SAME tx as the business change.
CREATE TABLE outbox (
    id         UUID PRIMARY KEY,
    topic      VARCHAR(128)  NOT NULL,
    msg_key    VARCHAR(128)  NOT NULL,           -- the Kafka key (orderId)
    type       VARCHAR(256)  NOT NULL,           -- payload class name
    payload    TEXT          NOT NULL,           -- JSON string (kept TEXT for simple JPA mapping)
    created_at TIMESTAMPTZ   NOT NULL DEFAULT now(),
    sent       BOOLEAN       NOT NULL DEFAULT FALSE,
    sent_at    TIMESTAMPTZ
);
CREATE INDEX idx_outbox_unsent ON outbox (sent, created_at) WHERE sent = FALSE;

-- ------- Idempotent consumers (Milestone 2 / Lesson 08) -------
-- One row per (consumer, messageId). The PK is the dedup backstop.
CREATE TABLE processed_messages (
    consumer    VARCHAR(64)  NOT NULL,
    message_id  VARCHAR(128) NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (consumer, message_id)
);

-- ------- Inventory service (Milestone 2 / 4) -------
CREATE TABLE inventory_item (
    sku        VARCHAR(64) PRIMARY KEY,
    available  INTEGER NOT NULL,
    reserved   INTEGER NOT NULL DEFAULT 0
);
-- seed one SKU so the happy path works out of the box
INSERT INTO inventory_item (sku, available, reserved) VALUES ('SKU-1', 100, 0);

-- ------- Payment service (Milestone 3 / 4) -------
CREATE TABLE payment (
    order_id  VARCHAR(64) PRIMARY KEY,
    amount    NUMERIC(12, 2) NOT NULL,
    status    VARCHAR(16)    NOT NULL             -- CHARGED | REFUNDED | FAILED
);

-- ------- Saga orchestrator state (Milestone 3 / 4 / Lesson 15) -------
CREATE TABLE saga_instance (
    order_id   VARCHAR(64) PRIMARY KEY,
    state      VARCHAR(32) NOT NULL,              -- STARTED | INVENTORY_RESERVED | COMPLETED | COMPENSATING | CANCELLED
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
