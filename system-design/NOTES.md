# Notes & Preferences

## Learner profile
- **Senior** engineer targeting **Staff/Principal**. This course covers competency **A1** of the broader
  plan in [`../02-skills-to-upgrade.md`](../02-skills-to-upgrade.md).
- Already builds and runs production services. Needs to move from “knows the patterns” to
  **leading the design conversation** and naming the trade-off behind every choice.

## Teaching preferences
- Visual and interactive: HTML lessons with Mermaid diagrams, scenario-based quizzes, “try it”
  exercises and a one-page cheat sheet per lesson, in the same format as `../event-sourcing`,
  `../Observability` and `../async-messaging`.
- `GUIDE.md` is the long-form markdown edition with Mermaid diagrams. Use it to read end to end or to search.
- The **4-step method** (requirements → high-level design → core components → scale it and name the
  trade-offs) is the spine. Every lesson says which step it serves.
- Every lesson ends with **staff-level judgement**: what “good” looks like, common pitfalls,
  and the questions to ask in a design review.
- “Try it” exercises use the learner's **own systems** (dashboards, configs, call graphs) rather than toy examples.

## Conventions
- Course identity colour: **cobalt** (#1f4f8f). Siblings: event-sourcing **indigo** (#4b3f8f),
  Observability **teal** (#14615f), async-messaging **brick** (#7a2e2e), technical-skills **bronze**
  (#8a5a12), leadership-skills **plum** (#6d2e6b).
- Lessons live in `lessons/` as HTML using the shared `/assets/course/` styles and scripts plus this
  topic's `assets/theme.css`. Cheat sheets live in `reference/`.
- Quizzes: 3 per lesson, 4 options of similar length, per-option feedback, and an explanation after answering.
- Terms follow [`GLOSSARY.md`](GLOSSARY.md) exactly. Sources come from [`RESOURCES.md`](RESOURCES.md).
- Estimates round to powers of ten, keep one significant figure, and state their assumptions.
