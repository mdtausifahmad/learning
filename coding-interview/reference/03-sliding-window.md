# Sliding windows: expand, repair, and count: retrieval sheet

## Recognition
- Contiguous input plus repeated interval statistics suggests maintained window state.
- At-most constraints whose validity survives deleting an endpoint support shrinking until valid.
- Minimum coverage supports expanding until valid, then shrinking while valid.

## Invariant
State must exactly describe the current interval. Expand by adding one endpoint; shrink only when the property has a monotone repair direction. Count or optimize only after stating whether the interval is valid. Every pointer moves forward, so total boundary movement is linear.

## Failure boundaries
- Use a positive-sum shrinking rule with negative values.: [1,-1,5], target sum at least 5: after reaching total 5, removing 1 gives 4 and stops, missing [5] of length 1. Repair: For shortest threshold sums with negatives, use prefix sums and a monotonic deque; the sum-window proof no longer holds.
- Move left to lastSeen+1 without taking max.: "abba": after the second b left=2; seeing final a would move left back to 1 and accept "bba". Repair: Set left=max(left,lastSeen+1).
- Minimum cover tracks only distinct required symbols.: s="ABC", t="AABC" would appear covered although it lacks the second A. Repair: Track multiplicity deficits or fully satisfied required frequencies.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.
