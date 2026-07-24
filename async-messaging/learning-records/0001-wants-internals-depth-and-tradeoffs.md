# Learner wants under-the-hood depth and explicit trade-offs

After Lesson 02, the learner spontaneously asked *why* Kafka caps active consumers at the partition
count, *how it's implemented under the hood* (so extras go idle), *why that design*, and its
*pros and cons*. This is a strong signal about their zone of proximal development and preferences.

**Implication for future sessions:** don't stop at the "what." For each mechanism, include (a) the
implementation under the hood, (b) the design rationale, and (c) an explicit pros/cons or trade-off
section — the learner is chasing staff-level judgment, not just usage. Internals-level Kafka content
(consumer group protocol, coordinator/leader, rebalancing) is now in their reach; lean into it and
keep tying it back to real-world failure modes ("scaled pods, throughput flat").

Prompted the creation of the Kafka consumer-group deep dive. At the learner's request it was
renumbered to **Lesson 03** (placed right after Lesson 02, which it deepens); Event vs Command vs
Query moved to Lesson 04, and delivery guarantees is now Lesson 05.
