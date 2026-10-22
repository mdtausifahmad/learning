# System Design — Glossary

The canonical language for this course. Lessons use these terms exactly.

## Scaling & availability

**Performance vs scalability problem**:
A performance problem means the system is slow for a single user. A scalability problem means it is
fast for one user but slow under load. They have different fixes.
_Avoid_: Using “slow” for both

**Latency vs throughput**:
Latency is the time to complete one operation; throughput is operations per unit of time. Batching and
queuing raise throughput but add latency. They are linked by **Little's Law**:
`Concurrency = Throughput × Latency`. Measure latency in **percentiles** (p50/p99), not averages.
_Avoid_: Speed (ambiguous)

**Vertical vs horizontal scaling**:
Vertical (scale up) buys a bigger machine: simple, but capped and a single point of failure. Horizontal
(scale out) adds machines: the real answer at scale, but it forces statelessness, load balancing and data partitioning.

**Stateless service**:
An app server that holds no session state locally (state lives in a shared cache, DB or signed token),
so any instance can serve any request and nodes can be added or removed freely.

**AKF scale cube**:
Three ways to scale. **X-axis**: clone the whole service behind a load balancer. **Y-axis**: split by
function (services). **Z-axis**: split by data (sharding), the hardest and most powerful.

**Availability (nines)**:
The fraction of time the system is up and serving correctly. 99.9% ≈ 8.77 h/year down; 99.99% ≈ 52.6 min.
It composes: components **in series multiply** (worse); redundant components **in parallel** improve it.

**SLI / SLO / SLA**:
SLI is the measured indicator (e.g. success rate); SLO is the internal target (e.g. 99.95%); SLA is the
contractual promise to customers, usually looser than the SLO.

## Consistency & replication

**CAP theorem**:
During a network **partition**, a distributed system must choose between **consistency** (every read sees
the latest write, or an error) and **availability** (every request gets a non-error answer). Partitions
are not optional, so the real choice is CP or AP.
_Avoid_: “Pick two of three”; confusing CAP’s C with ACID’s C (invariants within a transaction)

**PACELC**:
Extends CAP: if **P**artitioned, choose **A** or **C**; **E**lse choose **L**atency or **C**onsistency.
It names the everyday trade-off when nothing is broken.

**Linearizability (strong consistency)**:
The system behaves as if there is one copy of the data and each operation happens atomically at one
instant; a read always returns the latest committed write. The most intuitive and the most expensive.

**Eventual consistency**:
If writes stop, all replicas eventually converge. Reads may be stale meanwhile, and under load or
partition the lag can be seconds to minutes.
_Avoid_: Assuming “eventual” means a few milliseconds

**Read-your-writes**:
A session guarantee: you always see your own latest write, even if others don't yet. The usual UX fix
on eventually consistent stores.

**Quorum (N / W / R)**:
N replicas; a write succeeds after W acknowledgements; a read queries R replicas. `W + R > N` makes read
and write sets overlap, so reads see the latest write. `W + R ≤ N` gives eventual consistency with higher availability.

**Consensus**:
Nodes agreeing on an order of operations despite failures (Paxos, Raft): elect a leader, replicate an
ordered log, commit once a majority persists. You use it via etcd or ZooKeeper; you rarely build it.

**Replication topology**:
**Single-leader** (one primary, followers serve reads), **multi-leader** (several write nodes, needs
conflict resolution) or **leaderless** (Dynamo-style quorums). With async replication, **replication lag**
causes stale follower reads and can lose the last writes on failover.

## Data

**Federation**:
Splitting databases **by function** (users, products, orders). Smaller DBs and more write throughput;
cross-DB joins move into the application.
_Avoid_: Sharding (that splits the same table by data)

**Sharding (partitioning)**:
Splitting **one dataset** across nodes by a key (range, hash, consistent hash, directory or geo). Scales
writes and data volume; costs cross-shard joins, transactions and rebalancing.

**Shard key**:
The attribute that decides where a row lives. It needs high cardinality and even access; a skewed key
causes permanent hotspots.

**Consistent hashing**:
Nodes and keys sit on a hash ring; each key belongs to the next node clockwise. Adding or removing a node
moves only ~K/N keys. **Virtual nodes** (100–200 per machine) smooth the distribution.
_Avoid_: `hash(key) % N` (changing N reshuffles almost every key)

**ACID vs BASE**:
ACID (atomic, consistent, isolated, durable) puts correctness first. BASE (basically available, soft
state, eventually consistent) puts availability first.

**Isolation level**:
How much concurrent transactions see of each other: Read Uncommitted, Read Committed (the PostgreSQL and
Oracle default), Repeatable Read, Serializable. Weaker levels allow dirty reads, non-repeatable reads and phantoms.

**Lost update / write skew**:
Two transactions read-modify-write and one clobbers the other, or two decisions based on the same read
break an invariant. Prevent it with `SELECT … FOR UPDATE` or Serializable on that transaction.

**B-Tree vs LSM-Tree**:
B-Trees update pages in place: fast, predictable reads (PostgreSQL, InnoDB). LSM-Trees buffer writes in
a memtable and flush sorted SSTables sequentially: fast writes, with compaction cost (Cassandra, RocksDB).

**Bloom filter**:
A compact probabilistic set that can say “definitely not present”. LSM engines use it to skip
SSTables; caches use it to reject impossible keys.

## Caching

**Cache-aside (lazy loading)**:
The app reads the cache; on a miss it reads the DB and populates the cache with a TTL. The default
pattern; a cache outage costs latency, not correctness.

**Write-through / write-behind**:
Write-through writes cache and DB synchronously (fresh, slower writes). Write-behind acknowledges from the
cache and flushes to the DB asynchronously (fast, can lose data).

**Cache stampede (thundering herd)**:
A hot key expires and thousands of concurrent misses hit the DB at once. Fix with single-flight or a lock
per key, TTL jitter, or early refresh.

**Hot key**:
One key with so much traffic it saturates a single cache node. Fix by replicating the key, adding a local
in-process cache in front, or splitting the value.

**Cache penetration**:
Requests for keys that don't exist always miss and fall through to the DB. Fix by caching
“not found” with a short TTL or using a Bloom filter.

## Async & messaging

**Back-pressure**:
The system pushing back to slow producers when consumers can't keep up (bounded queues, rejection,
blocking), instead of letting a queue grow until it collapses. Messages that keep failing go to a
**dead-letter queue (DLQ)** so one poison message doesn't block the rest.

**At-least-once delivery**:
Messages are never lost but may be duplicated. The practical default; it requires idempotent consumers.
_Avoid_: “Exactly-once delivery” (exactly-once *processing* is at-least-once plus idempotency)

**Idempotency**:
Applying an operation more than once has the same effect as once. Achieved with dedupe tables, upserts on
natural keys, conditional writes or an `Idempotency-Key`.

**Dual-write problem**:
Writing to the DB and to a broker or cache separately, so a failure between them leaves them inconsistent.
Fix with the **transactional outbox** or **CDC**.

**Change data capture (CDC)**:
Streaming a database's inserts, updates and deletes as events by tailing its write-ahead log (e.g. Debezium).

**Partition & consumer group (Kafka)**:
A topic is split into ordered, append-only partitions; ordering holds only within a partition. Consumers in
a group split the partitions, so max parallelism equals the partition count; separate groups each read the full stream.

## Networking & APIs

**L4 vs L7 load balancing**:
L4 routes by IP and port per connection: fast and simple. L7 sees HTTP (path, headers, cookies) and can
route by content, terminate TLS, pin sessions and run canaries.

**Reverse proxy vs API gateway**:
A reverse proxy sits in front of servers for TLS, caching, compression and hiding topology; a load balancer
is a specialised one. An API gateway is the clients’ front door: authentication, rate limiting,
routing, aggregation and protocol translation, done once.

**Head-of-line (HOL) blocking**:
Later requests waiting behind an earlier one. HTTP/2 fixes it at the application level; HTTP/3 (QUIC over
UDP) fixes it at the transport level too.

**Cursor (keyset) pagination**:
Paging by a stable position marker rather than offset. It stays correct on live data and fast deep into a list.
_Avoid_: Offset pagination for large or fast-changing lists

**Token bucket**:
A rate-limiting algorithm that refills tokens at a fixed rate and allows bursts up to the bucket size.
Over the limit, the API returns `429` with `Retry-After`.

## Resilience

**Exponential backoff with jitter**:
Each retry waits longer (`base × 2^attempt`) plus randomness so clients don't retry in lockstep. Retries
are capped and only for idempotent operations and retryable errors (`503`, timeouts; never `400`/`404`).
_Avoid_: Bare retry loops (they cause retry storms)

**Circuit breaker**:
A guard with three states. **Closed** counts failures; **Open** fails fast without calling and returns a
fallback; **Half-Open** lets a trial request through after a cooldown.

**Bulkhead**:
Separate resource pools (threads, connections) per dependency, so exhaustion in one can't starve the others.

**Graceful degradation & load shedding**:
Degradation serves a reduced answer (stale cache, no recommendations) instead of an error. Shedding
deliberately rejects low-priority requests under overload so the core path stays healthy.

**Liveness vs readiness probe**:
Liveness asks “is the process alive?”, and failing it triggers a restart. Readiness asks
“can it take traffic?”, and failing it removes the instance from the load balancer without restarting it.
