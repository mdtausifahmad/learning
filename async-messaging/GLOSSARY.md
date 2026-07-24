# Asynchronism & Messaging Glossary

The canonical language for this workspace. Every lesson adheres to these terms. The glossary
grows as you *demonstrate* understanding — it is a record of compressed knowledge, not a
dictionary to pre-read. (Terms below are the bedrock nouns Lesson 01 defines precisely.)

## Terms

**Synchronous**:
A call in which the sender waits, holding its thread, until the receiver replies. Sender and
receiver are bound together in time.
_Avoid_: Blocking call (use for the mechanism, not the interaction style)

**Asynchronous**:
A call in which the sender hands off a message and continues immediately, without waiting for the
receiver to process it.
_Avoid_: Non-blocking, background (too vague)

**Temporal coupling**:
The requirement that sender and receiver both be healthy at the *same instant* for an interaction
to succeed. Asynchronous messaging removes it.
_Avoid_: Time dependency, tight coupling (reserve "tight coupling" for design-time coupling)

**Message**:
A self-contained packet of data passed from a producer to a consumer through a broker. The unit
that travels through a messaging system.
_Avoid_: Event, command (these are *kinds* of message — distinguished in a later lesson)

**Message broker**:
Infrastructure whose job is to durably store messages and deliver them to consumers when they are
ready (e.g. Kafka, RabbitMQ, SQS). The buffer that decouples producer from consumer.
_Avoid_: Queue, server, bus (a queue is one structure a broker offers; "bus" is overloaded)

**Producer**:
The component that creates and publishes a message to the broker.
_Avoid_: Sender, publisher (acceptable aliases; prefer "producer" for consistency)

**Consumer**:
The component that receives and processes a message from the broker.
_Avoid_: Receiver, subscriber, worker (prefer "consumer"; "subscriber" is reserved for pub/sub)

## Channels & delivery topology

**Queue (point-to-point channel)**:
A channel that delivers each message to **exactly one** consumer. The basis for distributing work.
_Avoid_: Line, buffer (buffer is the broker's role generally, not this specific channel type)

**Topic (publish-subscribe channel)**:
A channel that delivers a **copy of each message to every** subscriber. The basis for fan-out.
_Avoid_: Feed, stream (in this workspace "stream" means a log-structured channel, discussed later)

**Competing consumers**:
Multiple consumers reading one queue so each message is handled by exactly one of them; the way you
scale point-to-point throughput. Only works on a queue — extra consumers on a topic just duplicate.
_Avoid_: Load-balanced consumers, worker pool

**Fan-out**:
Delivering each message to multiple independent consumers, each getting its own copy (publish-subscribe).
The deliberate opposite of competing consumers.
_Avoid_: Broadcast, multicast (network terms; reserve for the wire, not the messaging pattern)

**Consumer group**:
A named set of consumers that jointly consume a topic as competing consumers — collectively the group
sees each message once. Multiple groups on one topic gives fan-out. (Kafka / Redis / Pulsar term.)
_Avoid_: Consumer pool, cluster

**Partition**:
A sub-log of a Kafka topic, itself a point-to-point channel. Partitions are the unit of **both**
parallelism (one active consumer per partition per group) **and** ordering (order holds within a
partition, chosen by the message key).
_Avoid_: Shard, slice (reserve "shard" for database partitioning)

**Message key**:
The value a producer attaches to a message that determines its partition via `hash(key) %
partitionCount`. Same key → same partition → strict per-key order; no key → round-robin (no order).
Choose it as the smallest entity whose events must stay ordered.
_Avoid_: Partition key (acceptable alias), routing key (reserve for RabbitMQ)

**Ordering unit**:
The scope within which a broker guarantees order — a Kafka partition, an SQS `MessageGroupId`, a
Pulsar `Key_Shared` key. There is no order across units, and parallelism trades off against how
coarse the unit is.
_Avoid_: Order group

## Flow control

**Load leveling**:
Using a durable buffer (queue/log) to decouple the producer's arrival rate from the consumer's
processing rate, so bursts become queue depth instead of a stampede on the consumer. Size consumers
for the sustained rate; let the buffer absorb peaks.
_Avoid_: Buffering (too generic), throttling

**Backpressure**:
The mechanism by which a slow consumer avoids being overwhelmed — automatic in pull systems (the
consumer fetches at its own pace and simply lags), explicit in push systems (a prefetch limit caps
in-flight messages). Good backpressure propagates upstream to a polite rejection.
_Avoid_: Rate limiting (a related but distinct producer-side control)

**Prefetch (basic.qos)**:
RabbitMQ's per-consumer cap on unacknowledged messages the broker will push before it waits for acks
— the primary push-based backpressure lever. A value of 0 means infinite (unbounded, dangerous).
_Avoid_: Batch size (that's a pull-side concept)

**Load shedding**:
Deliberately rejecting or dropping work the system can't handle (e.g. a bounded queue returning HTTP
503 so clients back off), rather than buffering it unbounded until collapse.
_Avoid_: Throttling, dropping

## Failure handling

**Poison message**:
A message that consistently fails processing (bad data, a permanent condition, or a bug). Retried in
place it causes head-of-line blocking; the remedy is bounded retries then a dead-letter queue.
_Avoid_: Bad message, corrupt message

**Head-of-line blocking**:
When one un-processable message at the front of a partition/queue stalls every message behind it,
because in-order processing can't move past it.
_Avoid_: Queue stall

**Dead-letter queue (DLQ)**:
A separate channel where messages that can't be processed (retries exhausted, or immediately
non-retriable) are parked for inspection, alerting, and replay. Unblocks the main flow. (EIP: Dead
Letter Channel.)
_Avoid_: Error queue, reject queue

**Backoff (retry)**:
Increasing the delay between retries — usually exponential (0.5s, 1s, 2s…) plus random jitter to avoid
a synchronized thundering herd on a recovering dependency.
_Avoid_: Retry delay

**Redrive**:
Moving messages out of a DLQ back to a source queue for reprocessing (an SQS term). The "replay" step
of DLQ triage.
_Avoid_: Reprocess, retry (reserve those for the pre-DLQ path)

## Broker models

**Log-based broker**:
A broker that appends messages to an immutable, ordered, retained log; consumers track their own
offset and reading does not delete (Kafka, Pulsar). Enables replay, free fan-out, and intrinsic
per-partition order.
_Avoid_: Stream broker (too loose), event store (a related but distinct pattern)

**Queue-based broker**:
A broker that holds pending messages and deletes each one once acknowledged; the broker tracks
per-message delivery state and redelivers unacked messages (RabbitMQ, SQS, JMS/AMQP). Enables
per-message priority/TTL and native DLQs, but not replay.
_Avoid_: Message queue (fine as an alias, but reserve "queue" for the channel type from L02)

**Retention**:
The policy governing how long a log keeps messages (by time or size), independent of consumption —
the property that makes replay possible. Contrast with a queue, where messages live only until acked.
_Avoid_: Expiry (reserve for per-message TTL)

**Log compaction**:
A Kafka retention mode (`cleanup.policy=compact`) that keeps only the latest message per key, turning
a topic into a durable "current state" changelog.
_Avoid_: Deduplication (a different concern — see L08)

## Coordination

**Choreography**:
Coordinating a multi-service flow by having each service publish domain events that trigger reactions
in others — no central coordinator; the end-to-end flow is emergent and implicit.
_Avoid_: Pub/sub (that's the delivery mechanism, not the coordination style)

**Orchestration**:
Coordinating a multi-service flow with a central orchestrator that sends commands and awaits replies,
holding the flow's state and deciding each next step. The flow is explicit in one place.
_Avoid_: Workflow (the engine/tool; orchestration is the style)

**Flow-visibility trap**:
The risk that a choreographed flow, having no explicit representation in code, can only be understood
by observing a live system — making it hard to see, debug, or change. (From Martin Fowler.)
_Avoid_: Spaghetti events

## Reliable publishing

**Dual-write problem**:
The impossibility of atomically updating a database and publishing a message to a broker, because they
are separate systems with no shared transaction — a crash between them loses the event or (reverse
order) publishes a phantom event.
_Avoid_: Two-phase write

**Transactional outbox**:
A pattern that avoids the dual write by inserting the outgoing message into an OUTBOX table in the
same local transaction as the business change, so both commit atomically; a relay publishes it later.
The event is sent iff the transaction commits.
_Avoid_: Event table (too generic)

**Message relay**:
The separate process that reads the outbox and publishes its messages to the broker — either by
**polling** the table or by **transaction log tailing** (change-data-capture, e.g. Debezium). It is
at-least-once, so consumers must be idempotent.
_Avoid_: Publisher (ambiguous with the producer)

**Change data capture (CDC)**:
Streaming a database's committed changes by tailing its transaction log (WAL/binlog) — the low-latency
way to relay an outbox (Debezium). Preserves order because the log is ordered.
_Avoid_: Log tailing (acceptable alias), replication

## Sagas

**Saga**:
A distributed transaction implemented as a sequence of local transactions across services, each
publishing a message that triggers the next; on failure, compensating transactions undo the completed
steps. Trades ACID guarantees for cross-service consistency without 2PC.
_Avoid_: Distributed transaction (a saga is the alternative to one)

**Compensating transaction**:
A new local transaction that semantically undoes a previously committed step of a saga
(ReserveInventory → ReleaseInventory). Must be reliable and idempotent; there is no automatic rollback.
_Avoid_: Rollback (a committed step can't be rolled back — only compensated)

**Semantic lock**:
A saga countermeasure for the lack of isolation: a state flag (e.g. `PENDING`) marking a record as
in-progress so concurrent sagas/readers treat it carefully until the saga completes.
_Avoid_: Lock (implies a real DB lock, which a saga deliberately avoids)

**Pivot transaction**:
The commit point of a saga: steps before it are compensatable (can be undone), the pivot makes the
saga's outcome final, and steps after it are retriable (must eventually succeed).
_Avoid_: Commit point (acceptable description, but "pivot transaction" is the term)

## Schema evolution

**Schema evolution**:
Changing an event's schema over time while keeping producers and consumers on different versions
interoperable. Required because deploys are independent and logs retain old events.
_Avoid_: Versioning (that's one tactic within evolution)

**Backward / forward / full compatibility**:
*Backward* = a new schema can read old data (upgrade consumers first; the default). *Forward* = an old
schema can read new data (upgrade producers first). *Full* = both. *Transitive* = holds against all
prior versions, not just the last.
_Avoid_: Compatible (unqualified — always say which direction)

**Schema registry**:
A service that stores schemas per subject, enforces the chosen compatibility rule at registration
(rejecting breaking schemas), and lets messages carry a small schema ID instead of the full schema.
_Avoid_: Schema store

**Tolerant reader**:
A consumer that ignores fields it doesn't recognize and doesn't over-validate (Postel's law) — which
grants forward compatibility for free.
_Avoid_: Lenient parser

## Event sourcing & CQRS

**Event sourcing**:
Persisting an entity's state as the append-only sequence of state-changing events, and deriving current
state by replaying them. The event log is the source of truth; saving an event is atomic (no dual write).
_Avoid_: Event logging (logging is observability; this is the system of record)

**Projection**:
An event handler that consumes an event stream and maintains a derived, query-optimized read model
(a view, index, or cache). The mechanism behind CQRS read models.
_Avoid_: View (a projection produces a view; they aren't the same)

**CQRS (Command Query Responsibility Segregation)**:
Using a different model to update information (command side) than to read it (query side), often with
separate stores kept eventually consistent. Fowler: valuable for complex domains, "risky complexity"
for most.
_Avoid_: Read/write split (too vague)

**Snapshot**:
A periodically saved copy of an event-sourced entity's current state, so rebuilding replays only the
events since the snapshot rather than from the beginning.
_Avoid_: Checkpoint (reserve for consumer offset/stream-processing checkpoints)

## Observability

**Context propagation**:
Carrying a trace context (trace id + span id) across service boundaries — through message headers for
async hops — so a consumer's work becomes a child span of the producer's and the whole flow forms one
trace. OpenTelemetry's default uses the W3C `traceparent` header.
_Avoid_: Tracing (that's the outcome; propagation is the mechanism)

**Correlation id**:
A single identifier attached to a message (and its logs and spans) that lets you follow one logical
flow across every service and hop. Often the trace id.
_Avoid_: Request id (reserve for a single synchronous request)

**Consumer lag**:
The gap between the latest offset in a partition and a consumer group's committed offset — the primary
health metric of an async system. Rising lag means the consumer is behind (or blocked by a hung
transaction). Alert and autoscale on it.
_Avoid_: Backlog (informal), delay

## Message intent

**Command**:
A message meaning "do this" — a directed request to perform an action, named imperatively
(`ShipOrder`). Has one logical owner; the sender expects it to be carried out. Couples the sender to
the receiver's capability. Typically sent to a queue.
_Avoid_: Request (reserve "request" for the synchronous request/reply of a query)

**Event**:
A message meaning "this happened" — a notification of a past fact, named in the past tense
(`OrderPlaced`). The publisher is indifferent to who (if anyone) reacts; each consumer decides its
own reaction. Typically published to a topic. The basis of extensible, loosely coupled systems.
_Avoid_: Notification (too generic), signal

**Query**:
A message meaning "tell me this" — a request for information that expects a reply, named
interrogatively (`GetOrderStatus`). Almost always synchronous request/reply; a poor fit for
fire-and-forget messaging.
_Avoid_: Lookup, fetch

**Passive-aggressive command** _(anti-pattern)_:
An event styled to look like a fact but which actually exists to make one specific consumer act
(the producer depends on the action). Hidden coupling with ambiguous ownership — name it as a
command instead. (Term from Martin Fowler.)

## Kafka consumer group internals

**Group coordinator**:
A broker that manages one consumer group's membership and offset commits. Chosen by
`hash(group.id) % __consumer_offsets partition count`. It tracks membership and relays assignments —
it does **not** compute them.
_Avoid_: Controller, master (the cluster controller is a different role)

**Group leader**:
One consumer, elected by the coordinator (usually the first to join), that computes the
partition→member assignment **client-side** using the configured strategy, then shares it via
`SyncGroup`.
_Avoid_: Coordinator (distinct role — the leader is a client, the coordinator is a broker)

**Partition assignment strategy**:
The pluggable algorithm the group leader runs to map partitions to members — e.g. `RangeAssignor`
(default; enables co-partitioned joins), `RoundRobinAssignor`, `StickyAssignor`,
`CooperativeStickyAssignor` (incremental rebalancing).
_Avoid_: Partitioner (the producer-side partitioner that picks a partition from a key is a different thing)

**Rebalance**:
The process of redistributing partitions across a group when membership changes. **Eager**
(stop-the-world) revokes all partitions then reassigns; **incremental cooperative** moves only the
partitions that must change. Triggered by joins/leaves or a missed `session.timeout.ms` heartbeat.
_Avoid_: Reassignment (reserve for broker-side partition-replica reassignment)

## Delivery guarantees

**At-most-once**:
A delivery semantic where each message is processed zero or one time — the offset/ack is committed
**before** the work, so a crash in the gap skips it. No duplicates, possible loss.
_Avoid_: Fire-and-forget (that's one way to get it, not the guarantee itself)

**At-least-once**:
A delivery semantic where each message is processed one or more times — the work happens **before**
the commit, so a crash in the gap causes redelivery. No loss, possible duplicates. The common default.
_Avoid_: Guaranteed delivery (too vague; doesn't say what happens on retry)

**Exactly-once / effectively-once**:
The outcome where each message affects the system once despite retries. Exactly-once *delivery* is
essentially impossible; exactly-once *processing* is achieved via at-least-once + idempotency, or
(inside Kafka) idempotent producer + transactions. Requires messaging-system + application cooperation.
_Avoid_: Exactly-once delivery (misleading — it's a processing property, not a wire guarantee)

**Idempotent consumer**:
A consumer whose effect of processing the same message repeatedly equals processing it once —
achieved by recording processed message ids (a `PROCESSED_MESSAGES` table) or by a naturally
idempotent side effect (upsert / set). The general route to effectively-once. (Pattern from C. Richardson.)
_Avoid_: Dedup consumer

**Idempotent producer**:
A Kafka producer that gets a producer id (PID) + per-partition sequence numbers so the broker drops
retried duplicate writes. Enabled with `enable.idempotence=true`, `acks=all`.
_Avoid_: Exactly-once producer (it only dedupes producer retries, not end-to-end)

## Idempotency & deduplication

**Idempotent operation**:
An operation whose repeated application has the same effect as a single application (e.g. `SET
status='PAID'`, upsert by id, delete by id). The opposite — increment, insert, send, charge — needs
protection.
_Avoid_: Safe operation (too vague)

**Natural vs synthetic idempotency**:
*Natural* = the write is replay-safe by construction (assignment/upsert/delete). *Synthetic* = a
non-idempotent side effect made safe by a dedup key + store. Always prefer natural first.
_Avoid_: Built-in idempotency

**Idempotency key**:
A stable identifier for a logical action, generated once by the producer (typically a V4 UUID) and
carried in the message, used to detect and skip duplicates — and passed to external APIs via an
`Idempotency-Key` header. Never the broker's offset/delivery id (unstable across retries).
_Avoid_: Message id (ambiguous with the broker's wire id), dedup token

**Dedup store**:
Where processed idempotency keys are recorded (e.g. a `PROCESSED_MESSAGES` table, or Redis) so repeats
can be detected and skipped. The record must commit atomically with the side effect, guarded by a
unique constraint.
_Avoid_: Cache (implies eviction/optional)

**Dedup window**:
The bounded time or size for which processed keys are retained. Must exceed the worst-case redelivery
gap (retries + DLQ replay + lag + retention) or late duplicates get reprocessed. Broker-native dedup
windows are short (SQS FIFO = 5 min; Stripe prunes keys at 24 h).
_Avoid_: TTL (use only for the concrete mechanism)

## Kafka exactly-once internals

**Producer ID (PID)**:
A broker-assigned identifier for a producer session. Combined with a per-partition sequence number,
it lets the broker dedupe retried writes.
_Avoid_: Producer key, client id (client.id is a separate, user-set label)

**Sequence number (idempotence)**:
A monotonically increasing number a producer attaches to each batch, per partition. The leader
tracks the highest accepted value per `(PID, partition)`; a repeat is dropped (still acked), a gap
raises `OutOfOrderSequenceException`. Persisted to the replicated log, so it survives failover.
_Avoid_: Offset (the offset is the broker's log position, assigned on append — different thing)

**Producer epoch / zombie fencing**:
A counter bumped when a `transactional.id` re-registers. Writes from an older-epoch producer with the
same id (a "zombie") are rejected, preventing duplicate output from a replaced instance.
_Avoid_: Generation (reserve for the consumer-group generation)

**Transaction marker (control message)**:
A commit or abort record the transaction coordinator writes into each partition a transaction touched.
Consumers use markers to decide whether transactional messages are delivered or filtered.
_Avoid_: Commit record (ambiguous with offset commits)

**Last Stable Offset (LSO)**:
The read horizon for a `read_committed` consumer — the offset just before the first still-open
transaction. The consumer never delivers messages past the LSO, so it never reads data that could
still be aborted.
_Avoid_: High-water mark (the HWM is the replicated-offset boundary; the LSO is about transactions)

**Isolation level**:
A consumer setting controlling transactional visibility. `read_committed` delivers only committed
transactional (and non-transactional) messages up to the LSO; `read_uncommitted` (default) delivers
everything immediately, including soon-to-be-aborted messages.
_Avoid_: Read mode

**transactional.id**:
A stable, user-assigned identifier that gives a transactional producer continuity across restarts and
enables zombie fencing. Must be stable across restarts and never shared by two concurrently-active
producers. Modern rule (EOSMode.V2 / KIP-447): a `transactionIdPrefix` unique per app instance; the
old rule was one id per input partition. Each id hashes to exactly one transaction coordinator.
_Avoid_: Producer id (the PID is broker-assigned and internal; transactional.id is user-set)

**transaction.timeout.ms**:
The producer-side limit on how long a transaction may stay open before the coordinator aborts it
(capped by the broker's `transaction.max.timeout.ms`). The safety net that clears a hung transaction —
and, until it fires, the cause of climbing `read_committed` consumer lag.
_Avoid_: Session timeout (that's consumer-group liveness, a different timer)
