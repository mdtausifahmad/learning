# Start from zero: how to think in a coding interview

The objective is to construct a correct algorithm from the problem's constraints, explain why it works, and implement it cleanly. A pattern is a reusable argument about structure. Its name is useful only after you understand that argument.

Choose Java or Kotlin as your primary interview language. Read the other version to learn the same idea in a second syntax, but rehearse writing one language fluently without autocomplete. Confirm your interview's supported language and environment with the recruiter.

## 1. Turn the story into a contract

Write down five things before coding: input, output, valid input assumptions, examples, and bounds. “Find two matching orders” leaves many unanswered questions: two different indices? one answer or all answers? may values repeat? is the input sorted? can it be changed? can a sum exceed a 32-bit integer?

For `[3,3]` and target `6`, a valid pair needs both positions. A set of values alone loses the distinction between one `3` and two `3`s. For `[1,2,3]` and target `100`, decide what “no answer” returns. These decisions change code and tests.

## 2. Write the obvious correct solution

For a pair summing to a target, try every pair of different indices. With n values, the number of pairs is `n(n−1)/2`. The nested loops take O(n²) time and O(1) extra space if you return the first pair.

The baseline is a reference for correctness and a way to locate repeated work. Here the repeated work is searching the earlier values for `target − current`. A hash map can remember those earlier values, replacing repeated scans with expected constant-time lookups.

Do not optimize before you know what must be computed. On very small constraints, a simple exhaustive search can be the right answer.

## 3. Understand time and space, from operations

Big-O describes how an upper bound grows as input grows; it is not a stopwatch prediction. Say what n means. For a graph, use V vertices and E edges. For a string dictionary, use the total number of characters rather than just the number of words.

| Growth | What creates it | A useful intuition |
|---|---|---|
| O(1) | Fixed amount of work | A bounded number of operations |
| O(log n) | Discard a constant fraction repeatedly | Halving a search interval |
| O(n) | Each item processed a bounded number of times | One pass, or two pointers moving only forward |
| O(n log n) | Comparison sorting, balanced tree operations | n operations with logarithmic cost |
| O(n²) | All pairs | Input doubling can quadruple work |
| O(2ⁿ) | Include/exclude choices | Every subset may matter |
| O(n!) | All permutations | Order every item in every way |

Nested syntax does not prove quadratic time. A sliding window can have an inner `while` loop yet stay O(n): the right pointer moves n times and the left pointer moves at most n times **over the whole run**. This is an amortized counting argument.

Separate auxiliary memory from output memory. Returning all subsets already requires Θ(n·2ⁿ) element storage if every subset is copied. Recursive DFS consumes a call stack proportional to the maximum depth; it is not constant space. Hash-map performance is normally expected or amortized, not a universal worst-case O(1) guarantee.

Constraints rule out approaches; they do not automatically select one. Values near 10⁹ can overflow a sum of only three ints. Java `long` and Kotlin `Long` help, but conversion must happen before arithmetic: `(long)a + b` or `a.toLong() + b`.

## 4. Define the state

State is the information you keep between steps. Too little loses necessary information. Too much repeats work or makes the algorithm hard to reason about.

For a pair sum, remember `value → earlier index`. For a window, remember its boundaries and counts. For shortest unweighted paths, remember the BFS frontier and visited vertices. For dynamic programming, remember the answer to each smaller subproblem.

Ask: if two partial solutions have the same state, can every possible future continuation treat them identically? If not, your state is incomplete. In a flight problem with a stop limit, “current city” alone may be insufficient because remaining stops affect what is possible next.

## 5. State an invariant and prove three things

An invariant is a fact that stays true at a particular point in each iteration. Specify the point. “The map contains earlier indices immediately before processing index i” is precise; “the map stores numbers” does not prove anything.

1. **Initialization:** why is it true before the first iteration? An empty map correctly represents the empty prefix.
2. **Maintenance:** why does one step preserve it? Check for the complement among earlier indices, then insert the current value.
3. **Termination:** why does it establish the answer? Every pair is considered when its later index is reached, and a reported index is always distinct.

The order “look up, then insert” follows from the proof. Inserting first could reuse the same position for target `2*x`. This is why a template memorized without its invariant is fragile.

## 6. Use a small drawing before implementation

For `[2,7,11]`, target `9`:

```text
before index 0: earlier = {}          need 7 → not found
after  index 0: earlier = {2:0}
before index 1: earlier = {2:0}       need 2 → found index 0
return [0,1]; both indices differ, and 2+7=9
```

The lessons include offline node diagrams and state-by-state execution viewers. A diagram shows relationships; a trace shows the actual changing values. Use both when a pointer update feels mysterious.

## 7. Recognize a pattern by its conditions

| Structural fact | Candidate technique | Question that can disqualify it |
|---|---|---|
| Need an earlier matching value | Hashing | Does ordering or multiplicity matter? |
| Sorted pair search | Two pointers | Is the sort allowed, and must original indices survive? |
| Contiguous segment with repairable validity | Sliding window | Can adding/removing values reverse the monotonicity? |
| Many range totals or signed subarray sums | Prefix sums | Static data or updates? Count, earliest, or existence? |
| Monotone yes/no predicate | Binary search | Can you prove all false values precede all true values? |
| Nearest unresolved larger/smaller value | Monotonic stack | What should happen to equal values? |
| Cheapest next edge/path | Heap / Dijkstra | Are any edge weights negative? |
| Explore every valid construction | Backtracking | Can you prune, and are equivalent paths repeated? |
| Repeated subproblems with shared future | Dynamic programming | What minimal state determines all continuations? |

Words such as “minimum” or “longest” are not sufficient clues. Longest unique substring uses a window; longest increasing **subsequence** is not contiguous and needs DP or a binary-search invariant. A shortest path with equal edge costs uses BFS; arbitrary nonnegative weights need more.

## 8. Learn the JVM tools you will actually use

| Need | Java | Kotlin/JVM | Cost or caveat |
|---|---|---|---|
| Primitive numbers | `int[]`, `long[]` | `IntArray`, `LongArray` | Avoid boxing for large numeric tables |
| Key lookup | `HashMap<K,V>` | `HashMap<K,V>` | Expected lookup O(1); define missing-value meaning |
| Queue/stack | `ArrayDeque<T>` | `java.util.ArrayDeque<T>` | End operations amortized O(1); no null elements |
| Smallest item | `PriorityQueue<T>` | `PriorityQueue<T>` | Offer/poll O(log n), peek O(1) |
| Sorted keys | `TreeMap<K,V>` | `java.util.TreeMap<K,V>` | O(log n) get/put; ceiling/floor queries |
| Sort values | `Arrays.sort(a)` | `a.sort()` | Mutates; `sortedArray()` copies |
| Build string | `StringBuilder` | `StringBuilder` | Repeated immutable concatenation can be quadratic |
| Output list | `ArrayList<T>` | `mutableListOf<T>()` | Copy when saving a mutable backtracking path |

In Java comparators use `Integer.compare(a,b)` rather than `a-b`, which can overflow. In Kotlin, distinguish structural equality `==` from referential equality `===`. Arrays need `contentEquals`, and mutable arrays are unsuitable as content-based hash keys without a wrapper. Kotlin's `0 until n` is empty when n=0; review inclusive ranges carefully. JVM strings contain UTF-16 code units: an ASCII count table is correct only if the contract guarantees ASCII; code points and user-perceived characters require different treatment. Deep recursion can exhaust the JVM stack; discuss iterative traversal when depth can be large.

The solution snippets are standard-library functions, not online judge boilerplate. Each download includes imports and any helper types needed for that problem. No dependencies or browser network access are required to read them.

## 9. Test the boundaries deliberately

Test the smallest valid input, no solution, many solutions, duplicates, all equal, negative numbers if allowed, maximum magnitude, already ordered/reversed, and a case where the tempting alternative fails. For trees, test a chain as well as a balanced tree. For graphs, test disconnected vertices and cycles. For DP, test the zero state and unreachable states.

For small random inputs, compare your optimized algorithm with the baseline. This catches mistakes in boundaries and assumptions that happy-path examples miss. Keep a failure notebook with: the wrong assumption, the smallest counterexample, the repaired invariant, and a future retrieval question.

## 10. Practice communication under time pressure

Use a 45-minute rehearsal as a starting format: 5 minutes to clarify and model, 8 to derive the baseline and improved approach, 22 to implement, 7 to test, and 3 to discuss a variation. Actual interview formats differ; ask your recruiter.

Say what you know and what you are testing: “If values can be negative, shrinking this sum window may remove a useful negative value. I’ll use prefix sums instead.” That sentence demonstrates transfer better than naming five patterns.

Start with [hashing and complements](lessons/01-hashing.html). Return here when a later invariant or complexity argument feels unfamiliar.
