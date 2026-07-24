# Capstone — Event-Driven Order Saga (Kotlin · Spring Boot · Kafka)

The hands-on capstone for the **Async & Messaging** course. You'll build an orchestration-based
**order saga** that exercises the patterns you learned: transactional outbox, idempotent consumers,
delivery guarantees, retries/DLQ, ordering keys, saga + compensation, and distributed tracing.

> The scaffold gives you the **infrastructure and plumbing**; you write the **pattern logic** at the
> `TODO(Mx: …)` markers, one milestone at a time. Full milestone brief + acceptance criteria:
> open **`../capstone-brief.html`**.

Stack: Spring Boot 4 · Kotlin · Spring Kafka · JPA/Postgres · Flyway · Micrometer Tracing → Jaeger.

---

## 1. Start the infrastructure

Runtime-agnostic compose — use **podman** (or docker):

```bash
podman compose up -d        # or: docker compose up -d
```

| Service    | URL / address                         |
| ---------- | ------------------------------------- |
| Kafka      | `localhost:9092` (from the app)       |
| Kafka UI   | http://localhost:8080                 |
| Jaeger     | http://localhost:16686                |
| Postgres   | `localhost:5432` — db `ordersaga`, user `app`, pass `app` |

## 2. Run the app

```bash
./gradlew bootRun
```

The Gradle wrapper auto-provisions a JDK 21 toolchain (foojay), so any recent JDK on your PATH is
fine. First run downloads Gradle + dependencies.

## 3. Place an order

```bash
curl -s -XPOST localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerId":"c-1","sku":"SKU-1","qty":1,"amount":250.00}' | jq

# poll the outcome
curl -s localhost:8080/orders/<orderId> | jq
```

Watch topics in **Kafka UI**, the DB in Postgres, and the end-to-end trace in **Jaeger**.

---

## Where the work is (by milestone)

| Milestone | You implement | File(s) | Lessons |
| --------- | ------------- | ------- | ------- |
| **M1** Outbox | `placeOrder`, `publishPending` | `order/OrderService.kt`, `order/OutboxRelay.kt` | 14, 09 |
| **M2** Idempotent consumer | `onReserve` | `inventory/InventoryListener.kt` | 08, 05 |
| **M3** Saga happy path | orchestrator handlers, `onCharge` | `orchestrator/SagaOrchestrator.kt`, `payment/PaymentListener.kt` | 13, 15 |
| **M4** Compensation | failure/compensation handlers, `onRelease` | `orchestrator/…`, `inventory/…` | 15 |
| **M5** Retries + DLQ | `errorHandler` + `ErrorHandlingDeserializer` | `config/KafkaConfig.kt`, `application.yml` | 11 |
| **M6** Guarantees + ordering | producer idempotence, keys, (opt) EOS | `application.yml`, sends | 05, 06, 09 |
| **M7** Observability | tracing + lag metrics + a load test | (tracing is pre-wired) | 10, 18 |

Grep for `TODO(M` to find every task. Full acceptance criteria are in **`../capstone-brief.html`**.

## Notes
- `spring.jpa.hibernate.ddl-auto=validate` — **Flyway owns the schema** (`db/migration/V1__init.sql`).
- Topics are declared explicitly (auto-create is off) in `config/KafkaConfig.kt` with 3 partitions.
- Every consumer must be **idempotent** — the outbox relay and Kafka are both at-least-once.
