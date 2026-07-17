# 02 · Skills to Upgrade

Every topic you need to level up, split into **Technical** and **Leadership** tracks. For each: *why it matters at Staff+* and *what "good" looks like.* Use the checklists at the end to find your gaps; get the resources to learn each in **[04-subjects-and-resources.md](04-subjects-and-resources.md)**.

> The recurring meta-theme across every source: **everything is a trade-off**, and a Staff+ engineer's job is to make, document, and align the organization around those trade-offs.[^1]

---

# Part A — Technical Skills

> 📂 Deep-dive folders: **[system-design/](system-design/README.md)** (A1) · **[technical-skills/](technical-skills/README.md)** (A2–A9). Each topic below links to its own read-and-learn file with Mermaid diagrams.

## A1. System Design & Architecture at Scale ⭐ (the core competency)
The single most-tested and most-expected Staff+ technical skill.[^1]

**Sub-areas to master** — each links to a self-contained deep dive (with Mermaid diagrams) in [`system-design/`](system-design/):
- **[Scalability trade-off axes](system-design/01-scalability-tradeoff-axes.md)** — performance vs scalability, latency vs throughput, vertical vs horizontal, the AKF scale cube, availability in "nines".
- **[CAP theorem & consistency models](system-design/02-cap-theorem-consistency-models.md)** — weak / eventual / strong; PACELC; linearizability spectrum; quorums (N/W/R); failover & replication.
- **[Databases & data modeling](system-design/03-databases-and-data-modeling.md)** — replication, federation, **sharding**, denormalization, SQL tuning; SQL vs NoSQL families; ACID vs BASE; isolation levels; consistent hashing; B-Tree vs LSM-Tree.
- **[Caching](system-design/04-caching.md)** — client/CDN/app/DB layers; cache-aside, write-through, write-behind, refresh-ahead; invalidation; stampede & hot keys.
- **[Asynchronism & messaging](system-design/05-asynchronism-and-messaging.md)** — queues, back-pressure, delivery semantics (at-least/at-most/exactly-once), idempotency, event sourcing, CDC, Kafka.
- **[Networking & API design](system-design/06-networking-and-api-design.md)** — TCP/UDP, gRPC, REST vs GraphQL vs RPC, HTTP/1→2→3, L4 vs L7 load balancers, reverse proxy, API gateway, DNS, CDN.
- **[Back-of-envelope estimation](system-design/07-back-of-envelope-estimation.md)** — powers of two, "latency numbers every programmer should know", a full worked example.

> 📂 **Start here:** [`system-design/README.md`](system-design/README.md) — the index and the 4-step design method that ties all seven topics together.

**What "good" looks like:** you can lead an open-ended design conversation using a repeatable method — *requirements/constraints → high-level design → core components → scale it and name the bottlenecks and trade-offs.*[^1]

## A2. Data-Intensive & Distributed Systems (deep knowledge)
> 📄 **Deep dive:** [technical-skills/A2-data-intensive-distributed-systems.md](technical-skills/A2-data-intensive-distributed-systems.md)
**Why:** these concepts sit underneath every "which database / how do we keep data consistent / how do we evolve schemas safely" decision. Staff+ engineers reason correctly about replication lag, quorums, consensus, and consistency guarantees rather than cargo-culting.[^2]

**Master (the DDIA spine):** reliability/scalability/maintainability · data models & encoding (schema evolution: Avro/Protobuf/Thrift) · storage engines · **replication** · **partitioning** · **transactions & isolation** · the trouble with distributed systems (clocks, partial failure) · **consistency & consensus** (linearizability, ordering, 2PC, Paxos/Raft) · batch & stream processing.[^2]

**What "good" looks like:** you can choose and defend a consistency model, design a safe schema-evolution path, and explain why a given system will or won't lose data under partition.

## A3. Reliability, Observability & Operating at Scale (SRE)
> 📄 **Deep dive:** [technical-skills/A3-reliability-observability-sre.md](technical-skills/A3-reliability-observability-sre.md)
**Why:** Staff+ engineers own the *reliability posture* of systems, not just their construction.[^3]

**Master:** SLIs/SLOs & **error budgets** · eliminating **toil** · the **four golden signals** & observability (metrics/logs/traces) · **simplicity** as a value · incident response & **blameless postmortems** · handling overload & **cascading failures** · load balancing · canarying · NALSD (non-abstract large system design).[^3]

**What "good" looks like:** you set SLOs tied to user experience, run postmortems that produce *systemic* fixes, and design for graceful degradation — measurable reliability, not heroics.

## A4. Software Architecture, Patterns & Evolutionary Design
> 📄 **Deep dive:** [technical-skills/A4-software-architecture.md](technical-skills/A4-software-architecture.md)
**Why:** *"Architecture is the important stuff — whatever that is."* The architect's job is to recognize what's important and keep it in good condition. And **high internal quality speeds up future delivery** (the payoff comes "in weeks not months").[^4]

**Master:** monolith vs microservices trade-offs · event-driven architecture · layering · **Strangler Fig** migration · Enterprise Integration Patterns · PoEAA · **evolutionary architecture & fitness functions** · service granularity & data ownership (the "hard parts").[^4]

**What "good" looks like:** you can analyze architecture *characteristics* (scalability, evolvability, etc.), pick a style with explicit trade-offs, and stage a large migration safely.

## A5. Engineering Strategy & Technical Vision ⭐ (the technical differentiator)
> 📄 **Deep dive:** [technical-skills/A5-engineering-strategy-and-vision.md](technical-skills/A5-engineering-strategy-and-vision.md)
This is what separates Senior from Staff+/Principal.[^5]

**Rumelt's definition** — a real strategy has three parts: **Diagnosis** (a theory of the challenge) → **Guiding policies** (approaches that *imply trade-offs* — "if a policy doesn't imply a tradeoff, be suspicious of it; 'work harder' isn't a policy") → **Coherent actions**.[^6]

**The Staff-engineer recipe:** *"To write an engineering **strategy**, write five design documents and pull the similarities out. To write an engineering **vision**, write five strategies and forecast their implications two years out."* Good strategy is "boring"; be specific (specific statements create alignment; generic ones create the *illusion* of alignment); be opinionated and show your work.[^5]

**What "good" looks like:** when a debate recurs 3–4 times you write the strategy that ends it; your vision is 2–3 years out, grounded in users/business, specific, and 1–2 pages.

## A6. Technical Writing — Design Docs, RFCs, ADRs
> 📄 **Deep dive:** [technical-skills/A6-technical-writing-docs-rfcs-adrs.md](technical-skills/A6-technical-writing-docs-rfcs-adrs.md)
**Why:** writing is the primary *leverage* mechanism at Staff+ — durable, opinionated, trade-off-explicit documents that outlive you and align teams asynchronously.[^7]

- **Design docs (Google-style):** Context & scope → Goals/non-goals → the design (with trade-offs) → **Alternatives considered** → Cross-cutting concerns (security, privacy, observability). ~10–20 pages. *Don't write one if there are no real trade-offs — just write the code.*
- **RFCs / tech specs:** start from the problem, keep the template simple, gather input widely but write alone.
- **ADRs (Architecture Decision Records):** one decision each — Context, Decision, Consequences. Rationale-driven, timestamped, **immutable** (supersede, don't edit).

**What "good" looks like:** your docs state the problem crisply, survey alternatives honestly, and make the rationale legible so others can extend it.

## A7. Security & Privacy as a Design Concern
> 📄 **Deep dive:** [technical-skills/A7-security-and-privacy.md](technical-skills/A7-security-and-privacy.md)
**Why:** a cross-cutting concern Staff+ engineers build in from the start — and often must *fund* "because no one else is asking for it."[^8]

**Master:** **OWASP Top 10** (broken access control, injection, crypto failures, insecure design, SSRF, …) · secure API design (authN/authZ, OAuth/JWT, MFA, rate limiting) · secrets management · least privilege · defense-in-depth · encryption in transit/at rest · threat modeling.[^8]

## A8. Cloud Infrastructure & Pragmatic Technology Choices
> 📄 **Deep dive:** [technical-skills/A8-cloud-infrastructure.md](technical-skills/A8-cloud-infrastructure.md)
**Why:** breadth to scale a system on cloud primitives end-to-end, plus the *judgment* to keep it simple.[^9]

**Master:** the scaling progression (single box → separate DB → LB + horizontal scaling → caching → CDN → autoscaling → multi-AZ/region) · the **AWS Well-Architected** 6 pillars. Two Staff-grade heuristics: **"Choose Boring Technology"** and **"Run Less Software"** (prefer managed/commodity over bespoke).[^9]

## A9. T-shaped Breadth + Depth
> 📄 **Deep dive:** [technical-skills/A9-tshaped-breadth-and-depth.md](technical-skills/A9-tshaped-breadth-and-depth.md)
**Why:** broad architectural judgment across many domains (A1–A8) *plus* genuine depth in at least one business-critical area (the Architect's "spike").[^10]

**What "good" looks like:** you're the go-to authority in one enduring domain *and* can reason competently across the whole stack.

---

# Part B — Leadership / "Soft" Skills (the biggest differentiators)

> 📂 Deep-dive folder: **[leadership-skills/](leadership-skills/README.md)** (B1–B8). Each topic below links to its own read-and-learn file with Mermaid diagrams and practice drills.

> The jump from Senior → Staff/Principal is *not* a technical jump — it's a shift into **influence, communication, and force-multiplication.** Counter-intuitively, authority *shrinks* rather than grows: you lead through influence, not command.[^11]

## B1. Influence Without Authority (the master skill)
> 📄 **Deep dive:** [leadership-skills/B1-influence-without-authority.md](leadership-skills/B1-influence-without-authority.md)
**Why:** Staff+ authority is *borrowed*. *"I'm steering and influencing more than saying 'I've got the authority to just tell you what to do.' I've never seen that style work well."*[^11]

**Master ("Learn to never be wrong"):**[^12] be right *while creating space for others*; three moves — (1) **listen through questions** ("ask three good questions before you share your perspective"), (2) **define the purpose** (reframe unclear discussions), (3) **read the room** (split off or escalate when people are too far apart). Handle difficult people by aligning *before* the meeting. And **"to lead, you have to follow"** — give support quickly to trustworthy leaders; make your feedback explicitly non-blocking.[^12]

## B2. Communication & Executive Writing
> 📄 **Deep dive:** [leadership-skills/B2-communication-and-executive-writing.md](leadership-skills/B2-communication-and-executive-writing.md)
**Why:** *"Increasingly, your impact will be constrained by your ability to influence executives effectively."*[^13]

**Master:**[^13]
- **SCQA** opening: **S**ituation → **C**omplication → **Q**uestion → **A**nswer.
- **Minto Pyramid Principle:** lead with the answer; group into ≤3 arguments, each ≤3 sub-arguments, ordered by importance.
- **nemawashi** — align with stakeholders *before* the meeting.
- With executives: go to *extract their perspective / align*, not to "win"; **never fight feedback**; **never present a question without an answer**; send an early draft to an exec attending and ask what to change.

**What "good" looks like:** you tailor the message to the audience, and a great exec meeting is "engaged discussion, not addressing every agenda item."

## B3. Technical & Project Leadership
> 📄 **Deep dive:** [leadership-skills/B3-technical-and-project-leadership.md](leadership-skills/B3-technical-and-project-leadership.md)
**Why:** setting technical direction = being a "part-time product manager for technology" — solving the *organization's* real needs, not the tech you personally find exciting.[^8]

**Master:** leading cross-team initiatives; **aggressively delegating** (delegate the *hard* pieces to grow people); managing **technical quality** as a program (escalate through the cheapest tool that works: hot spots → best practices → leverage points → technical vectors → measurement → quality team → quality program).[^14]

## B4. Mentorship & Sponsorship (a top differentiator)
> 📄 **Deep dive:** [leadership-skills/B4-mentorship-and-sponsorship.md](leadership-skills/B4-mentorship-and-sponsorship.md)
**Why:** growing others changes the company's trajectory more than heroics; pair *a little* mentorship with *considerably more* sponsorship.[^8]

**Mentorship = advice. Sponsorship = spending your capital to get someone *opportunities* and *promotions*.**[^15] Research shows women/non-binary engineers are "over-mentored but under-sponsored." Concrete sponsorship actions: suggest someone to lead a project / facilitate a postmortem / give a talk; forward their work to a wider audience; raise their name where decisions are made. *"Anyone can be a sponsor"* — you don't need to be a manager. Make **"who could grow from this?"** your default when work lands on your desk. Keep a **sponsorship journal.**[^15][^16]

## B5. Stakeholder Management, Trust & Political Capital
> 📄 **Deep dive:** [leadership-skills/B5-stakeholder-management-and-trust.md](leadership-skills/B5-stakeholder-management-and-trust.md)
**Why:** your authority is *proxied* from a sponsor (usually your manager) and depends on staying aligned.[^17]

**Master:**[^17] **"Never surprise your manager"** (treat each surprise as an incident to learn from); **"don't let your sponsor surprise you"** (proactively push information up); **build a network of peers** across teams (the most consistent advice from Staff+ engineers — "it gets lonely the higher up you go").

## B6. "Glue Work" — Made Visible
> 📄 **Deep dive:** [leadership-skills/B6-glue-work.md](leadership-skills/B6-glue-work.md)
**Why:** the invisible team-sustaining work (onboarding, roadmap upkeep, noticing dropped balls, alignment). *"Managed deliberately, glue work demonstrates and builds strong technical leadership skills. Left unconscious, it can be career limiting."*[^18]

**What "good" looks like:** you name glue work explicitly in planning and your promo packet, and frame it as leadership — not let it quietly derail your technical trajectory.

## B7. Decision-Making & Disagreeing Productively
> 📄 **Deep dive:** [leadership-skills/B7-decision-making.md](leadership-skills/B7-decision-making.md)
**Master:**[^16] **write it down** (document your *reasoning* so people learn, not just get directed); **circulate early** (before you've crystallized); **separate style from substance** (stop giving style feedback that won't change outcomes); **change your mind** ("one of the biggest signs of respect… is listening and then changing your mind"); don't weigh in on everything to "show value."

## B8. Prioritization at Scale — "Work on What Matters"
> 📄 **Deep dive:** [leadership-skills/B8-prioritization.md](leadership-skills/B8-prioritization.md)
**Why:** senior roles require accomplishing more in less time; leverage comes from *what* you choose.[^14]

**Master:** avoid **snacking / preening / chasing ghosts**; invest in **existential issues** first, then areas that matter *and still have room + attention*; think on a **longer time horizon** ("What will Future You wish Present You had done?"); telegraph what's coming; create institutional memory; pace your career sustainably.[^14][^19]

---

## Gap-analysis checklist — Technical

Rate each **1–5** (1 = beginner, 3 = solid Senior, 5 = Staff/Principal). Anything below 4 is a study target.

- [ ] System design & scalability trade-offs
- [ ] Distributed systems / DDIA depth (replication, consensus, consistency)
- [ ] Reliability / SRE (SLOs, error budgets, postmortems)
- [ ] Software architecture styles & evolutionary design
- [ ] Engineering strategy & technical vision writing
- [ ] Design docs / RFCs / ADRs
- [ ] Security (OWASP Top 10, secure API design, threat modeling)
- [ ] Cloud architecture (Well-Architected) & pragmatic tech choices
- [ ] Depth in ≥1 business-critical domain (your "spike")

## Gap-analysis checklist — Leadership

- [ ] Influence without authority
- [ ] Executive communication (SCQA / Pyramid Principle)
- [ ] Leading cross-team projects & delegation
- [ ] Sponsorship (not just mentorship)
- [ ] Stakeholder management & "never surprise your manager"
- [ ] Making glue work visible
- [ ] Decision-making & productive disagreement
- [ ] Prioritization / "work on what matters"

> **Rule of thumb:** most Senior engineers under-rate Part B and over-invest in Part A. The promotion is usually won on **B1, B2, and B4.**[^11]

---

### Sources

[^1]: System Design Primer — https://github.com/donnemartin/system-design-primer ; ByteByteGo System Design 101 — https://github.com/ByteByteGoHq/system-design-101
[^2]: Martin Kleppmann, *Designing Data-Intensive Applications* — https://dataintensive.net/ ; references — https://github.com/ept/ddia-references
[^3]: Google SRE Book — https://sre.google/sre-book/table-of-contents/ ; SRE Workbook — https://sre.google/workbook/table-of-contents/
[^4]: Martin Fowler, "Software Architecture Guide" — https://martinfowler.com/architecture/ ; "Is High Quality Software Worth the Cost?" — https://martinfowler.com/articles/is-quality-worth-cost.html
[^5]: StaffEng, "Writing engineering strategy" — https://staffeng.com/guides/engineering-strategy/
[^6]: Will Larson, "Eng strategies" (Rumelt framework) — https://lethain.com/eng-strategies/
[^7]: "Design Docs at Google" — https://www.industrialempathy.com/posts/design-docs-at-google/ ; ADRs — https://adr.github.io/ and https://github.com/joelparkerhenderson/architecture-decision-record ; RFCs — https://blog.pragmaticengineer.com/scaling-engineering-teams-via-writing-things-down-rfcs/
[^8]: StaffEng, "What do Staff engineers actually do?" — https://staffeng.com/guides/what-do-staff-engineers-actually-do/ ; OWASP Top 10 — https://owasp.org/www-project-top-ten/
[^9]: "Choose Boring Technology" — https://mcfunley.com/choose-boring-technology ; "Run Less Software" — https://www.intercom.com/blog/run-less-software/ ; AWS Well-Architected — https://aws.amazon.com/architecture/well-architected/
[^10]: StaffEng, "Staff archetypes" — https://staffeng.com/guides/staff-archetypes/
[^11]: StaffEng, "Operating at Staff" — https://staffeng.com/guides/operating-at-staff/
[^12]: StaffEng, "Learn to never be wrong" — https://staffeng.com/guides/learn-to-never-be-wrong/ ; "To lead, you have to follow" — https://staffeng.com/guides/to-lead-follow/
[^13]: StaffEng, "Present to executives" — https://staffeng.com/guides/present-to-executives/
[^14]: StaffEng, "Work on what matters" — https://staffeng.com/guides/work-on-what-matters/ ; "Manage technical quality" — https://staffeng.com/guides/manage-technical-quality/
[^15]: Lara Hogan, "What does sponsorship look like?" — https://larahogan.me/blog/what-sponsorship-looks-like/
[^16]: StaffEng, "Create space for others" — https://staffeng.com/guides/create-space-for-others/
[^17]: StaffEng, "Staying aligned with authority" — https://staffeng.com/guides/staying-aligned-with-authority/ ; "Build a network of peers" — https://staffeng.com/guides/network-of-peers/
[^18]: Tanya Reilly, "Being Glue" — https://www.noidea.dog/glue
[^19]: Tanya Reilly, *The Staff Engineer's Path* — https://newsletter.pragmaticengineer.com/p/the-staff-engineers-path ; Will Larson, "The forty year career" — https://lethain.com/forty-year-career/
