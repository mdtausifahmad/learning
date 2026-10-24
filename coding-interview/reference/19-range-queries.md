# Range queries: precompute reusable pieces, then update only dependencies: retrieval sheet

## Recognition
- Many queries ask for an aggregate over contiguous intervals of the same array.
- Static sums allow subtracting two prefixes, but point updates would invalidate many prefixes.
- The aggregate is associative, such as sum, minimum, maximum, gcd, or matrix multiplication: fixed interval summaries may compose.
- A value-order question such as counts smaller than a threshold can become a prefix query after coordinate compression.

## Invariant
Each stored summary corresponds to a precisely defined set of original elements. A query combines disjoint summaries covering exactly the requested interval, with neither gaps nor double counting. An update changes exactly the summaries whose sets contain the modified point. For compression, rank order and equality match original value order, so prefix frequency answers preserve the original comparison meaning.

## Failure boundaries
- Compute a range maximum by subtracting two prefix maxima.: Arrays [5,1] and [5,4] both have prefix maxima [5,5]. Their range [1,2) maxima are 1 and 4, so no subtraction of those equal summaries can recover the answer. Repair: Use a segment tree for mutable associative aggregates, or an appropriate sparse table for static idempotent queries.
- Use index zero directly in a one-based Fenwick update loop.: With i=0, i & -i=0. The update i+=lowbit(i) stays at zero forever and the loop does not terminate. Repair: Translate external index p to internal p+1; prefix queries receive a count of elements and stop at zero.

## Recall
- Draw the one-based block represented by bit[12] and derive both update and query transitions.
- Explain why associativity is enough for a segment tree but insufficient for subtracting prefixes.
- Implement strict-smaller counts and then change the boundary for smaller-or-equal.
