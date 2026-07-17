# 01 · What Is Required From a Staff / Principal Engineer

This file answers: *What is the role, really? What changes from Senior? What are they evaluating?*

---

## 1. Where Staff & Principal sit on the ladder

Most companies have a **dual-track ladder**: a management track and a parallel **individual-contributor (IC) technical-leadership track**. The canonical IC sequence is:

```
… → Senior → Staff → Principal → Distinguished → Fellow
                └─────── "Staff-plus" ───────┘
```

Key facts:

- **Senior is the "career level."** You're expected to reach it (often in 5–8 years) and can remain there indefinitely; "being promoted further is an exception rather than expected."[^1]
- **"Staff-plus"** is the umbrella term for Staff, Principal, Distinguished. Companies with only one such title almost always call it *Staff*.[^1]
- Not every company has every rung. **Amazon and Microsoft skip a distinct "Staff"** — Amazon jumps L6 Senior SDE → L7 Principal; Microsoft goes Senior (63–64) → Principal (65+).[^2]

**Numeric level bands (via levels.fyi):**[^2]

| Company | Senior | Staff | Principal | Beyond |
|---|---|---|---|---|
| Google | L5 | **L6** (Staff), L7 Sr Staff | **L8** Principal | L9 Distinguished, L10 Fellow |
| Meta | E5 | **E6** (Staff) | **E7** (Principal) | E8+ |
| Amazon | L6 (Sr SDE) | *(none)* | **L7** Principal, L8 Sr Principal | L10 Distinguished |
| Microsoft | 63–64 | *(none)* | **65–67** Principal | 68–69 Partner, 80 Fellow |
| Dropbox | IC4 | **IC5** | **IC6**, IC7 Sr Principal | — |

> ⚠️ **Titles mean wildly different things across companies.** At Skyscanner, Principal is *one* level above Senior (~1 per 15–30 engineers); at Uber, Principal is *four* levels above Senior (~1–2 principals per 4,000 engineers). A title *cut* can even come with a comp *raise* when you move to a higher-tier company.[^3]

---

## 2. The definition to internalize

> "Being a Staff engineer is not just a role. It's the intersection of **the role, your behaviors, your impact, and the organization's recognition of all those things.**"[^4]

All four must be present. You can do the *work* of a Tech Lead without the *impact* of a Staff engineer — and you won't be promoted until the organization *recognizes* it.

**Charity Majors' structural caveat — "is there oxygen at the next level?"**: higher levels require the company to actually *need* more of that kind of engineer. "There is only so much oxygen at each level." If the role you want doesn't exist at your company, you may need to change teams or companies.[^5]

---

## 3. The four Staff archetypes (Will Larson)

"Staff-plus" hides four distinct patterns. Most companies emphasize one or two. **Pick the one that energizes you *and* that your company needs.**[^6]

| Archetype | What it is | Appears when | First Staff role? |
|---|---|---|---|
| **Tech Lead** | Guides approach & execution of **one team or a cluster**; partners closely with a manager. ~1 per 8 engineers. Most common. | Always | ✅ Usually the first |
| **Architect** | Owns direction, quality & approach of a **critical, enduring domain** (API design, storage, frontend, cloud infra). *Not* ivory-tower — keeps "an intimate understanding of the business' needs, users' goals, and technical constraints." | ~100+ engineers | Sometimes |
| **Solver** | Trusted agent who goes **deep on knotty, high-risk problems**, bouncing from hotspot to hotspot. | Individual-ownership cultures | Sometimes |
| **Right Hand** | Extends an **executive's scope and authority** — "a senior organizational leader without direct reports." | Hundreds→1,000+ engineers | Rarely |

**Choosing:** Tech Lead & Architect work with the same people/problems for years (strong team sense). Solver & Right Hand bounce fire-to-fire, tightly aligned to executive priorities. Over a long career you'll sample several.[^6]

---

## 4. Tanya Reilly's three pillars

*The Staff Engineer's Path* frames the role as three skill areas:[^7]

1. **The Big Picture** — understand the company's technical landscape; know the *"shadow org chart"* (where decisions really get made and by whom); balance short-term delivery vs long-term scalability.
2. **Execution** — *"Leading a project means deliberately driving it."* Maxims: **"Wrong is better than vague"** ("wrong gets corrected, but vague sticks around"); write things down; **aggressively delegate**. *"What makes a great project lead? It's rarely genius: it's perseverance, courage, and a willingness to talk to other people."*
3. **Leveling Up others** — mentorship, sponsorship, role-modeling. **Measure impact by outcomes** (team efficiency, reliability), not outputs (lines of code).

Other core concepts: **social capital as a "bank account"** you pay into and spend; **managing up**; owning ambiguity (*"Whenever there's a feeling of 'someone should do something here,' there's a reasonable chance that the someone is you"*); **"no snacking"** (don't let low-priority work preempt high-priority work).[^7]

---

## 5. What Staff engineers actually *do* — the 5 shared activities

Regardless of archetype, the work is built on five things (and coding becomes *auxiliary*, not central):[^8]

1. **Setting & editing technical direction** — "accountable to the business and organization first, yourself second."
2. **Mentorship & sponsorship** — "You're far more likely to change your company's long-term trajectory by growing the engineers around you than through personal heroics." Pair *a little* mentorship with *considerably more* sponsorship.
3. **Providing engineering perspective** — get pulled "into the room" for time-sensitive decisions; represent *all* of engineering, not just yourself.
4. **Exploration** — ambiguous, high-risk, important problems ("hill-climbing on a foggy day").
5. **Being glue** — the invisible work that keeps everyone moving in the same direction.

On coding (Joy Ebertz): *"The more senior you get, the less your job is about code… even through principal, you'll likely be doing at least some coding."* Most write some, some write none — none write as much as they used to.[^8]

---

## 6. The exact gap: Senior → Staff → Principal

| Dimension | Senior | Staff | Principal / Staff+ |
|---|---|---|---|
| **Scope** | A team / a system | **Multiple teams** | **Org / company / industry** |
| **Impact** | Team features & improvements | Multiplies team output; drives architecture/process change | Moves top-line business metrics; sets company direction |
| **Autonomy** | Self-directs within team goals | Largely self-directs; *no standard path* | Operates like a senior leader without reports |
| **Ambiguity** | Breaks down given problems | Takes on unscoped, important, exploratory problems | Existential / strategic problems |
| **Time horizon** | Sprint → quarter | **Weeks, months, years** | Multi-year strategy |
| **Primary activity** | Writing code | Direction, sponsorship, glue, "in the room" | Strategy, editing org direction |
| **Authority** | Own output & expertise | **Borrowed / proxied** authority + earned trust | Borrowed executive authority |
| **Success measured by** | Your own shipped work | **The progress of people/teams around you** | Business impact at org/company scale |

**Dropbox's one-line scopes make it concrete:**[^9]
- **IC4 Senior:** "I autonomously deliver ongoing business impact across a team, product capability, or technical system."
- **IC5 Staff:** "I set the **multi-year, multi-team** technical strategy and deliver it through direct implementation or broad technical leadership."
- **IC6 Principal:** "I deliver results against a **strategic company-level business objective**… transcend organizational boundaries."

The through-line as you climb: **ambiguity handled** (open-ended → company-strategic), **time horizon** (annual → multi-year → industry-leading), **reach** (team → other teams → whole org), and **whose priorities you optimize** (your team → wider eng org → the company).

---

## 7. The competency matrices they score you against

### jorgef/engineeringladders — 5 axes you can self-score 1–5[^10]

| Axis | 1 | 2 | 3 | 4 | 5 |
|---|---|---|---|---|---|
| **Technology** | Adopts | Specializes | Evangelizes | Masters | Creates |
| **System** | Enhances | Designs | Owns | Evolves | Leads |
| **People** | Learns | Supports | Mentors | Coordinates | Manages |
| **Process** | Follows | Enforces | Challenges | Adjusts | Defines |
| **Influence** | Subsystem | Team | Multiple Teams | Company | Community |

The Senior→Principal climb is largely an **Influence-axis climb** plus Technology (Masters→Creates) and System (Owns→Evolves→Leads):
- **D4 (entry Senior):** Evangelizes · Owns · Mentors · Challenges · **Team**
- **D5 (Staff-ish):** Masters · Evolves · Mentors · Adjusts · **Multiple Teams**
- **D6 (Sr Staff/Principal):** Creates · Leads · Mentors · Adjusts · **Company**
- **D7 (Principal):** Creates · Leads · Mentors · Adjusts · **Community**

### Artsy — 4 evaluation criteria[^5]
**Knowledge Leadership** · **Impact** · **Influence** · **Discretion**. Scope ladder: `Self → Team → Teams → Org`. Staff = "can drive change and impact across **multiple teams**." The pivotal line: *"Increased mastery is a necessary condition but isn't sufficient to expand impact."*

### Dropbox — the *what* vs the *how*[^9]
- **Level Expectations** = scope, collaborative reach, and levers for impact (the *what*).
- **Core & Craft Responsibilities** across 4 pillars — **Results, Direction, Talent, Culture** (the *how*).
- Five **Levers for Impact** (you don't need all): Domain Expertise · Innovation · Product Expertise · Project Leadership · Technical Leadership & Mentorship.
- Impact = **Consistency × Velocity × Accountability**.

> Every ladder explicitly says it is **"not a promotion checklist"** — it describes what impact *could* look like at the next level.[^9][^10]

---

## 8. The mindset shifts that matter most

1. **From hero → force-multiplier.** Grow the engineers around you.[^8]
2. **From "being right" → being effective.** *"Learn to never be wrong"* — shift from winning arguments to understanding and communication; be right *while creating space for others* (don't "suck the oxygen out of the room").[^11]
3. **From self-interest → org-interest.** In the room, you represent all of engineering.[^8]
4. **Authority is borrowed, not granted by title.** It depends on staying aligned, trustworthy, and predictable.[^11]
5. **Tolerate slow feedback loops.** Replace the coding REPL's quick dopamine with the "uneven progress of mentorship, relationships, and strategy." *"It's normal to end some days… feeling like you haven't accomplished anything—keep at it!"*[^8]
6. **Reputation is a first-class deliverable.** *"You've probably already got the technical chops; what you need to do is work on your reputation."*[^12]

---

## 9. Myths & pitfalls to avoid

- **Myth: "Just do your job well and you'll get promoted."** False — you also need *visibility, sponsorship, and to work on the right things.* "You won't get promoted if your company's leadership doesn't know who you are."[^13]
- **Myth: "Staff = Senior with more coding skill."** False — it's a *role change*, not a skill upgrade.[^1]
- **Myth: "Promotion first, then I'll operate at the level."** Backwards — promotion is a **lagging indicator**; perform at the next level for months *first*.[^10]
- **Pitfall — how you spend time:**[^14]
  - **Snacking** — easy + low-impact work (psychologically rewarding, low leverage).
  - **Preening** — low-impact but *high-visibility* work ("many companies conflate high-visibility and high-impact").
  - **Chasing ghosts** — high-effort + low-impact (e.g., a strategy that misunderstands the real problem).
- **Pitfall — unmanaged glue work.** Managed deliberately it builds leadership credibility; left unconscious it's *career-limiting* — "it can push people into less technical roles and even out of the industry."[^4]
- **Reality — it's not a pure meritocracy.** Reaching Staff is "a combination of luck, timing, and work." Opportunity is unevenly distributed (infra vs product, HQ vs remote), and there's a documented structural disadvantage for underrepresented engineers.[^13]

---

### Sources

[^1]: StaffEng, "Getting the title where you are" — https://staffeng.com/guides/getting-the-title-where-you-are/ ; "Overview" — https://staffeng.com/guides/overview-overview/
[^2]: levels.fyi — Google https://www.levels.fyi/companies/google/salaries/software-engineer · Meta https://www.levels.fyi/companies/meta/salaries/software-engineer · Amazon https://www.levels.fyi/companies/amazon/salaries/software-engineer · Microsoft https://www.levels.fyi/companies/microsoft/salaries/software-engineer
[^3]: Gergely Orosz, "The Seniority Rollercoaster" — https://newsletter.pragmaticengineer.com/p/the-seniority-rollercoaster
[^4]: Will Larson, "Staff archetypes" — https://staffeng.com/guides/staff-archetypes/ ; Tanya Reilly, "Being Glue" — https://www.noidea.dog/glue
[^5]: Charity Majors, "Useful things to know about engineering levels" — https://charity.wtf/p/useful-things-to-know-about-engineering-levels ; Artsy ladder — https://github.com/artsy/README/blob/main/careers/ladder.md
[^6]: StaffEng, "Staff archetypes" — https://staffeng.com/guides/staff-archetypes/
[^7]: Tanya Reilly, *The Staff Engineer's Path* (O'Reilly, 2022) — https://noidea.dog/staff ; summary: https://www.goodreads.com/book/show/61058107-the-staff-engineer-s-path
[^8]: StaffEng, "What do Staff engineers actually do?" — https://staffeng.com/guides/what-do-staff-engineers-actually-do/
[^9]: Dropbox Engineering Career Framework — https://dropbox.github.io/dbx-career-framework/ (IC5 Staff, IC6 Principal pages; what_is_impact.html)
[^10]: jorgef/engineeringladders — https://github.com/jorgef/engineeringladders
[^11]: StaffEng, "Learn to never be wrong" — https://staffeng.com/guides/learn-to-never-be-wrong/ ; "Operating at Staff" — https://staffeng.com/guides/operating-at-staff/
[^12]: StaffEng, "Being visible" — https://staffeng.com/guides/being-visible/
[^13]: StaffEng, "Getting the title where you are" — https://staffeng.com/guides/getting-the-title-where-you-are/
[^14]: StaffEng, "Work on what matters" — https://staffeng.com/guides/work-on-what-matters/
