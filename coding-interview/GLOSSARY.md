# Coding interview glossary

| Term | Meaning |
|---|---|
| Contract | Input/output specification and valid assumptions, including mutation and no-answer behavior. |
| Baseline | A simple correct algorithm used to expose repeated work and validate optimized results. |
| State | Information retained between steps, sufficient to determine every future decision. |
| Invariant | A precise fact true at a specified point before/after every transition. |
| Monotone predicate | A yes/no condition with a single directional boundary across an ordered search domain. |
| Amortized bound | Total cost over a sequence of operations, even when individual operations can be expensive. |
| Dominance | One partial solution can be no worse for every possible future continuation. |
| Exchange argument | Transform an optimal solution to include a greedy choice without worsening its objective. |
| Relaxation | Improve a candidate path cost using a known path plus an edge. |
| Settlement | Proving a tentative path cost final under the algorithm's assumptions. |
| Topological order | An ordering placing every directed edge's source before its destination; exists only for DAGs. |
| Disjoint-set union | Maintain components under merges with representative lookup, often using path compression and union by size. |
| Memoization | Cache the result of each recursive subproblem so equivalent states are solved once. |
| Tabulation | Compute DP states in an explicit order that satisfies dependencies. |
| Coordinate compression | Replace ordered values by ranks while preserving comparisons, not arithmetic distances. |
| Auxiliary space | Working memory beyond the input and returned output; include recursive stacks. |
| Transfer | Deriving a method in a changed or unfamiliar problem instead of recalling a known answer. |
