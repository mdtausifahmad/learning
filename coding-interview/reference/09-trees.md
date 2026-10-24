# Trees: decide what crosses each parent-child boundary: retrieval sheet

## Recognition
- Every node has a unique route from the root: inherited constraints or prefix state can follow that route.
- A parent answer depends on its children: compute children first with postorder.
- Answers are organized by distance from a root: process a FIFO queue in complete levels.
- A path can turn at one node but cannot branch into three directions: separate a global completed answer from a one-branch return value.

## Invariant
Each helper processes exactly its subtree, receives a precise inherited context, and returns a summary sufficient for its parent. Structural induction proves correctness: establish the empty case, assume child contracts, then show the parent composition preserves the contract. For BFS, the queue at a level boundary contains exactly the next depth in left-to-right order.

## Failure boundaries
- Validate a BST using only node.left.val < node.val < node.right.val.: Root 10 has right child 15, and 15 has left child 6. Every local comparison passes, but 6 lies in the right subtree of 10 and violates its ancestor bound. Repair: Propagate the entire legal interval from all ancestors, or verify strictly increasing inorder values.
- Return a path containing both child gains to the parent.: At node 2 with children 3 and 4, returning 3+2+4=9 to parent 1 creates three incident path edges at node 2 after adding 1. That object is a fork, not a simple path. Repair: Use both children only for a completed candidate; return node plus at most one child gain.

## Recall
- Write a helper contract before coding LCA or path sum.
- Explain the difference between inherited bounds and synthesized gain.
- Convert recursive postorder into an explicit stack with visited-state frames.
