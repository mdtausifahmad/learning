# Technical Skills — Glossary

The canonical language for this course. Lessons and cheat sheets use these terms exactly. Distributed-systems
fundamentals such as CAP, caching and sharding live in the [System Design](../system-design/index.html) course.

## Distributed systems (Lesson 01)

**Backward / forward compatibility**:
Backward: new code can read data written by old code. Forward: old code can read data written by new code.
A rolling upgrade needs **both**, because old and new versions run side by side.
_Avoid_: "Compatible" without saying which direction

**Partial failure**:
One part of the system fails while others keep running. A silent node may be dead, slow, paused,
partitioned or overloaded, and you cannot tell which.

**Happens-before**:
The potential-causality relation created by local process order and by send-before-receive of messages.
_Avoid_: "Earlier timestamp" (wall-clock order is not causal order)

**Lamport timestamp**:
A logical counter. If A happened before B, then L(A) < L(B). The reverse is not guaranteed, so it cannot
detect concurrency.

**Vector clock**:
One counter per actor. It can tell causally ordered events from concurrent ones (neither vector dominates).
Its metadata grows with the number of actors.

**Consensus**:
Nodes agreeing on an ordered log of decisions despite failures (Raft, Paxos). It makes progress with a
majority and is unavailable without one. Used for leader election and configuration.
_Avoid_: Distributed transaction (consensus is not free atomic commit)

**Two-phase commit (2PC)**:
Atomic commit across participants run by a coordinator. It can **block** if the coordinator fails at the
wrong moment.

**Linearizability**:
Operations appear to take effect atomically, in real-time order. It is the precise meaning behind most uses
of "strong consistency".
_Avoid_: "Strong" without naming the guarantee

**Causal consistency**:
Effects are never observed before their causes (a reply never appears before its comment).

**Eventual consistency**:
Replicas converge if updates stop. It is a real contract about convergence, not "no consistency".

## Reliability & SRE (Lesson 02)

**SLI**:
A measured signal that approximates user experience, e.g. the share of valid checkout requests completing
under 800 ms.
_Avoid_: Host metrics such as CPU or uptime presented as an SLI

**SLO**:
A target for an SLI over a window, e.g. 99.9% over 30 days. It is internal and owned by the team.
_Avoid_: SLA (an external promise with consequences)

**Error budget**:
The allowed unreliability implied by an SLO (1 − SLO over the window). An agreed **policy** governs it:
ship normally while healthy, prioritise reliability when it is burning or spent.

**Toil**:
Manual, repetitive, automatable, tactical work that scales linearly with load and leaves no lasting value.
_Avoid_: All operational work (debugging a novel incident is engineering, not toil)

**Blameless postmortem**:
A review of how the system allowed a failure, ending in owned and verified action items. Accountability
stays; punishment goes.

**Graceful degradation**:
Keeping critical user journeys alive by switching off non-critical features under stress. Decided before
the incident.

**NALSD**:
Non-abstract large system design: reasoning from concrete numbers, failure domains, owners and rollout
mechanics rather than generic boxes.

## Architecture (Lesson 03)

**Architecture characteristic**:
A quality the system must exhibit (scalability, availability, evolvability, security, operability…). You
rank them and accept that some lose.
_Avoid_: Non-functional requirement used as a wish list

**Coupling / cohesion**:
Coupling is how much one part depends on another (runtime, data, semantic, temporal, deployment). Cohesion
is how much the things that change together live together.

**Conway's Law**:
A system's design mirrors its organisation's communication structure. Design teams and architecture together.

**Saga**:
A business transaction split into local commits, each with a compensating action. It is eventually
consistent and needs idempotency, visible intermediate states and an operator repair path.
_Avoid_: Distributed rollback

**Strangler Fig**:
Incremental migration: a routing facade sends capabilities to the new system one at a time until the
legacy one can be retired.
_Avoid_: Big-bang rewrite

**Fitness function**:
An automated check of an architectural property, such as dependency rules, schema compatibility,
latency budgets or ownership metadata, run in CI or at deploy time.

**Technical debt**:
A shortcut that creates a future carrying cost. Discuss it in terms of principal, interest and
repayment: deliberate prudent, deliberate reckless, accidental, or neglect.
_Avoid_: "Code I don't like"

## Strategy & writing (Lessons 04–05)

**Strategy kernel**:
Rumelt's structure: **diagnosis** (the challenge and why it is hard), **guiding policy** (the chosen
trade-off), **coherent action** (coordinated steps).

**Guiding policy**:
A rule set at the right altitude: resource allocation, a priority rule, a fundamental rule, escalation or
sequencing. If no team has to change plans, it is not strategic.
_Avoid_: Aspiration ("improve quality", "collaborate more")

**Engineering strategy**:
How we tackle today's most important technical challenge over roughly 6–18 months. It is specific,
opinionated and enforceable.
_Avoid_: Roadmap (a schedule with no diagnosis or trade-off)

**Technical vision**:
A short, optimistic but plausible description of the future state 2–3 years out. Strategies steer
toward it.

**Design doc**:
A structured, opinionated proposal for a meaningful change: context, goals, **non-goals**, design,
alternatives, cross-cutting concerns, rollout. A mini doc is 1–3 pages; a full doc is about 10–20.

**RFC**:
A reviewable proposal that gathers input widely but is written by one author and decided by a named owner.
_Avoid_: Vote, consensus document

**ADR (Architecture Decision Record)**:
One significant decision with its context, decision and consequences. It is timestamped and immutable;
when the decision changes, a new ADR supersedes it. The collection of ADRs is the **decision log**.

**SCQA**:
Situation, Complication, Question, Answer: a way to open a doc that leads with the answer.

## Security & privacy (Lesson 06)

**Authentication / authorization**:
Authentication answers "who are you?" Authorization answers "what are you allowed to do, to this object?"
Being authenticated never implies being authorized.
_Avoid_: Using "auth" for both

**OAuth2 / OIDC**:
OAuth2 is a framework for delegated authorization. OIDC adds standard identity (login) on top of it.

**JWT**:
A signed, self-contained token of claims. It is only safe if you validate signature, issuer, audience and
expiry, and it is hard to revoke.
_Avoid_: "Secure because it's signed"

**Least privilege**:
Only the access needed, at the narrowest scope, for the shortest reasonable time.

**Zero trust**:
No request is trusted because of where on the network it comes from. Identity and policy are verified at
every boundary.

**STRIDE**:
Threat categories applied to a data-flow diagram: Spoofing, Tampering, Repudiation, Information
disclosure, Denial of service, Elevation of privilege.

**Data minimisation**:
Collect only the personal data a stated purpose needs, keep it only as long as needed, then delete it.
_Avoid_: "Encrypted, therefore compliant"

## Cloud & expertise (Lessons 07–08)

**Infrastructure as Code (IaC)**:
Infrastructure that is versioned, reviewed, planned, applied and checked for drift, with no console clicks.
Usually paired with **immutable infrastructure**: replace instances, never mutate them.

**RTO / RPO**:
Recovery Time Objective is the maximum acceptable downtime. Recovery Point Objective is the maximum
acceptable data loss.

**Failure domain**:
The blast radius a single fault can take out: instance, Availability Zone or region.

**Innovation token**:
The limited budget for novel technology ("choose boring technology"). Spend one only where novelty is
essential, and give it an owner and an exit plan.

**FinOps**:
Treating cloud cost as an architectural constraint: tagging, unit economics, budgets and anomaly alerts,
right-sizing.

**T-shaped / pi-shaped**:
Broad working fluency across domains plus one (T) or two (pi) deep spikes of authority aligned with
durable business needs.
_Avoid_: Generalist with trivia-level breadth

**Staff archetypes**:
Tech Lead, Architect, Solver, Right Hand (StaffEng). Each needs both breadth and depth in different
proportions.
