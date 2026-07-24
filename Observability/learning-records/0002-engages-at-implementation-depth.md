# Learner engages at implementation depth, not just concept

After Lessons 01–02 (SLI/SLO/error budget, choosing SLIs), the learner immediately probed the
implementation edges: how to build a latency SLI from a Prometheus histogram (buckets, `le`, recording
rules) and whether to exclude 4xx from "valid" events. He grasps the concepts and wants to cash them
out in his real stack.

Implication for future sessions: bias toward concrete, runnable detail on his stack (Prometheus,
OpenTelemetry, Grafana) *alongside* the judgment framing. He can take tooling depth sooner than a
typical "green operator" — keep the operator-mindset lens, but don't shy from real queries/config.
Prioritise finding the docker-compose lab (RESOURCES gap) before Lesson 07.
