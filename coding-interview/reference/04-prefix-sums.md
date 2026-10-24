# Prefix sums, balance, and range counting: retrieval sheet

## Recognition
- Many static range-sum queries suggest one cumulative preprocessing pass.
- A subarray sum constraint can be rewritten as a relation between two prefixes.
- Divisibility means equality of normalized residues, even with negative values.

## Invariant
A prefix is tied to a boundary, not vaguely to an element. Store P[0]=0; after consuming i values, P[i] is their exact sum. For counting, only earlier eligible boundaries belong in the query state. Translate the range condition algebraically before choosing equality hashing or ordered counting.

## Failure boundaries
- Forget the empty prefix.: [3], k=3: there is no previously stored prefix 0, so the only valid range is missed. Repair: Seed frequency[0]=1 or prefix index 0 before scanning.
- Insert the current prefix before counting matches.: [5],k=0: the current prefix matches itself, incorrectly counting an empty subarray. Repair: Query first, then insert.
- Compare raw JVM negative remainders.: [-1,2], k=2 has prefixes with raw residues -1 and 1; their difference is divisible by 2, but raw equality misses [2]. Repair: Normalize remainders into 0..k-1 using floorMod.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.
