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
  0015 ✅ The saga pattern (local transactions + compensations; choreography vs orchestration sagas;
         no isolation → semantic lock; no auto-rollback; pivot/compensatable/retriable; outbox+idempotency)
  0016 ✅ Schema evolution & contracts (why hard: independent deploys/retained logs/unknown consumers;
         backward/forward/full/transitive; additive golden rules; formats; schema registry; breaking→migrate)
  0017 ✅ Event sourcing & CQRS (store events/derive state; event store; snapshots; no dual write;
         CQRS write/read split + projections; eventual consistency; benefits/costs; use-judiciously caution)
  0018 ✅ FINALE: Observability, operations & when NOT to use messaging (distributed tracing via
         traceparent in headers; lag/DLQ/latency/rebalance metrics; runbooks; the "when not to" wisdom;
         journey recap + communities). CORE CURRICULUM COMPLETE (L01–18). See learning-record 0002.

## Post-curriculum directions (learner's choice)
   • ★ ACTIVE: Hands-on capstone — event-driven Order Saga (Kotlin/Spring Boot 4 + Kafka).
     - Location: ./capstone/  (brief: capstone/capstone-brief.html ; repo: capstone/order-saga/)
     - Scope: FULL orchestration saga (Order→Inventory→Payment + compensation), scaffolded, per-milestone review.
     - Scaffold generated via Spring Initializr (SB 4.1.0), overlaid with compose (podman/docker), Flyway,
       entities/repos/DTOs complete; pattern logic left as TODO(Mx). VALIDATED: ./gradlew compileKotlin = BUILD SUCCESSFUL.
     - 7 milestones tracked in SQL todos (cap-m0..cap-m7). Learner does M0 (boot) first, then M1 outbox, etc.
     - Review loop: learner implements a milestone → pastes/pushes code + observations → I review vs acceptance criteria.
   • Tool-specific deep dives to broaden beyond Kafka: RabbitMQ, Pulsar, SQS/SNS, Redis Streams internals
   • Mock staff-level design review / interview
   • Spaced-repetition review of earlier lessons (retrieval practice — recall may lag recognition)
   • Event-driven architecture: choreography vs orchestration (Fowler flow-visibility trap)
   • Transactional outbox & the dual-write problem
   • Saga pattern & distributed workflows
   • Event sourcing & CQRS (Fowler's 4 EDA patterns)
   • Schema evolution & contracts
   • Observability, operations, and when NOT to use messaging (staff judgment)
- Quiz rule enforced: answer options kept ~equal length so formatting gives no clues.
