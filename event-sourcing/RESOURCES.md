# Event Sourcing — Resources

Curated, high-trust sources. Verify the links before you rely on them; vendor names change (EventStoreDB is now **KurrentDB**).

## Canonical
- [Martin Fowler — *Event Sourcing*](https://martinfowler.com/eaaDev/EventSourcing.html): the original pattern write-up.
- [Martin Fowler — *CQRS*](https://martinfowler.com/bliki/CQRS.html), including the warnings about when not to use it.
- [Greg Young — *Versioning in an Event Sourced System*](https://leanpub.com/esversioning): the reference on schema evolution.
- [Microsoft — Event Sourcing pattern](https://learn.microsoft.com/azure/architecture/patterns/event-sourcing) and [CQRS pattern](https://learn.microsoft.com/azure/architecture/patterns/cqrs).
- [Jérémie Chassaing — *Functional Event Sourcing Decider*](https://thinkbeforecoding.com/post/2021/12/17/functional-event-sourcing-decider).

## Practitioners
- [Oskar Dudycz — event-driven.io](https://event-driven.io/en/): Postgres stores, projections, the gap problem, closing the books and testing.
- [Kurrent (EventStoreDB) docs and blog](https://www.kurrent.io/blog): streams, subscriptions, ES patterns.
- [Marten](https://martendb.io/) (Postgres-based .NET store) and [Axon Framework](https://www.axoniq.io/) (JVM). Read their docs for design ideas even if you don't adopt them.
- [Dynamic Consistency Boundary](https://dcb.events/): the spec and examples.
- [Event Storming — Alberto Brandolini](https://www.eventstorming.com/).

## Adjacent
- [Debezium outbox pattern](https://debezium.io/documentation/reference/stable/transformations/outbox-event-router.html).
- [PostgreSQL — transaction ID and snapshot functions (`pg_current_snapshot`, `xid8`)](https://www.postgresql.org/docs/current/functions-info.html).
- Martin Kleppmann, *Designing Data-Intensive Applications*, chapters 11–12 (logs, derived data).
- Vaughn Vernon, *Implementing Domain-Driven Design* (aggregates, domain events).
