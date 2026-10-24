# Dependency order and dynamic connectivity: retrieval sheet

## Recognition
- A must happen before B: orient A -> B and track unfinished prerequisites, rather than sorting labels.
- You need a valid ordering satisfying all directed edges: topological ordering exists exactly for a directed acyclic graph.
- Undirected edges arrive and you only need component membership: disjoint-set union merges groups without storing paths.
- A proposed new link or cell joins several existing components: combine component sizes, deduplicating roots.

## Invariant
For Kahn's algorithm, indegree[v] counts precisely the incoming edges from vertices not yet processed. A queued vertex has zero such edges, so appending it never violates an edge to an earlier task. At termination, processing every vertex proves the produced list satisfies all dependencies; a nonempty residue with no source proves a cycle exists. For DSU, vertices have the same representative exactly when the processed undirected edges connect them. Union merges precisely two previously distinct equivalence classes, and compression preserves class membership. Component sizes are valid at representatives only.

## Failure boundaries
- Use DSU to detect cycles in a directed dependency graph.: A->B, A->C, B->D, C->D is a DAG. Ignoring directions, the final edge joins already-connected endpoints and DSU falsely flags a directed cycle. Repair: Use indegree removal or DFS active-stack colors for directed cycles.
- Sum the component size of every land neighbor when flipping a water cell.: [[1,1],[1,0]]: the three existing land cells belong to the same size-3 component. The bottom-right cell has two land neighbors; counting the same component twice suggests 7 instead of 4. Repair: Collect distinct representative roots, then sum each size once.
- Call every vertex left after Kahn's queue empties a cycle vertex.: A->B, B->A, B->C leaves A, B, and C unprocessed. C is blocked by the cycle but is not itself on one. Repair: Use the leftover count only for feasibility unless a separate cycle/SCC analysis identifies exact cycle membership.

## Recall
- Derive why a nonempty DAG has a zero-indegree vertex.
- Explain why leftover Kahn vertices need not all lie on cycles.
- State the DSU membership invariant and where size metadata is valid.
- Draw a DAG that DSU would incorrectly classify as cyclic.
- Explain why evaluating one island flip must leave other candidate water cells unmerged.
