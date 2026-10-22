# From Senior → Staff → Principal Engineer

> Your personal, evidence-based playbook for the next stage of your engineering career.
> Built from the canonical Staff+ literature: Will Larson's *StaffEng* / *An Elegant Puzzle*, Tanya Reilly's *The Staff Engineer's Path*, Gergely Orosz's *The Pragmatic Engineer*, public engineering ladders (Dropbox, jorgef, Artsy, CircleCI, Medium), and 10 first-person promotion stories.

---

## The one idea that changes everything

**A Staff engineer is not "a better Senior engineer." It is a qualitatively different job.**[^1]

Senior is the industry's *terminal / career level* — you can stay there for a full career and that's completely fine.[^2] Crossing into Staff/Principal means the work that *used to be* your core job (writing code, shipping features) becomes *auxiliary*, and is replaced by:

- **Setting technical direction** ("part-time product manager for technology")[^3]
- **Multiplying others** through sponsorship & mentorship (not personal heroics)[^3]
- **Injecting engineering perspective** into org-level decisions ("being in the room")[^3]
- **Exploration** of ambiguous, high-risk, important problems[^3]
- **"Glue work"** — the invisible work that keeps teams aligned[^4]

> "Increased mastery is a **necessary condition but isn't sufficient** to expand impact."[^5]

And the promotion itself is a **lagging indicator**: you get the title by *already operating at the next level for 6–12+ months*, making that impact *visible*, and having a *sponsor* who advocates for you.[^6]

---

## How to use this folder

Read the files in order the first time, then return to them as reference and re-run the operating cadence (file 06) every week.

| # | File | What it gives you |
|---|------|-------------------|
| 📖 | **[01-what-is-required.md](01-what-is-required.md)** | What Staff/Principal actually *is*: definitions, the 4 archetypes, competency matrices, and the exact Senior→Staff→Principal gap. |
| 🧠 | **[02-skills-to-upgrade.md](02-skills-to-upgrade.md)** | Every topic to level up — technical **and** leadership — with "why it matters" and "what good looks like." |
| 🗺️ | **[03-career-progression-plan.md](03-career-progression-plan.md)** | The step-by-step, phased 12–18 month action plan combining study + leadership + a promotion campaign. |
| 📚 | **[04-subjects-and-resources.md](04-subjects-and-resources.md)** | The detailed, sequenced study curriculum — read this and you'll gain the knowledge. Courses, books, lectures, time estimates, URLs. |
| 🎯 | **[05-promotion-playbook.md](05-promotion-playbook.md)** | How to actually get the title: promo packet, staff projects, the 4 levers, and the committee process. |
| 📊 | **[06-self-assessment-and-tracking.md](06-self-assessment-and-tracking.md)** | Self-score yourself, run a brag doc, track milestones, and the manager 1:1 script. |
| 💬 | **[07-real-world-stories.md](07-real-world-stories.md)** | 10 first-person stories of people who did it — patterns and honest lessons. |

---

## Courses

Every course folder has the same shape: an `index.html` course map, one interactive lesson per chapter in `lessons/`, a cheat sheet per lesson in `reference/`, and a single **`GUIDE.md`** with the whole course in one Markdown file. Shared styles and scripts live in `assets/course/`; each course keeps only its colour theme in `<course>/assets/theme.css`.

| Course | Covers | Interactive | Full guide |
|---|---|---|---|
| **System Design & Architecture at Scale** | Skill A1 — scalability, CAP, data, caching, messaging, APIs, estimation, resilience | [8 lessons](system-design/index.html) | [GUIDE.md](system-design/GUIDE.md) |
| **Technical Skills** | Skills A2–A9 — distributed data, SRE, architecture, strategy, writing, security, cloud, T-shape | [8 lessons](technical-skills/index.html) | [GUIDE.md](technical-skills/GUIDE.md) |
| **Leadership Skills** | Skills B1–B8 — influence, exec writing, tech leadership, sponsorship, trust, glue work, decisions, prioritization | [8 lessons](leadership-skills/index.html) | [GUIDE.md](leadership-skills/GUIDE.md) |
| **Async Messaging** | Queues, Kafka, delivery guarantees, idempotency, outbox, sagas, CDC | [19 lessons](async-messaging/index.html) | [GUIDE.md](async-messaging/GUIDE.md) |
| **Event Sourcing** | Deciders, event stores, projections, schema evolution, GDPR | [16 lessons](event-sourcing/index.html) | [GUIDE.md](event-sourcing/GUIDE.md) |
| **Reliability & Observability** | SLOs, error budgets, golden signals, incidents, overload, safe change | [16 lessons](Observability/index.html) | [GUIDE.md](Observability/GUIDE.md) |

---

## Quick start — do these 5 things this week

1. **Self-assess.** Score yourself 1–5 on the [jorgef radar](06-self-assessment-and-tracking.md) (Technology, System, People, Process, Influence). The **Influence axis** — Team → Multiple Teams → Company — is the one to over-index on.[^7]
2. **Start a brag document** and a **weekly "5-15" update** (15 min to write, 5 min to read). These are the data pipeline that feeds your promotion.[^8]
3. **Pick your archetype.** Which energizes you — Tech Lead, Architect, Solver, or Right Hand? Does your company *need* it?[^9]
4. **Name your sponsor.** It's almost always your direct manager. Tell them explicitly: *"Attaining Staff is a goal of mine — what's missing?"*[^6]
5. **Start file 04's Quarter 1** (System Design Foundations) at ~6 hrs/week.

---

## The 30-second mental model

```
Operate at the next level  ─┐
  (scope beyond your team)  │
                            ▼
Make the impact VISIBLE  ──►  A sponsor converts it into a promo packet
  (brag doc, docs, talks)     and advocates for you in calibration
                            ▲
Work on what MATTERS  ──────┘
  (not "snacking" or "preening")
```

Every ladder in the industry converges on the same Staff-level signal: **influence and ownership beyond your own team.**[^10] Everything in this folder is in service of getting there — and being *recognized* for it.

---

[^1]: StaffEng, "Getting the title where you are" — https://staffeng.com/guides/getting-the-title-where-you-are/
[^2]: StaffEng, "Overview" (Senior = the "career level") — https://staffeng.com/guides/overview-overview/
[^3]: StaffEng, "What do Staff engineers actually do?" — https://staffeng.com/guides/what-do-staff-engineers-actually-do/
[^4]: Tanya Reilly, "Being Glue" — https://www.noidea.dog/glue
[^5]: Artsy Engineering Career Ladder — https://github.com/artsy/README/blob/main/careers/ladder.md
[^6]: StaffEng, "Promotion packets" — https://staffeng.com/guides/promo-packets/ ; "Find your sponsor" — https://staffeng.com/guides/find-your-sponsor/
[^7]: jorgef/engineeringladders — https://github.com/jorgef/engineeringladders
[^8]: Julia Evans, "Brag documents" — https://jvns.ca/blog/brag-documents/ ; Will Larson, "Weekly updates" — https://lethain.com/weekly-updates/
[^9]: Will Larson, "Staff archetypes" — https://staffeng.com/guides/staff-archetypes/
[^10]: Synthesis across jorgef (Influence axis), Dropbox (scope + collaborative reach), Honeycomb (ownership), CircleCI (task→org).
