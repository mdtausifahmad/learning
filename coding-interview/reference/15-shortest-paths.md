# Shortest paths: matching the frontier to edge costs: retrieval sheet

## Recognition
- Equal-cost moves: BFS orders states by distance in edges.
- Nonnegative varying costs: always expand the smallest tentative total distance with Dijkstra.
- Edge costs are exactly zero or one: a deque maintains the necessary order more cheaply than a heap.
- The route has a maximum number of edges or stops: carry the resource dimension or update from a frozen previous layer.
- Negative edges: Dijkstra's settlement proof fails; consider Bellman-Ford and explicitly address reachable negative cycles.

## Invariant
All finite tentative distances correspond to actual legal paths. BFS first discovery is final only under equal edge costs. Dijkstra settles a vertex only when its smallest nonstale heap entry is removed, relying on nonnegative edges. Zero-one BFS maintains a deque in nondecreasing current candidate cost by putting zero-cost extensions before cost-one extensions. In bounded Bellman-Ford, after e passes the array contains exact optima among paths of at most e edges, because the next pass extends only previous-layer paths. These are separate correctness arguments; a universal visited rule cannot replace them.

## Failure boundaries
- Use ordinary BFS to minimize weighted cost.: Edges S->T cost 10, S->A cost 1, A->T cost 1. BFS discovers the direct one-edge route first, but the cheapest route costs 2. Repair: For nonnegative varying weights, use a min-heap of total distances.
- Mark a weighted vertex permanently visited when first pushed.: S->A cost 8, S->B cost 1, B->A cost 1. First discovery sets A=8, but the optimum is 2. Repair: Allow improvements; settle only when removing the smallest nonstale candidate.
- Update the same distance array in one constrained-flight pass.: Edges 0->1 cost 1 and 1->2 cost 1, K=0 stops. An in-place pass can set dist[1]=1 then dist[2]=2, illegally using two flights where only one is allowed. Repair: Read the frozen previous layer and write into a copied next layer.
- Apply Dijkstra when a reachable negative edge exists.: S->A cost 1, S->B cost 2, B->A cost -5. Settling A at 1 before exploring B misses the better cost -3 under the settlement proof. Repair: Use an algorithm whose proof allows negative edges; address negative cycles and any edge budget.

## Recall
- Give a weighted counterexample to visited-on-enqueue.
- Derive why nonnegative weights justify Dijkstra settlement.
- Simulate a zero-cost edge placed at the front of a deque.
- Explain why cloning the distance array enforces a flight count.
- Distinguish unreachable targets from an unbounded negative-cycle objective.
