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
- [Google — "Addressing Cascading Failures" (SRE Book Ch 22)](https://sre.google/sre-book/addressing-cascading-failures/) ✅
  Use for: overload, retries, load shedding, thundering herd.
- [AWS — "Timeouts, retries, and backoff with jitter" (Builders' Library)](https://aws.amazon.com/builders-library/timeouts-retries-and-backoff-with-jitter/) ✅
  Use for: the canonical retry / backoff / jitter argument, with the reasoning and math.
- [Martin Fowler — CircuitBreaker](https://martinfowler.com/bliki/CircuitBreaker.html) ✅
  Use for: precise circuit-breaker semantics and states.

## Wisdom (Communities)
- [r/sre](https://reddit.com/r/sre) — practitioner Q&A; incident and SLO war stories. Use for:
  reality-checking practices against how teams actually operate.
- [r/devops](https://reddit.com/r/devops) — tooling and pipeline debates. Use for: telemetry/rollout design critique.
- [CNCF Slack](https://slack.cncf.io/) — `#prometheus`, `#opentelemetry` channels with maintainers.
  Use for: deep tool troubleshooting.
- [SREcon (USENIX) — talks & proceedings](https://www.usenix.org/srecon) ✅ — how real orgs operate at
  scale. Use for: wisdom from practitioners; watch a few recorded talks.

## Gaps
- Need a concrete **runnable local lab** (docker-compose: sample app + Prometheus + Grafana + OTel
  Collector) for the hands-on instrumentation lessons (~lesson 7). Find & verify before then.
- Need a strong, free **incident-command** walkthrough (PagerDuty's incident-response docs are a
  candidate) before the on-call lessons (~lesson 9). Verify liveness when we reach it.
- Community preference **not yet asked** — check whether he wants to join r/sre / CNCF Slack before
  leaning on them for wisdom.
