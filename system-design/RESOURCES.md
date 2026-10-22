# System Design — Resources

Curated, high-trust sources, drawn from each lesson's further reading. Check the links before you rely on them.

## Canonical
- [System Design Primer](https://github.com/donnemartin/system-design-primer): the broadest free map of the field. Every lesson points into one of its sections.
- [ByteByteGo — System Design 101](https://github.com/ByteByteGoHq/system-design-101) and [bytebytego.com](https://bytebytego.com): one-page visual explainers. Good for revision and for diagrams in reviews.
- [Martin Kleppmann — *Designing Data-Intensive Applications*](https://dataintensive.net/): the deepest single book on replication, partitioning, transactions, consistency and streams.
- [Google SRE Book](https://sre.google/sre-book/table-of-contents/): how availability targets, error budgets and overload handling work in production.
- [Martin Fowler — *Patterns of Distributed Systems*](https://martinfowler.com/articles/patterns-of-distributed-systems/): named building blocks (leader/follower, quorum, write-ahead log) behind real systems.
- [Michael Nygard — *Release It!* (2nd ed.)](https://pragprog.com/titles/mnee2/release-it-second-edition/): the book that popularised circuit breakers and bulkheads.

## Scaling & availability (Lesson 01)
- [AKF Scale Cube](https://akfpartners.com/growth-blog/scale-cube): the original X/Y/Z-axis model for choosing how to scale.
- [Google SRE — *Embracing Risk*](https://sre.google/sre-book/embracing-risk/): why 100% is the wrong target and how to pick availability goals.

## Consistency & consensus (Lesson 02)
- [System Design Primer — consistency patterns](https://github.com/donnemartin/system-design-primer#consistency-patterns): weak, eventual and strong consistency in brief.
- [Kleppmann — *Please stop calling databases CP or AP*](https://martin.kleppmann.com/2015/05/11/please-stop-calling-databases-cp-or-ap.html): why CAP labels mislead and what to say instead.
- [PACELC design principle](https://en.wikipedia.org/wiki/PACELC_design_principle): the latency-vs-consistency trade-off when there is no partition.
- [Raft](https://raft.github.io/): the paper and an interactive visualisation of leader election and log replication.

## Databases & data modeling (Lesson 03)
- [System Design Primer — Database](https://github.com/donnemartin/system-design-primer#database): replication, federation, sharding, denormalisation, SQL tuning and NoSQL.
- [Amazon Dynamo paper](https://www.allthingsdistributed.com/files/amazon-dynamo-sosp2007.pdf): consistent hashing, quorums and leaderless replication at the source.
- DDIA chapters 2, 3, 6 and 7: data models, B-Tree vs LSM storage engines, partitioning, and transactions/isolation.

## Caching (Lesson 04)
- [System Design Primer — Cache](https://github.com/donnemartin/system-design-primer#cache): cache layers and the cache-aside, write-through, write-behind and refresh-ahead patterns.
- [AWS — Caching best practices](https://aws.amazon.com/caching/best-practices/): patterns, TTLs and the thundering-herd problem from an operator's view.
- [AWS — Redis vs Memcached](https://aws.amazon.com/elasticache/redis-vs-memcached/): a feature-by-feature comparison for choosing a distributed cache.

## Async & messaging (Lesson 05)
- [System Design Primer — Asynchronism](https://github.com/donnemartin/system-design-primer#asynchronism): message queues, task queues and back-pressure.
- [Jay Kreps — *The Log*](https://engineering.linkedin.com/distributed-systems/log-what-every-software-engineer-should-know-about-real-time-datas-unifying): why the append-only log unifies data integration, and the idea behind Kafka.
- [Debezium](https://debezium.io/): open-source change data capture, the standard fix for dual writes.
- DDIA chapter 11 (stream processing): logs, CDC and event sourcing in depth.

## Networking & APIs (Lesson 06)
- [System Design Primer — Communication](https://github.com/donnemartin/system-design-primer#communication): TCP/UDP, RPC, REST, DNS, CDN, load balancers and reverse proxies.
- [Google API Design Guide](https://cloud.google.com/apis/design): resource-oriented design, naming, errors and versioning used across Google's APIs.
- [Cloudflare — What is HTTP/3?](https://www.cloudflare.com/learning/performance/what-is-http3/): QUIC and why it removes transport-level head-of-line blocking.

## Estimation (Lesson 07)
- [System Design Primer — Appendix](https://github.com/donnemartin/system-design-primer#appendix): powers of two and latency numbers on one page.
- [Latency numbers every programmer should know (interactive)](https://colin-scott.github.io/personal_website/research/interactive_latency.html): Jeff Dean's numbers, updated by year.
- Alex Xu, *System Design Interview* Vol. 1, chapter 2 ([bytebytego.com](https://bytebytego.com)): back-of-the-envelope estimation worked step by step.

## Resilience (Lesson 08)
- [Google SRE — *Addressing Cascading Failures*](https://sre.google/sre-book/addressing-cascading-failures/): how overload spreads and how to stop it.
- [AWS Builders' Library — *Timeouts, retries, and backoff with jitter*](https://aws.amazon.com/builders-library/timeouts-retries-and-backoff-with-jitter/): how Amazon bounds waits and avoids retry storms.
- [Resilience4j](https://resilience4j.readme.io/): JVM circuit breaker, retry, bulkhead and rate limiter you can read and use.
- [Kubernetes — Liveness, readiness and startup probes](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/): configuring the health checks that drive restarts and routing.

## Related courses in this repo
- [Async messaging](../async-messaging/index.html): queues, Kafka and delivery guarantees in depth (extends Lesson 05).
- [Event sourcing & CQRS](../event-sourcing/index.html): events as the source of truth, projections and the outbox.
- [Observability](../Observability/index.html): measuring the SLIs, latency percentiles and saturation these designs depend on.
- [Technical skills](../technical-skills/index.html): the other technical competencies (A2–A9) in the plan.
