# Mission: Technical Skills (Part A), Senior → Staff/Principal

## Why
A senior engineer who ships hard projects well wants to reach **Staff/Principal**. Getting there takes more
than knowing more technology. It takes **naming the trade-off behind every technical choice and writing it
down** so that many teams can align without the author in the room. This course covers Part A of
[Skills to upgrade](../02-skills-to-upgrade.md), the technical half of the roadmap. A1 (system design at
scale) is its own course: [System Design & Architecture at Scale](../system-design/index.html).

## Success looks like
- State **guarantees precisely**: linearizable vs causal vs eventual, backward vs forward compatibility,
  and 2PC vs consensus vs saga. Design for **partial failure** and rolling upgrades from day one
  ([Lesson 01](lessons/0001-data-intensive-distributed-systems.html)).
- Define reliability as **user-centred SLOs with an error-budget policy**. Run incidents with clear roles,
  write blameless postmortems with real action items, and design against overload
  ([Lesson 02](lessons/0002-reliability-observability-sre.html)).
- Explain an architecture as **ranked quality trade-offs**. Draw boundaries along data ownership, migrate
  incrementally (Strangler Fig, branch by abstraction, parallel run) and guard intent with **fitness functions**
  ([Lesson 03](lessons/0003-software-architecture.html)).
- Turn recurring debates into **engineering strategy**: diagnosis, guiding policy, coherent action. Forecast
  strategies into a 2–3 year **vision**, and enforce it
  ([Lesson 04](lessons/0004-engineering-strategy-and-vision.html)).
- Pick the **right artifact**: code, mini doc, design doc, RFC or ADR. Write it answer-first, and supersede
  ADRs rather than edit them ([Lesson 05](lessons/0005-technical-writing-docs-rfcs-adrs.html)).
- Raise **security and privacy in design**: OWASP, AuthN vs AuthZ, token validation, STRIDE on a
  data-flow diagram and data minimisation ([Lesson 06](lessons/0006-security-and-privacy.html)).
- Match **cloud complexity to real need**: primitives, IaC, deployment strategies, failure domains,
  RTO/RPO and FinOps. Choose boring technology and run less software
  ([Lesson 07](lessons/0007-cloud-infrastructure.html)).
- Shape a deliberate **T- (then pi-) shaped** profile: broad working fluency plus a business-critical
  spike, grown with a quarterly loop ([Lesson 08](lessons/0008-tshaped-breadth-and-depth.html)).

## Constraints
- About **3–5 hrs/week** over 8–10 weeks. Each lesson is self-contained and ends with a "try it" you can
  run against your own systems.
- Vendor-neutral. Examples name AWS, Kubernetes, Kafka or PostgreSQL where it helps, but the reasoning
  must transfer.
- Long-form reading lives in [GUIDE.md](GUIDE.md). Quick recall lives in the
  [reference cheat sheets](index.html).

## Out of scope
- **A1 · System design at scale**: scalability axes, CAP, caching, messaging, API design and estimation.
  See [System Design](../system-design/index.html).
- **Deep observability and SRE tooling** (OpenTelemetry pipelines, Prometheus, alert design). See
  [Observability](../Observability/index.html); Lesson 02 covers only the principles.
- **Part B · leadership skills** (influence, executive communication, mentoring). See
  [Leadership Skills](../leadership-skills/index.html).
- Vendor certifications, and hands-on Kubernetes or Terraform administration.
