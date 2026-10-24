# Practice room: make the pattern survive a twist

Use the experiments below to inspect actual state changes. Then solve a transfer prompt before opening the related lesson. Your aim is to explain why the technique applies when the story and assumptions change.

## A repeatable session

1. Read the contract and create an example with a boundary case.
2. Write a correct baseline and count its operations.
3. Identify the repeated work or redundant choices.
4. Write the smallest state that removes that work.
5. State the invariant before writing code.
6. Implement in your primary language, then test against the contract.
7. Change an assumption and decide whether your proof survives.

Use the lesson hints as graduated help: recognition first, state next, transition next, implementation last. After reading a solution, close it and reconstruct it the following day.

## Change one condition at a time

| Original | Twist | What you must reconsider |
|---|---|---|
| Positive subarray sum | Negative numbers allowed | Window monotonicity fails; prefix relations can still hold |
| Return existence | Count every solution | Keep frequencies, not only a set |
| Static range totals | Values change between queries | Prefix recomputation versus Fenwick/segment tree updates |
| Shortest path with equal costs | Unequal nonnegative costs | FIFO order no longer settles cheapest paths |
| Any solution | Lexicographically smallest solution | Traversal ordering, feasibility and tie-breaking |
| Tree input | Graph with shared nodes and cycles | Visited state, cycle handling, parent assumptions |
| Contiguous segment | Subsequence | Endpoints no longer describe the selected values |
| Reusable item | Use each item once | DP loop direction changes what states can be reused |
| Score only | Reconstruct the chosen items | Store predecessors or recompute a valid path |

## Mixed mock rounds

The links use neutral labels so the pattern title is not visible until you open the original problem. Attempt one pair per session. Difficulty labels are practice-platform classifications, not company hiring thresholds.

| Round | First problem | Second problem | Follow-up to say aloud |
|---|---|---|---|
| A | [Prompt A1](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | [Prompt A2](https://leetcode.com/problems/subarray-sum-equals-k/) | Which assumption makes shrinking safe or unsafe? |
| B | [Prompt B1](https://leetcode.com/problems/number-of-islands/) | [Prompt B2](https://leetcode.com/problems/course-schedule/) | What does visited mean in each problem? |
| C | [Prompt C1](https://leetcode.com/problems/kth-largest-element-in-an-array/) | [Prompt C2](https://leetcode.com/problems/search-in-rotated-sorted-array/) | What information can you discard permanently? |
| D | [Prompt D1](https://leetcode.com/problems/house-robber/) | [Prompt D2](https://leetcode.com/problems/coin-change/) | What future decisions does the state have to remember? |
| E | [Prompt E1](https://leetcode.com/problems/merge-intervals/) | [Prompt E2](https://leetcode.com/problems/non-overlapping-intervals/) | Does the sorting criterion need a correctness proof? |
| F | [Prompt F1](https://leetcode.com/problems/word-search-ii/) | [Prompt F2](https://leetcode.com/problems/sliding-window-maximum/) | Which two techniques cooperate here? |

## Score your reasoning, not only the result

After a mock, assign 0 (missing), 1 (with help), or 2 (independent) for each: contract, baseline, state choice, invariant, implementation, edge tests, complexity, and follow-up. A correct output with an unexplained invariant is an unfinished learning task. This score is a personal diagnostic, not a prediction of hiring decisions.

Keep a small error log:

```text
Problem and date:
My first model:
Smallest input that breaks it:
Which assumption failed:
Repaired state and invariant:
What I can now derive without looking:
Next retrieval: +1, +3, +7, +21 days
```

## Advanced breadth and the limits of a pattern library

After these lessons, explore string matching (KMP and Z-function), rolling hashes and collision verification, quickselect and randomized selection, divide-and-conquer counting, meet-in-the-middle, sweep lines, interval DP, tree DP, and bitmask DP. Several appear as extensions here; they are not all taught with the same depth as the core 20 patterns. Scheduling and graph questions may also require a combination of techniques.

Keep room for unfamiliar problems. You may need to invent a representation instead of selecting a known pattern. Practice explaining a partial correct solution, reasoning about trade-offs, and incorporating a hint. Interview outcomes also depend on communication, role expectations and the particular round; no finite question list guarantees success.
