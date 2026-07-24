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
  4. Error-budget **policy**: turning budget into ship/freeze decisions; burn-rate alerting
  5. Observability I: metrics, logs, traces — the three pillars & what each is for
  6. The four golden signals (+ RED / USE); designing telemetry, cardinality, naming
  7. Instrumentation in practice: Prometheus + OpenTelemetry; PromQL basics (needs a local lab)
  8. Alerting on **symptoms** (burn rate), not causes; killing alert noise
  9. On-call & the incident lifecycle: roles, severity, comms, mitigation
  10. Blameless postmortems that reduce risk (not paperwork)
  11. Overload & cascading failure: load shedding, backoff + jitter, circuit breakers, bulkheads
  12. Graceful degradation & the thundering herd
  13. Progressive delivery: canary, feature flags, safe & fast rollback
  14. Toil & automation; simplicity as a reliability control
  15. NALSD: reasoning about reliability from concrete production numbers (Staff judgment)
- **Quiz rule enforced**: answer options kept ~equal length / word-count so formatting leaks no clues.
- **Storage vs fluency**: re-surface SLO / error-budget vocab via spaced retrieval in later lessons;
  interleave resilience patterns once several are known.
