# Reliability, Observability & SRE — Resources

Curated, high-trust sources. Lesson knowledge is drawn from here — not from memory.
All links verified live on **2026-07-24** (✅).

## Knowledge

### Canonical — SRE foundations
- [Google — *Site Reliability Engineering* (the "SRE Book", free online)](https://sre.google/sre-book/table-of-contents/) ✅
  The field's founding text. Use for: SLIs/SLOs/SLAs (Ch 4), embracing risk & error budgets (Ch 3),
  monitoring & the four golden signals (Ch 6), on-call (Ch 11), postmortems (Ch 15), cascading
  failures (Ch 22).
- [Google — *The SRE Workbook* (free online)](https://sre.google/workbook/table-of-contents/) ✅
  The practical companion. Use for: **Implementing SLOs**, error-budget **policy**, alerting on SLOs
  (burn rate), toil automation — the "how", with worked recipes.
- [Google — *The Art of SLOs* workshop](https://sre.google/resources/practices-and-processes/art-of-slos/) ✅
  Hands-on curriculum for choosing SLIs and setting SLOs. Use for: practising SLI/SLO selection.
- [Google — all SRE books & resources index](https://sre.google/books/) ✅ / [SRE resources hub](https://sre.google/resources/) ✅

### Observability & instrumentation — his stack
- [OpenTelemetry docs](https://opentelemetry.io/docs/) ✅
  Vendor-neutral traces/metrics/logs; "understand internal state from outputs". Use for: the
  instrumentation model, semantic conventions, the Collector, language SDKs.
- [OpenTelemetry Demo (runnable microservices + Prometheus/Grafana/Jaeger)](https://opentelemetry.io/docs/demo/) ✅
  [repo](https://github.com/open-telemetry/opentelemetry-demo) ✅ — `docker compose up` for a fully
  instrumented app. Use for: the hands-on lab (Lesson 08, Track A); real golden-signal dashboards & traces.
- [OTel Collector — Quick start](https://opentelemetry.io/docs/collector/quick-start/) ✅ &
  [Zero-code instrumentation](https://opentelemetry.io/docs/zero-code/) ✅
  Use for: the Collector pipeline (receivers/processors/exporters) and auto-instrumenting your own service.
- [Prometheus docs — overview](https://prometheus.io/docs/introduction/overview/) ✅
  Pull-based time-series metrics, labels, PromQL. Use for: metrics data model, querying,
  recording/alerting rules.
- [Grafana docs](https://grafana.com/docs/grafana/latest/) ✅ — dashboards & visualisation over Prometheus and others.
- [Google — "Monitoring Distributed Systems" (SRE Book Ch 6)](https://sre.google/sre-book/monitoring-distributed-systems/) ✅
  Source of the **four golden signals**. Use for: what to measure and why; symptom-vs-cause alerting.
- [Brendan Gregg — the USE method](https://www.brendangregg.com/usemethod.html) ✅
  Utilisation / Saturation / Errors, per resource. Use for: infra-side saturation analysis.
- [Charity Majors, Liz Fong-Jones, George Miranda — *Observability Engineering* (O'Reilly, free via Honeycomb)](https://info.honeycomb.io/observability-engineering-oreilly-book-2022) ✅
  Use for: high-cardinality, event-based observability and why dashboards alone fall short.

### Depth & judgment
- [Google — "Managing Incidents" (SRE Book Ch 14)](https://sre.google/sre-book/managing-incidents/) ✅
  The Incident Command System applied to outages: IC / Ops / Comms / Planning roles, the living
  incident doc. Use for: how to run an incident without it spiralling.
- [PagerDuty — Incident Response](https://response.pagerduty.com/) ✅
  Superbly practical, battle-tested guide: roles, severity, comms, being on-call, running the call.
  Use for: templates and the human mechanics of response.
- [Google — "Addressing Cascading Failures" (SRE Book Ch 22)](https://sre.google/sre-book/addressing-cascading-failures/) ✅
  Use for: overload, retries, load shedding, thundering herd.
- [AWS — "Timeouts, retries, and backoff with jitter" (Builders' Library)](https://aws.amazon.com/builders-library/timeouts-retries-and-backoff-with-jitter/) ✅
  Use for: the canonical retry / backoff / jitter argument, with the reasoning and math.
- [Martin Fowler — CircuitBreaker](https://martinfowler.com/bliki/CircuitBreaker.html) ✅
  Use for: precise circuit-breaker semantics and states.
- [Principles of Chaos Engineering](https://principlesofchaos.org/) ✅
  The canonical definition + the four experiment steps + advanced principles. Use for: designing chaos
  experiments / GameDays to verify resilience (Lesson 16).

## Wisdom (Communities)
- [r/sre](https://reddit.com/r/sre) — practitioner Q&A; incident and SLO war stories. Use for:
  reality-checking practices against how teams actually operate.
- [r/devops](https://reddit.com/r/devops) — tooling and pipeline debates. Use for: telemetry/rollout design critique.
- [CNCF Slack](https://slack.cncf.io/) — `#prometheus`, `#opentelemetry` channels with maintainers.
  Use for: deep tool troubleshooting.
- [SREcon (USENIX) — talks & proceedings](https://www.usenix.org/srecon) ✅ — how real orgs operate at
  scale. Use for: wisdom from practitioners; watch a few recorded talks.

## Gaps
- ~~Runnable local lab~~ **RESOLVED (2026-07-24):** minimal validated scaffold lives in
  `assets/lab/` (Collector + Prometheus + Grafana + Jaeger); official OTel Demo is the richer option.
- Need a strong, free **incident-command** walkthrough — **RESOLVED (2026-07-24):** SRE "Managing
  Incidents" + PagerDuty Incident Response (both added above, verified).
- Community preference **not yet asked** — check whether he wants to join r/sre / CNCF Slack before
  leaning on them for wisdom.
