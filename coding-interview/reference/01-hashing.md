# Hashing, complements, and canonical keys: retrieval sheet

## Recognition
- Repeated membership or equality searches inside a loop suggest a set or map.
- An equation with one known operand suggests deriving its complement.
- Grouping by rearrangement suggests a canonical key rather than pairwise comparisons.

## Invariant
Choose the exact information the future needs. Query state representing only eligible earlier items, account for the answer, then update state. A key must preserve the equivalence relation, and a stored value must preserve the requested multiplicity or position.

## Failure boundaries
- Insert before checking Two Sum.: [3] with target 6 finds the current index twice and reports an invalid pair. Repair: Query earlier state first, then insert.
- Use a set of letters as an anagram signature.: "aab" and "ab" both become {a,b}, although their multiplicities differ. Repair: Use sorted characters or a complete frequency vector.
- Walk a consecutive sequence from every value.: [1,2,3,4] walks lengths 4+3+2+1; the general case is quadratic. Repair: Only walk from values whose predecessor is absent.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.
