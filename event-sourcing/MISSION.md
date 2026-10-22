# Mission: Event Sourcing & CQRS, from zero to Staff

## Why
A senior engineer who builds solid CRUD services wants to reach **Staff**. That means being the person
who can say *when* event sourcing is the right tool, design it so it survives years of production,
and defend that call in an architecture review. Knowing the pattern's vocabulary is not enough.

## Success looks like
- Explain **state vs history** precisely, and model a domain as **commands → decisions → events**.
- Implement a **functional decider** in Java 21 and a **PostgreSQL event store** with optimistic
  concurrency (`UNIQUE(stream_id, stream_version)`).
- Build **projections** that are idempotent, checkpointed and rebuildable. Handle eventual
  consistency in the UX.
- Operate it: **snapshots**, the **subscription gap problem**, **outbox → Kafka**, sagas/process
  managers, and event-store selection (Postgres / KurrentDB / Axon / Marten).
- Evolve it: **schema versioning and upcasting**, **GDPR crypto-shredding**, bi-temporal queries.
- Test it with **given–when–then** specifications and property tests.
- Lead it: run **Event Storming**, plan a **CRUD → ES migration**, write the **ADR**, and know
  **when not to** use event sourcing.

## Constraints
- ~3–5 hrs/week. Self-contained lessons with one tangible win each.
- Stack: **Java 21, Spring, PostgreSQL, Kafka**. Containers run with **Podman**.

## Out of scope
- Vendor certification for any one event store.
- Full DDD strategic design. It's touched only where it shapes stream and aggregate boundaries.
