# Event Sourcing — Glossary

The canonical language for this workspace. Lessons use these terms exactly.

## Terms

**Event**:
An immutable fact that something happened in the domain, named in the past tense (`MoneyDeposited`).
Once appended it is never updated or deleted.
_Avoid_: Message, notification (those are transport concepts; an event is a domain fact)

**Command**:
A request to change state (`WithdrawMoney`). It can be rejected. If accepted, it produces zero or more events.
_Avoid_: Event (commands are imperative and may fail; events are past and certain)

**Stream**:
The ordered sequence of events for one consistency boundary (usually one aggregate instance), e.g. `account-42`.
_Avoid_: Topic (a Kafka topic holds many entities and is not a per-entity consistency boundary)

**Event store**:
An append-only database of streams. It supports append-with-expected-version, read-stream and read-all-by-position.
_Avoid_: Event bus, message broker

**Aggregate / Decider**:
The consistency boundary that decides whether a command is valid against current state. The **decider**
is its functional form: `decide(command, state) → events`, `evolve(state, event) → state`, `initialState`.
_Avoid_: Entity, model (too generic)

**Rehydration (fold)**:
Rebuilding current state by applying a stream's events in order to the initial state.
_Avoid_: Replay (keep "replay" for re-running projections over the log)

**Optimistic concurrency (expected version)**:
An append succeeds only if the stream is still at the version the writer read. Otherwise it is
rejected and the writer retries. This is the core guard against lost updates.
_Avoid_: Locking (no lock is held between read and write)

**Projection**:
A function that folds events into a **read model** shaped for a query. It is disposable and rebuildable from the log.
_Avoid_: View (a view is one kind of read model; a projection is the process that builds it)

**Read model**:
A query-optimised representation of data built by a projection. It is eventually consistent with the write side.

**CQRS**:
Command Query Responsibility Segregation: separate models for writes (decisions) and reads (queries).
Often paired with event sourcing, but it is independent of it.
_Avoid_: Event sourcing (CQRS doesn't require ES, and ES doesn't strictly require CQRS)

**Checkpoint**:
The last global position a subscription or projection has durably processed.

**Global position**:
A monotonically increasing number ordering every event in the store, used by read-all subscriptions.

**Gap problem**:
With sequence-based positions, a transaction can commit a *lower* position after a *higher* one is
visible. A naive poller then checkpoints past it and skips the event forever.

**Snapshot**:
A cached state of a stream at a version, used to speed up rehydration. It is an optimisation, never the source of truth.

**Closing the books**:
Ending a long stream with a summary event and starting a new stream for the next period. This is a
modelling fix for long streams, unlike a snapshot.

**Domain event vs integration event**:
A domain event is internal and fine-grained, and can change freely. An integration event is a public,
versioned contract published to other services.
_Avoid_: Publishing your domain events directly as your API

**Outbox**:
Writing state and outgoing messages atomically in one DB transaction, with a relay publishing later.
In ES, the event store itself acts as the outbox.

**Saga / Process manager**:
Coordination of a long-running, multi-aggregate or multi-service process through events and
commands, with compensation in place of distributed transactions.

**Upcasting**:
Transforming an old event schema version into the current one at read time, so that domain code
only ever sees the latest version.

**Crypto-shredding**:
Encrypting personal data with a per-subject key and deleting the key to "forget" the subject, while
the immutable log stays intact.

**Bi-temporal**:
Tracking both *occurred_at* (when it happened in the world) and *recorded_at* (when we learned of it).

**Dynamic Consistency Boundary (DCB)**:
An approach in which an append is conditioned on a *query* over event tags rather than on a single
stream's version. It relaxes rigid aggregate boundaries.

**Event Storming**:
A collaborative workshop that maps a domain as a timeline of events, commands, policies and read models.
