# Reliability, Observability & SRE — Glossary

The canonical language for this workspace. Every lesson adheres to these terms. The glossary grows
as you *demonstrate* understanding — it is a record of compressed knowledge, not a dictionary to
pre-read. (The terms below are the bedrock nouns Lesson 01 defines precisely.)

## Terms

**Reliability**:
Whether a service does what its users need, *often enough*, measured from the user's point of view —
not whether the servers are "up". Managed as an explicit, quantified target, never as an aspiration.
_Avoid_: Uptime, availability (uptime is one narrow proxy; availability is one *kind* of SLI)

**SLI (Service Level Indicator)**:
A carefully defined quantitative measure of one aspect of the service as users experience it —
e.g. the proportion of requests that succeed under 300 ms. Usually a *good-events ÷ valid-events* ratio.
_Avoid_: Metric (an SLI is a *user-facing* metric chosen to proxy happiness, not any raw number)

**SLO (Service Level Objective)**:
A target value or range for an SLI over a window — e.g. "99.9% of checkout requests succeed over 30
days". The internal goal the team engineers toward and defends.
_Avoid_: SLA, uptime target (an SLO has no external penalty; see SLA)

**SLA (Service Level Agreement)**:
An explicit contract with users that attaches a *consequence* (a refund, a penalty) to meeting or
missing an SLO. The five-second test: *"what happens if it's missed?"* — no consequence ⇒ it's an SLO.
_Avoid_: SLO (the SLA is the *promise with teeth*; the SLO is the internal target beneath it)

**Error budget**:
The allowed unreliability implied by an SLO: `1 − SLO`. A 99.9% SLO grants a 0.1% budget of failed
work per window — a currency you *spend* on releases, risk, and change.
_Avoid_: Downtime allowance (it covers any failed work, not only time down)

**Nines**:
Shorthand for an availability SLO by its leading digits — "three nines" = 99.9%, "four nines" =
99.99%. Each extra nine is ~10× less allowed failure, at steeply rising cost.
_Avoid_: Percent uptime (say the nines; tie them to budget minutes, not vague "high availability")

**Valid events**:
The events an SLI holds the service accountable for — real user attempts — forming the SLI
denominator. Excludes what you don't own: malformed requests, health checks, synthetic probes, and
(by default) client-side 4xx.
_Avoid_: Total requests (only the events you're accountable for count)

**Good events**:
The valid events that met the bar — succeeded (2xx/3xx) or were fast enough. The SLI numerator.
_Avoid_: Successful requests ("good" also covers latency and other SLIs, not just success)
