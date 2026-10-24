# Dynamic programming on grids and strings: retrieval sheet

## Recognition
- Two independently changing boundaries determine the remaining task: two string prefixes, a row and column, or an item count and capacity.
- Different paths reach the same state; their future depends on the state, not the route taken to it.
- A local choice can be suboptimal, but all choices reduce to smaller states that can be compared or combined.

## Invariant
dp[i][j] denotes the exact answer for the stated pair of prefixes or coordinates. Every transition enumerates an exhaustive set of final decisions, with no missing valid solution and no unjustified double counting. The evaluation order computes dependencies before their consumers. A space optimization must preserve this meaning even while entries change from previous-row to current-row values.

## Failure boundaries
- Treat subsequence as contiguous substring: a="abc", b="ac": longest common subsequence length is 2 (a,c), while the longest common contiguous substring length is 1. Repair: Subsequence DP can skip either final character. Substring DP requires a matching suffix and resets to zero on mismatch.
- Initialize every empty-prefix answer to zero: Edit distance from "" to "abc" is 3, not 0. Regex empty string against "a*" is true, while against "a" is false. Repair: Derive base cases from the contract and grammar before writing general transitions.
- Overwrite the previous row without saving a diagonal: In LCS of "a" and "aa", using an already updated left value as the previous-row diagonal can count the same source character twice and return 2. Repair: Track diagonal before assigning dp[j]. Distinguish old up, new left, and saved diagonal at each step.

## Recall
- Derive the grid-path recurrence by partitioning paths according to their final step.
- Draw old up, new left and saved diagonal for one LCS row update.
- Explain the edit-distance empty row without reading code.
- Derive zero and positive repetitions for star from the pattern grammar.
- What changes between maximizing a subsequence length and counting subsequences?
