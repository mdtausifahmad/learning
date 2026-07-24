# Mission: Asynchronism & Messaging in Distributed Systems

## Why
A mid-level engineer who has only built **synchronous request/response** systems wants to
master asynchronism and messaging end-to-end — so they can confidently design, build, and
operate real-world event-driven systems at work, and grow the design judgment expected of a
**staff engineer**. The goal is durable, practical mastery ("the ins and outs"), not exam trivia.

## Success looks like
- Justify **sync vs async** for a given problem in a design review, and defend the trade-off.
- Design a full message flow (producer → broker → consumer) with correct **delivery, ordering,
  and failure handling**.
- Reason precisely about **delivery guarantees** (at-most / at-least / exactly-once),
  **idempotency**, **ordering**, and **backpressure**.
- Choose the right tool — **Kafka, RabbitMQ, SQS/SNS, Redis Streams, Pulsar** — for a problem
  and explain why.
- Handle failure: **retries, dead-letter queues, poison messages, duplicate delivery**.
- Apply staff-level patterns — **outbox, saga, event sourcing / CQRS** — and know when *not* to.

## Constraints
- ~3–5 hours/week. Lessons must be short and self-contained (small working-memory budget).
- Learns best through **real-world application** — bias every lesson toward practical use and
  concrete trade-offs over pure theory.
- Starting point: comfortable building services, but **synchronous-only** in practice.

## Out of scope (for now)
- Building a broker from scratch.
- Deep networking/protocol internals (TCP flow control, etc.) beyond what a user needs.
- Consensus deep-dives (Raft/Paxos) — revisit only where they intersect Kafka replication at
  the staff tier.
