# Greedy choices and exchange arguments: retrieval sheet

## Recognition
- A decision permanently consumes a resource, and the remaining problem has the same structure. Look for an exchange argument, not a keyword.
- Sorted thresholds, earliest finishes, and dominance can make one partial solution at least as good for every possible future.
- If a locally attractive choice can harm future feasibility, a greedy rule needs repair or the problem needs DP.

## Invariant
After each irreversible decision, there exists an optimal solution whose decisions agree with the chosen prefix, or the maintained summary dominates every reachable competitor. State which form applies to the specific problem. Initialization has an empty prefix; maintenance follows from a feasibility-preserving exchange or a dominance comparison; termination converts the prefix theorem into global optimality.

## Failure boundaries
- Choose the largest immediate reward without an exchange proof: Coin denominations [1,3,4], amount 6: taking 4 first produces 4+1+1 (3 coins), while 3+3 uses 2. Local denomination size does not dominate the future remainder. Repair: Use amount-based DP for arbitrary denominations, or prove a special property of the denomination system.
- Reuse an unweighted greedy rule after adding weights: Intervals [0,1] value 1, [1,2] value 1, [0,2] value 100: earliest finish maximizes count but selects total value 2 instead of 100. Repair: Define the objective first. Weighted scheduling needs predecessor compatibility and a choose/skip DP; an unweighted exchange does not preserve value.

## Recall
- Derive the cookie exchange without referring to the code.
- Give a minimum-size counterexample to largest-coin-first.
- Explain why max(left,right), rather than their sum, is the minimal candy assignment.
- Distinguish a greedy choice from the data structure used to implement it.
