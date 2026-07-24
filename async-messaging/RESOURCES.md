# Asynchronism & Messaging Resources

Curated, high-trust sources. Knowledge for lessons is drawn from here — not from memory.
Verified live on 2026-07-24 where marked ✅.

## Knowledge

- [Book: _Designing Data-Intensive Applications_ — Martin Kleppmann](https://dataintensive.net/)
  The definitive text for this whole topic. Ch. 11 (Stream Processing), Ch. 4 (Encoding &
  Evolution), Ch. 8–9 (distributed trouble, consistency & consensus).
  Use for: delivery guarantees, ordering, log-based messaging, the truth about exactly-once.
- [Enterprise Integration Patterns — Gregor Hohpe & Bobby Woolf](https://www.enterpriseintegrationpatterns.com/patterns/messaging/) ✅
  The canonical, technology-independent catalog of 65 messaging patterns + a shared vocabulary.
  Use for: channels, routing, competing consumers, dead-letter channel, message construction.
- [microservices.io — Chris Richardson](https://microservices.io/patterns/communication-style/messaging.html) ✅
  Pattern-level design decisions: messaging, saga, transactional outbox, CQRS, event sourcing.
  Use for: how messaging fits microservice architecture and its trade-offs.
- [The System Design Primer — Donne Martin (Asynchronism)](https://github.com/donnemartin/system-design-primer#asynchronism) ✅
  High-level framing and interview-breadth on message queues, task queues, back pressure.
  Use for: quick orientation and the big picture.
- [Article: "The Log: What every software engineer should know…" — Jay Kreps (LinkedIn Eng)](https://engineering.linkedin.com/distributed-systems/log-what-every-software-engineer-should-know-about-real-time-datas-unifying)
  Foundational essay on the log abstraction behind Kafka and modern data systems.
  Use for: the mental model that unifies queues, streams, and databases.
- [Book (free): _Kafka: The Definitive Guide_ — Narkhede, Shapira, Palino (Confluent)](https://www.confluent.io/resources/kafka-the-definitive-guide/)
  Use for: Kafka internals — partitions, offsets, consumer groups, replication, exactly-once.
- [Confluent Documentation & Blog](https://docs.confluent.io/)
  Use for: Kafka concepts, exactly-once semantics (EOS), Schema Registry, ksqlDB.
- [AWS: Amazon SQS Developer Guide](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/welcome.html)
  Use for: managed-queue semantics — visibility timeout, FIFO vs standard, redrive/DLQ.
- [RabbitMQ Documentation & Tutorials](https://www.rabbitmq.com/tutorials)
  Use for: the AMQP model — exchanges, bindings, routing keys, acknowledgements, quorum queues.
- [Martin Fowler — "What do you mean by Event-Driven?"](https://martinfowler.com/articles/201701-event-driven.html)
  Use for: precise EDA vocabulary (event notification, event-carried state transfer, sourcing, CQRS).

## Wisdom (Communities)

- [r/apachekafka](https://reddit.com/r/apachekafka) — focused, practitioner-heavy.
  Use for: Kafka design critique and tool-selection debates.
- [r/softwarearchitecture](https://reddit.com/r/softwarearchitecture) & [r/dataengineering](https://reddit.com/r/dataengineering).
  Use for: architecture-level feedback on event-driven designs.
- [Confluent Community (Forum + Slack)](https://www.confluent.io/community/)
  Use for: deep Kafka troubleshooting with experts.
- [Stack Overflow tags: apache-kafka, rabbitmq, message-queue](https://stackoverflow.com/questions/tagged/message-queue)
  Use for: specific, reproducible implementation questions.

## Gaps
- Need a strong, free primary source specifically on **Redis Streams** and **Apache Pulsar**
  internals (user listed both). Fill before those lessons — likely official docs + a talk.
- Need a concrete, runnable **local lab** (docker-compose) resource for hands-on practice.
