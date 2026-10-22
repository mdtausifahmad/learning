# Notes & Preferences

## Learner profile
- Mid-level engineer targeting **Staff/Principal**; part of a broader plan
  (see `../README.md` and `../technical-skills/A3-reliability-observability-sre.md` — the curriculum spine).
- **Strong builder, green operator**: builds services but rarely runs them in prod — no on-call,
  no SLOs owned yet. Calibrate *up from the operator mindset*, not from senior-SRE assumptions.
- Driver: **both** — durable Staff-level judgment *and* practical application to systems he'll own.

## Teaching preferences
- **Medium time budget** (3–5 hrs/week) → short lessons, one win each, completable in a sitting.
- Bias to **concrete practice and real trade-offs** over theory (this topic is skills-heavy).
- Ground examples in his stack: **Prometheus + Grafana, OpenTelemetry, cloud-native monitoring**
  (CloudWatch / GCP Monitoring / Azure Monitor).
- Sibling course `../async-messaging` uses the same Tufte lesson format — keep the family consistent
  (this workspace has its own **teal** identity to tell the courses apart).

## Working notes
- Curriculum arc (teach one tightly-scoped lesson at a time, in ZPD; sequence tracks the A3 doc):
  1. Why SRE? Reliability as a measured target, not heroics — SLI → SLO → error budget  ← **lesson 0001**
  2. Choosing good SLIs: the success-ratio model; user journeys; the menu of SLI types  ← **lesson 0002**
  3. Setting the SLO: choosing the target, the window (rolling vs calendar), computing the budget  ← **lesson 0003**
  4. Error-budget **policy**: turning the budget into ship/freeze decisions (the SLO's teeth)  ← **lesson 0004**
  5. Burn-rate alerting on SLOs: multi-window / multi-burn-rate; killing alert noise  ← **lesson 0005**
  6. Observability I: metrics, logs, traces — the three pillars & what each is for  ← **lesson 0006**
  7. The four golden signals (+ RED / USE); designing telemetry, cardinality, naming  ← **lesson 0007**
  8. Instrumentation in practice: Prometheus + OpenTelemetry; PromQL basics (needs a local lab)  ← **lesson 0008**
  9. On-call & the incident lifecycle: roles, severity, comms, mitigation  ← **lesson 0009**
  10. Blameless postmortems that reduce risk (not paperwork)  ← **lesson 0010**
  11. Overload & cascading failure: load shedding, backoff + jitter, circuit breakers, bulkheads  ← **lesson 0011**
  12. Graceful degradation & the thundering herd  ← **lesson 0012**
  13. Progressive delivery: canary, feature flags, safe & fast rollback  ← **lesson 0013**
  14. Toil & automation; simplicity as a reliability control  ← **lesson 0014**
  15. NALSD: reasoning about reliability from concrete production numbers (Staff judgment)  ← **lesson 0015 · CORE ARC COMPLETE ✅**
  — Extensions (post-core, taught on request / in ZPD):
  16. Chaos engineering: verify resilience by experiment; GameDays  ← **lesson 0016**
- **Quiz rule enforced**: answer options kept ~equal length / word-count so formatting leaks no clues.
- **Storage vs fluency**: re-surface SLO / error-budget vocab via spaced retrieval in later lessons;
  interleave resilience patterns once several are known.
- **Core 15-lesson arc delivered (2026-07-24)** — 15 lessons + 16 reference sheets + a runnable lab +
  `index.html` course map. Next phase: shift from new-material to (a) **application** — run his real
  services / designs / incidents through the methods; (b) **wisdom** — a community (ask preference
  first); (c) **spaced retrieval** to convert fluency → storage strength. Optional depth if wanted:
  capacity planning, chaos engineering, multi-region/DR, tracing internals, SLO tooling (Sloth).
- **Step-by-step animations added to all 16 lessons (2026-07-24)** — reusable engine `../assets/course/anim.js`
  + styles in `../assets/course/course.css` (Back/Play/Next/Replay, step dots, cumulative reveal via
  `data-step`/`data-only`/`data-from`/`data-to`; print-safe = all steps shown, controls hidden).
  Each lesson's core-concept figure is now a click-through explainer with one caption per step.
  Reuse-first: new lessons should drive the SAME engine (markup convention documented at the top of
  `anim.js`), never inline bespoke animation code. Learner values visual, build-up explanations.
- **Shared course assets** — `course.css`, `quiz.js`, `anim.js`, `stepflow.js`, `mermaid.js` and `links.js`
  live in `../assets/course/` and are shared by every topic; `assets/theme.css` holds only this
  course's teal palette. `GUIDE.md` is the long-form markdown edition of all lessons.
