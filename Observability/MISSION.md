# Mission: Reliability, Observability & Operating at Scale (SRE)

## Why
A strong service **builder** who rarely **operates** what he ships — no on-call, no SLOs — wants to
become an engineer who can *define, measure, and defend* reliability in production. The goal is the
durable operational judgment expected at **Staff/Principal** (to "be in the room" for reliability
decisions), grown through **hands-on practice** he can apply to the systems he'll increasingly own.

## Success looks like
- Define **SLIs/SLOs** for a real user journey and wield the **error budget** as a decision policy —
  not a dashboard decoration.
- Instrument a service with the **four golden signals** using **Prometheus/Grafana + OpenTelemetry**,
  and know when to reach for metrics vs. logs vs. traces.
- Debug production through **observability** — ask *new* questions of a live system, not just
  pre-baked ones.
- Run the **incident lifecycle** — roles, severity, mitigation — and write a **blameless postmortem**
  that actually reduces risk.
- Design against **overload & cascading failure**: load shedding, backoff + jitter, circuit breakers,
  graceful degradation.
- Ship safely: **canary, feature flags, fast rollback** — separating *deploy* from *release*.
- Carry reliability into design reviews (**NALSD**): how a system is monitored, how it fails, how it
  recovers, and who owns it.

## Constraints
- ~3–5 hrs/week. Short, self-contained lessons (small working-memory budget); one tangible win each.
- **Green on operations**: start from the operator mindset, not tool trivia. Bias hard to concrete
  practice and real trade-offs.
- Ground examples in his stack: **Prometheus, Grafana, OpenTelemetry, cloud-native monitoring**
  (CloudWatch / GCP Monitoring / Azure Monitor).

## Out of scope (for now)
- Building a monitoring or tracing system from scratch.
- Vendor-specific config trivia and Kubernetes internals beyond what reliability actually needs.
- Managing or growing an SRE *organisation* (people leadership) — revisit at the Principal tier.
