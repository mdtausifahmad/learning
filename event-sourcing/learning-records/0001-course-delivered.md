# Course delivered: event sourcing, level zero → Staff

The learner (senior → Staff) asked for a ground-up event-sourcing guide, first in markdown and then as
interactive HTML like `../Observability`. Both were delivered:

- `GUIDE.md`: the long-form markdown edition with Mermaid diagrams (Levels 0–5, capstone, 8-week plan).
- `index.html`: 16 lessons and 12 reference sheets with step-through animations, quizzes and five live
  simulators (replay, concurrency, gap, projection, upcast).
- `assets/lab/`: a runnable Postgres event store (Podman), verified for append/OCC, immutability,
  projection rebuild and the gap-safe read.

Baseline assumption: strong CRUD builder, new to ES. Natural next step: Lesson 01, then build the lab's
Java decider (Lesson 03) against `append_events`.
