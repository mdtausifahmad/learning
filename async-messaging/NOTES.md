# Notes & Preferences

## Learner profile
- Mid-level engineer; comfortable building services, **synchronous-only** in practice so far.
- Has read a little about async but not applied it.
- Goal orientation: **practical, real-world application** and full "ins and outs" — long arc
  from fundamentals to **staff-level** design judgment.

## Teaching preferences
- **Code stack (IMPORTANT):** ALL code examples in **Kotlin + Spring Boot**. This is their daily
  stack — use idiomatic Spring (constructor injection, `@Service`, `KafkaTemplate`,
  `@KafkaListener`, `RabbitTemplate`, `spring-cloud-aws` for SQS, etc.). No Python/JS/Java samples.
- **Medium time budget** (3–5 hrs/week) → keep lessons short, one win each.
- Bias toward **concrete trade-offs and real systems** over abstract theory.
- Use their broker stack for examples: **Kafka, RabbitMQ, SQS/SNS, Redis Streams, Pulsar**.
- Open to **joining communities** for real-world wisdom (see RESOURCES.md).

## Working notes
- Curriculum arc (rough map — teach one tightly-scoped lesson at a time, in ZPD):
  0001 ✅ Why async? sync vs async & the problems it solves
  0002 ✅ Point-to-point (queues) vs publish/subscribe (topics); competing consumers
  0003 ✅ DEEP DIVE (learner request; renumbered to 03): Kafka consumer groups, partition
         assignment, rebalancing, pros/cons  (see learning-record 0001)
  0004 ✅ Event vs command vs query — message intent & coupling
  0005 ✅ Delivery guarantees: at-most / at-least / exactly-once (idempotent consumer/producer, Kafka EOS)
  0006 ✅ DEEP DIVE (learner request): inside Kafka exactly-once — idempotent producer numbering (PID +
         sequence #), transactions, read_committed / LSO
  0007 ✅ DEEP DIVE (learner request): operating Kafka exactly-once — hung-txn lag/LSO,
         transactional.id assignment (EOSMode.V2), commit-interval throughput↔latency
  0008 ✅ Idempotency & deduplication in depth (natural vs synthetic, keys, dedup store/window,
         external-API idempotency keys, broker-native dedup) — broker-agnostic
  0009 ✅ Ordering & partitioning (no global order; key→partition; ordering↔parallelism tension;
         retries/repartition/hot-key/consumer-side pitfalls; per-broker models)
  0010 ✅ Backpressure & load leveling (buffer absorbs bursts not sustained load; pull vs push;
         prefetch; lag-as-metric/autoscale; bounded buffers & load shedding)
  0011 ✅ Retries, dead-letter queues & poison messages (classify transient/permanent; head-of-line
         blocking; backoff+jitter; blocking vs non-blocking retry; DLQ per broker; DLQ-as-workflow)
  0012 ✅ Log-based (Kafka/Pulsar) vs queue-based (RabbitMQ/SQS) brokers: consume-and-delete vs
         append-and-retain; how it explains replay/fan-out/ordering/offsets/DLQ; internals; when to choose
  0013 ✅ EDA: choreography (events, decentralized, implicit flow) vs orchestration (commands, central,
         explicit flow); Fowler flow-visibility trap; coupling/visibility trade-offs; when to choose; tooling
  0014 ✅ Transactional outbox & the dual-write problem (lost/phantom events; why 2PC/@Transactional
         fail; outbox in same tx + relay; polling vs CDC/Debezium; at-least-once → idempotent consumer)
  0015 ← NEXT: The Saga pattern & distributed workflows (compensation)
  Backlog (unscheduled, order flexible):
   • Delivery semantics in practice per tool
   • Event-driven architecture: choreography vs orchestration (Fowler flow-visibility trap)
   • Transactional outbox & the dual-write problem
   • Saga pattern & distributed workflows
   • Event sourcing & CQRS (Fowler's 4 EDA patterns)
   • Schema evolution & contracts
   • Observability, operations, and when NOT to use messaging (staff judgment)
- Quiz rule enforced: answer options kept ~equal length so formatting gives no clues.
