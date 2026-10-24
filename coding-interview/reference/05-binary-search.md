# Binary search on positions, structure, and answers: retrieval sheet

## Recognition
- A sorted predicate with one false-to-true boundary suggests lower bound.
- A requested minimum feasible or maximum feasible numeric answer suggests searching the answer domain.
- Two sorted arrays may permit searching a partition rather than merging elements.

## Invariant
Define what each endpoint means and show that the answer remains between them after every update. The comparison must prove an entire discarded region cannot contain the answer, and the candidate interval must strictly shrink. Feasibility search additionally requires a correct checker and a monotone predicate.

## Failure boundaries
- Set lo=mid in a first-true search with floor midpoint.: lo=0,hi=1 gives mid=0; if false, lo stays 0 forever. Repair: For a half-open first-true search, false means lo=mid+1.
- Binary-search any numeric objective without proving feasibility monotone.: Predicate "exactly two divisors" is true at 2, false at 4, true at 5; discarding a half based on one test loses solutions. Repair: State the false/true ordering of the predicate or choose another algorithm.
- Average two int median values before widening.: [2147483647] and [2147483647] overflow int addition and produce a negative median. Repair: Widen both boundary values before summing.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.
