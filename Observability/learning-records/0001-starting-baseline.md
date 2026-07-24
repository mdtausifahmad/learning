# Starting baseline: strong builder, green operator

Learner disclosed (via intake) that he builds services competently but **rarely operates them in
production** — no on-call, doesn't own SLOs or run incidents. So this workspace starts from the
*operator mindset*, not from senior-SRE assumptions. On the SRE Workbook maturity ladder he's at
**rung 1** (monitoring exists, but no SLO; unspoken 100%-uptime goal).

Driver is **both** durable Staff/Principal judgment *and* practical application to systems he'll
increasingly own. Stack to ground every example: **Prometheus + Grafana, OpenTelemetry, cloud-native
monitoring**. Budget ~3–5 hrs/week.

Implication for future sessions: teach the *why* and the operator's mental model before tooling
mechanics; every concept should cash out in something he can apply to a real service. Next natural
step after Lesson 01 (SLI→SLO→error budget) is **Lesson 02 · Choosing good SLIs**.
