# Linear dynamic programming: decisions, states, and dependencies: retrieval sheet

## Recognition
- A recursive take/skip or last-choice search repeats the same remaining subproblem.
- An optimum for a prefix can be expressed using a few smaller-prefix optima.
- The legal next choices depend on a compact state rather than the entire history.

## Invariant
Each state answers a precise subproblem, and every transition partitions its feasible solutions into exhaustive choices. Base values describe empty or impossible subproblems accurately. Bottom-up order guarantees dependencies are solved before use; memory compression preserves every dependency until its final consumer.

## Failure boundaries
- Always take the currently largest house.: [2,3,2]: taking 3 gives 3, but taking both 2s gives 4. Repair: Compare take and skip for every prefix rather than committing to a local reward.
- Use largest-coin-first greedy for minimum coin count.: coins=[1,3,4],amount=6: greedy uses 4+1+1 (3 coins), while 3+3 uses 2. Repair: Evaluate every possible final coin using solved predecessor amounts.
- Use earliest-finish scheduling for weighted reward.: Jobs [1,2,5],[2,3,5],[1,3,20]: earliest finish earns 10, but the longer job earns 20. Repair: Use compatible-prefix take/skip DP; the unweighted exchange proof does not preserve reward.
- Use an ascending one-dimensional 0/1 subset loop.: One item of value 3 and target 6: ascending updates dp[3] then dp[6], reusing the same item twice. Repair: For one-use items iterate amounts downward so dp[x-item] still describes the previous item layer.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.
