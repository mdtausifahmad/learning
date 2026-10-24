# Graphs: reachability and shortest unweighted paths: retrieval sheet

## Recognition
- An answer depends on what can be reached through local moves: define vertices and edges, even if the input never says graph.
- You need the minimum number of equal-cost moves: FIFO breadth-first search explores distance layers.
- Several sources spread simultaneously: initialize one BFS queue with all sources at distance zero.
- Objects reference one another and can form cycles: store visited identity or an original-to-copy map before following outgoing references.

## Invariant
Every frontier state has already been marked discovered and is reachable from the starting set. Every processed state's legal neighbors have been discovered. At termination no reachable state can remain undiscovered: on a hypothetical path to one, the first undiscovered state would follow a processed reachable predecessor. For BFS, frontier distances are nondecreasing, and first discovery is a shortest distance because all shorter predecessor layers were processed earlier. This proof assumes equal edge costs. For cloning, the map additionally preserves one distinct copy per discovered original; recording before expansion handles cycles and self-loops.

## Failure boundaries
- Use DFS and return the first path as the shortest path.: Edges S-A, A-B, B-T and S-T. DFS taking A first finds length 3 although direct S-T has length 1. Repair: Use BFS for equal-cost edges; use weighted shortest-path methods when costs differ.
- Mark visited only when a node is removed from the queue.: S connects to A and B; both connect to C. A and B can enqueue C before C is processed, repeating its work. A layered graph compounds the duplication. Repair: Mark at enqueue time. The first BFS discovery already has a shortest distance.
- Clone graph nodes by label instead of identity.: Two distinct adjacent objects both labeled 7 collapse into one self-loop if a map keyed only by label is used. Repair: Map original object identity to its one copy.

## Recall
- Model a word transformation graph without drawing every possible edge.
- Explain why marking on enqueue prevents duplicate frontier work.
- Derive the simultaneous-spread algorithm from nearest-source distance.
- Show a cyclic two-node graph and the moment each clone enters the map.
- Give a maze where position alone is insufficient state.
