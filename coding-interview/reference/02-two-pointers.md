# Two pointers and safe elimination: retrieval sheet

## Recognition
- Sorted values plus a pair relation often allow opposite endpoints.
- Symmetric constraints suggest comparing the two ends of an interval.
- A local result limited by the smaller of two known boundaries suggests settling that side.

## Invariant
Before each move, describe the candidates still unresolved. A pointer advances only when a comparison proves that every candidate it abandons is impossible or has been fully accounted for. Termination follows from a decreasing interval or increasing processed region.

## Failure boundaries
- Use sorted-pair pointer moves on arbitrary order.: [3,2,4], target 6: 3+4 is too large, moving right loses the valid pair 2+4. Repair: Sort while preserving indices or use hashing.
- At a palindrome mismatch always delete the left character.: "abbab": deleting rightmost b gives "abba"; deleting leftmost a gives "bbab", which fails. Repair: Test both possible single deletions.
- For rainwater, sum distances to the nearest taller neighbor.: [3,1,2,0,3] has water limited by the distant height-3 boundaries; nearest local peaks underestimate cells. Repair: Use maximum boundary heights, not nearest neighbors.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.
