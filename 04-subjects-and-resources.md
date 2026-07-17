# 04 · Subjects & Resources — The Study Curriculum

A concrete, **sequenced** self-study plan. Follow the order, budget **~6 hrs/week over 12–18 months**, and you'll build genuine Staff/Principal-level knowledge. Allocate roughly **70% technical / 20% hands-on / 10% continuous reading**, with the leadership track ramping in the second half.

> 💡 Prices are approximate (vendor pages are JS-rendered — verify on-site). All course *scope/format* details are verified from the sources cited.

---

## Recommended learning order & rationale

| Order | Topic | Why here | Effort |
|---|---|---|---|
| 1 | **System-design breadth** | Shared vocabulary (caching, sharding, LB, CAP, queues) everything builds on | 40–60 hrs |
| 2 | **Distributed-systems depth (DDIA)** | The theoretical spine: replication, consistency, consensus | 50–70 hrs |
| 3 | **Reliability / SRE** | How the above behaves in production | 25–35 hrs |
| 4 | **Software architecture** | Zoom out to system/org-level trade-offs & decisions | 45–55 hrs |
| 5 | **Security (cross-cutting)** | A focused late pass; also woven throughout | 15–20 hrs |
| — | **Leadership (parallel track)** | Ramps in the second half — the real differentiator | 30–40 hrs |

The primer itself says *"Start broad and go deeper in a few areas."* Cambridge/MIT syllabi list DDIA as the depth reading *after* fundamentals.[^1][^2]

---

## Phased plan (18-month version, 6 quarters)

### Q1 — System Design Foundations (Months 1–3, ~70 hrs)
- Read the **System Design Primer** breadth (topic index, the 4-step method, latency numbers) + **Alex Xu, *System Design Interview* Vol 1**.[^3]
- Take **one** structured course start-to-finish (ByteByteGo *or* Grokking SDI).[^4]
- **Hands-on:** do back-of-envelope estimates; write 2–3 real design docs at work; start an ADR log.
- **Continuous (start now, never stop):** subscribe to ByteByteGo + The Pragmatic Engineer.

### Q2 — Distributed Systems Depth (Months 4–6, ~75 hrs)
- **DDIA** cover-to-cover (~1 chapter / 1.5 weeks). Pair the hard chapters (5 Replication, 7 Transactions, 8 Trouble with Distributed Systems, 9 Consistency & Consensus) with **Kleppmann's video lectures** + free notes.[^5]
- **Hands-on:** implement MIT 6.5840 **Lab 1 (MapReduce)**; begin **Lab 2 (Raft)**.[^2]

### Q3 — Distributed Systems Practice + SRE (Months 7–9, ~75 hrs)
- Finish the **Raft lab**; read 3–4 landmark papers (MapReduce, GFS, Dynamo, Spanner — all linked from the primer).[^3]
- **Google SRE book** core chapters (SLIs/SLOs, error budgets, toil, on-call, postmortems) + **Well-Architected Reliability pillar**.[^6][^7]
- **Hands-on:** define SLOs for a real service; run a game-day/chaos exercise; write a blameless postmortem.

### Q4 — Software Architecture (Months 10–12, ~75 hrs)
- **Fundamentals of Software Architecture** (Ford & Richards) + **Martin Fowler's architecture guide**.[^8]
- **AWS Well-Architected Framework** end-to-end (6 pillars) + Well-Architected Labs.[^7]
- **Hands-on:** run an "architecture kata"; write an architecture-characteristics analysis + ADR set for a real system.

### Q5 — The Hard Parts + Security + Leadership ramp (Months 13–15, ~75 hrs)
- **Software Architecture: The Hard Parts** (service granularity, data ownership, sagas, distributed transactions).[^8]
- **Security pass:** OWASP Top 10 + Cheat Sheets + Well-Architected Security pillar; threat-model a real service.[^9]
- **Leadership:** StaffEng guides + start **The Staff Engineer's Path**; begin authoring RFCs and leading design reviews.[^10]

### Q6 — Integration & Influence (Months 16–18, ~75 hrs)
- **Staff Engineer: Leadership Beyond the Management Track** (Larson); drive one cross-team initiative end-to-end.[^10]
- Contribute to OSS; publish a blog post or give an internal tech talk; run monthly mock system-design sessions (as candidate *and* interviewer).

> **12-month compression:** merge Q2+Q3 (DDIA + Raft + SRE essentials, skip MapReduce lab) and Q5+Q6 (one architecture book + security + leadership); defer "The Hard Parts" to continuous reading.

---

## Resource catalog (with URLs, cost & duration)

### System Design
| Resource | Notes | Cost | Time |
|---|---|---|---|
| **System Design Primer** (donnemartin) — https://github.com/donnemartin/system-design-primer | Free, CC-BY. Study plan by timeline, 4-step method, topic index, 8 worked problems, Anki decks, latency table, "real-world architectures" papers | Free | 40–60h |
| **ByteByteGo — System Design Interview** — https://bytebytego.com/courses/system-design-interview/foreword | Text + illustrations, by Alex Xu & Sahn Lam; free previews | ~$16/mo or ~$130/yr | 20–40h |
| **Grokking the System Design Interview** — https://www.designgurus.io/course/grokking-the-system-design-interview | Built by ex-FAANG hiring managers, 440k+ learners | ~$79 one-time | 20–30h |
| **Grokking SDI (Educative)** — https://www.educative.io/courses/grokking-the-system-design-interview | RESHADED framework, 13+ case studies, AI mock interviews | ~$199/yr sub | 20–30h |
| **Alex Xu, *System Design Interview* Vol 1 & 2** — https://bytebytego.com | 4-step framework + worked designs (rate limiter, KV store, URL shortener, chat, YouTube, message queue, metrics, ad aggregation…) | ~$30–40 each | — |

### Distributed Systems
| Resource | Notes | Cost | Time |
|---|---|---|---|
| **Designing Data-Intensive Applications** (Kleppmann) — https://dataintensive.net/ | *The* depth text. Part I foundations · II distributed data · III derived data | ~$40–50 | 40–50h |
| **MIT 6.5840** (formerly 6.824) — https://pdos.csail.mit.edu/6.824/ | Grad course: lectures + papers + **Go labs** (MapReduce, Raft, fault-tolerant KV, sharded KV) | Free | 40–80h |
| **Kleppmann's Distributed Systems lectures** — https://www.youtube.com/playlist?list=PLeKd45zvjcDFUEv_ohr_HdUFe97RItdiB + notes https://www.cl.cam.ac.uk/teaching/2122/ConcDisSys/ | Clocks, broadcast, replication, quorums, **consensus/Raft**, 2PC, linearizability, CAP, CRDTs | Free | 8–12h |
| **DDIA references** (Kleppmann's own) — https://github.com/ept/ddia-references | Confirms structure; source for going deeper | Free | — |
| **Patterns of Distributed Systems** (Fowler/Joshi) — https://martinfowler.com/articles/patterns-of-distributed-systems/ | WAL, quorum, leader/followers, Lamport clock, lease, gossip… | Free | — |
| *Free companions:* Tanenbaum, *Distributed Systems* 3rd ed — https://www.distributed-systems.net/index.php/books/ds3/ ; Jepsen — https://jepsen.io | | Free | — |

### Reliability / SRE
- **Google SRE Book** (free) — https://sre.google/sre-book/table-of-contents/
- **Google SRE Workbook** (free, hands-on) — https://sre.google/workbook/table-of-contents/
- All SRE books — https://sre.google/books/
- *(Adjacent)* *Observability Engineering* (Honeycomb/O'Reilly) — the three pillars & high-cardinality observability.

### Software Architecture
- **Fundamentals of Software Architecture** (Ford & Richards) — https://www.amazon.com/Fundamentals-Software-Architecture-Engineering-Approach/dp/1492043451 · characteristics, styles, trade-off analysis, architect soft skills · ~$50–60 · 20–25h
- **Software Architecture: The Hard Parts** — https://www.amazon.com/Software-Architecture-Trade-Off-Distributed-Architectures/dp/1492086894 · service granularity, distributed data, workflow/transactions · 25–30h
- **Martin Fowler — Software Architecture Guide** (free) — https://martinfowler.com/architecture/
- **Strangler Fig pattern** — https://martinfowler.com/bliki/StranglerFigApplication.html
- *Building Evolutionary Architectures* (Ford/Parsons/Kua) — fitness functions.

### Security
- **OWASP Top 10** — https://owasp.org/www-project-top-ten/
- **OWASP Cheat Sheet Series** — https://cheatsheetseries.owasp.org/

### Cloud
- **AWS Well-Architected Framework** (free, 6 pillars) — https://aws.amazon.com/architecture/well-architected/
- **AWS Well-Architected Labs** (free, hands-on) — https://www.wellarchitectedlabs.com/
- **AWS Builders' Library** (free deep operational essays) — https://aws.amazon.com/builders-library/
- Heuristics: **Choose Boring Technology** — https://mcfunley.com/choose-boring-technology · **Run Less Software** — https://www.intercom.com/blog/run-less-software/

### Engineering Strategy, Vision & Writing
- StaffEng, "Writing engineering strategy" — https://staffeng.com/guides/engineering-strategy/
- Will Larson, "Eng strategies" (Rumelt framework) — https://lethain.com/eng-strategies/ · *An Elegant Puzzle* — https://lethain.com/elegant-puzzle/
- **Design Docs at Google** — https://www.industrialempathy.com/posts/design-docs-at-google/
- **ADRs** — https://adr.github.io/ + templates https://github.com/joelparkerhenderson/architecture-decision-record + Nygard's original https://www.cognitect.com/blog/2011/11/15/documenting-architecture-decisions
- **RFC culture** (Pragmatic Engineer) — https://blog.pragmaticengineer.com/scaling-engineering-teams-via-writing-things-down-rfcs/
- Example strategies to study: "How Big Technical Changes Happen at Slack" — https://slack.engineering/how-big-technical-changes-happen-at-slack/

### Leadership / Staff+ track (the real differentiator)
- **StaffEng guides** (free, read all) — https://staffeng.com/guides/
- **Staff Engineer: Leadership Beyond the Management Track** (Larson) — https://staffeng.com/book
- **The Staff Engineer's Path** (Tanya Reilly, O'Reilly) — https://noidea.dog/staff · excerpts https://newsletter.pragmaticengineer.com/p/the-staff-engineers-path
- **Being Glue** (Reilly) — https://www.noidea.dog/glue
- **What sponsorship looks like** (Lara Hogan) — https://larahogan.me/blog/what-sponsorship-looks-like/
- **The Pyramid Principle** (Barbara Minto) — communication craft (referenced in "Present to executives")
- *The Manager's Path* (Camille Fournier) — relevant leadership concepts even for ICs.

---

## Hands-on practice (do these alongside the reading)

1. **Build/implement:** MIT 6.5840 Go labs (MapReduce → Raft → sharded KV), or build a URL-shortener / rate-limiter / KV-store from Alex Xu Vol 1.[^2][^3]
2. **Mock system-design sessions:** one/month using primer problems — practice the 4-step method. (Educative's course has AI mock interviews.)[^3]
3. **Write design docs / RFCs / ADRs for real work** — adopt ADRs from https://adr.github.io/ and https://github.com/joelparkerhenderson/architecture-decision-record.[^11]
4. **SRE practice:** define SLIs/SLOs + error budgets; write a blameless postmortem (Workbook templates).[^6]
5. **Contribute to OSS** — builds public credibility (a "being visible" lever).[^12]
6. **Study real architectures:** work the primer's papers table (MapReduce, GFS, Bigtable, Dynamo, Spanner, Kafka, Zookeeper, Dapper).[^3]
7. **Architecture katas:** trade-off analysis on invented briefs (paired with the architecture books).

---

## Follow continuously — blogs, newsletters, podcasts

**Newsletters:** ByteByteGo — https://blog.bytebytego.com/ · The Pragmatic Engineer — https://newsletter.pragmaticengineer.com/ · InfoQ — https://www.infoq.com/

**Company engineering blogs:** Netflix — https://netflixtechblog.com/ · Uber — https://www.uber.com/blog/engineering/ · Cloudflare — https://blog.cloudflare.com/ · Stripe — https://stripe.com/blog/engineering · Discord — https://discord.com/category/engineering · Slack — https://slack.engineering/ · Meta — https://engineering.fb.com/ · *(aggregator)* High Scalability — http://highscalability.com/

**Podcasts / leadership media:** LeadDev (+ StaffPlus conferences) — https://leaddev.com/ · Software Engineering Daily — https://softwareengineeringdaily.com/ · The Pragmatic Engineer Podcast — https://newsletter.pragmaticengineer.com/podcast

**Roadmaps:** https://roadmap.sh/software-design-architecture · https://roadmap.sh/system-design · https://roadmap.sh/backend

---

## Master booklist (priority order)

1. **Designing Data-Intensive Applications** — Kleppmann *(the single highest-leverage technical book)*
2. **The Staff Engineer's Path** — Tanya Reilly *(the definitive "how to operate" book)*
3. **Staff Engineer: Leadership Beyond the Management Track** — Will Larson
4. **System Design Interview Vol 1 & 2** — Alex Xu
5. **Fundamentals of Software Architecture** — Ford & Richards
6. **Software Architecture: The Hard Parts** — Ford, Richards, et al.
7. **Site Reliability Engineering** + **The SRE Workbook** — Google (free online)
8. **An Elegant Puzzle** — Will Larson *(eng management/strategy)*
9. **The Manager's Path** — Camille Fournier
10. **The Pyramid Principle** — Barbara Minto *(communication)*

---

### Sources

[^1]: System Design Primer — https://github.com/donnemartin/system-design-primer ; Cambridge Concurrent & Distributed Systems — https://www.cl.cam.ac.uk/teaching/2122/ConcDisSys/
[^2]: MIT 6.5840 Distributed Systems — https://pdos.csail.mit.edu/6.824/
[^3]: donnemartin/system-design-primer — https://github.com/donnemartin/system-design-primer ; Alex Xu / ByteByteGo — https://bytebytego.com
[^4]: ByteByteGo — https://bytebytego.com/courses/system-design-interview/foreword ; DesignGurus — https://www.designgurus.io/course/grokking-the-system-design-interview ; Educative — https://www.educative.io/courses/grokking-the-system-design-interview
[^5]: Kleppmann, DDIA — https://dataintensive.net/ ; lectures — https://www.youtube.com/playlist?list=PLeKd45zvjcDFUEv_ohr_HdUFe97RItdiB ; notes — https://www.cl.cam.ac.uk/teaching/2122/ConcDisSys/
[^6]: Google SRE Workbook — https://sre.google/workbook/table-of-contents/ ; SRE Book — https://sre.google/sre-book/table-of-contents/
[^7]: AWS Well-Architected — https://aws.amazon.com/architecture/well-architected/ ; Labs — https://www.wellarchitectedlabs.com/
[^8]: Fundamentals of Software Architecture — https://www.amazon.com/dp/1492043451 ; The Hard Parts — https://www.amazon.com/dp/1492086894 ; Fowler — https://martinfowler.com/architecture/
[^9]: OWASP Top 10 — https://owasp.org/www-project-top-ten/ ; Cheat Sheets — https://cheatsheetseries.owasp.org/
[^10]: StaffEng guides — https://staffeng.com/guides/ ; *The Staff Engineer's Path* — https://noidea.dog/staff ; *Staff Engineer* — https://staffeng.com/book
[^11]: ADRs — https://adr.github.io/ ; templates — https://github.com/joelparkerhenderson/architecture-decision-record
[^12]: StaffEng, "Being visible" — https://staffeng.com/guides/being-visible/
