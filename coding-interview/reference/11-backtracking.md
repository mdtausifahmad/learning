# Backtracking: enumerate choices with a reversible state: retrieval sheet

## Recognition
- The output contains all subsets, arrangements, placements, or routes satisfying constraints.
- A partial decision can be checked before the remaining decisions are known, allowing early rejection.
- The same partial buffer can be reused if each recursive change is undone on return.
- Duplicates in input create duplicate decision paths unless a canonical representative rule is imposed.

## Invariant
At entry to a recursive call, path describes exactly the decisions made along its root-to-call route, and all auxiliary markers agree with those decisions. Every child choice is legal under that state. On return the state is restored exactly to its entry value. Completeness follows by showing each valid answer has one permitted sequence of choices; uniqueness follows from canonical ordering or duplicate rules.

## Failure boundaries
- Add the mutable path directly to output.: For subsets of [1], storing the same path reference first empty, then [1], and finally removing 1 leaves both output references pointing to the same empty list. Repair: Copy the current path when recording an answer; continue reusing only the working buffer.
- Prune whenever the running sum exceeds target for arbitrary integers.: Candidates [5,-2], target 3: after choosing 5, sum exceeds 3, but adding -2 creates the valid solution [5,-2]. Repair: Use overshoot pruning only under a strictly positive candidate contract; otherwise redesign state, bounds, and termination.

## Recall
- Draw the decision tree before writing a helper signature.
- Explain which choice rule gives uniqueness and which condition gives pruning.
- Give a counterexample to overshoot pruning when negative values are allowed.
