# Sorting, intervals, scheduling, and sweep lines: retrieval sheet

## Recognition
- Union of overlapping ranges suggests sorting by start and maintaining a current component.
- Maximum number of compatible unweighted appointments suggests earliest finish.
- Questions about simultaneous activity across coordinates suggest endpoint events and a sweep line.

## Invariant
State endpoint semantics first. Sorting makes the unprocessed future obey a monotone order. Finalize a component only when the next start proves no future interval can reach it; select an interval only with an exchange argument; emit a sweep answer only after all events at the same coordinate have been applied.

## Failure boundaries
- When merging, replace the current end with the next end.: [[1,10],[2,3],[4,5]] would shrink the component to 3 and wrongly split [4,5], although all belong to [1,10]. Repair: Update end=max(end,next.end).
- Select intervals by earliest start for maximum count.: [1,10),[2,3),[3,4),[4,5): earliest start keeps one while earliest finish keeps three. Repair: Use earliest finish with its exchange proof.
- Emit a skyline point after each individual event.: Buildings [1,2,3] and [2,3,3] may emit a spurious drop to zero and rise at x=2. Repair: Batch all events with the same x before observing height.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.
