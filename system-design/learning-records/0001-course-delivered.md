# Course delivered: system design & architecture at scale, senior → Staff/Principal

The learner (senior → Staff/Principal) had eight markdown deep dives covering competency A1 of
[`../../02-skills-to-upgrade.md`](../../02-skills-to-upgrade.md). They were converted into the same
interactive course format as `../../event-sourcing`:

- [`../GUIDE.md`](../GUIDE.md): the long-form markdown edition with Mermaid diagrams, the 4-step method,
  a self-assessment, a 6-week study plan and the capstone.
- [`../index.html`](../index.html): the course map. It has 8 HTML lessons in `../lessons/`, each with a
  mission tie, 3 scenario quizzes, “try it” exercises on your own systems, a primary source and a
  follow-up, plus 8 print-friendly cheat sheets in `../reference/`.
- Course docs: [`../MISSION.md`](../MISSION.md), [`../NOTES.md`](../NOTES.md),
  [`../GLOSSARY.md`](../GLOSSARY.md) (45 canonical terms) and [`../RESOURCES.md`](../RESOURCES.md).

## Structure
The 4-step method is the spine: requirements & constraints → high-level design → core components →
scale it and name the trade-offs.

| Part | Lessons | Serves step |
|---|---|---|
| Mental models | [01 Scalability trade-offs](../lessons/0001-scalability-tradeoff-axes.html), [02 CAP & consistency](../lessons/0002-cap-theorem-consistency-models.html) | 4: naming the trade-off |
| Building blocks | [03 Databases](../lessons/0003-databases-and-data-modeling.html), [04 Caching](../lessons/0004-caching.html), [05 Async & messaging](../lessons/0005-asynchronism-and-messaging.html), [06 Networking & APIs](../lessons/0006-networking-and-api-design.html) | 3 and 4: components and scaling |
| Quantitative glue | [07 Estimation](../lessons/0007-back-of-envelope-estimation.html) | 1: requirements and numbers |
| Staying up | [08 Resilience](../lessons/0008-resilience-and-fault-tolerance.html) | 4: hardening against failure |

## Next steps
Baseline assumption: an experienced service builder who knows most patterns by name but has not led
open-ended design reviews. Start with Lesson 01, then follow the week-by-week plan in `../GUIDE.md`.
Finish with the capstone: a 45-minute design review using the 4-step method, with the
[estimation cheat sheet](../reference/0007-back-of-envelope-estimation-cheatsheet.html) and the
[resilience cheat sheet](../reference/0008-resilience-and-fault-tolerance-cheatsheet.html) at hand.
