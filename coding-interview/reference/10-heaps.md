# Heaps: keep the next winner and forget full order: retrieval sheet

## Recognition
- The next action repeatedly consumes the smallest or largest currently available candidate.
- Only k winners matter, and k is much smaller than the full input size.
- Several already sorted streams must become one: only their exposed heads can win next.
- A dynamic median asks for the boundary between a lower half and an upper half, not a fully sorted sequence.

## Invariant
A heap guarantees only its parent-child order and therefore one accessible extreme. The problem-specific invariant must say which candidates are stored, why omitted candidates cannot improve the requested answer, and what the root represents. Charge insert/remove costs to individual candidate events; peak heap size, rather than total input size, determines the logarithmic factor.

## Failure boundaries
- Use a max-heap of k values to keep the k largest by evicting its root.: With k=2 and retained [10,9], incoming 8 followed by evicting the maximum leaves [9,8], discarding the true largest value 10. Repair: Keep a min-heap so the smallest retained winner is evicted.
- Compare heap priorities with a-b.: a=2147483647 and b=-1 gives overflowed a-b=-2147483648 in Int, incorrectly ordering the larger value first in an ascending comparator. Repair: Use comparison APIs and promote sums before arithmetic; do not derive ordering by overflowing subtraction.

## Recall
- For each heap problem say what the root represents before choosing direction.
- Reconstruct the median invariants and explain each rebalance transfer.
- Explain why changing a stored entry priority silently can corrupt future results.
