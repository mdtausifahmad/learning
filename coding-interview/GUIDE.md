# Coding interview: reason from invariants

A ground-up Java and Kotlin course. No course guarantees an interview outcome. The curriculum is based on standard algorithms; current employer guidance and company question frequencies are not verified here. See RESOURCES.md for evidence status.

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




# Pattern 01: Hashing, complements, and canonical keys

An array scan often hides a repeated question: have I seen this value, how many times, or where? Start with the literal algorithm. For each new value, scan the previous values. That works, but repeats the same lookup and costs quadratic time. A hash table records the answer to the repeated question so the next query usually takes constant time. A set records membership; a map records a value such as count, index, or a whole group. These are different states, and choosing the wrong one loses information.

## Build the model
An array scan often hides a repeated question: have I seen this value, how many times, or where? Start with the literal algorithm. For each new value, scan the previous values. That works, but repeats the same lookup and costs quadratic time. A hash table records the answer to the repeated question so the next query usually takes constant time. A set records membership; a map records a value such as count, index, or a whole group. These are different states, and choosing the wrong one loses information.

A complement is the missing piece of an equation. If x + y = target and you are reading x, the only useful partner is target - x. Deriving that equation is more reliable than remembering the name Two Sum. The same thinking extends to prefix sums, residues, string signatures, and graph states. First ask which details can be forgotten while preserving exactly what the future needs. Two words can share a group if their sorted characters match; a set of characters would forget repeated letters and would be an invalid signature.

Hashing changes the lookup operation, not the logical order of the algorithm. If the current element must pair with an earlier element, query before insertion. Otherwise a lone 3 can appear to pair with itself for target 6. If you need all pair counts, store frequencies rather than one index. If you need the earliest position, preserve the first index rather than overwriting it. Each of these choices follows from the requested output, not from the container syntax.

Expected constant-time lookup is a performance model, not a mathematical worst-case guarantee. We report expected linear time for ordinary hash-based solutions and linear additional storage. Hash collisions are handled by the implementation; a collision of hash codes does not make unequal keys equal. By contrast, inventing an ambiguous signature can merge genuinely different inputs. Use immutable, equality-correct keys. For Java arrays, object identity is not content equality, so a List or String is often a safer signature.

To recognize a disguised hashing problem, narrate the brute force and locate a repeated equality test. Then derive a key from the relation, decide what information belongs behind the key, and establish when that information becomes valid. The advanced consecutive-sequence problem adds an amortization lesson: a nested loop can still be linear when every value is visited in the inner loop only once, starting at genuine sequence beginnings.

## Invariant
Choose the exact information the future needs. Query state representing only eligible earlier items, account for the answer, then update state. A key must preserve the equivalence relation, and a stored value must preserve the requested multiplicity or position.

## Recognize it
- Repeated membership or equality searches inside a loop suggest a set or map.
- An equation with one known operand suggests deriving its complement.
- Grouping by rearrangement suggests a canonical key rather than pairwise comparisons.

## Execution trace
1. Two Sum [3,2,4], target=6; i=0; map={}

Need 3; absent. Insert 3→0 only after the query.

2. i=1; map={3:0}; need=4

4 is absent; insert 2→1.

3. i=2; map={3:0,2:1}; need=2

Find index 1; return [1,2], using distinct positions.

4. Consecutive set={1,2,3,4,100,200}; starts={1,100,200}

Each start owns a disjoint walk; 2,3,4 never launch redundant walks.

## Wrong turns
### Insert before checking Two Sum.
[3] with target 6 finds the current index twice and reports an invalid pair.

Query earlier state first, then insert.

### Use a set of letters as an anagram signature.
"aab" and "ab" both become {a,b}, although their multiplicities differ.

Use sorted characters or a complete frequency vector.

### Walk a consecutive sequence from every value.
[1,2,3,4] walks lengths 4+3+2+1; the general case is quadratic.

Only walk from values whose predecessor is absent.

### Two Sum: derive the complement (Easy)

Given a non-null int array and a long target, return indices of any two distinct elements whose sum is target, or an empty int array if none. Do not mutate input.

[3, 2, 4], target 6 => [1, 2].

Baseline: Check all index pairs: O(n²) time and O(1) auxiliary space.

1. At index i, solve nums[j] = target - nums[i]. Only earlier indices may answer.
2. Store each previously seen number with one index. Query the long complement only if it fits an int.
3. Return on a hit; otherwise insert the current value. At exhaustion every pair has been considered at its later endpoint.

Correctness: Before i, the map contains a representative index for each value in indices below i; therefore a hit always uses distinct positions.

```java
static int[] twoSum(int[] nums, long target) {
    Map<Integer,Integer> seen = new HashMap<>();
    for (int i=0;i<nums.length;i++) {
        long need=target-(long)nums[i];
        if (need>=Integer.MIN_VALUE && need<=Integer.MAX_VALUE) {
            Integer j=seen.get((int)need);
            if (j!=null) return new int[]{j,i};
        }
        seen.put(nums[i],i);
    }
    return new int[0];
}
```

```kotlin
fun twoSum(nums: IntArray, target: Long): IntArray {
    val seen = HashMap<Int, Int>()
    for (i in nums.indices) {
        val need = target - nums[i].toLong()
        if (need in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()) {
            val j = seen[need.toInt()]
            if (j != null) return intArrayOf(j, i)
        }
        seen[nums[i]] = i
    }
    return intArrayOf()
}
```

Expected O(n) time, O(n) auxiliary space; output has at most two indices.

- [] => []
- [3], target 6 => []
- [3,3], target 6 => [0,1]

Twist: Count index pairs instead: add the complement frequency before incrementing the current frequency; use long for the total.

Practice source: https://leetcode.com/problems/two-sum/

### Group Anagrams: design an equality key (Medium)

Given a non-null array of non-null lowercase a-z strings, group strings that contain the same character multiplicities. Return all groups in any order; keep duplicates. Do not mutate input.

["eat","tea","tan","ate"] => [["eat","tea","ate"],["tan"]].

Baseline: Compare each string with existing group representatives by counting characters: up to O(m²L), for m words of maximum length L.

1. Two strings belong together exactly when their sorted characters are equal.
2. Create a sorted character copy as an immutable String key; append the original word to its key bucket.
3. Each input contributes exactly once; equality of keys proves equality of multiplicities.

Correctness: After k words, every processed word is in exactly the bucket for its canonical character ordering.

```java
static List<List<String>> groupAnagrams(String[] words) {
    Map<String,List<String>> groups=new HashMap<>();
    for (String word:words) {
        char[] key=word.toCharArray(); Arrays.sort(key);
        groups.computeIfAbsent(new String(key),k->new ArrayList<>()).add(word);
    }
    return new ArrayList<>(groups.values());
}
```

```kotlin
fun groupAnagrams(words: Array<String>): List<List<String>> {
    val groups = HashMap<String, MutableList<String>>()
    for (word in words) {
        val chars = word.toCharArray(); chars.sort()
        val key = String(chars)
        groups.getOrPut(key) { mutableListOf() }.add(word)
    }
    return groups.values.map { it.toList() }
}
```

O(Σ Li log Li) sorting time for word lengths Li (with O(m) overhead); O(Σ Li + m) auxiliary keys/buckets and output references.

- [] => []
- ["",""] => [["",""]]
- ["aab","ab"] => separate groups

Twist: For arbitrary Unicode code points, sort code points instead of UTF-16 chars if the intended unit is a Unicode character. For lowercase a-z, a 26-count signature reduces key construction to O(L+26).

Practice source: https://leetcode.com/problems/group-anagrams/

### Longest Consecutive Sequence: count work globally (Medium)

Given a non-null unsorted int array, return the largest number of distinct consecutive integer values present. Duplicates do not extend a sequence. Do not mutate input.

[100,4,200,1,3,2,2] => 4 for 1,2,3,4.

Baseline: For each value repeatedly search the array for its successor: O(n³) in a literal implementation; sorting gives O(n log n).

1. Put all values in a set so duplicates disappear.
2. Only start walking at x if x-1 is absent. A non-start would repeat a suffix of a sequence already counted.
3. Walk successors using long arithmetic at int boundaries. Every distinct value belongs to one start, so total successful successor visits are linear.

Correctness: Each completed walk accounts for one maximal disjoint sequence; no value is walked from a non-start.

```java
static int longestConsecutive(int[] nums) {
    Set<Long> values=new HashSet<>();
    for (int x:nums) values.add((long)x);
    int best=0;
    for (long x:values) {
        if (values.contains(x-1)) continue;
        long cur=x; int length=0;
        while (values.contains(cur)) { length++; cur++; }
        best=Math.max(best,length);
    }
    return best;
}
```

```kotlin
fun longestConsecutive(nums: IntArray): Int {
    val values = nums.map { it.toLong() }.toHashSet()
    var best = 0
    for (x in values) {
        if (x - 1 in values) continue
        var cur = x; var length = 0
        while (cur in values) { length++; cur++ }
        best = maxOf(best, length)
    }
    return best
}
```

Expected O(n) time and O(u) auxiliary space, where u is the number of unique values; scalar output.

- [] => 0
- [7,7] => 1
- [2147483647,-2147483648] => 1

Twist: With online insertions and a request after every insertion, store component lengths at sequence boundaries and merge left/right lengths; ignore duplicate insertions.

Practice source: https://leetcode.com/problems/longest-consecutive-sequence/

### First Missing Positive: encode membership in the array (Hard)

Given a non-null mutable int array, return its smallest absent positive integer. Input may be reordered. Empty input returns 1.

[3,4,-1,1] => 2.

Baseline: Test 1,2,... with repeated array scans: O(n²) time and O(1) auxiliary space; a set uses O(n) space.

1. The answer is in 1..n+1: n slots cannot contain all n+1 positive candidates.
2. Use slot x-1 as the membership location for x in 1..n. Swap until the current item is out of range or already in its destination.
3. Avoid swapping equal duplicates forever. The first slot without its expected value gives the answer.

Correctness: Each successful swap fixes at least one destination slot permanently; a fixed slot cannot be displaced by a different value because only its own value targets it.

```java
static int firstMissingPositive(int[] a) {
    int n=a.length;
    for (int i=0;i<n;i++) {
        while (a[i]>=1 && a[i]<=n && a[a[i]-1]!=a[i]) {
            int j=a[i]-1, tmp=a[j]; a[j]=a[i]; a[i]=tmp;
        }
    }
    for (int i=0;i<n;i++) if (a[i]!=i+1) return i+1;
    return n+1;
}
```

```kotlin
fun firstMissingPositive(a: IntArray): Int {
    val n = a.size
    for (i in a.indices) {
        while (a[i] >= 1 && a[i] <= n && a[a[i] - 1] != a[i]) {
            val j = a[i] - 1; val tmp = a[j]; a[j] = a[i]; a[i] = tmp
        }
    }
    for (i in a.indices) if (a[i] != i + 1) return i + 1
    return n + 1
}
```

O(n) time by at most n successful placements, O(1) auxiliary space; mutates input.

- [] => 1
- [1,1] => 2
- [-1,0] => 1
- [1,2,3] => 4

Twist: If mutation is forbidden, use a membership set or copy the array, paying O(n) space. This is a bounded direct-address table, illustrating how a domain bound can replace hashing.

Practice source: https://leetcode.com/problems/first-missing-positive/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="Two Sum: the lookup stores only earlier positions; query before insertion."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">solve the equation</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">query prior history</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">distinct indices guaranteed</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Read x = 4 at index 2</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Derive missing value: 6 − 4 = 2</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Earlier map {3→0, 2→1}</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Found 2 at index 1: return [1,2]</div></foreignObject></svg></div><figcaption>Two Sum: the lookup stores only earlier positions; query before insertion.</figcaption></figure>

## Additional practice

### [Contains Duplicate](https://leetcode.com/problems/contains-duplicate/) (Easy)

Hint: What single bit of history is needed?

Approach: Maintain a set; a failed insertion means repetition. Expected O(n) time, O(n) space. Streaming input needs the same state.

### [Valid Anagram](https://leetcode.com/problems/valid-anagram/) (Easy)

Hint: Preserve multiplicity, not just distinct letters.

Approach: Count symbols and subtract the other string; all counts must be zero. O(n+m) time and O(alphabet) space; define code-point versus UTF-16 semantics.

### [Intersection of Two Arrays II](https://leetcode.com/problems/intersection-of-two-arrays-ii/) (Easy)

Hint: The output contract keeps duplicates.

Approach: Count the shorter array and consume counts while scanning the longer: expected O(n+m), O(min(n,m)) auxiliary space plus output.

### [Isomorphic Strings](https://leetcode.com/problems/isomorphic-strings/) (Easy)

Hint: One-way consistency is insufficient.

Approach: Maintain mappings in both directions so two sources cannot map to one target. O(n) time and O(alphabet) state; account for chosen symbol representation.

### [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) (Medium)

Hint: Turn a range sum into a difference of cumulative sums.

Approach: Count earlier prefix values equal to currentPrefix-k, querying before insertion. Expected O(n), O(n) state. Negative values are allowed; see prefix-sums lesson.

### [Substring with Concatenation of All Words](https://leetcode.com/problems/substring-with-concatenation-of-all-words/) (Hard)

Hint: Equal word lengths create independent alignment classes.

Approach: For each alignment, slide by word length with required/current frequencies and shrink excess words. O(text length × word length) for straightforward substring keys; O(distinct words) map state plus results.

## Check your reasoning

Which state counts all index pairs with sum T, including duplicates?

1. Set of values
2. One index per value
3. Frequency per value

Answer: 3. A frequency preserves how many earlier positions can pair with the current one; query before incrementing to exclude self-pairing.

Why start a consecutive walk only when the predecessor is absent?

1. To sort the set
2. To visit every distinct value in only one walk
3. To avoid hash collisions

Answer: 2. Each maximal consecutive component has exactly one start. Walking from every member repeats suffixes and becomes quadratic.

## Transfer
A stream contains customer IDs and amounts. Return how many earlier purchases by the same customer make a pair totaling T with the current purchase. Negative amounts and duplicates are allowed.

Which fields must be part of a key?

What information is lost if you store only one earlier index?

Maintain customer → amount → long frequency. For each record, add the count of T-currentAmount in that customer bucket, then increment currentAmount. Query before insert excludes self-pairing. Expected O(n) time and O(n) space; count pairs in long and use overflow-aware arithmetic if amounts span all long values.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.

## Sources
- [Two Sum: derive the complement](https://leetcode.com/problems/two-sum/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Group Anagrams: design an equality key](https://leetcode.com/problems/group-anagrams/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Longest Consecutive Sequence: count work globally](https://leetcode.com/problems/longest-consecutive-sequence/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [First Missing Positive: encode membership in the array](https://leetcode.com/problems/first-missing-positive/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.


# Pattern 02: Two pointers and safe elimination

Two pointers means maintaining two positions whose movement removes a whole family of candidates. Merely having two indices does not make an algorithm useful. The essential question is: after comparing the values at these positions, why can every candidate skipped by moving a pointer be discarded? Start with a quadratic search over all pairs. A sorted array gives order, which lets one comparison rule out many pairs simultaneously.

## Build the model
Two pointers means maintaining two positions whose movement removes a whole family of candidates. Merely having two indices does not make an algorithm useful. The essential question is: after comparing the values at these positions, why can every candidate skipped by moving a pointer be discarded? Start with a quadratic search over all pairs. A sorted array gives order, which lets one comparison rule out many pairs simultaneously.

Suppose a sorted pair sum is too small. Keeping the left value and moving the right endpoint inward can only make the sum smaller. Therefore no pair using this left value and any current or smaller right value can succeed. Advance left. If the sum is too large, discard the right value for the symmetric reason. The proof explains both the direction and the requirement for sorted data. Sorting is not free, and if the requested output is original indices, you must retain indices in records or choose hashing instead.

A second form compares symmetric positions. For a palindrome, every outer pair must match; after matching them, only the inner substring remains undecided. An allowed deletion changes the state: at the first mismatch, one of exactly two endpoints must be removed. You cannot arbitrarily skip both. This becomes a useful small branching argument followed by deterministic scans.

A third form maintains a processed region and an unresolved region, as in trapping water. Water above a position is limited by the smaller of its best left wall and best right wall. Initially it seems that both complete wall arrays are necessary. But if the best known left wall is no taller than the best known right wall, the unresolved future on the right cannot reduce the known right boundary below the left wall. The left position can be settled now. Choosing the smaller boundary is a correctness argument, not a greedy guess.

Three Sum evolves the sorted pair technique by fixing one value and solving a smaller pair problem on its suffix. This adds a loop, but the inner two-pointer scan remains linear for each fixed value. The output asks for unique value triples, so duplicates require deliberate skipping. Duplicate handling is part of the output proof: it prevents repeated triples without removing the first occurrence of a valid value combination.

When facing a twist, classify the pointer roles: opposite endpoints of candidates, symmetric checks, or boundaries of settled work. Write the region still eligible before each move. Then construct an adversarial input that violates a hidden assumption, such as unsorted values, negative heights, or an altered distance objective. A familiar pointer motion is safe only when its elimination proof still holds.

## Invariant
Before each move, describe the candidates still unresolved. A pointer advances only when a comparison proves that every candidate it abandons is impossible or has been fully accounted for. Termination follows from a decreasing interval or increasing processed region.

## Recognize it
- Sorted values plus a pair relation often allow opposite endpoints.
- Symmetric constraints suggest comparing the two ends of an interval.
- A local result limited by the smaller of two known boundaries suggests settling that side.

## Execution trace
1. Sorted [1,2,4,7], target 6; l=0,r=3; sum=8

The right value 7 is too large even with the smallest candidate; discard it.

2. l=0,r=2; sum=5

The left value 1 is too small even with the largest remaining candidate; discard it.

3. l=1,r=2; sum=6

Return indices [1,2]; neither prior elimination could remove a solution.

4. Rain [3,0,3]: leftMax=3,rightMax=3, unresolved middle height=0

Either side can settle the middle; its exact contribution is 3.

## Wrong turns
### Use sorted-pair pointer moves on arbitrary order.
[3,2,4], target 6: 3+4 is too large, moving right loses the valid pair 2+4.

Sort while preserving indices or use hashing.

### At a palindrome mismatch always delete the left character.
"abbab": deleting rightmost b gives "abba"; deleting leftmost a gives "bbab", which fails.

Test both possible single deletions.

### For rainwater, sum distances to the nearest taller neighbor.
[3,1,2,0,3] has water limited by the distant height-3 boundaries; nearest local peaks underestimate cells.

Use maximum boundary heights, not nearest neighbors.

### Pair Sum in Sorted Order (Easy)

Given a non-null ascending int array and a long target, return zero-based indices of a matching distinct pair, or an empty array. Input is not modified.

[1,2,4,7], target 6 => [1,2].

Baseline: Enumerate all O(n²) pairs with O(1) space.

1. Start left at 0 and right at n-1, covering all candidate pairs.
2. If sum is too small, all pairs using left with endpoints at most right are too small; increment left. If too large, decrement right.
3. Stop at equality or when pointers meet, leaving no distinct candidate pair.

Correctness: At each step any still-possible answer lies between the pointers; every discarded endpoint is proved unable to participate in a remaining answer.

```java
static int[] sortedPair(int[] a,long target) {
    int l=0,r=a.length-1;
    while(l<r) {
        long sum=(long)a[l]+a[r];
        if(sum==target) return new int[]{l,r};
        if(sum<target) l++; else r--;
    }
    return new int[0];
}
```

```kotlin
fun sortedPair(a: IntArray, target: Long): IntArray {
    var l = 0; var r = a.lastIndex
    while (l < r) {
        val sum = a[l].toLong() + a[r]
        if (sum == target) return intArrayOf(l, r)
        if (sum < target) l++ else r--
    }
    return intArrayOf()
}
```

O(n) time because each move decreases the gap; O(1) auxiliary space.

- [] => []
- [3], target 6 => []
- [3,3], target 6 => [0,1]

Twist: If input is unsorted and original indices are required, sort (value,index) records with O(n log n) time/O(n) space, or use a complement map.

Practice source: https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/

### Valid Palindrome After At Most One Deletion (Easy)

Given a non-null string, decide whether deleting zero or one UTF-16 code unit can make it a palindrome. All characters matter, including punctuation and case. Empty string is valid.

"abca" => true by removing b or c; "abc" => false.

Baseline: Try deleting each position and scan the remainder: O(n²) time, with O(n) copies if physically constructing strings.

1. Move inward while outer code units match. Matching endpoints can be retained in a valid one-deletion solution.
2. At the first mismatch, at least one endpoint must be deleted. Test the two remaining intervals, with no further deletion permitted.
3. Return true if either interval is an exact palindrome; two checks are linear, not a recursive exponential search.

Correctness: All pairs outside [l,r] already match. The one allowed deletion is unused until the first mismatch; thereafter exactly one endpoint is skipped.

```java
static boolean validPalindrome(String s) {
    int l=0,r=s.length()-1;
    while(l<r) {
        if(s.charAt(l)!=s.charAt(r)) return exact(s,l+1,r)||exact(s,l,r-1);
        l++; r--;
    }
    return true;
}
static boolean exact(String s,int l,int r) {
    while(l<r) if(s.charAt(l++)!=s.charAt(r--)) return false;
    return true;
}
```

```kotlin
fun validPalindrome(s: String): Boolean {
    fun exact(start: Int, end: Int): Boolean {
        var l = start; var r = end
        while (l < r) if (s[l++] != s[r--]) return false
        return true
    }
    var l = 0; var r = s.lastIndex
    while (l < r) {
        if (s[l] != s[r]) return exact(l + 1, r) || exact(l, r - 1)
        l++; r--
    }
    return true
}
```

O(n) time and O(1) auxiliary space under UTF-16 code-unit semantics.

- "" => true
- "a" => true
- "deeee" => true
- "abc" => false

Twist: If the intended characters are Unicode code points, convert to a code-point array first, using O(n) space. For k deletions, interval DP or bounded branching is needed; blindly skipping either mismatch no longer gives a linear proof.

Practice source: https://leetcode.com/problems/valid-palindrome-ii/

### Three Sum Without Duplicate Triples (Medium)

Given a non-null int array, return every unique value triple summing to zero. Each triple uses three distinct indices. Order is arbitrary. Sort a copy, leaving input unchanged.

[-1,0,1,2,-1,-4] => [[-1,-1,2],[-1,0,1]].

Baseline: Enumerate O(n³) triples, canonicalize each, and deduplicate.

1. Sort a copy so value order becomes monotone. Fix each distinct first value.
2. Run a pair scan on the suffix for target -first, using long sums to avoid overflow.
3. On equality record the triple, then skip all duplicates of both selected endpoint values; skip duplicate first values in the outer loop.

Correctness: For each fixed first value, the unresolved pair region contains all unseen solutions; endpoint moves discard impossible pairs, and duplicate skipping removes only identical value triples.

```java
static List<List<Integer>> threeSum(int[] nums) {
    int[] a=nums.clone(); Arrays.sort(a);
    List<List<Integer>> ans=new ArrayList<>();
    for(int i=0;i<a.length-2;i++) {
        if(i>0 && a[i]==a[i-1]) continue;
        int l=i+1,r=a.length-1;
        while(l<r) {
            long sum=(long)a[i]+a[l]+a[r];
            if(sum<0) l++; else if(sum>0) r--;
            else {
                ans.add(List.of(a[i],a[l],a[r]));
                int lv=a[l],rv=a[r];
                while(l<r && a[l]==lv) l++;
                while(l<r && a[r]==rv) r--;
            }
        }
    }
    return ans;
}
```

```kotlin
fun threeSum(nums: IntArray): List<List<Int>> {
    val a = nums.copyOf(); a.sort()
    val ans = mutableListOf<List<Int>>()
    for (i in 0 until maxOf(0, a.size - 2)) {
        if (i > 0 && a[i] == a[i - 1]) continue
        var l = i + 1; var r = a.lastIndex
        while (l < r) {
            val sum = a[i].toLong() + a[l] + a[r]
            when {
                sum < 0 -> l++
                sum > 0 -> r--
                else -> {
                    ans.add(listOf(a[i], a[l], a[r]))
                    val lv = a[l]; val rv = a[r]
                    while (l < r && a[l] == lv) l++
                    while (l < r && a[r] == rv) r--
                }
            }
        }
    }
    return ans
}
```

O(n²) time plus O(n log n) sorting; O(n) copy and O(log n) primitive-sort stack, plus O(k) triples in output.

- [] => []
- [0,0,0,0] => [[0,0,0]]
- [-2147483648,1,2147483647] => one triple

Twist: If the output counts index triples, skipping duplicate values is wrong: multiply endpoint frequencies, with a combination count when both endpoints have the same value.

Practice source: https://leetcode.com/problems/3sum/

### Trapping Rain Water (Hard)

Given a non-null array of nonnegative int bar heights with unit width, return total trapped water as long. Empty input returns 0; input is unchanged.

[0,1,0,2,1,0,1,3,2,1,2,1] => 6.

Baseline: For every position scan both sides for their maximum wall: O(n²) time/O(1) space. Prefix/suffix maxima improve to O(n) time/O(n) space.

1. At each index water equals min(maxLeft,maxRight)-height, never negative.
2. Track the largest settled wall from each side. Process the side with smaller current maximum; the opposite known wall is already high enough, so unseen bars cannot reduce the limiting boundary.
3. Add the gap for that side and advance it. Every position becomes settled once.

Correctness: All positions outside [l,r] have their exact contribution counted; leftMax and rightMax summarize the settled boundary heights, and the chosen smaller bound is sufficient to finalize the next cell.

```java
static long trap(int[] h) {
    int l=0,r=h.length-1,leftMax=0,rightMax=0;
    long water=0;
    while(l<=r) {
        if(leftMax<=rightMax) {
            leftMax=Math.max(leftMax,h[l]);
            water+=(long)leftMax-h[l++];
        } else {
            rightMax=Math.max(rightMax,h[r]);
            water+=(long)rightMax-h[r--];
        }
    }
    return water;
}
```

```kotlin
fun trap(h: IntArray): Long {
    var l = 0; var r = h.lastIndex
    var leftMax = 0; var rightMax = 0; var water = 0L
    while (l <= r) {
        if (leftMax <= rightMax) {
            leftMax = maxOf(leftMax, h[l]); water += leftMax.toLong() - h[l]; l++
        } else {
            rightMax = maxOf(rightMax, h[r]); water += rightMax.toLong() - h[r]; r--
        }
    }
    return water
}
```

O(n) time, O(1) auxiliary space; long total avoids overflow across many bars.

- [] => 0
- [3,0,3] => 3
- [1,2,3] => 0
- [3,0,0,3] => 6

Twist: With variable widths, multiply each finalized gap by its bar width using long. In a 2D height map, opposite pointers do not describe the boundary; use a min-heap flood from the perimeter.

Practice source: https://leetcode.com/problems/trapping-rain-water/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="Sorted pair search discards a whole family of impossible pairs at each move."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">start at opposite ends</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">discard 7: even smallest partner is too large</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">discard 1: even largest partner is too small</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">[1, 2, 4, 7], target 6</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">1 + 7 = 8 &gt; 6</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">[1, 2, 4]: 1 + 4 = 5 &lt; 6</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">[2, 4]: sum 6</div></foreignObject></svg></div><figcaption>Sorted pair search discards a whole family of impossible pairs at each move.</figcaption></figure>

## Additional practice

### [Move Zeroes](https://leetcode.com/problems/move-zeroes/) (Easy)

Hint: Separate the read position from the next write position.

Approach: Copy each nonzero forward, preserving order, then fill the tail with zero. O(n), O(1), mutates input; the written prefix contains exactly processed nonzero values.

### [Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/) (Easy)

Hint: The output prefix stores one representative per run.

Approach: Read every item and write only if it differs from the last retained value. O(n), O(1). Allowing at most k copies changes the comparison to write-k.

### [Container With Most Water](https://leetcode.com/problems/container-with-most-water/) (Medium)

Hint: Which endpoint limits area, and what happens as width shrinks?

Approach: Record area and move the shorter endpoint; keeping it while reducing width cannot improve the area. O(n), O(1). This is a different objective from total rainwater.

### [Sort Colors](https://leetcode.com/problems/sort-colors/) (Medium)

Hint: Maintain finished zero, one, unknown, and two regions.

Approach: Dutch-national-flag partition in O(n), O(1). After swapping an unknown with the right region, inspect the incoming value before advancing.

### [Four Sum](https://leetcode.com/problems/4sum/) (Medium)

Hint: Fix two values, then solve a sorted pair problem.

Approach: Sort, skip duplicates at both outer levels, and two-pointer scan the suffix. O(n³) plus output, O(n) if preserving input. Use long sums.

### [Boats to Save People](https://leetcode.com/problems/boats-to-save-people/) (Medium)

Hint: The heaviest remaining person must go now.

Approach: If lightest+heaviest fits, pair them; otherwise heaviest goes alone. Sort O(n log n), scan O(n). Assumes at most two per boat and every person individually fits.

## Check your reasoning

For sorted pair sum, the current sum is too small. Which move is justified?

1. Decrease right
2. Increase left
3. Move either endpoint arbitrarily

Answer: 2. Keeping the current left value with any smaller right value can only decrease the sum, so that left endpoint has no remaining valid partner.

For unique Three Sum value triples, when should duplicate endpoint values be skipped?

1. After recording a matching triple
2. Never
3. Before checking any pair, remove all repeated values from the array

Answer: 1. After one matching value pair is recorded, equal endpoint values only repeat that triple. Globally removing duplicates would lose valid triples such as [-1,-1,2].

## Transfer
A sorted list of sensor readings must be split into as many disjoint pairs as possible, each with sum at most a limit. Every unpaired reading is allowed. Return the maximum pair count.

Consider the largest remaining reading.

If it cannot fit with the smallest, can it fit with anyone?

Use opposite endpoints. If smallest+largest exceeds the limit, discard the largest as unpairable. Otherwise pair them and advance both. An exchange argument shows an optimum can use this pair: if each endpoint was paired elsewhere, re-pair their partners, whose sum is no greater than the original feasible cross-sum. O(n) time and O(1) space; sortedness is required.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.

## Sources
- [Pair Sum in Sorted Order](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Valid Palindrome After At Most One Deletion](https://leetcode.com/problems/valid-palindrome-ii/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Three Sum Without Duplicate Triples](https://leetcode.com/problems/3sum/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.


# Pattern 03: Sliding windows: expand, repair, and count

A contiguous subarray or substring is described by two boundaries. Brute force chooses a left boundary, chooses a right boundary, then inspects everything between them. Even after incremental sums remove the innermost scan, there are quadratic many intervals. Sliding windows reduce the number of boundary choices only when neighboring intervals have a useful relationship. We maintain information for one current interval and change it when an endpoint enters or leaves.

## Build the model
A contiguous subarray or substring is described by two boundaries. Brute force chooses a left boundary, chooses a right boundary, then inspects everything between them. Even after incremental sums remove the innermost scan, there are quadratic many intervals. Sliding windows reduce the number of boundary choices only when neighboring intervals have a useful relationship. We maintain information for one current interval and change it when an endpoint enters or leaves.

Begin with a fixed width. All candidate intervals have exactly k items. Moving one step removes the old left item and adds the new right item. A rolling sum is sufficient because addition can be undone by subtraction. A rolling maximum cannot generally be updated by simply subtracting an outgoing value: when the maximum leaves, the remaining maximum is unknown. That variant needs a monotonic deque. Ask what operation summarizes the interval and whether removing an item is supported.

For a variable width, the critical extra structure is monotonic validity. In a substring with no repeated symbols, adding symbols can make a valid interval invalid, and removing from the left cannot introduce a duplicate. For each right endpoint, move left until the interval is valid again. The earliest valid left boundary produces the longest valid interval ending here. Each boundary moves forward at most n times, so a while loop inside a for loop can still be linear.

At most K distinct symbols has the same direction. Once the interval is valid, every suffix ending at the same right endpoint is also valid. If its length is r-l+1, exactly that many intervals end here. This converts longest-window reasoning into counting. Exactly K can be computed as atMost(K)-atMost(K-1). Derive this set difference rather than treating it as a magic formula: an interval counted with at most K but not with at most K-1 contains exactly K distinct values.

Minimum cover reverses the objective. Expand until the interval contains all required multiplicities; then shrink while it remains valid, recording candidates before each removal. Coverage is not equivalent to containing each distinct required character once. If the target is AABC, two As are necessary. Maintain how many required character occurrences are still missing, or how many character requirements are satisfied. Be explicit about which interpretation your counter uses.

Negative numbers expose why the proof matters. For positive numbers, removing the left item cannot increase a sum, and expanding cannot decrease it. With negative numbers those statements fail, so a sum-threshold window can discard a boundary that later becomes useful. Symbol-count windows do not rely on numeric signs, so they remain valid. Recognize the structural reason for monotonicity rather than applying a template whenever a prompt says subarray.

## Invariant
State must exactly describe the current interval. Expand by adding one endpoint; shrink only when the property has a monotone repair direction. Count or optimize only after stating whether the interval is valid. Every pointer moves forward, so total boundary movement is linear.

## Recognize it
- Contiguous input plus repeated interval statistics suggests maintained window state.
- At-most constraints whose validity survives deleting an endpoint support shrinking until valid.
- Minimum coverage supports expanding until valid, then shrinking while valid.

## Execution trace
1. Unique substring "abba": r=0, c=a, left=0, last={a:0}, best=1

The singleton window is valid.

2. r=1, c=b, left=0, last={a:0,b:1}, best=2

No prior b; extend the longest valid interval.

3. r=2, c=b, left=2, last={a:0,b:2}, best=2

Drop everything through prior b at index 1.

4. r=3, c=a, prior a=0, left remains 2, window="ba"

The old a lies outside the window; moving left backward would violate uniqueness.

## Wrong turns
### Use a positive-sum shrinking rule with negative values.
[1,-1,5], target sum at least 5: after reaching total 5, removing 1 gives 4 and stops, missing [5] of length 1.

For shortest threshold sums with negatives, use prefix sums and a monotonic deque; the sum-window proof no longer holds.

### Move left to lastSeen+1 without taking max.
"abba": after the second b left=2; seeing final a would move left back to 1 and accept "bba".

Set left=max(left,lastSeen+1).

### Minimum cover tracks only distinct required symbols.
s="ABC", t="AABC" would appear covered although it lacks the second A.

Track multiplicity deficits or fully satisfied required frequencies.

### Maximum Sum of a Fixed-Size Window (Easy)

Given a non-null int array and k with 1<=k<=n, return the maximum sum among all contiguous windows of length k as long. Throw IllegalArgumentException for invalid k. Input is unchanged.

[-5,-2,-8,-1], k=2 => -7 from [-5,-2].

Baseline: Recompute every length-k sum: O(nk) time/O(1) space.

1. Sum the first k items using long. Initialize best to this actual window, not zero.
2. For each next item, add it and subtract the item k positions behind. The updated sum is exactly the next window.
3. Record the maximum after each transition; all n-k+1 windows appear once.

Correctness: After processing right index r>=k-1, sum equals a[r-k+1..r] and best is the largest sum among all windows ending at or before r.

```java
static long maxWindowSum(int[] a,int k) {
    if(k<1 || k>a.length) throw new IllegalArgumentException("invalid k");
    long sum=0; for(int i=0;i<k;i++) sum+=a[i];
    long best=sum;
    for(int r=k;r<a.length;r++) {
        sum+=(long)a[r]-a[r-k]; best=Math.max(best,sum);
    }
    return best;
}
```

```kotlin
fun maxWindowSum(a: IntArray, k: Int): Long {
    require(k >= 1 && k <= a.size) { "invalid k" }
    var sum = 0L
    for (i in 0 until k) sum += a[i]
    var best = sum
    for (r in k until a.size) {
        sum += a[r].toLong() - a[r - k]; best = maxOf(best, sum)
    }
    return best
}
```

O(n) time, O(1) auxiliary space. Negative values are supported; long protects the sum.

- [7], k=1 => 7
- [-3,-2], k=1 => -2
- [], k=1 => exception

Twist: For a fixed-window maximum value instead of sum, use a deque of indices with decreasing values; addition/subtraction cannot recover a departing maximum.

Practice source: https://leetcode.com/problems/maximum-average-subarray-i/

### Longest Substring Without Repetition (Medium)

Given a non-null String, return the maximum length of a contiguous substring with distinct UTF-16 code units. Empty string returns 0. Input is unchanged.

"abba" => 2, from "ab" or "ba".

Baseline: Enumerate substrings and test distinctness: O(n³) literally, or O(n²) with a fresh set for each start.

1. Store the most recent index of each code unit. At right r, its prior position p would conflict if p>=left.
2. Move left to max(left,p+1); never move left backward for a repeat outside the current interval.
3. Update last occurrence, then maximize r-left+1. This directly jumps over the minimum invalid prefix.

Correctness: The interval [left,r] has unique symbols and left is the smallest possible valid start for a substring ending at r.

```java
static int longestUnique(String s) {
    Map<Character,Integer> last=new HashMap<>();
    int left=0,best=0;
    for(int r=0;r<s.length();r++) {
        char c=s.charAt(r); Integer p=last.put(c,r);
        if(p!=null) left=Math.max(left,p+1);
        best=Math.max(best,r-left+1);
    }
    return best;
}
```

```kotlin
fun longestUnique(s: String): Int {
    val last = HashMap<Char, Int>(); var left = 0; var best = 0
    for (r in s.indices) {
        val p = last.put(s[r], r)
        if (p != null) left = maxOf(left, p + 1)
        best = maxOf(best, r - left + 1)
    }
    return best
}
```

Expected O(n) time; O(min(n,65536)) map space for UTF-16 code units.

- "" => 0
- "aaaa" => 1
- "abba" => 2

Twist: For user-perceived characters, decide whether to tokenize code points or grapheme clusters first; code-unit lengths are not necessarily displayed-character lengths.

Practice source: https://leetcode.com/problems/longest-substring-without-repeating-characters/

### Subarrays With Exactly K Distinct Values (Hard)

Given a non-null int array and k, return the number of nonempty contiguous subarrays containing exactly k distinct values as long. k<=0 returns 0. Input is unchanged.

[1,2,1,2,3], k=2 => 7.

Baseline: Enumerate each start and grow a frequency map: expected O(n²) time/O(n) state.

1. First solve at most k distinct. Expand right, then remove from left until the frequency map has at most k keys.
2. Every start between left and right gives a valid interval ending at right: add right-left+1.
3. Subtract atMost(k-1) from atMost(k). Intervals with fewer distinct values cancel exactly once.

Correctness: For each right endpoint, left is the earliest valid start for atMost(k), and precisely all starts in [left,right] are counted.

```java
static long exactlyK(int[] a,int k) {
    return k<=0 ? 0 : atMost(a,k)-atMost(a,k-1);
}
static long atMost(int[] a,int k) {
    if(k<0) return 0;
    Map<Integer,Integer> freq=new HashMap<>(); int left=0; long total=0;
    for(int r=0;r<a.length;r++) {
        freq.merge(a[r],1,Integer::sum);
        while(freq.size()>k) {
            int x=a[left++], c=freq.get(x)-1;
            if(c==0) freq.remove(x); else freq.put(x,c);
        }
        total+=r-left+1;
    }
    return total;
}
```

```kotlin
fun exactlyK(a: IntArray, k: Int): Long {
    fun atMost(limit: Int): Long {
        if (limit < 0) return 0L
        val freq = HashMap<Int, Int>(); var left = 0; var total = 0L
        for (r in a.indices) {
            freq[a[r]] = (freq[a[r]] ?: 0) + 1
            while (freq.size > limit) {
                val x = a[left++]; val c = freq.getValue(x) - 1
                if (c == 0) freq.remove(x) else freq[x] = c
            }
            total += (r - left + 1).toLong()
        }
        return total
    }
    return if (k <= 0) 0L else atMost(k) - atMost(k - 1)
}
```

Expected O(n) time from two linear scans; O(min(n,k+1)) auxiliary frequency state for positive k; scalar long output.

- [] with k=2 => 0
- [1,1], k=1 => 3
- [1,2], k=0 => 0

Twist: Exactly K odd values has the same at-most difference proof, with a scalar odd counter instead of a frequency map. The new state follows from the property being counted.

Practice source: https://leetcode.com/problems/subarrays-with-k-different-integers/

### Minimum Window Covering a Multiset (Hard)

Given non-null strings s and t, return a shortest substring of s containing every UTF-16 code unit of t with its required multiplicity. Return "" when t is empty or no cover exists. On ties keep the earliest. Input is unchanged.

s="ADOBECODEBANC", t="ABC" => "BANC".

Baseline: Enumerate O(n²) substrings and count each: O(n³+m) literal time.

1. Store required counts and initialize missing=t.length. A count is allowed to become negative, representing surplus copies.
2. When adding c, decrement its count; reduce missing only if the previous count was positive. Once missing is zero, the window covers t.
3. Record the candidate, then remove left. Increment that character count; if it becomes positive, one required occurrence was lost and expansion must resume.

Correctness: The map stores required minus current counts for relevant characters; missing counts unsatisfied required occurrences. While missing is zero every recorded interval covers t, and shrinking explores the shortest cover for that right endpoint.

```java
static String minWindow(String s,String t) {
    if(t.isEmpty()) return "";
    Map<Character,Integer> need=new HashMap<>();
    for(char c:t.toCharArray()) need.merge(c,1,Integer::sum);
    int missing=t.length(),left=0,bestStart=0,bestLen=Integer.MAX_VALUE;
    for(int r=0;r<s.length();r++) {
        char c=s.charAt(r);
        if(need.containsKey(c)) {
            int old=need.get(c); if(old>0) missing--; need.put(c,old-1);
        }
        while(missing==0) {
            if(r-left+1<bestLen) { bestLen=r-left+1; bestStart=left; }
            char gone=s.charAt(left++);
            if(need.containsKey(gone)) {
                int now=need.get(gone)+1; need.put(gone,now); if(now>0) missing++;
            }
        }
    }
    return bestLen==Integer.MAX_VALUE ? "" : s.substring(bestStart,bestStart+bestLen);
}
```

```kotlin
fun minWindow(s: String, t: String): String {
    if (t.isEmpty()) return ""
    val need = HashMap<Char, Int>()
    for (c in t) need[c] = (need[c] ?: 0) + 1
    var missing = t.length; var left = 0; var bestStart = 0; var bestLen = Int.MAX_VALUE
    for (r in s.indices) {
        val c = s[r]; val old = need[c]
        if (old != null) { if (old > 0) missing--; need[c] = old - 1 }
        while (missing == 0) {
            if (r - left + 1 < bestLen) { bestLen = r - left + 1; bestStart = left }
            val gone = s[left++]; val count = need[gone]
            if (count != null) { val now = count + 1; need[gone] = now; if (now > 0) missing++ }
        }
    }
    return if (bestLen == Int.MAX_VALUE) "" else s.substring(bestStart, bestStart + bestLen)
}
```

Expected O(n+m) time for string lengths n,m; O(distinct symbols in t) auxiliary map space, plus O(best length) output copy.

- s="",t="A" => ""
- s="AA",t="AA" => "AA"
- s="AB",t="AA" => ""
- t="" => ""

Twist: If the required tokens are words rather than code units, tokenize and use token frequencies. Preserve original offsets if the result must be a substring of the original text.

Practice source: https://leetcode.com/problems/minimum-window-substring/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="Unique-symbol window: expand right, then jump left past an in-window duplicate."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">expand: still unique</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">right reaches second b; set left=2</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">old a at 0 is outside; never move left backward</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">a | b b a: window a</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">a b | b a: window ab</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">a b | b | a: duplicate b at 1 and 2</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">a b | b a: left stays 2; window ba</div></foreignObject></svg></div><figcaption>Unique-symbol window: expand right, then jump left past an in-window duplicate.</figcaption></figure>

## Additional practice

### [Minimum Size Subarray Sum](https://leetcode.com/problems/minimum-size-subarray-sum/) (Medium)

Hint: Prove the shrink rule using strictly positive input.

Approach: Expand rolling sum, shrink while sum>=target, recording lengths. O(n), O(1). With negatives use prefix/deque instead.

### [Permutation in String](https://leetcode.com/problems/permutation-in-string/) (Medium)

Hint: Equal lengths turn multiset containment into equality.

Approach: Maintain fixed-window counts or deficits and compare matched categories. O(n+alphabet), O(alphabet); define symbol domain.

### [Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/) (Medium)

Hint: A window needs length-maxFrequency replacements.

Approach: Maintain counts and a historical maximum frequency; shrink on length-maxFrequency>k. O(n), O(alphabet). A stale maximum is safe for maximizing length but cannot certify the final window contents; an exact maximum is simpler to prove.

### [Max Consecutive Ones III](https://leetcode.com/problems/max-consecutive-ones-iii/) (Medium)

Hint: Treat zero count as the resource budget.

Approach: Shrink when zeros exceed k, maximize valid length. O(n), O(1), requires binary values or an explicit definition of replaceable items.

### [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) (Hard)

Hint: Remove dominated values while retaining their indices.

Approach: Maintain decreasing deque; expire out-of-window indices at the front and pop smaller/equal new-dominated values at the back. O(n), O(k), output O(n-k+1).

### [Shortest Subarray with Sum at Least K](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/) (Hard)

Hint: Negative values require a different monotone object.

Approach: Use prefix sums and an increasing deque of candidate prefix indices. Pop front while threshold is met and pop back dominated larger prefixes. O(n), O(n); long prefix sums.

## Check your reasoning

A minimum-cover target is AABC. Which state is sufficient?

1. Membership of A,B,C
2. Per-character multiplicity deficits
3. Only current window length

Answer: 2. Coverage requires two As. Distinct membership loses exactly the information needed to verify the target multiset.

Why is a positive-sum threshold window unsafe with negative numbers?

1. Hash maps cannot contain negatives
2. Removing the left value may increase the sum instead of decreasing it
3. The sum can never reach the threshold

Answer: 2. For [1,-1,5] and threshold 5, removing 1 temporarily breaks validity, but removing the following -1 restores it. A stop-on-first-invalid shrink misses [5].

## Transfer
In a stream of device events, find the longest contiguous period involving at most three device IDs, where each ID may appear no more than twice.

There are two independent reasons a window becomes invalid.

Can removing the oldest event ever make either constraint worse?

Maintain per-ID frequencies, number of distinct IDs, and number of IDs with frequency above two. Add right; shrink left while distinct>3 or overfull>0. Both constraints improve or stay unchanged under deletion, so the earliest valid left yields the longest valid ending interval. Expected O(n) time and O(1) bounded active state, plus a transient extra ID.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.

## Sources
- [Maximum Sum of a Fixed-Size Window](https://leetcode.com/problems/maximum-average-subarray-i/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Longest Substring Without Repetition](https://leetcode.com/problems/longest-substring-without-repeating-characters/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Subarrays With Exactly K Distinct Values](https://leetcode.com/problems/subarrays-with-k-different-integers/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Minimum Window Covering a Multiset](https://leetcode.com/problems/minimum-window-substring/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.


# Pattern 04: Prefix sums, balance, and range counting

A prefix is everything before a boundary. Define P[0]=0 and P[i+1]=P[i]+a[i]. P[i] is the sum of exactly the first i elements, so the half-open range [l,r) has sum P[r]-P[l]. The empty prefix is a real state, not a special trick: it allows ranges beginning at zero to obey the same formula as every other range. Drawing boundaries between elements often prevents the off-by-one mistakes that arise when prefixes are described vaguely as sums up to an index.

## Build the model
A prefix is everything before a boundary. Define P[0]=0 and P[i+1]=P[i]+a[i]. P[i] is the sum of exactly the first i elements, so the half-open range [l,r) has sum P[r]-P[l]. The empty prefix is a real state, not a special trick: it allows ranges beginning at zero to obey the same formula as every other range. Drawing boundaries between elements often prevents the off-by-one mistakes that arise when prefixes are described vaguely as sums up to an index.

Begin with repeated static range queries. Computing every query by a loop repeats addition over overlapping ranges. One linear preprocessing pass stores cumulative sums, then each query needs two lookups and one subtraction. This works because addition has an inverse. Minimum does not: knowing the minimum of a large prefix and a smaller prefix does not reveal the minimum of their difference. For mutable input, changing one element invalidates many ordinary prefixes; Fenwick trees or segment trees provide a different update/query tradeoff.

Now turn the representation around. Instead of asking for the sum of a known range, ask which earlier boundary creates a desired range ending here. The equation P[r]-P[l]=k becomes P[l]=P[r]-k. A hash map can count earlier prefixes with exactly that value. Query before inserting P[r], because l must be strictly earlier for a nonempty range. Insert the empty prefix with count one so a range beginning at zero is counted naturally. Unlike a positive-sum sliding window, this equation remains correct when values are negative.

The value stored per prefix depends on the objective. A count answers how many ranges. The earliest index gives the longest range. The latest index gives the shortest nonempty range, with careful query-before-update order. The map key may change too: divisibility of a range sum by k means the two prefixes have the same remainder modulo k. Normalize negative remainders so mathematically equal residue classes share a key on the JVM.

An equilibrium index is a local application of the same decomposition. Let left be the sum strictly before the index, total the whole-array sum, and x the current value. The right sum is total-left-x. Check equality before adding x to left. The difference between before and through is the entire source of many bugs.

The advanced range-sum-count problem asks for a whole interval of eligible prefix values rather than one equality. Hashing is no longer sufficient. Divide-and-conquer sorts prefix values in each half while preserving the distinction between earlier left-half boundaries and later right-half boundaries. Two moving bounds count right values whose difference lies in [lower,upper], then a merge prepares the next level. This connects prefix representation to ordered counting: derive the equation first, then choose the data structure that answers it.

## Invariant
A prefix is tied to a boundary, not vaguely to an element. Store P[0]=0; after consuming i values, P[i] is their exact sum. For counting, only earlier eligible boundaries belong in the query state. Translate the range condition algebraically before choosing equality hashing or ordered counting.

## Recognize it
- Many static range-sum queries suggest one cumulative preprocessing pass.
- A subarray sum constraint can be rewritten as a relation between two prefixes.
- Divisibility means equality of normalized residues, even with negative values.

## Execution trace
1. a=[1,-1,0],k=0; prefix=0; freq={0:1}; answer=0

The empty boundary is eligible before any element.

2. After 1: prefix=1; query freq[1]=0; freq={0:1,1:1}

No zero-sum range ends at the first item.

3. After -1: prefix=0; add freq[0]=1; freq={0:2,1:1}; answer=1

The first two values sum to zero.

4. After 0: prefix=0; add freq[0]=2; answer=3

Two earlier zero prefixes produce [0] and [1,-1,0].

## Wrong turns
### Forget the empty prefix.
[3], k=3: there is no previously stored prefix 0, so the only valid range is missed.

Seed frequency[0]=1 or prefix index 0 before scanning.

### Insert the current prefix before counting matches.
[5],k=0: the current prefix matches itself, incorrectly counting an empty subarray.

Query first, then insert.

### Compare raw JVM negative remainders.
[-1,2], k=2 has prefixes with raw residues -1 and 1; their difference is divisible by 2, but raw equality misses [2].

Normalize remainders into 0..k-1 using floorMod.

### Immutable Range Sum Queries (Easy)

Given a non-null int array and non-null queries [l,r] with 0<=l<=r<n, return a long sum for each inclusive range. Invalid query bounds throw IllegalArgumentException. Empty arrays may have zero queries. Input is unchanged.

a=[2,-1,4,3], queries=[[0,2],[1,3]] => [5,6].

Baseline: Sum each range independently: O(qn) worst-case time and O(q) output.

1. Build a long prefix array of length n+1 with prefix[0]=0.
2. Translate inclusive [l,r] into boundaries l and r+1. Subtract prefix[l] from prefix[r+1].
3. Validate bounds before indexing. Each output is exact because the common first l terms cancel.

Correctness: prefix[i] equals the sum of a[0..i), so subtraction removes exactly the elements outside a queried interval.

```java
static long[] rangeSums(int[] a,int[][] queries) {
    long[] prefix=new long[a.length+1];
    for(int i=0;i<a.length;i++) prefix[i+1]=prefix[i]+a[i];
    long[] ans=new long[queries.length];
    for(int i=0;i<queries.length;i++) {
        int l=queries[i][0],r=queries[i][1];
        if(l<0 || l>r || r>=a.length) throw new IllegalArgumentException("bounds");
        ans[i]=prefix[r+1]-prefix[l];
    }
    return ans;
}
```

```kotlin
fun rangeSums(a: IntArray, queries: Array<IntArray>): LongArray {
    val prefix = LongArray(a.size + 1)
    for (i in a.indices) prefix[i + 1] = prefix[i] + a[i]
    return LongArray(queries.size) { i ->
        val l = queries[i][0]; val r = queries[i][1]
        require(l >= 0 && l <= r && r < a.size) { "bounds" }
        prefix[r + 1] - prefix[l]
    }
}
```

O(n+q) time, O(n) auxiliary prefix space, plus O(q) output; query rows must contain exactly two entries.

- a=[],queries=[] => []
- a=[5],queries=[[0,0]] => [5]
- [2147483647,2147483647],query=[0,1] => 4294967294

Twist: With point updates between queries, ordinary prefixes become expensive to maintain. A Fenwick tree supports updates and range sums in O(log n), with O(n) storage.

Practice source: https://leetcode.com/problems/range-sum-query-immutable/

### Find the Leftmost Equilibrium Index (Easy)

Given a non-null int array, return the smallest index whose elements strictly to the left and right have equal sums, or -1. Empty sides have sum 0. Input is unchanged.

[1,7,3,6,5,6] => 3 because both side sums are 11.

Baseline: For each index sum both sides: O(n²) time/O(1) space.

1. Compute total once in long. Keep left=0 before examining index 0.
2. At i, derive right=total-left-a[i]. Compare left and right before adding the current element.
3. Return the first match; otherwise advance left by a[i].

Correctness: Before index i, left is exactly the sum of indices below i; total-left-a[i] is exactly the sum of indices above i.

```java
static int pivotIndex(int[] a) {
    long total=0,left=0; for(int x:a) total+=x;
    for(int i=0;i<a.length;i++) {
        if(left==total-left-a[i]) return i;
        left+=a[i];
    }
    return -1;
}
```

```kotlin
fun pivotIndex(a: IntArray): Int {
    var total = 0L; for (x in a) total += x
    var left = 0L
    for (i in a.indices) {
        if (left == total - left - a[i]) return i
        left += a[i]
    }
    return -1
}
```

O(n) time, O(1) auxiliary space. Long sums support arbitrary int values.

- [] => -1
- [9] => 0
- [0,0] => 0
- [1,2,3] => -1

Twist: For all equilibrium indices, collect every match instead of returning early. For an online mutable array, updates require a different representation; this two-pass scan assumes static data.

Practice source: https://leetcode.com/problems/find-pivot-index/

### Count Subarrays With a Given Sum (Medium)

Given a non-null int array and a long k, return the count of nonempty contiguous subarrays whose sum equals k. Negative values and zero are allowed. Input is unchanged.

[1,-1,0], k=0 => 3: [1,-1], [0], and [1,-1,0].

Baseline: Grow every start with a running sum: O(n²) time/O(1) auxiliary space.

1. Write P[r]-P[l]=k, then rearrange to earlierPrefix=P[r]-k.
2. Initialize frequency[0]=1 for the empty prefix. For each new prefix, add its required earlier frequency before inserting the new prefix.
3. Store frequencies and answer in long because repeated zero prefixes create quadratically many subarrays.

Correctness: Before querying prefix P[r], the map counts exactly P[0] through P[r-1]; each hit corresponds one-to-one to a nonempty range ending at r-1.

```java
static long countSum(int[] a,long k) {
    Map<Long,Long> freq=new HashMap<>(); freq.put(0L,1L);
    long prefix=0,answer=0;
    for(int x:a) {
        prefix+=x;
        // Prefix sums fit long for any JVM int array. Guard subtraction overflow.
        if(!((k>0 && prefix<Long.MIN_VALUE+k) || (k<0 && prefix>Long.MAX_VALUE+k)))
            answer+=freq.getOrDefault(prefix-k,0L);
        freq.merge(prefix,1L,Long::sum);
    }
    return answer;
}
```

```kotlin
fun countSum(a: IntArray, k: Long): Long {
    val freq = HashMap<Long, Long>(); freq[0L] = 1L
    var prefix = 0L; var answer = 0L
    for (x in a) {
        prefix += x
        val overflow = (k > 0 && prefix < Long.MIN_VALUE + k) ||
            (k < 0 && prefix > Long.MAX_VALUE + k)
        if (!overflow) answer += freq[prefix - k] ?: 0L
        freq[prefix] = (freq[prefix] ?: 0L) + 1L
    }
    return answer
}
```

Expected O(n) time, O(n) auxiliary map space; long answer can hold at most n(n+1)/2 for a JVM int array.

- [] => 0
- [0,0],k=0 => 3
- [1],k=1 => 1
- [1],k=Long.MIN_VALUE => 0

Twist: For the longest matching range, store the earliest index for each prefix rather than its count. Preserve the first occurrence; later overwrites lose longer candidates.

Practice source: https://leetcode.com/problems/subarray-sum-equals-k/

### Count Subarrays Divisible by K (Medium)

Given a non-null int array and positive int k, return the count of nonempty contiguous subarrays with sum divisible by k as long. Throw for k<=0. Negative values are permitted; input is unchanged.

[4,5,0,-2,-3,1], k=5 => 7.

Baseline: Enumerate O(n²) running subarray sums and test each modulus.

1. A range sum is divisible by k exactly when its endpoint prefixes have equal residues modulo k.
2. Maintain the current normalized residue, using long for residue+x. The JVM remainder may be negative, so normalize with floorMod.
3. Add the number of earlier occurrences of this residue, then increment its frequency. Seed residue zero once for the empty prefix.

Correctness: Residue frequencies count exactly earlier prefix boundaries; each equal-residue pair defines one nonempty divisible range.

```java
static long countDivisible(int[] a,int k) {
    if(k<=0) throw new IllegalArgumentException("k must be positive");
    Map<Integer,Long> freq=new HashMap<>(); freq.put(0,1L);
    int residue=0; long answer=0;
    for(int x:a) {
        residue=(int)Math.floorMod((long)residue+x,(long)k);
        answer+=freq.getOrDefault(residue,0L);
        freq.merge(residue,1L,Long::sum);
    }
    return answer;
}
```

```kotlin
fun countDivisible(a: IntArray, k: Int): Long {
    require(k > 0) { "k must be positive" }
    val freq = HashMap<Int, Long>(); freq[0] = 1L
    var residue = 0; var answer = 0L
    for (x in a) {
        residue = Math.floorMod(residue.toLong() + x, k.toLong()).toInt()
        answer += freq[residue] ?: 0L
        freq[residue] = (freq[residue] ?: 0L) + 1L
    }
    return answer
}
```

Expected O(n) time, O(min(n,k)) auxiliary map space. A frequency array uses O(k) space when k is suitably small.

- [] with k=3 => 0
- [-1,2,9],k=2 => 2
- [0,0],k=1 => 3

Twist: For subarrays with remainder q, query normalized(currentResidue-q) rather than equal residues. For a length-at-least-two requirement, retain earliest indices or delay frequency eligibility.

Practice source: https://leetcode.com/problems/subarray-sums-divisible-by-k/

### Count Range Sums in a Numeric Band (Hard)

Given a non-null int array and long lower<=upper with both bounds between -10^18 and 10^18, count nonempty contiguous ranges whose sums lie in [lower,upper]. Input is unchanged. Invalid bounds throw.

[-2,5,-1], lower=-2, upper=2 => 3: [-2], [-1], [-2,5,-1].

Baseline: Enumerate all O(n²) running sums and count those in the band.

1. Form long prefix sums. A valid earlier/later pair satisfies lower<=P[r]-P[l]<=upper.
2. Divide prefix indices into halves. Recursively count pairs wholly inside either half, then sort each half by prefix value. For each left prefix, maintain two advancing right-half boundaries delimiting differences in the band.
3. Count high-low cross pairs and merge the halves. Every pair belongs uniquely to a recursive side or the cross step at its first split.

Correctness: Recursive calls return sorted prefix values and exact pair counts for their original index halves. During a cross step every left-half prefix occurred earlier than every right-half prefix in the original array.

```java
static long countRangeSum(int[] a,long lower,long upper) {
    if(lower>upper || lower< -1_000_000_000_000_000_000L || upper>1_000_000_000_000_000_000L)
        throw new IllegalArgumentException("bounds");
    long[] p=new long[a.length+1],tmp=new long[p.length];
    for(int i=0;i<a.length;i++) p[i+1]=p[i]+a[i];
    return countMerge(p,tmp,0,p.length,lower,upper);
}
static long countMerge(long[] p,long[] tmp,int lo,int hi,long lower,long upper) {
    if(hi-lo<=1) return 0;
    int mid=lo+(hi-lo)/2;
    long ans=countMerge(p,tmp,lo,mid,lower,upper)+countMerge(p,tmp,mid,hi,lower,upper);
    int first=mid,after=mid;
    for(int i=lo;i<mid;i++) {
        while(first<hi && p[first]-p[i]<lower) first++;
        while(after<hi && p[after]-p[i]<=upper) after++;
        ans+=after-first;
    }
    int i=lo,j=mid,w=lo;
    while(i<mid || j<hi) tmp[w++]=(j==hi || (i<mid && p[i]<=p[j])) ? p[i++] : p[j++];
    System.arraycopy(tmp,lo,p,lo,hi-lo);
    return ans;
}
```

```kotlin
fun countRangeSum(a: IntArray, lower: Long, upper: Long): Long {
    require(lower <= upper && lower >= -1_000_000_000_000_000_000L && upper <= 1_000_000_000_000_000_000L)
    val p = LongArray(a.size + 1); val tmp = LongArray(p.size)
    for (i in a.indices) p[i + 1] = p[i] + a[i]
    fun solve(lo: Int, hi: Int): Long {
        if (hi - lo <= 1) return 0L
        val mid = lo + (hi - lo) / 2
        var ans = solve(lo, mid) + solve(mid, hi)
        var first = mid; var after = mid
        for (i in lo until mid) {
            while (first < hi && p[first] - p[i] < lower) first++
            while (after < hi && p[after] - p[i] <= upper) after++
            ans += (after - first).toLong()
        }
        var i = lo; var j = mid; var w = lo
        while (i < mid || j < hi) {
            if (j == hi || (i < mid && p[i] <= p[j])) tmp[w++] = p[i++] else tmp[w++] = p[j++]
        }
        for (k in lo until hi) p[k] = tmp[k]
        return ans
    }
    return solve(0, p.size)
}
```

O(n log n) time, O(n) prefix/merge buffers and O(log n) recursion stack. JVM int-array length bounds keep actual prefix differences within long.

- [] => 0
- [0,0],lower=upper=0 => 3
- [1],lower=2,upper=3 => 0

Twist: If sums are queried online, coordinate compression plus a Fenwick tree can count earlier prefixes in [current-upper,current-lower]. The offline merge works because future prefix values are known.

Practice source: https://leetcode.com/problems/count-of-range-sum/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="A range is the difference between two cumulative boundary sums."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">one cumulative pass</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">inclusive end becomes boundary r+1</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">cancel the shared prefix</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Array [2, −1, 4, 3]</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Boundary prefixes [0, 2, 1, 5, 8]</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Query indices [1,3] = boundaries [1,4)</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">P[4] − P[1] = 8 − 2 = 6</div></foreignObject></svg></div><figcaption>A range is the difference between two cumulative boundary sums.</figcaption></figure>

## Additional practice

### [Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/) (Medium)

Hint: Separate the product strictly before and strictly after each position.

Approach: Write prefix products to output, then multiply by a rolling suffix product. O(n), O(1) extra excluding output. Avoid division to support zeros; specify bounded products or use big integers.

### [Contiguous Array](https://leetcode.com/problems/contiguous-array/) (Medium)

Hint: Equal zero/one counts become zero sum after mapping 0 to -1.

Approach: Store the first index of each balance, initialized balance 0 at index -1. Maximize currentIndex-earliestIndex. O(n), O(n); input must be binary.

### [Continuous Subarray Sum](https://leetcode.com/problems/continuous-subarray-sum/) (Medium)

Hint: Same residue plus a distance constraint.

Approach: Store earliest prefix index per residue and require distance>=2. O(n), O(min(n,k)) for positive k. If allowing k=0, seek equal exact prefixes instead.

### [Make Sum Divisible by P](https://leetcode.com/problems/make-sum-divisible-by-p/) (Medium)

Hint: Remove a range whose residue equals the total residue.

Approach: For each current residue, query current-total mod p, keeping latest indices for shortest removal. O(n), O(min(n,p)). Reject removing the entire array.

### [Range Sum Query 2D - Immutable](https://leetcode.com/problems/range-sum-query-2d-immutable/) (Medium)

Hint: Draw the overlap of two excluded rectangles.

Approach: 2D prefix sums answer rectangles by inclusion-exclusion: bottom-right minus top strip minus left strip plus their overlap. O(rows×cols) build, O(1) query.

### [Corporate Flight Bookings](https://leetcode.com/problems/corporate-flight-bookings/) (Medium)

Hint: A range increment changes only two boundaries in a difference representation.

Approach: Add seats at start and subtract after end, then prefix the difference array. O(n+q), O(n), long if aggregate seats may exceed int.

## Check your reasoning

Why initialize earlierPrefixCount[0] to one?

1. To count an empty output range
2. To represent the boundary before index zero
3. To avoid sorting

Answer: 2. A nonempty range starting at element zero needs the earlier empty prefix boundary. Query-before-insert still excludes empty subarrays.

For the longest range with sum K, what belongs behind each prefix key?

1. Its latest index
2. Its earliest index
3. Its frequency only

Answer: 2. For a fixed right boundary, the earliest matching left boundary maximizes distance. Overwriting it with a later occurrence loses candidates.

## Transfer
Given daily integer profit, return the longest nonempty period whose total profit is divisible by a positive integer m. Loss days are allowed.

What relation must the two boundary prefixes satisfy?

For the longest period, which occurrence of each residue should survive?

Normalize cumulative residues modulo m. Seed residue 0 at boundary index 0. At each boundary r, if its residue first appeared at l, r-l is a divisible period; retain only the earliest l and maximize the distance. Expected O(n) time and O(min(n,m)) space. Unlike counting, frequencies are unnecessary.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.

## Sources
- [Immutable Range Sum Queries](https://leetcode.com/problems/range-sum-query-immutable/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Find the Leftmost Equilibrium Index](https://leetcode.com/problems/find-pivot-index/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Count Subarrays With a Given Sum](https://leetcode.com/problems/subarray-sum-equals-k/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Count Subarrays Divisible by K](https://leetcode.com/problems/subarray-sums-divisible-by-k/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Count Range Sums in a Numeric Band](https://leetcode.com/problems/count-of-range-sum/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.


# Pattern 05: Binary search on positions, structure, and answers

Binary search is an argument about a boundary in an ordered set of possibilities. It is broader than finding a value in a sorted array. Start by imagining a yes/no predicate over candidate positions: false, false, false, true, true. The task is to locate the first true position. Each test discards half the remaining candidates because monotonicity proves that every position on one side has the same answer. Without that proof, halving is only a guess.

## Build the model
Binary search is an argument about a boundary in an ordered set of possibilities. It is broader than finding a value in a sorted array. Start by imagining a yes/no predicate over candidate positions: false, false, false, true, true. The task is to locate the first true position. Each test discards half the remaining candidates because monotonicity proves that every position on one side has the same answer. Without that proof, halving is only a guess.

A lower-bound search uses the half-open interval [lo,hi). We seek the first index whose value is at least target. Initially lo=0 and hi=n; n is the legitimate answer when every value is smaller. At midpoint, a value below target proves every position through mid is too small, so lo=mid+1. Otherwise mid may be the first valid position, so keep it by setting hi=mid. The interval shrinks on every iteration and termination lo==hi identifies the boundary. Duplicates need no special case because the predicate still has a single false-to-true transition.

Rotated arrays show that global sortedness is not always necessary, but local structure must replace it. With distinct values, at least one half around the midpoint is sorted. Determine which half, check whether the target lies in that half's value interval, and retain the appropriate side. Duplicates can make the sorted-half inference ambiguous; then the worst case may require linear shrinking. State the distinctness assumption rather than quietly inheriting it from a remembered solution.

Searching an answer space requires two separate algorithms: a feasibility checker and the boundary search around it. For shipping packages in order, a capacity is feasible if a greedy scan uses at most D days. Larger capacity cannot make a feasible schedule fail, giving monotonicity. The minimum possible capacity is the heaviest package, and the sum of all weights is always sufficient for at least one day. Greedy is correct because filling each day as far as possible leaves no more remaining work than any other schedule with the same number of days. Binary search then finds the first feasible capacity.

Median partition is advanced because the search variable is a cut, not a returned value. Choose how many elements to take from the first sorted array; the required left-half size determines the cut in the other. A partition is correct when both left boundary values are no greater than the opposite right boundary. If the first left boundary is too large, reduce that cut; if the other left boundary is too large, increase it. Sentinels model empty sides. Search the shorter array so every complementary cut is in range.

For unfamiliar twists, first write the candidate domain, monotone predicate, endpoint meaning, and progress measure. Then test empty input, all-false, all-true, duplicates, and a two-candidate interval by hand. Overflow-safe midpoint calculation prevents an implementation failure, but it does not repair a false monotonicity assumption.

## Invariant
Define what each endpoint means and show that the answer remains between them after every update. The comparison must prove an entire discarded region cannot contain the answer, and the candidate interval must strictly shrink. Feasibility search additionally requires a correct checker and a monotone predicate.

## Recognize it
- A sorted predicate with one false-to-true boundary suggests lower bound.
- A requested minimum feasible or maximum feasible numeric answer suggests searching the answer domain.
- Two sorted arrays may permit searching a partition rather than merging elements.

## Execution trace
1. Lower bound a=[1,2,2,5], target=2; lo=0,hi=4,mid=2

a[2]>=2, so position 2 may be first; set hi=2.

2. lo=0,hi=2,mid=1

a[1]>=2; retain it by setting hi=1.

3. lo=0,hi=1,mid=0

a[0]<2; discard through index 0 by lo=1.

4. lo=hi=1; return 1

Every earlier value is too small and this position meets the predicate.

## Wrong turns
### Set lo=mid in a first-true search with floor midpoint.
lo=0,hi=1 gives mid=0; if false, lo stays 0 forever.

For a half-open first-true search, false means lo=mid+1.

### Binary-search any numeric objective without proving feasibility monotone.
Predicate "exactly two divisors" is true at 2, false at 4, true at 5; discarding a half based on one test loses solutions.

State the false/true ordering of the predicate or choose another algorithm.

### Average two int median values before widening.
[2147483647] and [2147483647] overflow int addition and produce a negative median.

Widen both boundary values before summing.

### Lower Bound: First Position At Least Target (Easy)

Given a non-null ascending int array, return the first index with value >=target, or n when no such value exists. Empty input returns 0. Input is unchanged.

[1,2,2,5], target 2 => 1; target 6 => 4.

Baseline: Scan left-to-right until a value meets the threshold: O(n) time/O(1) space.

1. Define the monotone predicate a[i]>=target. Search candidate boundary positions from 0 through n.
2. Use [lo,hi) with hi=n. If a[mid]<target discard through mid; otherwise retain mid as a possible first valid index.
3. At termination all indices before lo fail and every existing index at or after lo meets the threshold.

Correctness: All indices below lo are too small, and all indices at or above hi are large enough. The first valid boundary remains in [lo,hi].

```java
static int lowerBound(int[] a,int target) {
    int lo=0,hi=a.length;
    while(lo<hi) {
        int mid=lo+(hi-lo)/2;
        if(a[mid]<target) lo=mid+1; else hi=mid;
    }
    return lo;
}
```

```kotlin
fun lowerBound(a: IntArray, target: Int): Int {
    var lo = 0; var hi = a.size
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (a[mid] < target) lo = mid + 1 else hi = mid
    }
    return lo
}
```

O(log(n+1)) time, O(1) auxiliary space.

- [] => 0
- [2,2],target=2 => 0
- [2,2],target=3 => 2

Twist: Upper bound changes the predicate to a[i]>target. The difference upperBound(target)-lowerBound(target) counts duplicates without scanning them.

Practice source: https://leetcode.com/problems/search-insert-position/

### Search a Rotated Sorted Array (Medium)

Given a non-null array obtained by rotating a strictly increasing int array, return the target index or -1. Values are distinct; empty input returns -1. Input is unchanged.

[4,5,6,7,0,1,2], target 0 => 4.

Baseline: Scan all values: O(n) time/O(1) space.

1. Compare midpoint to the target first. If equal, return.
2. If a[lo]<=a[mid], the left half is sorted. Keep it only when a[lo]<=target<a[mid]; otherwise keep the right half.
3. Otherwise the right half is sorted; retain it precisely when a[mid]<target<=a[hi]. Each update removes the midpoint and one impossible half.

Correctness: If the target exists, it remains in the inclusive candidate interval [lo,hi]. Distinctness makes the sorted-half classification decisive.

```java
static int searchRotated(int[] a,int target) {
    int lo=0,hi=a.length-1;
    while(lo<=hi) {
        int mid=lo+(hi-lo)/2;
        if(a[mid]==target) return mid;
        if(a[lo]<=a[mid]) {
            if(a[lo]<=target && target<a[mid]) hi=mid-1; else lo=mid+1;
        } else {
            if(a[mid]<target && target<=a[hi]) lo=mid+1; else hi=mid-1;
        }
    }
    return -1;
}
```

```kotlin
fun searchRotated(a: IntArray, target: Int): Int {
    var lo = 0; var hi = a.lastIndex
    while (lo <= hi) {
        val mid = lo + (hi - lo) / 2
        if (a[mid] == target) return mid
        if (a[lo] <= a[mid]) {
            if (a[lo] <= target && target < a[mid]) hi = mid - 1 else lo = mid + 1
        } else {
            if (a[mid] < target && target <= a[hi]) lo = mid + 1 else hi = mid - 1
        }
    }
    return -1
}
```

O(log(n+1)) time and O(1) auxiliary space for distinct values.

- [] => -1
- [1],target=1 => 0
- [3,1],target=1 => 1

Twist: Allow duplicates: if a[lo]==a[mid]==a[hi], shrink both ends after checking midpoint. Ambiguity can force O(n) time, for example all equal values with one hidden exceptional value.

Practice source: https://leetcode.com/problems/search-in-rotated-sorted-array/

### Minimum Shipping Capacity Within D Days (Medium)

Given a non-null array of nonnegative int package weights, preserve their order and ship a contiguous batch each day. Return the minimum long capacity that ships everything in at most positive int days. Empty input returns 0. Invalid days or negative weights throw. Input is unchanged.

[1,2,3,1,1], days=4 => 3, using [1,2],[3],[1],[1].

Baseline: Try every integer capacity from maximum weight through total weight and simulate: O(nS), where S is the numeric search range.

1. Capacity must be at least the heaviest package and at most the total weight. Sum in long.
2. For a candidate capacity, greedily put each next package on the current day if it fits; otherwise start a new day. This minimizes days because no schedule can place a longer prefix on that same day.
3. Feasibility changes from false to true as capacity grows. Keep mid on feasible, discard through mid on infeasible, and return the first feasible value.

Correctness: The minimum feasible capacity lies in [lo,hi]; hi is feasible. The checker counts the fewest days possible at its capacity.

```java
static long shipWithinDays(int[] weights,int days) {
    if(days<=0) throw new IllegalArgumentException("days");
    long lo=0,hi=0;
    for(int w:weights) {
        if(w<0) throw new IllegalArgumentException("negative weight");
        lo=Math.max(lo,w); hi+=w;
    }
    while(lo<hi) {
        long mid=lo+(hi-lo)/2,load=0; int used=1;
        for(int w:weights) {
            if(load+w>mid) { used++; load=0; }
            load+=w;
        }
        if(used<=days) hi=mid; else lo=mid+1;
    }
    return lo;
}
```

```kotlin
fun shipWithinDays(weights: IntArray, days: Int): Long {
    require(days > 0) { "days" }
    var lo = 0L; var hi = 0L
    for (w in weights) { require(w >= 0); lo = maxOf(lo, w.toLong()); hi += w }
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2; var load = 0L; var used = 1
        for (w in weights) {
            if (load + w > mid) { used++; load = 0L }
            load += w
        }
        if (used <= days) hi = mid else lo = mid + 1
    }
    return lo
}
```

O(n log(S+1)) time for S=sum(weights)-max(weights); O(1) auxiliary space. Input validation is O(n) even when all weights are zero.

- [] with days=1 => 0
- [0,0],days=3 => 0
- [10,20],days=1 => 30
- [10,20],days=5 => 20

Twist: If reordering packages is allowed, the greedy contiguous-batch checker no longer models the problem. Monotonicity of capacity may remain, but the feasibility subproblem can become bin packing.

Practice source: https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/

### Median of Two Sorted Arrays by Partition (Hard)

Given two non-null ascending int arrays with at least one total element, return their combined median as double without merging. Empty+empty throws. Input is unchanged.

[1,3] and [2] => 2.0; [1,2] and [3,4] => 2.5.

Baseline: Merge the arrays: O(m+n) time, O(m+n) extra space; a partial merge can reduce auxiliary space to O(1).

1. Search a cut i in the shorter array. The other cut j satisfies i+j=(m+n+1)/2, making the left side have the extra item when total length is odd.
2. Use long sentinels at missing boundary elements. A valid cut satisfies leftA<=rightB and leftB<=rightA, so all left-half values are at most all right-half values.
3. If leftA>rightB, reduce i. Otherwise if leftB>rightA, increase i. At a valid cut, odd median is max(lefts), even median averages max(lefts) and min(rights).

Correctness: The desired partition remains among the candidate cuts. Increasing the first cut increases leftA and decreases the second cut, giving the directional correction when either cross-order constraint fails.

```java
static double medianSorted(int[] first,int[] second) {
    int[] a=first,b=second;
    if(a.length>b.length) { a=second; b=first; }
    int m=a.length,n=b.length;
    if(m==0 && n==0) throw new IllegalArgumentException("empty");
    int half=(int)(((long)m+n+1)/2),lo=0,hi=m;
    while(lo<=hi) {
        int i=lo+(hi-lo)/2,j=half-i;
        long al=i==0?Long.MIN_VALUE:a[i-1], ar=i==m?Long.MAX_VALUE:a[i];
        long bl=j==0?Long.MIN_VALUE:b[j-1], br=j==n?Long.MAX_VALUE:b[j];
        if(al<=br && bl<=ar) {
            long left=Math.max(al,bl);
            if(((long)m+n)%2==1) return (double)left;
            return (left+Math.min(ar,br))/2.0;
        }
        if(al>br) hi=i-1; else lo=i+1;
    }
    throw new IllegalArgumentException("arrays must be sorted");
}
```

```kotlin
fun medianSorted(first: IntArray, second: IntArray): Double {
    val a = if (first.size <= second.size) first else second
    val b = if (first.size <= second.size) second else first
    val m = a.size; val n = b.size
    require(m != 0 || n != 0) { "empty" }
    val half = ((m.toLong() + n + 1) / 2).toInt()
    var lo = 0; var hi = m
    while (lo <= hi) {
        val i = lo + (hi - lo) / 2; val j = half - i
        val al = if (i == 0) Long.MIN_VALUE else a[i - 1].toLong()
        val ar = if (i == m) Long.MAX_VALUE else a[i].toLong()
        val bl = if (j == 0) Long.MIN_VALUE else b[j - 1].toLong()
        val br = if (j == n) Long.MAX_VALUE else b[j].toLong()
        if (al <= br && bl <= ar) {
            val left = maxOf(al, bl)
            return if ((m.toLong() + n) % 2 == 1L) left.toDouble()
                else (left + minOf(ar, br)) / 2.0
        }
        if (al > br) hi = i - 1 else lo = i + 1
    }
    throw IllegalArgumentException("arrays must be sorted")
}
```

O(log(min(m,n)+1)) time and O(1) auxiliary space. Real boundary values are int, so their sum fits long; sortedness is a precondition, not exhaustively validated.

- [] and [1] => 1.0
- [1,1] and [1] => 1.0
- [2147483647] and [2147483647] => 2147483647.0

Twist: To find the kth smallest, use left size k and restrict i to max(0,k-n)..min(k,m). The valid partition returns max(left boundaries), with 1<=k<=m+n.

Practice source: https://leetcode.com/problems/median-of-two-sorted-arrays/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="Lower bound locates a boundary in a false-to-true predicate."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">ask value ≥ target</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">true midpoint remains a candidate</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">false midpoint is discarded</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Values [1, 2, 2, 5]; target 2</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Predicate [false, true, true, true]</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">[lo,hi) = [0,4) → [0,2) → [0,1)</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">a[0] fails → lo=1=hi; first true is 1</div></foreignObject></svg></div><figcaption>Lower bound locates a boundary in a false-to-true predicate.</figcaption></figure>

## Additional practice

### [Find First and Last Position of Element in Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) (Medium)

Hint: Search two boundaries rather than one arbitrary occurrence.

Approach: Find first >=target and first >target. If the first does not equal target, return absent; otherwise last=upper-1. O(log n), O(1), no target+1 overflow.

### [Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/) (Medium)

Hint: Compare the midpoint to the rightmost value.

Approach: If mid>right, minimum is strictly right of mid; otherwise retain mid and left half. O(log n), O(1) for distinct values. Duplicates require ambiguity shrinking.

### [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) (Medium)

Hint: Can a fixed speed finish within h hours?

Approach: Count hours as Σ ceil(pile/speed) with long. Search 1..maxPile when h>=number of positive piles. O(n log maxPile), O(1). State impossible-input policy.

### [Find Peak Element](https://leetcode.com/problems/find-peak-element/) (Medium)

Hint: A slope points toward at least one peak.

Approach: Compare a[mid] to a[mid+1]; rise means a peak exists rightward, fall means one exists at or left of mid. O(log n), O(1), assumes adjacent elements differ and virtual boundaries are -infinity.

### [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/) (Hard)

Hint: For nonnegative values, the minimum number of blocks for a limit is greedy.

Approach: Search maximum block-sum limit with a contiguous greedy checker. O(n log total), O(1). Exact k nonempty blocks follows by splitting blocks when k<=n; negative values invalidate this checker.

### [Kth Smallest Element in a Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/) (Medium)

Hint: Search a value and count entries <=it.

Approach: Staircase count in a row/column sorted matrix takes O(rows+cols); binary search the value range. O((rows+cols)log range), O(1), with long midpoint arithmetic.

## Check your reasoning

In half-open lower bound, a[mid] is below target. What is the safe update?

1. lo=mid
2. hi=mid−1
3. lo=mid+1

Answer: 3. All indices through mid are too small, so all must be discarded. lo=mid can fail to make progress on a two-boundary interval.

What makes shipping capacity suitable for answer-space binary search?

1. The average package weight
2. Feasibility is monotone and a greedy scan checks it correctly
3. Package weights are distinct

Answer: 2. If capacity C works, every larger capacity works. The checker separately needs the proof that maximal-prefix daily packing minimizes days.

## Transfer
Jobs arrive in fixed order. A worker processes one contiguous batch per day. Each nonempty day also consumes a fixed setup cost c from its capacity. Find the smallest daily capacity needed to finish within D days.

Can the setup cost be separated from the variable workload limit?

What happens to feasibility when capacity grows?

For nonnegative job workloads and c>=0, any nonempty day at capacity C can process at most C-c workload. Greedily pack in order using this effective capacity; search from maxJob+c to sumJobs+c. Larger C cannot increase needed days, so first-feasible binary search works in O(n log(sumJobs+1)) time and O(1) auxiliary space. Empty input requires no setup and returns zero; use overflow-aware long bounds.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.

## Sources
- [Lower Bound: First Position At Least Target](https://leetcode.com/problems/search-insert-position/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Search a Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Minimum Shipping Capacity Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Median of Two Sorted Arrays by Partition](https://leetcode.com/problems/median-of-two-sorted-arrays/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.


# Pattern 06: Sorting, intervals, scheduling, and sweep lines

Intervals carry two coordinates, so an unsorted collection appears to require comparing every pair. Sorting chooses a useful direction in which to process them. Once intervals are ordered by start, every future interval starts at least as late as the current one. That fact lets us finish old components: if the next start lies beyond the current merged end, no still-later interval can bridge the gap. Sorting is therefore part of the proof, not cosmetic preparation.

## Build the model
Intervals carry two coordinates, so an unsorted collection appears to require comparing every pair. Sorting chooses a useful direction in which to process them. Once intervals are ordered by start, every future interval starts at least as late as the current one. That fact lets us finish old components: if the next start lies beyond the current merged end, no still-later interval can bridge the gap. Sorting is therefore part of the proof, not cosmetic preparation.

Before touching code, define endpoint semantics. Closed intervals [a,b] include both endpoints, so [1,3] and [3,5] overlap and merge. Half-open appointments [a,b) allow one meeting to start exactly when another ends. An overlap comparison that uses < instead of <= can change the answer at every boundary. Empty intervals, zero duration, invalid reversed endpoints, and whether touching counts as conflict belong in the problem contract. Most interview mistakes in interval code are disagreements about these details.

Merging maintains one unfinished union component. If the next start is within its current end, extend the end to the larger endpoint; never replace it with a smaller nested interval's end. Otherwise emit the component and begin a new one. Inserting one interval into an already sorted disjoint list is the same proof without sorting: copy intervals strictly before it, absorb every overlap into the growing new interval, emit it, then copy the suffix. The input promise provides the ordering work for free.

Scheduling asks a different question from merging. To retain the maximum number of nonoverlapping appointments, choose the appointment that finishes earliest, then repeat among compatible choices. Why earliest finish rather than earliest start or shortest duration? An exchange proof says the first appointment of an optimal schedule can be replaced by an earliest-finishing appointment, leaving at least as much room for every later choice. Applying the same argument to the remaining suffix proves optimality. If appointments have different rewards, the number-based exchange argument no longer proves anything about total reward; use weighted interval scheduling DP.

A sweep line treats endpoints as events. Imagine moving a vertical line from left to right and updating which intervals are active. Counting active intervals answers overlap queries. Keeping the maximum active height answers a skyline. A heap alone does not directly support arbitrary ending-building deletion; a multiset such as TreeMap with occurrence counts does. Process every event at the same coordinate as one batch before emitting a visible-height change, so the output does not contain artificial intermediate states.

To adapt this family to twists, identify what order exposes future information, what state summarizes active or unfinished work, and when an answer can be finalized. For every comparison, narrate what happens when endpoints are equal. Then distinguish union, selection, overlap count, and maximum active value: all use intervals, but they demand different invariants and data structures.

## Invariant
State endpoint semantics first. Sorting makes the unprocessed future obey a monotone order. Finalize a component only when the next start proves no future interval can reach it; select an interval only with an exchange argument; emit a sweep answer only after all events at the same coordinate have been applied.

## Recognize it
- Union of overlapping ranges suggests sorting by start and maintaining a current component.
- Maximum number of compatible unweighted appointments suggests earliest finish.
- Questions about simultaneous activity across coordinates suggest endpoint events and a sweep line.

## Execution trace
1. Merge sorted [[1,3],[2,6],[8,10],[10,12]]; current=[1,3]

The first component is unfinished until the next start is known.

2. Next [2,6] starts before end 3; current=[1,6]

Absorb the overlap; keep the greater endpoint.

3. Next [8,10] starts after end 6; emit [1,6], current=[8,10]

Every future start is at least 8, so no later interval can bridge the gap.

4. Next [10,12] touches end 10; current=[8,12]; emit at end

Closed endpoint semantics treat touching as overlap. Final union is [[1,6],[8,12]].

## Wrong turns
### When merging, replace the current end with the next end.
[[1,10],[2,3],[4,5]] would shrink the component to 3 and wrongly split [4,5], although all belong to [1,10].

Update end=max(end,next.end).

### Select intervals by earliest start for maximum count.
[1,10),[2,3),[3,4),[4,5): earliest start keeps one while earliest finish keeps three.

Use earliest finish with its exchange proof.

### Emit a skyline point after each individual event.
Buildings [1,2,3] and [2,3,3] may emit a spurious drop to zero and rise at x=2.

Batch all events with the same x before observing height.

### Merge Closed Intervals (Medium)

Given a non-null array of two-element int intervals [start,end] with start<=end, return their union as sorted disjoint closed intervals. Touching intervals merge. Empty input returns an empty array. Do not mutate input; invalid reversed intervals throw.

[[1,3],[2,6],[8,10],[10,12]] => [[1,6],[8,12]].

Baseline: Repeatedly find and merge any overlapping pair until stable: up to O(n³) in a straightforward implementation.

1. Clone intervals and sort by start, breaking ties by end. Use comparison functions rather than subtraction to avoid overflow.
2. Maintain one current component [start,end]. If next.start<=end, it overlaps and extends end to max(end,next.end).
3. Otherwise emit the old component: sortedness proves that no future interval can reach back across this gap. Emit the last component after the loop.

Correctness: Emitted intervals are final disjoint union components; the current component is exactly the union of the remaining processed overlapping chain.

```java
static int[][] mergeIntervals(int[][] intervals) {
    int[][] a=new int[intervals.length][2];
    for(int i=0;i<a.length;i++) {
        if(intervals[i][0]>intervals[i][1]) throw new IllegalArgumentException("reversed interval");
        a[i]=intervals[i].clone();
    }
    Arrays.sort(a,Comparator.comparingInt((int[] x)->x[0]).thenComparingInt(x->x[1]));
    List<int[]> ans=new ArrayList<>();
    for(int[] x:a) {
        if(ans.isEmpty() || x[0]>ans.get(ans.size()-1)[1]) ans.add(x.clone());
        else ans.get(ans.size()-1)[1]=Math.max(ans.get(ans.size()-1)[1],x[1]);
    }
    return ans.toArray(new int[0][]);
}
```

```kotlin
fun mergeIntervals(intervals: Array<IntArray>): Array<IntArray> {
    val a = intervals.map { require(it[0] <= it[1]); it.copyOf() }
        .sortedWith(compareBy<IntArray> { it[0] }.thenBy { it[1] })
    val ans = mutableListOf<IntArray>()
    for (x in a) {
        if (ans.isEmpty() || x[0] > ans.last()[1]) ans.add(x.copyOf())
        else ans.last()[1] = maxOf(ans.last()[1], x[1])
    }
    return ans.toTypedArray()
}
```

O(n log n) time, O(n) auxiliary copies/sorting references plus O(n) output intervals.

- [] => []
- [[1,4],[2,3]] => [[1,4]]
- [[1,1],[1,2]] => [[1,2]]

Twist: For half-open intervals, touching [1,3) and [3,5) may be treated as separate appointments even though their set union can still be represented as [1,5). Decide whether the output represents union components or conflicts before changing the comparison.

Practice source: https://leetcode.com/problems/merge-intervals/

### Insert Into Sorted Disjoint Closed Intervals (Medium)

Given non-null closed intervals sorted by start with previous.end<next.start, and one new closed interval, return the merged union after insertion. Rows have two entries and start<=end. Do not mutate any input. Empty lists are supported; invalid ordering or endpoints throw.

[[1,3],[6,9]], new=[2,5] => [[1,5],[6,9]].

Baseline: Append then sort and merge: O(n log n) time/O(n) space.

1. Copy intervals whose end<new.start; they cannot overlap the inserted component.
2. While interval.start<=growingEnd, absorb it by taking min starts and max ends. The growing end can reveal more overlaps.
3. Emit the merged new component and copy the untouched suffix. Only a linear scan is required because input is already ordered and disjoint.

Correctness: The prefix is final and strictly before the growing inserted union; every absorbed interval belongs to that union, and the remaining suffix is sorted and unprocessed.

```java
static int[][] insertInterval(int[][] a,int[] added) {
    if(added[0]>added[1]) throw new IllegalArgumentException("new interval");
    for(int j=0;j<a.length;j++) {
        if(a[j][0]>a[j][1] || (j>0 && a[j-1][1]>=a[j][0]))
            throw new IllegalArgumentException("sorted disjoint intervals required");
    }
    List<int[]> ans=new ArrayList<>(); int i=0,start=added[0],end=added[1];
    while(i<a.length && a[i][1]<start) ans.add(a[i++].clone());
    while(i<a.length && a[i][0]<=end) {
        start=Math.min(start,a[i][0]); end=Math.max(end,a[i][1]); i++;
    }
    ans.add(new int[]{start,end});
    while(i<a.length) ans.add(a[i++].clone());
    return ans.toArray(new int[0][]);
}
```

```kotlin
fun insertInterval(a: Array<IntArray>, added: IntArray): Array<IntArray> {
    require(added[0] <= added[1])
    for (j in a.indices) require(a[j][0] <= a[j][1] && (j == 0 || a[j - 1][1] < a[j][0]))
    val ans = mutableListOf<IntArray>(); var i = 0; var start = added[0]; var end = added[1]
    while (i < a.size && a[i][1] < start) ans.add(a[i++].copyOf())
    while (i < a.size && a[i][0] <= end) {
        start = minOf(start, a[i][0]); end = maxOf(end, a[i][1]); i++
    }
    ans.add(intArrayOf(start, end))
    while (i < a.size) ans.add(a[i++].copyOf())
    return ans.toTypedArray()
}
```

O(n) time including validation, O(n) output and list references; O(1) scalar working state beyond output construction.

- [],new=[2,3] => [[2,3]]
- [[1,2],[4,5]],new=[2,4] => [[1,5]]
- [[1,10]],new=[3,4] => [[1,10]]

Twist: If existing intervals may overlap, validate/sort/merge first or use a more general merge algorithm. The three-phase scan relies on the sorted-disjoint promise.

Practice source: https://leetcode.com/problems/insert-interval/

### Minimum Removals for a Nonoverlapping Schedule (Medium)

Given non-null half-open intervals [start,end) with start<end, return the fewest intervals to remove so the rest do not overlap. Touching endpoints are compatible. Input is unchanged; invalid intervals throw.

[[1,2],[2,3],[3,4],[1,3]] => 1, removing [1,3].

Baseline: Enumerate all subsets and check compatibility: exponential time, O(n) recursion state.

1. Removing the fewest is equivalent to keeping the most. Sort a copied reference array by finishing time.
2. Keep an interval if its start is at least the end of the last kept one. Earliest finish leaves maximal room for the future.
3. The exchange argument replaces the first interval of an optimum by this earliest-finishing one without invalidating any later interval; repeat on the remaining suffix.

Correctness: After each greedy selection, some optimal-cardinality schedule begins with all chosen intervals. Skipped intervals conflict with the last selected interval under the current earliest-finish ordering.

```java
static int eraseOverlap(int[][] intervals) {
    int[][] a=intervals.clone();
    for(int[] x:a) if(x[0]>=x[1]) throw new IllegalArgumentException("nonempty interval required");
    Arrays.sort(a,Comparator.comparingInt((int[] x)->x[1]).thenComparingInt(x->x[0]));
    int kept=0; long end=Long.MIN_VALUE;
    for(int[] x:a) if(x[0]>=end) { kept++; end=x[1]; }
    return a.length-kept;
}
```

```kotlin
fun eraseOverlap(intervals: Array<IntArray>): Int {
    for (x in intervals) require(x[0] < x[1])
    val a = intervals.sortedWith(compareBy<IntArray> { it[1] }.thenBy { it[0] })
    var kept = 0; var end = Long.MIN_VALUE
    for (x in a) if (x[0].toLong() >= end) { kept++; end = x[1].toLong() }
    return a.size - kept
}
```

O(n log n) time, O(n) copied references/sorting space; rows are only read and never mutated.

- [] => 0
- [[1,2],[2,3]] => 0
- [[1,4],[2,3],[3,4]] => 1

Twist: If each interval has a reward, earliest finish no longer maximizes total reward. Sort by end and use DP: best[i]=max(best[i-1],reward[i]+best[last compatible]). Binary-search compatibility for O(n log n).

Practice source: https://leetcode.com/problems/non-overlapping-intervals/

### The Skyline: Sweep a Multiset of Active Heights (Hard)

Given non-null building rows [left,right,height] with left<right and height>0, return skyline key points [x,newHeight], sorted by x, suppressing consecutive equal heights. Buildings cover [left,right); ground height is zero. Input is unchanged; invalid rows throw.

[[2,9,10],[3,7,15],[5,12,12]] => [[2,10],[3,15],[7,12],[12,0]].

Baseline: At every distinct endpoint, scan every building to find the maximum active height: O(n²) time plus sorting.

1. Convert each building into a start event (height,+1) and end event (height,-1), then sort events by x.
2. Maintain a TreeMap height→active occurrence count, seeded with ground height 0. At each x apply every event at that coordinate before looking at the maximum.
3. If the maximum differs from the previously emitted height, append one key point. Counts preserve overlapping buildings with equal heights.

Correctness: After processing all events at x, the multiset contains exactly buildings covering points immediately to the right of x. Its maximum is the skyline height until the next event coordinate.

```java
static List<List<Integer>> skyline(int[][] buildings) {
    List<int[]> events=new ArrayList<>();
    for(int[] b:buildings) {
        if(b[0]>=b[1] || b[2]<=0) throw new IllegalArgumentException("building");
        events.add(new int[]{b[0],b[2],1}); events.add(new int[]{b[1],b[2],-1});
    }
    events.sort(Comparator.comparingInt(e->e[0]));
    TreeMap<Integer,Integer> active=new TreeMap<>(); active.put(0,1);
    List<List<Integer>> ans=new ArrayList<>(); int i=0,previous=0;
    while(i<events.size()) {
        int x=events.get(i)[0];
        while(i<events.size() && events.get(i)[0]==x) {
            int[] e=events.get(i++); int count=active.getOrDefault(e[1],0)+e[2];
            if(count==0) active.remove(e[1]); else active.put(e[1],count);
        }
        int height=active.lastKey();
        if(height!=previous) { ans.add(List.of(x,height)); previous=height; }
    }
    return ans;
}
```

```kotlin
fun skyline(buildings: Array<IntArray>): List<List<Int>> {
    val events = mutableListOf<IntArray>()
    for (b in buildings) {
        require(b[0] < b[1] && b[2] > 0)
        events.add(intArrayOf(b[0], b[2], 1)); events.add(intArrayOf(b[1], b[2], -1))
    }
    events.sortBy { it[0] }
    val active = TreeMap<Int, Int>(); active[0] = 1
    val ans = mutableListOf<List<Int>>(); var i = 0; var previous = 0
    while (i < events.size) {
        val x = events[i][0]
        while (i < events.size && events[i][0] == x) {
            val e = events[i++]; val count = (active[e[1]] ?: 0) + e[2]
            if (count == 0) active.remove(e[1]) else active[e[1]] = count
        }
        val height = active.lastKey()
        if (height != previous) { ans.add(listOf(x, height)); previous = height }
    }
    return ans
}
```

O(n log n) time for event sorting and ordered multiset updates; O(n) events/active state and O(n) output.

- [] => []
- [[1,2,3],[2,3,3]] => [[1,3],[3,0]]
- [[1,4,3],[2,3,3]] => [[1,3],[4,0]]

Twist: For maximum simultaneous meetings, replace the height multiset with an active counter and track its maximum. For reporting room assignments, a heap of ending times tracks which room becomes free.

Practice source: https://leetcode.com/problems/the-skyline-problem/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="Closed-interval union finalizes only when sorted starts expose a gap."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">next start lies within current end</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">future starts cannot bridge this gap</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">closed endpoints merge when equal</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Sort: [1,3], [2,6], [8,10], [10,12]</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Absorb overlap: [1,6]</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Start 8 &gt; end 6: finalize [1,6]</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Touch 10: remaining union [8,12]</div></foreignObject></svg></div><figcaption>Closed-interval union finalizes only when sorted starts expose a gap.</figcaption></figure>

## Additional practice

### [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) (Medium)

Hint: A room can be reused exactly when its previous meeting ends.

Approach: Sort starts, use a min-heap of occupied ending times, release all ends<=next start, then add the meeting and track peak size. O(n log n), O(n); half-open semantics.

### [Minimum Number of Arrows to Burst Balloons](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) (Medium)

Hint: An arrow at the earliest finishing balloon endpoint preserves future choices.

Approach: Sort closed intervals by end. Place an arrow at the first end, then add another only when the next start is strictly greater than that coordinate. O(n log n), O(n) if preserving input.

### [Partition Labels](https://leetcode.com/problems/partition-labels/) (Medium)

Hint: An observed symbol commits you through its last occurrence.

Approach: Compute last occurrence, scan while extending the required end, and close when current index reaches it. O(n), O(alphabet). This is interval union induced by symbols.

### [My Calendar I](https://leetcode.com/problems/my-calendar-i/) (Medium)

Hint: Only the predecessor and successor can conflict in an ordered disjoint calendar.

Approach: Use a TreeMap of start→end; test predecessor.end<=newStart and successor.start>=newEnd. O(log n) per booking, O(n) state; half-open intervals.

### [Maximum Profit in Job Scheduling](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) (Hard)

Hint: Greedy count scheduling loses reward information.

Approach: Sort by finish, binary-search the latest compatible job, and compute take/skip DP with long rewards. O(n log n), O(n); reconstruct choices with parent decisions if required.

### [Employee Free Time](https://leetcode.com/problems/employee-free-time/) (Hard)

Hint: Free periods are gaps in the union of all busy intervals.

Approach: Flatten and merge busy schedules in O(N log N), then output finite gaps. A k-way heap merge exploits each employee schedule already being sorted, giving O(N log k).

## Check your reasoning

For half-open appointments, are [1,3) and [3,5) compatible?

1. Yes
2. No
3. Only if they have equal duration

Answer: 1. The first appointment excludes time 3, so the second may start there. Closed-interval merging uses a different equality rule.

Why does a skyline multiset need height frequencies?

1. The skyline is the sum of heights
2. Two overlapping buildings may have equal heights
3. All building coordinates must be unique

Answer: 2. Ending one of two height-3 buildings must leave the other height-3 building active. A plain set would remove the height too early.

## Transfer
A service receives temporary capacity grants [start,end,units]. Find the earliest time at which active capacity reaches at least a target, using half-open time intervals.

Which two events does each grant create?

What does an end and start at the same coordinate mean for the observed capacity?

Create +units at start and -units at end; sort by coordinate and sum all deltas at equal coordinates before checking the threshold. The aggregate is capacity on the following time span. Return the first coordinate meeting the target, or absence. O(n log n) time, O(n) events; use long for units totals. If target<=0 and time is unbounded, "earliest" needs an explicit domain start.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.

## Sources
- [Merge Closed Intervals](https://leetcode.com/problems/merge-intervals/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Insert Into Sorted Disjoint Closed Intervals](https://leetcode.com/problems/insert-interval/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Minimum Removals for a Nonoverlapping Schedule](https://leetcode.com/problems/non-overlapping-intervals/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [The Skyline: Sweep a Multiset of Active Heights](https://leetcode.com/problems/the-skyline-problem/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.


# Pattern 07: Linked lists: preserve the path while changing links

An array tells you where element i lives. A linked list gives you only an entry reference and one next reference at each node. The challenge is to rearrange connections without losing access to unfinished work. Dummy nodes, three-reference reversal, and unequal-speed pointers turn fragile special cases into repeatable reasoning.

## Build the model
A node is an object, and a reference is a handle to that object. Two references can identify the same node even when many nodes have equal values. Drawing only the values hides this distinction: write A(5), B(5) when identity matters. The variable head is an entry handle; moving a local cursor does not move the actual nodes. Assigning node.next changes the graph and can disconnect an entire suffix.

Start every pointer problem by drawing the edges you intend to replace. For reversal, current points into the unprocessed suffix and previous points into the already reversed prefix. Before redirecting current.next, save its old target. This temporary reference is the only remaining entry to the suffix after the edge changes. Then advance the two region boundaries. The operation is constant work per node because no node needs a random lookup.

A dummy node is an artificial predecessor whose next initially points to the real head. Removing or inserting at the beginning then uses exactly the same rule as removing or inserting later. The returned answer is dummy.next; the artificial node is never part of the result. A tail reference is a different role: it identifies where the next output node should be attached. Do not mix the two roles just because both are references.

Fast and slow pointers measure relative distance. On an acyclic chain, a pointer advancing twice per iteration reaches the end when the other is around the midpoint. Inside a cycle of length c, their relative position advances by one modulo c, so they eventually meet. Identity comparison detects that meeting; equal payloads provide no evidence. For a cycle entry, resetting one cursor to head after a meeting and moving both one step uses the equation between distance to entry and distance traveled around the cycle.

Build harder transformations by identifying a bounded segment, proving that segment exists, transforming it, and reconnecting its two boundaries. Never start modifying a group before knowing whether an incomplete trailing group must remain intact.

## Invariant
Maintain explicit reachability: every original node belongs to either the completed result or an unfinished region reachable through a saved reference. No node may silently disappear, and no unintended cycle may appear. For each operation, state which predecessor and successor remain fixed. Termination follows because the unfinished region shrinks or a cursor progresses toward an end; cycle algorithms instead use finite modular positions.

## Recognize it
- The input consists of node references and next links; changing connectivity can replace expensive copying.
- An operation treats the first node differently: a dummy predecessor can make the first position ordinary.
- A cycle or unknown midpoint asks about relative progress along one successor chain.
- Sorted chains must be combined while preserving their internal order: compare their current heads.

## Execution trace
1. previous=null; current=A→B→C

The completed prefix is empty; the entire input is still reachable.

2. saved=B; A.next=null; previous=A; current=B

Saving B before redirection preserves the suffix.

3. saved=C; B.next=A; previous=B→A; current=C

Exactly one node moves from unfinished suffix to reversed prefix.

4. previous=C→B→A; current=null

The suffix is empty, so previous is the new head.

## Wrong turns
### Overwrite next and then advance through next.
On A→B→C, setting A.next=null before saving B makes A.next lead nowhere; B and C are lost to the algorithm.

Save next=A.next before every destructive redirect, then advance using the saved reference.

### Detect a cycle by comparing node values.
A(7)→B(7)→null has repeated values but no cycle. Value equality would incorrectly return true.

Compare reference identity: Java slow==fast; Kotlin slow===fast.

### Reverse a chain (Easy)

Given a nullable head of a finite acyclic singly linked list, reverse the links in place and return the new head. Values are arbitrary Int values; node identity is preserved. Empty input returns null.

1→2→3 returns 3→2→1, using the original three nodes.

Baseline: Copy values into an array and rebuild nodes: O(n) time and O(n) extra space; rebuilding violates identity preservation.

1. First isolate the repeated work: every original edge must point in the opposite direction, but traversal still needs the old edge. Keep previous and current as the two region heads.
2. Save current.next. Redirect current.next toward previous. Move previous to current and current to the saved suffix. This order matters because mutation destroys the old route.
3. When current becomes null all original nodes are in the reversed region. Return previous; returning the original head would return only the last node.

Correctness: Before each iteration previous is the reversed processed prefix, current is the untouched suffix, and the regions contain every node exactly once.

```java
static class Node { int val; Node next; Node(int v) { val=v; } }
static Node reverse(Node head) {
 Node previous=null, current=head;
 while (current!=null) {
  Node saved=current.next; current.next=previous;
  previous=current; current=saved;
 }
 return previous;
}
```

```kotlin
class Node(val value: Int, var next: Node? = null)
fun reverse(head: Node?): Node? {
 var previous: Node? = null
 var current = head
 while (current != null) {
  val saved = current.next
  current.next = previous
  previous = current
  current = saved
 }
 return previous
}
```

O(n) time for n nodes; O(1) auxiliary space; no output allocation.

- null ⇒ null
- one node ⇒ same node
- 1→1 ⇒ reversed node identities despite equal values

Twist: Reverse only positions left through right: locate a dummy predecessor, reverse exactly that segment, then reconnect both boundaries. Validate positional bounds before mutation.

Practice source: https://leetcode.com/problems/reverse-linked-list/

### Find the entry of a cycle (Medium)

Given a nullable head whose next references form either a terminating chain or a chain entering a cycle, return the entry node of that cycle by identity; otherwise return null. Do not mutate links.

A→B→C→D→B returns B. A→B→null returns null.

Baseline: Store every visited node identity in a hash set: O(n) time and O(n) space.

1. Move slow one edge and fast two edges while fast and fast.next exist. An end proves no cycle. In a cycle, the relative offset changes by one, guaranteeing a meeting.
2. Let a be the distance to entry and b the distance from entry to meeting. At meeting slow traveled a+b; fast traveled twice that, so a+b is a multiple of the cycle length, allowing extra completed laps.
3. Reset a cursor to head. Advance it and slow one edge together. After a moves, the first reaches the entry and the second advances from offset b to offset b+a≡0. The first identity match is the entry.

Correctness: Before detection both pointers follow original edges. After a meeting, equally advancing the reset cursor and meeting cursor preserves the modular equation leading to the entry.

```java
static class Node { int val; Node next; Node(int v) { val=v; } }
static Node cycleEntry(Node head) {
 Node slow=head, fast=head;
 while (fast!=null && fast.next!=null) {
  slow=slow.next; fast=fast.next.next;
  if (slow==fast) {
   Node p=head;
   while (p!=slow) { p=p.next; slow=slow.next; }
   return p;
  }
 }
 return null;
}
```

```kotlin
class Node(val value: Int, var next: Node? = null)
fun cycleEntry(head: Node?): Node? {
 var slow = head
 var fast = head
 while (fast != null && fast.next != null) {
  slow = slow?.next
  fast = fast.next?.next
  if (slow === fast) {
   var p = head
   while (p !== slow) { p = p?.next; slow = slow?.next }
   return p
  }
 }
 return null
}
```

O(n) time and O(1) extra space, where n counts distinct reachable nodes. Detection and entry search each take at most a constant multiple of n steps.

- null ⇒ null
- single self-loop ⇒ that node
- two equal values without cycle ⇒ null

Twist: If asked for the midpoint of an acyclic list, reuse unequal speeds but define which middle is required for even length; initializing fast=head returns the second middle.

Practice source: https://leetcode.com/problems/linked-list-cycle-ii/

### Merge two sorted chains (Easy to Medium)

Given two nullable, disjoint, acyclic lists sorted in nondecreasing order, splice their existing nodes into one sorted chain. Return its head. This mutates links; shared input nodes are outside the contract. Equal values from the first list should come first.

1→3 and 1→2 produce 1(first)→1(second)→2→3.

Baseline: Collect nodes, sort by value, and relink: O((m+n) log(m+n)) time and O(m+n) space.

1. Sorted order means the smallest remaining value must be at one of the two current heads. Compare only those heads; no scan into their suffixes is necessary.
2. Use a dummy predecessor and a tail. Attach the smaller head, advance that input, then advance tail. Choosing the first input on equality preserves the requested stable tie order.
3. When one input ends, attach the other suffix as a whole. It is already sorted, and all its values are at least the last chosen value. Return dummy.next.

Correctness: The chain through tail is sorted and contains exactly the consumed nodes; each unconsumed input suffix is still sorted and reachable.

```java
static class Node { int val; Node next; Node(int v) { val=v; } }
static Node merge(Node a, Node b) {
 Node dummy=new Node(0), tail=dummy;
 while (a!=null && b!=null) {
  if (a.val<=b.val) { tail.next=a; a=a.next; }
  else { tail.next=b; b=b.next; }
  tail=tail.next;
 }
 tail.next=(a!=null?a:b);
 return dummy.next;
}
```

```kotlin
class Node(val value: Int, var next: Node? = null)
fun merge(first: Node?, second: Node?): Node? {
 val dummy = Node(0)
 var tail = dummy
 var a = first
 var b = second
 while (a != null && b != null) {
  if (a.value <= b.value) { tail.next = a; a = a.next }
  else { tail.next = b; b = b.next }
  tail = tail.next!!
 }
 tail.next = a ?: b
 return dummy.next
}
```

O(m+n) time for input lengths m,n; O(1) auxiliary space including one dummy node. Output reuses input nodes.

- both null ⇒ null
- one null ⇒ the other head
- all equal ⇒ first-list nodes before second-list nodes

Twist: For k sorted lists, the choice has k candidates instead of two; use a min-heap of current heads for O(N log k) time or pairwise merge rounds.

Practice source: https://leetcode.com/problems/merge-two-sorted-lists/

### Reverse complete groups of k (Hard)

Given a finite acyclic list and k≥1, reverse each complete consecutive group of k nodes in place. Leave the final group unchanged if it has fewer than k nodes. Return the resulting head. Throw for k<1.

1→2→3→4→5 with k=2 returns 2→1→4→3→5.

Baseline: Store all node references, reorder groups, relink: O(n) time, O(n) auxiliary space.

1. Place a dummy before head and maintain before, the predecessor of the next group. Walk k edges from before to find the group end before changing anything. If that node is absent, preserve the suffix and finish.
2. Save after=end.next. Reverse the segment with previous initialized to after so that the original first node automatically reconnects to the untouched suffix. Stop when current reaches after by identity.
3. Connect before.next to the old end. The old first node is now the group tail, so make it the next before. Each completed step advances by k nodes, and no incomplete group is mutated.

Correctness: Everything before before is finalized. The suffix beginning at before.next retains its original internal order until a full group is confirmed; segment reversal preserves the after boundary.

```java
static class Node { int val; Node next; Node(int v) { val=v; } }
static Node reverseGroups(Node head, int k) {
 if (k<1) throw new IllegalArgumentException("k must be positive");
 Node dummy=new Node(0); dummy.next=head; Node before=dummy;
 while (true) {
  Node end=before;
  for (int i=0;i<k && end!=null;i++) end=end.next;
  if (end==null) return dummy.next;
  Node after=end.next, current=before.next, previous=after;
  while (current!=after) {
   Node saved=current.next; current.next=previous;
   previous=current; current=saved;
  }
  Node oldFirst=before.next; before.next=end; before=oldFirst;
 }
}
```

```kotlin
class Node(val value: Int, var next: Node? = null)
fun reverseGroups(head: Node?, k: Int): Node? {
 require(k >= 1)
 val dummy = Node(0, head)
 var before = dummy
 while (true) {
  var end: Node? = before
  var count = 0
  while (count < k && end != null) { end = end?.next; count++ }
  val groupEnd = end ?: return dummy.next
  val after = groupEnd.next
  var current = before.next
  var previous = after
  while (current !== after) {
   val node = current!!
   val saved = node.next
   node.next = previous
   previous = node
   current = saved
  }
  val oldFirst = before.next!!
  before.next = groupEnd
  before = oldFirst
 }
}
```

O(n) time for n nodes: each complete group is scanned then reversed, and the final probe stops at null. O(1) auxiliary space.

- null with k=1 ⇒ null
- 1→2 with k=3 ⇒ unchanged
- k=1 ⇒ unchanged identities

Twist: Reverse alternating complete groups: keep an additional boolean phase; both phases must use the same completeness check and boundary advancement.

Practice source: https://leetcode.com/problems/reverse-nodes-in-k-group/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="One reversal step preserves an entry to both regions before changing the edge."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">save old next</text><path d="M365,72 Q195,15 25,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="20" text-anchor="middle" font-size="12">redirect next</text><path d="M745,72 Q575,15 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="20" text-anchor="middle" font-size="12">advance current to saved</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">previous: 2→1→null</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">current: 3</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">saved: 4→5→null</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">after: 3→2→1→null</div></foreignObject></svg></div><figcaption>One reversal step preserves an entry to both regions before changing the edge.</figcaption></figure>

## Additional practice

### [Remove Nth Node From End](https://leetcode.com/problems/remove-nth-node-from-end-of-list/) (Medium)

Hint: Keep a gap of n between two cursors starting around a dummy.

Approach: Advance fast n steps from head, then move fast and a dummy predecessor together; unlink predecessor.next. O(n) time/O(1) space; reject n outside 1..length.

### [Palindrome Linked List](https://leetcode.com/problems/palindrome-linked-list/) (Easy)

Hint: Compare the first half with a reversed second half.

Approach: Find midpoint, reverse second half, compare, and restore links before returning. O(n) time/O(1) extra; odd middle is ignored.

### [Reorder List](https://leetcode.com/problems/reorder-list/) (Medium)

Hint: Split, reverse, weave.

Approach: Find first-half tail, detach second half, reverse it, then alternate nodes while saving both next references. O(n) time/O(1) space; detachment avoids a cycle.

### [Intersection of Two Linked Lists](https://leetcode.com/problems/intersection-of-two-linked-lists/) (Easy)

Hint: Equalize total distance without comparing values.

Approach: For acyclic lists, switch each cursor to the other head at null; both travel m+n edges before meeting or both reaching null. O(m+n)/O(1); cycles require a different contract.

### [Copy List with Random Pointer](https://leetcode.com/problems/copy-list-with-random-pointer/) (Medium)

Hint: A node identity map separates allocation from wiring.

Approach: Allocate one clone per node, then assign next and random via the map; O(n) time/space. Interleaving clones can reduce auxiliary space but temporarily mutates the source.

### [Sort List](https://leetcode.com/problems/sort-list/) (Medium)

Hint: Merge is the combining operation; midpoint supplies division.

Approach: Split with slow/fast, recursively sort each half, then merge. O(n log n) time/O(log n) stack; bottom-up merge sort yields O(1) auxiliary space.

## Check your reasoning

Why initialize previous to after when reversing a bounded group?

1. It connects the new tail directly to the untouched suffix.
2. It sorts the group.
3. It avoids needing to find the group end.

Answer: 1. The original first node receives next=after during the first reversal step; completeness still must be checked.

Two cursors hold nodes with value 9. What proves they met?

1. Equal values
2. Reference identity
3. Equal distance traveled

Answer: 2. Different nodes may carry the same value. The cycle proof requires occupying the same object.

## Transfer
A conveyor log is a chain of event objects. Reverse each consecutive block whose size is specified by the next entry in a size array; leave an incomplete final block untouched. Preserve every event identity. Derive an in-place algorithm.

A block size changes how far you probe, not the reversal primitive.

Keep a predecessor and validate the entire block before changing edges.

Use a dummy and a boundary predecessor. For each positive block size, probe that many nodes; if absent, stop. Save the successor, reverse until that successor, reconnect the predecessor, advance to the old first node. Across processed blocks, each node is scanned at most twice: O(n+s) time for n reachable nodes and s sizes examined, O(1) auxiliary space. Reject nonpositive sizes before mutating if all-or-nothing validation is required.

## Recall
- Draw reversal with named node identities and identify the reference that would be lost.
- Prove the cycle-entry reset using distances a,b,c.
- Implement group reversal without looking at code; state both boundaries first.

## Sources
- [LeetCode linked-list problem contracts](https://leetcode.com/tag/linked-list/): Reference link; not retrieved in this run. Practice taxonomy and public problem contracts; no company frequency inference.
- [Cycle entry problem](https://leetcode.com/problems/linked-list-cycle-ii/): Reference link; not retrieved in this run. Canonical identity-based cycle-entry task.


# Pattern 08: Stacks, monotonic frontiers, and deques

A stack remembers unfinished obligations in the order they must be resolved. A queue remembers arrival order. Monotonic stacks and deques go further: they delete candidates that can never win again, avoiding repeated scans into the past.

## Build the model
Imagine a stack of plates: you can add or remove only the top. This restriction is useful when the most recent unfinished task must be completed first. In bracket matching, an opening bracket creates an obligation; a closing bracket must discharge the latest obligation of the matching kind. Merely counting openings loses nesting information. A queue instead removes the earliest arrival. A deque permits insertion and removal at both ends, combining chronological expiration at the front with candidate elimination at the back.

A monotonic stack is not a magical data structure. It is an ordinary stack plus a rule that its values stay ordered. Derive that rule from the question. In next-greater-element problems, keep indices whose answers remain unknown and whose values are nonincreasing from bottom to top. When a larger value arrives, it is the first larger value for every smaller top that it pops: earlier positions between that top and the new position already failed to exceed it. Store indices because distances, duplicate values, and window expiration all depend on position.

The harder leap is dominance. Suppose a later window element is at least as large as an earlier element. The later element is better for a maximum, and it expires later. The earlier candidate can never become useful again, so removing it is safe. This argument needs both value quality and lifetime; value order alone is insufficient. For a minimum window, reverse the comparison. For the nearest strictly greater position, preserve equal unresolved values because equality does not answer the question.

Histogram rectangles use the same frontier for a different meaning. A bar remains pending until a smaller bar proves its rectangle cannot extend farther right. After popping a bar, the new top marks the last smaller boundary on the left. The width is rightBoundary-leftBoundary-1, not a distance between arbitrary neighboring bars. A virtual zero-height bar flushes pending positive bars at the end without changing the input.

Nested while loops do not imply quadratic time. Charge each pop to the unique earlier push of the same index. Across the whole scan, each index enters once and leaves at most once. This accounting is the amortized proof behind linear time.

## Invariant
The container stores exactly the unresolved obligations or nondominated candidates needed for future answers. Its order is stated explicitly: chronological indices plus either nonincreasing or nondecreasing values. A deletion must have a proof: resolved forever, expired forever, or dominated forever. Since indices enter once and leave at most once, total container work is O(n).

## Recognize it
- Nested opening and closing operations require resolving the most recently opened obligation first.
- For each position, find the first greater or smaller position on one side: unresolved indices can form an ordered stack.
- An element becomes irrelevant forever when a newer, better element arrives: maintain a monotonic frontier.
- A moving window needs an extreme value and expiration by index: a deque supports removing both dominated candidates and expired candidates.

## Execution trace
1. a=[4,2,5,3], k=3; i=0; deque=[0:4]

First value is the only candidate.

2. i=1; deque=[0:4,1:2]

The smaller later value might outlive 4, so keep it.

3. i=2; deque=[2:5]; output=[5]

5 removes both smaller older candidates and is the maximum of indices 0..2.

4. i=3; deque=[2:5,3:3]; output=[5,5]

5 remains active in indices 1..3; 3 may matter after it expires.

## Wrong turns
### Match brackets using three independent counters.
([)] has balanced counts for parentheses and square brackets but invalid nesting. The first closing parenthesis conflicts with the latest open square bracket.

Keep opening kinds in a stack and compare each close with the top.

### Keep only values for a sliding maximum.
For [5,5,1] with window size 2, the first 5 expires while the second remains. A value alone cannot identify which 5 expired.

Store indices; remove front indices outside the active window, then read their values.

### Validate nested brackets (Easy)

Given a non-null string containing only ()[]{} characters, return whether all brackets are correctly nested and closed. Empty string is valid. Any other character returns false. Input is unchanged.

([]{}) returns true; ([)] returns false.

Baseline: Repeatedly delete adjacent matched pairs until stable: O(n²) time with repeated string copying and O(n) space.

1. An opening bracket creates a required future closing bracket. Push that required closer, which makes a comparison at closing time direct.
2. For a closing bracket, the stack must be nonempty and its top must equal this character. Popping any deeper item would cross nesting boundaries.
3. After scanning, accept only an empty stack. A nonempty stack represents openings never closed, even when no mismatched close appeared.

Correctness: The stack from bottom to top contains exactly the unmatched required closers, with the latest obligation on top.

```java
static boolean validBrackets(String s) {
 Objects.requireNonNull(s);
 Deque<Character> stack=new ArrayDeque<>();
 for (int i=0;i<s.length();i++) {
  char c=s.charAt(i);
  if(c=='(') stack.push(')');
  else if(c=='[') stack.push(']');
  else if(c=='{') stack.push('}');
  else if(c==')'||c==']'||c=='}') {
   if(stack.isEmpty()||stack.pop()!=c) return false;
  } else return false;
 }
 return stack.isEmpty();
}
```

```kotlin
fun validBrackets(s: String): Boolean {
 val stack = ArrayDeque<Char>()
 for (c in s) {
  when (c) {
   '(' -> stack.push(')')
   '[' -> stack.push(']')
   '{' -> stack.push('}')
   ')', ']', '}' -> if (stack.isEmpty() || stack.pop() != c) return false
   else -> return false
  }
 }
 return stack.isEmpty()
}
```

O(n) time, O(n) auxiliary space in the all-open case; n is the number of characters.

- empty ⇒ true
- ] ⇒ false
- (( ⇒ false

Twist: If ordinary text is allowed, skip it explicitly; do not quietly change the validation contract. For wildcard brackets the state may branch, so one deterministic stack is no longer automatically sufficient.

Practice source: https://leetcode.com/problems/valid-parentheses/

### Wait for a warmer temperature (Medium)

Given a non-null Int array of daily temperatures, return an equally sized array where answer[i] is days until the first strictly warmer day, or 0 if none exists. Arbitrary Int values are supported; do not mutate input.

[73,71,72,75] returns [3,1,1,0].

Baseline: For every day scan future days: O(n²) time and O(n) output space.

1. A day cannot be answered until a strictly warmer day appears. Store unanswered indices on a stack, maintaining nonincreasing temperatures.
2. At day i, repeatedly pop while temperature[i] is strictly greater than the top temperature. The current day is the first qualifying day because every earlier scanned day failed for that pending index.
3. Assign i-index for each pop, then push i. Leave unpopped positions as zero. Equality must not pop because the request says strictly warmer.

Correctness: Stack indices are increasing and their temperatures are nonincreasing; all have no warmer day among the positions already scanned.

```java
static int[] warmer(int[] t) {
 int[] answer=new int[t.length]; Deque<Integer> stack=new ArrayDeque<>();
 for(int i=0;i<t.length;i++) {
  while(!stack.isEmpty() && t[i]>t[stack.peek()]) {
   int j=stack.pop(); answer[j]=i-j;
  }
  stack.push(i);
 }
 return answer;
}
```

```kotlin
fun warmer(t: IntArray): IntArray {
 val answer = IntArray(t.size)
 val stack = ArrayDeque<Int>()
 for (i in t.indices) {
  while (stack.isNotEmpty() && t[i] > t[stack.peek()]) {
   val j = stack.pop()
   answer[j] = i - j
  }
  stack.push(i)
 }
 return answer
}
```

O(n) time because every index is pushed once and popped at most once; O(n) auxiliary stack and O(n) output.

- [] ⇒ []
- [2,2] ⇒ [0,0]
- [3,2,1] ⇒ [0,0,0]

Twist: For circular next-greater values, scan 2n logical positions and push original indices only on the first pass. Return values rather than distances if the contract changes.

Practice source: https://leetcode.com/problems/daily-temperatures/

### Largest rectangle under a histogram (Hard)

Given a non-null array of nonnegative Int bar heights, each with width 1, return the maximum area as Long. Empty input returns 0. Reject negative heights. Do not mutate input.

[2,1,5,6,2,3] returns 10 from the height-5 rectangle spanning indices 2..3.

Baseline: For every interval maintain its minimum height: O(n²) time, O(1) auxiliary space.

1. Every optimal rectangle can be expanded until blocked by a smaller bar, so choose a bar as the limiting height and find its maximal legal span. Keep indices of nondecreasing heights.
2. When a shorter bar arrives at i, pop a taller index j. The current index is the first smaller boundary on its right; after popping, the new stack top is a usable left boundary. Compute height[j] × (i-left-1).
3. Equal bars can remain. Their individual widths may differ, but the earliest equal bar eventually obtains the full span. At i=n, use virtual height zero to resolve all positive bars; zero bars cannot improve a positive area.

Correctness: Pending indices have nondecreasing heights; a popped positive bar has just discovered its right boundary, and its candidate span contains no smaller bar.

```java
static long largestRectangle(int[] h) {
 for(int value:h) if(value<0) throw new IllegalArgumentException("negative height");
 Deque<Integer> stack=new ArrayDeque<>(); long best=0;
 for(int i=0;i<=h.length;i++) {
  int current=i==h.length?0:h[i];
  while(!stack.isEmpty() && h[stack.peek()]>current) {
   int j=stack.pop(); int left=stack.isEmpty()?-1:stack.peek();
   best=Math.max(best,(long)h[j]*(i-left-1));
  }
  if(i<h.length) stack.push(i);
 }
 return best;
}
```

```kotlin
fun largestRectangle(h: IntArray): Long {
 require(h.all { it >= 0 })
 val stack = ArrayDeque<Int>()
 var best = 0L
 for (i in 0..h.size) {
  val current = if (i == h.size) 0 else h[i]
  while (stack.isNotEmpty() && h[stack.peek()] > current) {
   val j = stack.pop()
   val left = if (stack.isEmpty()) -1 else stack.peek()
   best = maxOf(best, h[j].toLong() * (i - left - 1))
  }
  if (i < h.size) stack.push(i)
 }
 return best
}
```

O(n) time and O(n) auxiliary space. Convert height to Long before multiplication; Int height times an Int-sized array length fits signed Long.

- [] ⇒ 0
- [2,2] ⇒ 4
- [0,0] ⇒ 0
- [2147483647,2147483647] ⇒ 4294967294

Twist: For a binary matrix, accumulate consecutive-one heights row by row and solve this histogram at each row: O(rows×columns) time, O(columns) space.

Practice source: https://leetcode.com/problems/largest-rectangle-in-histogram/

### Maximum in every sliding window (Hard)

Given non-null Int array a and window width k with 1≤k≤a.length, return maxima of each consecutive width-k window. Reject invalid k, including empty input with any k. Input is unchanged.

[1,3,-1,-3,5,3,6,7], k=3 returns [3,3,5,5,6,7].

Baseline: Scan all k entries of every window: O(nk) time, O(n-k+1) output space.

1. An index is useful only while it remains in the current window. Remove front indices ≤i-k before producing the answer. Chronological index order makes expiration local to the front.
2. Remove back indices whose values are ≤a[i]. The current index is at least as large and expires later, so those candidates can never again be the maximum. Push i at the back.
3. After i reaches k-1, the front is the current maximum: every removed active candidate was dominated, and remaining candidates are ordered by decreasing value. Emit one result per complete window.

Correctness: Deque indices increase from front to back, their values strictly decrease, and all indices are in the current window. Each excluded active index has a newer candidate at least as large.

```java
static int[] windowMax(int[] a,int k) {
 if(k<1||k>a.length) throw new IllegalArgumentException("invalid window");
 int[] out=new int[a.length-k+1]; Deque<Integer> q=new ArrayDeque<>();
 for(int i=0;i<a.length;i++) {
  while(!q.isEmpty() && q.peekFirst()<=i-k) q.removeFirst();
  while(!q.isEmpty() && a[q.peekLast()]<=a[i]) q.removeLast();
  q.addLast(i);
  if(i>=k-1) out[i-k+1]=a[q.peekFirst()];
 }
 return out;
}
```

```kotlin
fun windowMax(a: IntArray, k: Int): IntArray {
 require(k >= 1 && k <= a.size)
 val out = IntArray(a.size - k + 1)
 val q = ArrayDeque<Int>()
 for (i in a.indices) {
  while (q.isNotEmpty() && q.peekFirst() <= i - k) q.removeFirst()
  while (q.isNotEmpty() && a[q.peekLast()] <= a[i]) q.removeLast()
  q.addLast(i)
  if (i >= k - 1) out[i - k + 1] = a[q.peekFirst()]
 }
 return out
}
```

O(n) time, O(k) auxiliary deque space, and O(n-k+1) output space. Each index enters once and leaves once.

- [4],k=1 ⇒ [4]
- [5,5,1],k=2 ⇒ [5,5]
- [1,2],k=3 ⇒ rejected

Twist: When the condition bounds max-min rather than fixed width, maintain both decreasing and increasing deques and move the left boundary until the difference is legal; subtract using Long.

Practice source: https://leetcode.com/problems/sliding-window-maximum/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="For a maximum window, later larger values dominate earlier smaller ones."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M555,72 Q385,15 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="20" text-anchor="middle" font-size="12">5 dominates 2</text><path d="M555,72 Q290,15 25,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="290" y="20" text-anchor="middle" font-size="12">5 dominates 4</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">push newest candidate</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">index 0: value 4</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">index 1: value 2</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">index 2: value 5</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">deque after index 2: [2]</div></foreignObject></svg></div><figcaption>For a maximum window, later larger values dominate earlier smaller ones.</figcaption></figure>

## Additional practice

### [Min Stack](https://leetcode.com/problems/min-stack/) (Medium)

Hint: Store the minimum alongside each pushed value.

Approach: Each frame stores value and prefix minimum; pop restores the previous minimum automatically. O(1) each operation/O(n) space; handle duplicate minima.

### [Evaluate Reverse Polish Notation](https://leetcode.com/problems/evaluate-reverse-polish-notation/) (Medium)

Hint: An operator resolves the latest two values.

Approach: Pop right then left, compute left operator right, push result. O(n) time/O(n) space; operand order matters for subtraction and division.

### [Next Greater Element II](https://leetcode.com/problems/next-greater-element-ii/) (Medium)

Hint: Circularity changes traversal count, not dominance.

Approach: Scan twice modulo n; resolve pending indices with strict greater comparison, push once. O(n) time/O(n) space; guard empty input before modulo.

### [Sum of Subarray Minimums](https://leetcode.com/problems/sum-of-subarray-minimums/) (Medium)

Hint: Count intervals for which each index is the selected minimum.

Approach: Find previous strictly smaller and next smaller-or-equal boundaries, then multiply left choices by right choices by value. O(n)/O(n); asymmetric ties prevent double counting; use required modulus.

### [Remove K Digits](https://leetcode.com/problems/remove-k-digits/) (Medium)

Hint: A smaller incoming digit can improve the earliest undecided position.

Approach: Pop larger prior digits while deletions remain, append current digit, delete remaining suffix, strip leading zeros. O(n)/O(n); return 0 for empty result.

### [Longest Continuous Subarray with Absolute Diff ≤ Limit](https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/) (Medium)

Hint: Two deques summarize window extremes.

Approach: Extend right, maintain max/min deques, shrink left while max-min exceeds limit. O(n)/O(n); cast to Long before subtraction.

## Check your reasoning

Why is the histogram algorithm linear despite an inner while?

1. The stack has constant size.
2. Each bar is pushed once and popped at most once.
3. The bars must be sorted.

Answer: 2. The accounting is global across all iterations; a long pop sequence consumes indices that cannot be popped again.

For strictly warmer days, should equal temperatures resolve each other?

1. Yes, always.
2. Only for negative temperatures.
3. No.

Answer: 3. Equality does not satisfy strictly greater, so both days remain pending until a larger value arrives.

## Transfer
A monitoring service receives timestamped scores in increasing timestamp order. After each event, report the maximum score among events no more than 60 seconds old. Several events can share a timestamp. Design the state.

Expiration uses timestamps rather than a fixed count.

A newer score at least as large can replace an older score.

Keep a deque of (timestamp,sequence,score). Remove front records with timestamp<now-60, remove back records with score≤newScore, then append the event. Front is the maximum. Each event enters and leaves at most once: amortized O(1) processing and O(w) space for events in the active time span. Specify inclusive age and require nondecreasing input time; out-of-order arrivals break the expiration and dominance proof.

## Recall
- Derive the comparison sign for next greater, previous smaller, and window minimum.
- Explain why both index age and value matter for dominance.
- Draw histogram left and right boundaries and derive the minus one.

## Sources
- [LeetCode stack taxonomy](https://leetcode.com/tag/stack/): Reference link; not retrieved in this run. Primary practice catalogue and task statements.
- [Sliding window maximum](https://leetcode.com/problems/sliding-window-maximum/): Reference link; not retrieved in this run. Canonical fixed-window extrema contract; no frequency claim.


# Pattern 09: Trees: decide what crosses each parent-child boundary

A tree breaks a large problem into smaller trees. The central design choice is what information a parent sends down and what summary each child returns. Traversal order follows that dependency, turning many apparently different questions into a few reusable proof shapes.

## Build the model
A binary tree node stores a value and references to at most two children. A proper tree has no cycles and no shared child object; otherwise recursive tree algorithms may revisit nodes or fail to terminate. An empty child is itself a meaningful base case. Ask what answer an empty subtree should contribute before writing recursion: depth zero, no target found, or a gain of zero when choosing to omit a branch. Different problems need different identities.

There are three useful moments at a node. Preorder acts before children, which fits passing inherited state such as a legal numeric interval. Inorder acts between left and right children; in a valid binary search tree it produces sorted values. Postorder acts after children and fits synthesizing a result from their summaries. These are schedules, not independent tricks to memorize. If a parent requires child depths, any implementation must obtain those depths first, whether using recursion or an explicit stack.

Think of each recursive call as a tiny module with a contract. State the meaning of its parameters and the meaning of its return. A search-tree validator can receive open bounds (low,high); a node and every descendant must fit all ancestor decisions, not only the immediate parent. A lowest-common-ancestor helper can return which targets were found and the best common ancestor if both were found. This richer return prevents an absent target from being mistaken for a valid result.

For path problems, distinguish what is legal inside a subtree from what can cross its connection to the parent. A complete path may join a left branch, the current node, and a right branch. A value returned upward cannot contain both branches, because the parent would create a fork rather than a simple path. Return the best one-branch gain; separately update the best complete path. Negative gains can be excluded, but the current node itself cannot be excluded when the path must be nonempty.

A breadth-first queue offers another dependency schedule. Snapshot the queue size at the start of a level so newly enqueued children are processed at the next level. Without that snapshot, the boundary between depths disappears. Recursion uses O(h) call stack for height h, which can equal n for a chain. JVM stack depth is finite; use explicit frames for deeply skewed or adversarial trees.

## Invariant
Each helper processes exactly its subtree, receives a precise inherited context, and returns a summary sufficient for its parent. Structural induction proves correctness: establish the empty case, assume child contracts, then show the parent composition preserves the contract. For BFS, the queue at a level boundary contains exactly the next depth in left-to-right order.

## Recognize it
- Every node has a unique route from the root: inherited constraints or prefix state can follow that route.
- A parent answer depends on its children: compute children first with postorder.
- Answers are organized by distance from a root: process a FIFO queue in complete levels.
- A path can turn at one node but cannot branch into three directions: separate a global completed answer from a one-branch return value.

## Execution trace
1. Tree: -10 with left 9 and right 20; 20 has children 15,7. Leaf 9 returns 9; best=9.

A leaf has no optional gains; its nonempty path is its own value.

2. Leaf 15 returns 15; best=15. Leaf 7 returns 7; best=15.

Independent child summaries are ready before their parent.

3. At 20: completed=15+20+7=42; upward=20+15=35; best=42.

The full two-branch path is legal locally but only one branch may extend upward.

4. At -10: completed=9-10+35=34; upward=25; final best=42.

A globally best path need not pass through the root.

## Wrong turns
### Validate a BST using only node.left.val < node.val < node.right.val.
Root 10 has right child 15, and 15 has left child 6. Every local comparison passes, but 6 lies in the right subtree of 10 and violates its ancestor bound.

Propagate the entire legal interval from all ancestors, or verify strictly increasing inorder values.

### Return a path containing both child gains to the parent.
At node 2 with children 3 and 4, returning 3+2+4=9 to parent 1 creates three incident path edges at node 2 after adding 1. That object is a fork, not a simple path.

Use both children only for a completed candidate; return node plus at most one child gain.

### Maximum depth by complete levels (Easy)

Given a nullable root of a proper finite binary tree, return the maximum number of nodes along a root-to-leaf path. Empty tree has depth 0. Values are arbitrary Int values and are ignored; do not mutate the tree.

A root with one child has depth 2.

Baseline: Run a root-to-leaf search separately for every leaf: potentially O(nh) repeated work and O(h) stack.

1. Depth is the count of edges from root plus one. A queue groups nodes by that count, so counting complete queue levels directly yields depth.
2. At the start of each iteration snapshot size. Remove exactly size nodes, enqueuing their non-null children. Newly appended children belong to the next depth and must not increase this iteration count.
3. When the queue empties every node has been processed once. The number of completed levels is the longest root-to-leaf node count.

Correctness: Before a level iteration the queue contains exactly all nodes at that depth; each child is enqueued exactly once. Both assumptions rely on a proper tree.

```java
static class Node { int val; Node left,right; Node(int v) { val=v; } }
static int maxDepth(Node root) {
 if(root==null) return 0;
 Deque<Node> q=new ArrayDeque<>(); q.add(root); int depth=0;
 while(!q.isEmpty()) {
  int size=q.size(); depth++;
  for(int i=0;i<size;i++) {
   Node n=q.remove();
   if(n.left!=null) q.add(n.left);
   if(n.right!=null) q.add(n.right);
  }
 }
 return depth;
}
```

```kotlin
class Node(val value: Int, var left: Node? = null, var right: Node? = null)
fun maxDepth(root: Node?): Int {
 if (root == null) return 0
 val q = ArrayDeque<Node>()
 q.add(root)
 var depth = 0
 while (q.isNotEmpty()) {
  val size = q.size
  depth++
  repeat(size) {
   val n = q.remove()
   n.left?.let { q.add(it) }
   n.right?.let { q.add(it) }
  }
 }
 return depth
}
```

O(n) time and O(w) queue space where w is maximum tree width; worst-case O(n). No recursive stack.

- null ⇒ 0
- single node ⇒ 1
- chain of four nodes ⇒ 4

Twist: To return level-order values, collect each snapshot into a list. Zigzag order changes the output positions, not the actual BFS visitation proof.

Practice source: https://leetcode.com/problems/maximum-depth-of-binary-tree/

### Validate every ancestor constraint in a BST (Medium)

Given a nullable proper binary tree with Int node values, return whether every left descendant is strictly smaller and every right descendant strictly larger than each ancestor. Duplicate values are invalid. Empty tree is valid; do not mutate.

10 with right child 15 containing left child 6 returns false.

Baseline: For each node rescan all descendants to check side constraints: O(n²) worst-case time, O(h) recursion space.

1. The root can take any Int value. Represent its legal interval using Long.MIN_VALUE and Long.MAX_VALUE so Int extremes are not accidentally excluded.
2. At a node, check low<value<high. Its left subtree inherits (low,value), and its right subtree inherits (value,high). Ancestor bounds survive every recursive step.
3. Return true for null. Reject immediately on any bound violation, otherwise require both child results. The interval encodes the full search-tree definition, so passing local checks is sufficient.

Correctness: The open interval passed to a subtree is exactly the intersection of ordering requirements created by all its ancestors.

```java
static class Node { int val; Node left,right; Node(int v) { val=v; } }
static boolean isBST(Node root) {
 return within(root,Long.MIN_VALUE,Long.MAX_VALUE);
}
static boolean within(Node n,long low,long high) {
 if(n==null) return true;
 if(n.val<=low || n.val>=high) return false;
 return within(n.left,low,n.val) && within(n.right,n.val,high);
}
```

```kotlin
class Node(val value: Int, var left: Node? = null, var right: Node? = null)
fun isBST(root: Node?): Boolean {
 fun within(n: Node?, low: Long, high: Long): Boolean {
  if (n == null) return true
  val v = n.value.toLong()
  if (v <= low || v >= high) return false
  return within(n.left, low, v) && within(n.right, v, high)
 }
 return within(root, Long.MIN_VALUE, Long.MAX_VALUE)
}
```

O(n) time, O(h) recursive stack; h can be n. Bounds use Long; no arithmetic on bounds is needed.

- null ⇒ true
- root Int.MIN_VALUE ⇒ true
- root 2 with left child 2 ⇒ false

Twist: If duplicates are permitted on only one side, bounds need inclusive/exclusive metadata; simply replacing both comparisons by non-strict ones violates the one-sided policy.

Practice source: https://leetcode.com/problems/validate-binary-search-tree/

### Lowest common ancestor with missing targets (Medium)

Given a nullable proper binary tree and two non-null target node references p,q, return their deepest common ancestor only if both identities occur in the tree; otherwise return null. p may equal q. Values may repeat. Do not mutate.

Root A has children B,C: LCA(B,C)=A; LCA(B,B)=B; a detached target yields null.

Baseline: Find both root-to-target paths then compare their prefixes: O(n) time, O(h) stored paths plus search stack. This is valid but stores whole routes.

1. A subtree must report target presence, not merely a possible ancestor. Encode p found as bit 1 and q found as bit 2; a node equal to both sets both bits.
2. Combine child masks with identity matches at the current node. If a child already contains both, its recorded ancestor is deeper and remains the answer. Otherwise the current node is the first place that can unite both presences.
3. At the root return an ancestor only for mask 3. This final check distinguishes a lone present target from a valid ancestor, unlike the shortcut that assumes both targets exist.

Correctness: Each helper returns exactly the presence mask for its subtree and, for mask 3, the deepest node whose subtree contains both target identities.

```java
static class Node { int val; Node left,right; Node(int v) { val=v; } }
static class LcaInfo {
 int mask; Node ancestor;
 LcaInfo(int m,Node a) { mask=m; ancestor=a; }
}
static Node lca(Node root,Node p,Node q) {
 Objects.requireNonNull(p); Objects.requireNonNull(q);
 LcaInfo result=lcaInfo(root,p,q);
 return result.mask==3?result.ancestor:null;
}
static LcaInfo lcaInfo(Node n,Node p,Node q) {
 if(n==null) return new LcaInfo(0,null);
 LcaInfo l=lcaInfo(n.left,p,q), r=lcaInfo(n.right,p,q);
 int mask=l.mask|r.mask|(n==p?1:0)|(n==q?2:0);
 Node a=l.mask==3?l.ancestor:r.mask==3?r.ancestor:mask==3?n:null;
 return new LcaInfo(mask,a);
}
```

```kotlin
class Node(val value: Int, var left: Node? = null, var right: Node? = null)
data class LcaInfo(val mask: Int, val ancestor: Node?)
fun lca(root: Node?, p: Node, q: Node): Node? {
 fun visit(n: Node?): LcaInfo {
  if (n == null) return LcaInfo(0, null)
  val l = visit(n.left)
  val r = visit(n.right)
  val mask = l.mask or r.mask or (if (n === p) 1 else 0) or (if (n === q) 2 else 0)
  val a = when {
   l.mask == 3 -> l.ancestor
   r.mask == 3 -> r.ancestor
   mask == 3 -> n
   else -> null
  }
  return LcaInfo(mask, a)
 }
 val result = visit(root)
 return if (result.mask == 3) result.ancestor else null
}
```

O(n) time, O(h) live recursive space; O(n) small result objects allocated cumulatively. No ancestor paths are retained.

- null root ⇒ null
- p===q and present ⇒ p
- one absent target ⇒ null

Twist: For many LCA queries on a fixed tree, preprocess parent/depth tables for binary lifting; O(n log n) preprocessing and O(log n) per query replaces repeating full DFS.

Practice source: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/

### Maximum sum of any nonempty path (Hard)

Given a nullable proper tree with Int values, return the maximum sum over nonempty simple paths as nullable Long. A path may start and end anywhere but cannot repeat nodes. Empty tree returns null; do not mutate.

[-10,9,20,null,null,15,7] returns 42 from 15→20→7.

Baseline: Enumerate every pair of endpoints and find their path: O(n²h) straightforward time and O(h) traversal space.

1. Every simple path has a highest node in the rooted tree. At that node it uses at most one downward branch from each child. Obtain the best downward gain from each child with postorder.
2. Clamp negative child gains to zero because a branch is optional. Consider value+left+right as a completed path, updating a global best initialized to Long.MIN_VALUE so an all-negative tree remains nonempty.
3. Return value+max(left,right). This return can extend through the parent without creating a fork. Every possible highest node is considered once, which covers every optimal path.

Correctness: The returned gain is the best nonempty downward path beginning at this node; best is the maximum completed path found anywhere in processed subtrees.

```java
static class Node { int val; Node left,right; Node(int v) { val=v; } }
static Long maximumPath(Node root) {
 if(root==null) return null;
 long[] best={Long.MIN_VALUE}; gain(root,best); return best[0];
}
static long gain(Node n,long[] best) {
 if(n==null) return 0;
 long left=Math.max(0,gain(n.left,best));
 long right=Math.max(0,gain(n.right,best));
 best[0]=Math.max(best[0],(long)n.val+left+right);
 return (long)n.val+Math.max(left,right);
}
```

```kotlin
class Node(val value: Int, var left: Node? = null, var right: Node? = null)
fun maximumPath(root: Node?): Long? {
 if (root == null) return null
 var best = Long.MIN_VALUE
 fun gain(n: Node?): Long {
  if (n == null) return 0L
  val left = maxOf(0L, gain(n.left))
  val right = maxOf(0L, gain(n.right))
  best = maxOf(best, n.value.toLong() + left + right)
  return n.value.toLong() + maxOf(left, right)
 }
 gain(root)
 return best
}
```

O(n) time and O(h) recursive space. Long holds sums for Int-valued trees with an Int-sized node count. Extremely deep trees need explicit postorder frames.

- null ⇒ null
- [-5] ⇒ -5
- root -2 with child -3 ⇒ -2

Twist: To reconstruct the path, record which child supplied each positive downward gain and remember the best turning node. Reconstruct only once at the end to avoid copying paths at every node.

Practice source: https://leetcode.com/problems/binary-tree-maximum-path-sum/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 950 180" role="img" aria-label="A node combines two child summaries but exposes only the legal summary to its parent."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">return</text><path d="M555,72 Q385,15 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="20" text-anchor="middle" font-size="12">return</text><path d="M365,72 Q480,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="480" y="46" text-anchor="middle" font-size="12">consider globally</text><path d="M365,72 Q575,72 785,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">return one branch</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">left subtree gain 4</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">node value 2</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">right subtree gain 7</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">complete candidate 4+2+7=13</div></foreignObject><rect x="785" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="790" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">upward gain 2+max(4,7)=9</div></foreignObject></svg></div><figcaption>A node combines two child summaries but exposes only the legal summary to its parent.</figcaption></figure>

## Additional practice

### [Binary Tree Inorder Traversal](https://leetcode.com/problems/binary-tree-inorder-traversal/) (Easy)

Hint: An explicit stack stores suspended ancestor visits.

Approach: Push the left chain, pop and emit, then move to its right child. O(n) time/O(h) stack plus O(n) output; shared nodes are outside the tree contract.

### [Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/) (Easy)

Hint: Return height but update a two-child candidate.

Approach: Postorder returns max child height+1, updates diameter with leftHeight+rightHeight in edges. O(n)/O(h); distinguish edge count from node count.

### [Path Sum III](https://leetcode.com/problems/path-sum-iii/) (Medium)

Hint: A current root-path prefix sum asks for earlier prefix minus target.

Approach: Maintain frequencies of prefix sums only on the active recursion path; add before descending and remove while backtracking. O(n) expected time/O(h) map and stack; use Long.

### [Construct Binary Tree from Preorder and Inorder Traversal](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) (Medium)

Hint: Preorder names the root; inorder divides descendants.

Approach: For distinct values, map inorder positions and recurse on index intervals with a preorder cursor. O(n) time/O(n) map+O(h) stack; duplicate values need extra identity information.

### [Serialize and Deserialize Binary Tree](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/) (Hard)

Hint: Null child markers make structure recoverable.

Approach: Preorder emits value/null tokens; parser consumes one token per recursive subtree. O(n) time/O(n) serialized output and stack; define escaping and malformed-input behavior.

### [Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) (Medium)

Hint: Inorder traversal exposes rank order.

Approach: Count iterative inorder visits until k. O(h+k) time/O(h) space; repeated queries benefit from subtree sizes maintained through updates.

## Check your reasoning

A returned maximum-path gain may include how many child branches?

1. At most one
2. Always two
3. Any number if positive

Answer: 1. The connection to the parent consumes another path edge. Two children plus parent would create degree three.

Why do BST bounds use Long for Int node values?

1. Trees require floating point.
2. Int extremes must remain valid inside open outer bounds.
3. It makes recursion iterative.

Answer: 2. Using Int.MIN_VALUE as an exclusive lower bound would incorrectly reject a valid node with that value.

## Transfer
In a company hierarchy, each employee has an integer benefit. Select a nonempty chain of directly connected employees, possibly climbing then descending once, to maximize total benefit. A manager may have many direct reports. Derive the returned state.

A simple chain can use at most two child branches at its highest manager.

Only one branch can be exposed to a manager above.

Postorder each employee, retain the two largest positive child downward gains, update best with employee benefit plus both, return employee benefit plus the largest. This generalizes the binary-tree path proof to arbitrary degree. O(n) time/O(h) stack when scanning each reporting edge once; use Long and preserve an all-negative answer.

## Recall
- Write a helper contract before coding LCA or path sum.
- Explain the difference between inherited bounds and synthesized gain.
- Convert recursive postorder into an explicit stack with visited-state frames.

## Sources
- [LeetCode tree taxonomy](https://leetcode.com/tag/tree/): Reference link; not retrieved in this run. Primary source of named practice contracts.
- [Maximum path sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/): Reference link; not retrieved in this run. Canonical nonempty path task underlying the branch-return distinction.


# Pattern 10: Heaps: keep the next winner and forget full order

Sorting establishes the order of every pair even when a problem needs only the next minimum, a few winners, or a middle boundary. A heap maintains enough partial order to expose one extreme quickly while supporting incremental changes.

## Build the model
A binary heap is a complete binary tree usually stored in an array. In a min-heap each parent is no larger than its children. Consequently the root is a minimum, but siblings and different subtrees are not sorted. Reading heap storage or iterating a priority queue does not return sorted order. To emit sorted values, repeatedly remove the root. Insertion restores order by moving a new leaf upward; removal replaces the root with a last leaf and moves that value downward. Both need O(log m) work for m stored values because the tree height is logarithmic.

Derive the direction of the heap from what you need to discard. To keep the k largest values, the least desirable retained winner is the smallest of them; a min-heap exposes that eviction boundary. A max-heap of all inputs also works but keeps more state. Before coding, say aloud what the root means. If the root means worst retained winner, a better incoming item replaces it. If it means next output, removing it advances a stream. These are different invariants using the same structure.

For merging sorted streams, each stream hides a suffix behind its head. Nothing inside that suffix can be smaller than the head, so the global next value must be among the visible heads. Keep at most one entry per nonempty stream, including enough metadata to reveal its successor after removal. The heap eliminates a repeated scan across all stream heads.

Two heaps maintain a median by separating values rather than sorting them. The lower half is a max-heap, the upper half a min-heap. Every lower value must be at most every upper value. Keep the lower half the same size as the upper half or one larger. Then an odd median is the lower root, and an even median averages the two roots. Balance alone is insufficient if the ordering partition is wrong.

Comparators are part of correctness. Comparing integers by subtraction can overflow and reverse their order, so use Integer.compare or compareBy. Never change a heap entry’s priority in place and expect the heap to reorganize itself; remove/reinsert it or use immutable entries and a deliberate stale-entry policy. The foundational top-k exercise below starts around medium because selection and eviction proofs matter more than syntax.

## Invariant
A heap guarantees only its parent-child order and therefore one accessible extreme. The problem-specific invariant must say which candidates are stored, why omitted candidates cannot improve the requested answer, and what the root represents. Charge insert/remove costs to individual candidate events; peak heap size, rather than total input size, determines the logarithmic factor.

## Recognize it
- The next action repeatedly consumes the smallest or largest currently available candidate.
- Only k winners matter, and k is much smaller than the full input size.
- Several already sorted streams must become one: only their exposed heads can win next.
- A dynamic median asks for the boundary between a lower half and an upper half, not a fully sorted sequence.

## Execution trace
1. Insert 5: lower=[5], upper=[]; median=5

The lower half owns the extra item.

2. Insert 2: lower=[2], upper=[5]; median=3.5

Move the previous lower maximum to upper to restore size balance.

3. Insert 8: lower=[5,2], upper=[8]; median=5

Insert into upper, then transfer its minimum to lower.

4. Insert 1: lower=[2,1], upper=[5,8]; median=3.5

All lower values stay no larger than all upper values while sizes become equal.

## Wrong turns
### Use a max-heap of k values to keep the k largest by evicting its root.
With k=2 and retained [10,9], incoming 8 followed by evicting the maximum leaves [9,8], discarding the true largest value 10.

Keep a min-heap so the smallest retained winner is evicted.

### Compare heap priorities with a-b.
a=2147483647 and b=-1 gives overflowed a-b=-2147483648 in Int, incorrectly ordering the larger value first in an ascending comparator.

Use comparison APIs and promote sums before arithmetic; do not derive ordering by overflowing subtraction.

### Kth largest value, counting duplicates (Easy to Medium)

Given non-null Int array a and 1≤k≤a.length, return the kth largest element with duplicate occurrences counted separately. Reject invalid k; input is unchanged. This is a foundational selection exercise.

[3,2,1,5,6,4], k=2 returns 5; [5,5,1], k=2 returns 5.

Baseline: Sort a copy and select index n-k: O(n log n) time and O(n) copied space.

1. After reading a prefix, keep only its largest k values, or all values if fewer than k have arrived. The smallest retained value is the current kth-largest boundary.
2. Insert each value into a min-heap. If size exceeds k, remove its minimum. That removed value has at least k retained values no smaller, so it cannot improve the final kth-largest value.
3. At the end the heap contains exactly k winners. Its root is the kth largest, even when equal values occur because each occurrence is inserted independently.

Correctness: After each input item, the heap is a multiset of the min(k,prefixLength) largest occurrences in that prefix.

```java
static int kthLargest(int[] a,int k) {
 if(k<1||k>a.length) throw new IllegalArgumentException("invalid k");
 PriorityQueue<Integer> heap=new PriorityQueue<>();
 for(int value:a) {
  heap.add(value);
  if(heap.size()>k) heap.remove();
 }
 return heap.element();
}
```

```kotlin
fun kthLargest(a: IntArray, k: Int): Int {
 require(k >= 1 && k <= a.size)
 val heap = PriorityQueue<Int>()
 for (value in a) {
  heap.add(value)
  if (heap.size > k) heap.remove()
 }
 return heap.element()
}
```

O(n log(k+1)) time and O(k) auxiliary space. The +1 handles k=1 without claiming zero work.

- [1],k=1 ⇒ 1
- [5,5,1],k=2 ⇒ 5
- empty array ⇒ invalid k

Twist: A one-shot in-memory selection can use quickselect for expected O(n) time, but it mutates a copy/input and has different worst-case guarantees. A heap also supports a stream.

Practice source: https://leetcode.com/problems/kth-largest-element-in-an-array/

### Top k frequent values with deterministic ties (Medium)

Given non-null Int array a and k≥0, return k distinct values with highest frequency. Break ties by preferring smaller numeric values. Output order is unspecified. k must not exceed the number of distinct values; k=0 returns empty. Input is unchanged.

[4,4,2,2,9], k=1 returns [2] because 2 and 4 tie and 2 is preferred.

Baseline: Count then sort all u distinct values by frequency descending and value ascending: O(n+u log u) time/O(u) space.

1. Frequencies are properties of distinct values, so count all occurrences first. A heap of raw occurrences would answer a different question. Each heap entry is a value plus its fixed final count.
2. The root must be the worst retained entry: lower frequency is worse; on a tie, larger numeric value is worse. Define both comparator levels explicitly to implement the requested preference.
3. Insert all distinct entries and evict whenever size exceeds k. Every evicted entry is no better than all remaining k candidates. Return their values; heap removal order need not match preference order because output order is unspecified.

Correctness: The heap contains the k most preferred entries among the distinct values examined, with the least preferred retained entry at its root.

```java
static int[] topFrequent(int[] a,int k) {
 Map<Integer,Integer> counts=new HashMap<>();
 for(int v:a) counts.merge(v,1,Integer::sum);
 if(k<0||k>counts.size()) throw new IllegalArgumentException("invalid k");
 PriorityQueue<int[]> heap=new PriorityQueue<>((x,y)-> {
  int c=Integer.compare(x[1],y[1]);
  return c!=0?c:Integer.compare(y[0],x[0]);
 });
 for(Map.Entry<Integer,Integer> e:counts.entrySet()) {
  heap.add(new int[]{e.getKey(),e.getValue()});
  if(heap.size()>k) heap.remove();
 }
 int[] out=new int[k];
 for(int i=0;i<k;i++) out[i]=heap.remove()[0];
 return out;
}
```

```kotlin
fun topFrequent(a: IntArray, k: Int): IntArray {
 val counts = HashMap<Int, Int>()
 for (v in a) counts[v] = (counts[v] ?: 0) + 1
 require(k >= 0 && k <= counts.size)
 val heap = PriorityQueue<IntArray>(Comparator { x, y ->
  val c = x[1].compareTo(y[1])
  if (c != 0) c else y[0].compareTo(x[0])
 })
 for ((value, count) in counts) {
  heap.add(intArrayOf(value, count))
  if (heap.size > k) heap.remove()
 }
 return IntArray(k) { heap.remove()[0] }
}
```

O(n+u log(k+1)) expected time with u distinct values; O(u) count map, O(k) heap, O(k) output.

- [],k=0 ⇒ []
- [1,1,2],k=2 ⇒ {1,2}
- [Int.MIN_VALUE,Int.MAX_VALUE],k=1 ⇒ [Int.MIN_VALUE]

Twist: If frequencies change after every event, mutating count fields inside existing heap entries breaks heap order. Use indexed updates or immutable versioned entries plus stale-entry removal.

Practice source: https://leetcode.com/problems/top-k-frequent-elements/

### Merge sorted streams (Hard)

Given a non-null array of non-null Int arrays, each sorted in nondecreasing order, return all values in sorted order. Arrays may be empty. Input is unchanged. Total result length must fit Int indexing; throw if length overflows. Sortedness is a precondition.

[[1,4],[1,3,5],[],[2]] returns [1,1,2,3,4,5].

Baseline: Concatenate then sort: O(N log N) time and O(N) output storage.

1. From each nonempty stream expose only its first unread value. Any hidden value is at least that stream head, so the global minimum must be one exposed head.
2. Store (value,row,column) in a min-heap. Remove the smallest entry and append its value. Its successor is now the only new candidate from that stream, so insert that successor if present.
3. When the heap empties all streams are exhausted. The emitted sequence is sorted because each emission was the smallest among every unread value, not merely among arbitrary samples.

Correctness: The heap contains exactly one head for every stream with unread values, and output is the sorted prefix of all input occurrences.

```java
static class Entry {
 int value,row,col;
 Entry(int v,int r,int c) { value=v; row=r; col=c; }
}
static int[] mergeStreams(int[][] streams) {
 int total=0;
 PriorityQueue<Entry> heap=new PriorityQueue<>(Comparator.comparingInt(e->e.value));
 for(int r=0;r<streams.length;r++) {
  total=Math.addExact(total,streams[r].length);
  if(streams[r].length>0) heap.add(new Entry(streams[r][0],r,0));
 }
 int[] out=new int[total]; int position=0;
 while(!heap.isEmpty()) {
  Entry e=heap.remove(); out[position++]=e.value;
  int next=e.col+1;
  if(next<streams[e.row].length) heap.add(new Entry(streams[e.row][next],e.row,next));
 }
 return out;
}
```

```kotlin
data class Entry(val value: Int, val row: Int, val col: Int)
fun mergeStreams(streams: Array<IntArray>): IntArray {
 var total = 0
 val heap = PriorityQueue<Entry>(compareBy { it.value })
 for (r in streams.indices) {
  total = Math.addExact(total, streams[r].size)
  if (streams[r].isNotEmpty()) heap.add(Entry(streams[r][0], r, 0))
 }
 val out = IntArray(total)
 var position = 0
 while (heap.isNotEmpty()) {
  val e = heap.remove()
  out[position++] = e.value
  val next = e.col + 1
  if (next < streams[e.row].size) heap.add(Entry(streams[e.row][next], e.row, next))
 }
 return out
}
```

O(k+N log(k+1)) time for k streams and N total items; O(k) auxiliary heap and O(N) output. Entry allocation is O(N) cumulatively.

- [] ⇒ []
- [[],[]] ⇒ []
- [[2,2],[2]] ⇒ [2,2,2]

Twist: For lazy or infinite streams return an iterator and read only enough winners for the requested prefix. Equal values need a secondary stream/sequence tie-breaker only if stable identity order is required.

Practice source: https://leetcode.com/problems/merge-k-sorted-lists/

### Median of an insertion-only stream (Hard)

Implement add(Int) and median(): Double over all inserted occurrences. Calling median before any insertion throws. Do not discard duplicates. The number of inserted values fits Int; no deletion operation is required.

After adding 5,2,8,1, medians are 5.0,3.5,5.0,3.5.

Baseline: Keep a sorted array, inserting by shifting: O(n) insertion, O(1) median, O(n) space.

1. Partition values into lower and upper halves. A max-heap exposes the largest lower value and a min-heap exposes the smallest upper value, the only two values relevant to the median.
2. Insert relative to the lower root. If lower becomes two larger, move its maximum to upper. If upper becomes larger, move its minimum to lower. These root transfers preserve cross-half ordering.
3. For unequal sizes return lower.peek. For equal sizes average the two roots after converting to Long. This avoids Int overflow for two extreme values.

Correctness: Every lower value is ≤every upper value, and lower.size equals upper.size or upper.size+1. Together these establish the middle rank(s).

```java
static class MedianStream {
 private final PriorityQueue<Integer> lower=new PriorityQueue<>(Comparator.reverseOrder());
 private final PriorityQueue<Integer> upper=new PriorityQueue<>();
 void add(int value) {
  if(lower.isEmpty()||value<=lower.element()) lower.add(value);
  else upper.add(value);
  if(lower.size()>upper.size()+1) upper.add(lower.remove());
  else if(upper.size()>lower.size()) lower.add(upper.remove());
 }
 double median() {
  if(lower.isEmpty()) throw new IllegalStateException("empty stream");
  if(lower.size()>upper.size()) return lower.element();
  return ((long)lower.element()+upper.element())/2.0;
 }
}
```

```kotlin
class MedianStream {
 private val lower = PriorityQueue<Int>(Comparator.reverseOrder())
 private val upper = PriorityQueue<Int>()
 fun add(value: Int) {
  if (lower.isEmpty() || value <= lower.element()) lower.add(value)
  else upper.add(value)
  if (lower.size > upper.size + 1) upper.add(lower.remove())
  else if (upper.size > lower.size) lower.add(upper.remove())
 }
 fun median(): Double {
  check(lower.isNotEmpty()) { "empty stream" }
  if (lower.size > upper.size) return lower.element().toDouble()
  return (lower.element().toLong() + upper.element()) / 2.0
 }
}
```

O(log(n+1)) per insertion, O(1) median, O(n) stored values after n insertions. No sorting is performed.

- add(Int.MAX_VALUE) twice ⇒ median 2147483647.0
- add(Int.MIN_VALUE),add(Int.MAX_VALUE) ⇒ -0.5
- empty median ⇒ exception

Twist: A sliding-window median additionally needs removal of expired values. Add identity-aware lazy deletion with logical heap sizes or use balanced multisets; ordinary priority-queue arbitrary removal is O(n).

Practice source: https://leetcode.com/problems/find-median-from-data-stream/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="Two heaps expose exactly the two values bordering the median."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">ordered lower half</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">ordered upper half</text><path d="M175,72 Q385,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">one extra lower item</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">lower max-heap: 1,2,4; root 4</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">partition: every lower ≤ every upper</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">upper min-heap: 7,9; root 7</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">odd median = lower root = 4</div></foreignObject></svg></div><figcaption>Two heaps expose exactly the two values bordering the median.</figcaption></figure>

## Additional practice

### [Last Stone Weight](https://leetcode.com/problems/last-stone-weight/) (Easy)

Hint: Repeatedly take the two largest active values.

Approach: Use a max-heap, remove two, insert positive difference. O(n log n) time/O(n) space; a final empty heap means zero.

### [K Closest Points to Origin](https://leetcode.com/problems/k-closest-points-to-origin/) (Medium)

Hint: Worst retained distance belongs at the root.

Approach: Maintain a max-heap of k points by squared distance. O(n log(k+1))/O(k); with unrestricted Int coordinates use BigInteger or a proven safe coordinate bound because two squared Int coordinates can exceed signed Long.

### [Find K Pairs with Smallest Sums](https://leetcode.com/problems/find-k-pairs-with-smallest-sums/) (Medium)

Hint: Each row of pair sums is a sorted stream.

Approach: Seed (i,0) for up to k rows of sorted inputs, pop and advance the same row. O(k log(min(k,m)+1)) after seeding; use Long sums and stop at available pairs.

### [Reorganize String](https://leetcode.com/problems/reorganize-string/) (Medium)

Hint: Choose the most frequent available symbol while withholding the last used one.

Approach: Max-heap remaining counts; release previous symbol only after choosing a different one. O(n log u)/O(u); impossible when maximum count>(n+1)/2.

### [IPO](https://leetcode.com/problems/ipo/) (Hard)

Hint: Separate eligibility order from reward order.

Approach: Sort projects by required capital, insert newly affordable profits into max-heap, select up to k. O(n log n+k log n)/O(n); nonnegative profits justify stopping when none are available.

### [Smallest Range Covering Elements from K Lists](https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/) (Hard)

Hint: A heap minimum plus a running maximum describes one covering range.

Approach: Keep one item per sorted list, update best range, advance the minimum list. Stop when a list ends. O(N log k)/O(k); compare widths using Long.

## Check your reasoning

What does a min-heap of the k largest values expose?

1. The largest value overall
2. The smallest retained winner, which is the eviction boundary
3. The median of all values

Answer: 2. The root is intentionally the worst winner; a larger incoming value may replace it.

Are balanced two-heap sizes sufficient for a correct median?

1. Yes
2. No, cross-half ordering must also hold
3. Only for odd counts

Answer: 2. Lower=[100] and upper=[1] have equal sizes but the partition is reversed. Ordering and size must both be invariant.

## Transfer
A service reads many sorted files of timestamps and must publish only the first 1000 events globally, preserving source order for equal timestamps. Design the candidate state without loading all files.

Only one unread head per file can win next.

A deterministic tie rule can include file index and per-file sequence.

Maintain a min-heap of (timestamp,fileId,sequence,event) for one current head per nonempty file. Pop, publish, and read the next record from that file until 1000 events or exhaustion. O(k+1000 log(k+1)) heap work after seeding k files and O(k) memory, excluding output and I/O buffers. The tie key must match the exact meaning of source order; no global stable order exists across different files unless specified.

## Recall
- For each heap problem say what the root represents before choosing direction.
- Reconstruct the median invariants and explain each rebalance transfer.
- Explain why changing a stored entry priority silently can corrupt future results.

## Sources
- [Java PriorityQueue documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html): Reference link; not retrieved in this run. Heap ordering, iterator order, and operation complexity.
- [LeetCode heap catalogue](https://leetcode.com/tag/heap-priority-queue/): Reference link; not retrieved in this run. Primary public practice statements; no company frequency inference.


# Pattern 11: Backtracking: enumerate choices with a reversible state

Some problems ask for many valid objects rather than one numeric answer. Their solution space is a tree of partial choices. Backtracking explores that tree while maintaining one reusable partial object, undoing each choice before trying a sibling.

## Build the model
Begin with a decision tree, not with a recursive function. A partial solution is a node in that tree. An edge chooses one next decision. A leaf is either a complete valid answer or a failed branch. Recursion is just a depth-first walk of this tree. Name the state that makes future choices well-defined: a starting index for combinations, a used-position array for permutations, occupied columns and diagonals for queens. Without that state, a loop can revisit choices or omit valid ones.

The central rhythm is choose, explore, unchoose. Append a value to a path, recurse, then remove exactly that appended value. If a boolean says an item is used, set it before recursion and reset it afterward. The undo step restores the parent state, so the next sibling starts from the same history. When recording an answer, copy the path. Storing the same mutable list repeatedly makes all output entries refer to one changing object.

Subsets and permutations differ in what order means. A subset is identified by membership, so choosing indices in increasing order gives one representation for each subset. A permutation assigns a value to each output position, so any unused index may be next. Combination sum allows a value to repeat, so recursive start may remain at the selected index; changing it to i+1 would impose single use. Derive that transition from the contract rather than memorizing it.

Pruning removes a whole subtree only when no extension can be valid. With sorted strictly positive candidates, a candidate larger than remaining sum proves every later candidate is also impossible. This argument fails with negative values: a later negative could repair an overshoot. Constraint pruning in queens is stronger: two placed queens already attacking cannot be fixed by adding more queens, so reject immediately.

Duplicate suppression is a symmetry rule. In sorted unique-permutation generation, when equal occurrences are both unused, choose the earlier occurrence first. Equal occurrences become interchangeable labels; choosing a canonical label order produces every distinct value arrangement once. It is not the same as removing duplicates from the input, because multiplicity is part of the answer.

Exponential output is not automatically a poor algorithm. If all 2^n subsets must be emitted, the output itself is exponential. State runtime in terms of search nodes, pruning assumptions, and the cost of copying each complete answer. Never promise polynomial time merely because the recursion looks short.

## Invariant
At entry to a recursive call, path describes exactly the decisions made along its root-to-call route, and all auxiliary markers agree with those decisions. Every child choice is legal under that state. On return the state is restored exactly to its entry value. Completeness follows by showing each valid answer has one permitted sequence of choices; uniqueness follows from canonical ordering or duplicate rules.

## Recognize it
- The output contains all subsets, arrangements, placements, or routes satisfying constraints.
- A partial decision can be checked before the remaining decisions are known, allowing early rejection.
- The same partial buffer can be reused if each recursive change is undone on return.
- Duplicates in input create duplicate decision paths unless a canonical representative rule is imposed.

## Execution trace
1. Input [1,2]; path=[]; start=0; output=[[]]

The empty subset is valid; record a copy before choosing.

2. Choose 1: path=[1]; start=1; output=[[],[1]]

Only later indices may follow, preventing reordered duplicates.

3. Choose 2: path=[1,2]; start=2; output=[[],[1],[1,2]]

No candidates remain; return and remove 2, then remove 1.

4. Choose 2 from root: path=[2]; start=2; output=[[],[1],[1,2],[2]]

Restored parent state allows a separate branch without retaining 1.

## Wrong turns
### Add the mutable path directly to output.
For subsets of [1], storing the same path reference first empty, then [1], and finally removing 1 leaves both output references pointing to the same empty list.

Copy the current path when recording an answer; continue reusing only the working buffer.

### Prune whenever the running sum exceeds target for arbitrary integers.
Candidates [5,-2], target 3: after choosing 5, sum exceeds 3, but adding -2 creates the valid solution [5,-2].

Use overshoot pruning only under a strictly positive candidate contract; otherwise redesign state, bounds, and termination.

### All subsets through increasing indices (Easy to Medium)

Given non-null Int array of distinct values, return every subset as a list of values in input order. Return each subset once; overall output order is unspecified. Empty input returns a list containing the empty list. Input is unchanged. Distinct values are a precondition.

[1,2] returns [], [1], [1,2], [2].

Baseline: Enumerate all bit masks and inspect every bit: O(n2^n) time including output, O(n) working space plus output; this is also a good alternative.

1. At every state the current path is already a valid subset, so record it immediately rather than waiting for a full-length leaf.
2. Choose any index i from start onward and recurse with i+1. Increasing indices ensure each membership set has one ordering and prevent selecting the same index twice.
3. After returning remove the last value. The next iteration chooses a different extension from the same parent subset. For empty input, the initial recorded path supplies the required empty subset.

Correctness: Path contains values at strictly increasing input indices less than start; all extensions use only indices at least start.

```java
static List<List<Integer>> subsets(int[] a) {
 List<List<Integer>> out=new ArrayList<>();
 subsetDfs(a,0,new ArrayList<>(),out); return out;
}
static void subsetDfs(int[] a,int start,List<Integer> path,List<List<Integer>> out) {
 out.add(new ArrayList<>(path));
 for(int i=start;i<a.length;i++) {
  path.add(a[i]); subsetDfs(a,i+1,path,out); path.remove(path.size()-1);
 }
}
```

```kotlin
fun subsets(a: IntArray): List<List<Int>> {
 val out = mutableListOf<List<Int>>()
 val path = mutableListOf<Int>()
 fun dfs(start: Int) {
  out.add(path.toList())
  for (i in start until a.size) {
   path.add(a[i]); dfs(i + 1); path.removeAt(path.lastIndex)
  }
 }
 dfs(0)
 return out
}
```

O(n2^n) time including copying; O(n) auxiliary path/stack and O(n2^n) output elements for n input values.

- [] ⇒ [[]]
- [7] ⇒ [[],[7]]

Twist: If input has duplicates, sort a copy and skip equal values only when i>start and a[i]==a[i-1]; equal occurrences may still be used at deeper levels.

Practice source: https://leetcode.com/problems/subsets/

### Unbounded combination sum with positive candidates (Medium)

Given non-null Int candidates, all strictly positive, and target≥0, return every distinct nondecreasing value combination summing to target. Values may be reused any number of times. Duplicate candidate entries do not duplicate output. Reject nonpositive candidates or negative target. Input is unchanged.

[2,3,6,7], target=7 returns [2,2,3] and [7]. Target 0 returns [[]].

Baseline: Generate arbitrary sequences up to target/minCandidate, then deduplicate permutations: exponentially many redundant orders and large deduplication storage.

1. Sort a copy to make order canonical and enable an overshoot bound. State is start index, remaining target, and current path. Skip equal candidate values at the same recursion depth.
2. Choose a[i] only if it does not exceed remaining. Recurse with i, allowing reuse of this value while forbidding smaller prior values. Remaining strictly decreases because candidates are positive, guaranteeing termination.
3. When remaining is zero record a copy and return. If a candidate exceeds remaining, break because all later values are at least as large. This rejects an entire impossible suffix of choices.

Correctness: Path is nondecreasing, remaining equals target minus its sum, and future choices use indices at least start, so every value multiset has one canonical route.

```java
static List<List<Integer>> combinationSum(int[] candidates,int target) {
 if(target<0) throw new IllegalArgumentException("negative target");
 for(int v:candidates) if(v<=0) throw new IllegalArgumentException("nonpositive candidate");
 int[] a=candidates.clone(); Arrays.sort(a);
 List<List<Integer>> out=new ArrayList<>();
 combinationDfs(a,0,target,new ArrayList<>(),out); return out;
}
static void combinationDfs(int[] a,int start,int remaining,List<Integer> path,List<List<Integer>> out) {
 if(remaining==0) { out.add(new ArrayList<>(path)); return; }
 for(int i=start;i<a.length;i++) {
  if(i>start && a[i]==a[i-1]) continue;
  if(a[i]>remaining) break;
  path.add(a[i]); combinationDfs(a,i,remaining-a[i],path,out);
  path.remove(path.size()-1);
 }
}
```

```kotlin
fun combinationSum(candidates: IntArray, target: Int): List<List<Int>> {
 require(target >= 0 && candidates.all { it > 0 })
 val a = candidates.sortedArray()
 val out = mutableListOf<List<Int>>()
 val path = mutableListOf<Int>()
 fun dfs(start: Int, remaining: Int) {
  if (remaining == 0) { out.add(path.toList()); return }
  for (i in start until a.size) {
   if (i > start && a[i] == a[i - 1]) continue
   if (a[i] > remaining) break
   path.add(a[i]); dfs(i, remaining - a[i]); path.removeAt(path.lastIndex)
  }
 }
 dfs(0, target)
 return out
}
```

Let m be candidate count, d=target/minPositive for nonempty input, S solutions, and V visited states. O(m log m+mV+Sd) conservative time, O(m+d) auxiliary space plus O(Sd) output. V can grow exponentially; huge targets may exceed JVM recursion depth. Empty candidates take O(1) search.

- [],target=0 ⇒ [[]]
- [2,2],target=4 ⇒ [[2,2]]
- [2],target=3 ⇒ []
- [0],target=1 ⇒ rejected

Twist: For single-use combination sum recurse with i+1, retaining same-depth duplicate skipping. For counting only, dynamic programming avoids materializing every solution.

Practice source: https://leetcode.com/problems/combination-sum/

### Distinct permutations of a multiset (Medium)

Given a non-null Int array that may contain duplicates, return every distinct full-length value permutation. Output order is unspecified; input is unchanged. Empty input returns [[]].

[1,1,2] returns [1,1,2], [1,2,1], [2,1,1].

Baseline: Generate n! index permutations then hash complete value lists: O(n n!) time plus duplicate output/deduplication storage.

1. Sort a copy so equal occurrences are adjacent. Keep used[i] to distinguish whether an occurrence is already in the current path; multiplicity must remain available.
2. Skip a used index. Also skip equal a[i] when its previous equal occurrence is unused: both could fill this position, and choosing the earlier occurrence first is the canonical representative. If the previous equal occurrence is already used, choosing this one is necessary and legal.
3. At path length n copy the permutation. Undo both path append and used flag after every child. Canonical occurrence order removes symmetry without removing valid multiplicities.

Correctness: Path uses each selected index once; among equal values, selected occurrences appear in increasing original sorted-index order. Every distinct value permutation has exactly one compatible occurrence assignment.

```java
static List<List<Integer>> uniquePermutations(int[] input) {
 int[] a=input.clone(); Arrays.sort(a);
 List<List<Integer>> out=new ArrayList<>();
 permutationDfs(a,new boolean[a.length],new ArrayList<>(),out); return out;
}
static void permutationDfs(int[] a,boolean[] used,List<Integer> path,List<List<Integer>> out) {
 if(path.size()==a.length) { out.add(new ArrayList<>(path)); return; }
 for(int i=0;i<a.length;i++) {
  if(used[i] || (i>0 && a[i]==a[i-1] && !used[i-1])) continue;
  used[i]=true; path.add(a[i]); permutationDfs(a,used,path,out);
  path.remove(path.size()-1); used[i]=false;
 }
}
```

```kotlin
fun uniquePermutations(input: IntArray): List<List<Int>> {
 val a = input.sortedArray()
 val used = BooleanArray(a.size)
 val path = mutableListOf<Int>()
 val out = mutableListOf<List<Int>>()
 fun dfs() {
  if (path.size == a.size) { out.add(path.toList()); return }
  for (i in a.indices) {
   if (used[i] || (i > 0 && a[i] == a[i - 1] && !used[i - 1])) continue
   used[i] = true; path.add(a[i]); dfs()
   path.removeAt(path.lastIndex); used[i] = false
  }
 }
 dfs()
 return out
}
```

O(n n!) worst-case time including scanning choices and copying output; fewer branches when values repeat. O(n) auxiliary state/stack plus O(Sn) output for S distinct permutations.

- [] ⇒ [[]]
- [2,2] ⇒ [[2,2]]
- [1,2] ⇒ two permutations

Twist: If only the next lexicographic permutation is requested, do not enumerate: find a decreasing suffix, swap its pivot with the next larger suffix value, and reverse the suffix in O(n) time.

Practice source: https://leetcode.com/problems/permutations-ii/

### Place nonattacking queens (Hard)

For 0≤n≤12, return all n×n boards containing exactly n queens, one in each row, with no shared column or diagonal. Each board is a list of strings using Q and .. n=0 returns one empty board. Reject n outside the stated range.

n=4 has two boards, including [.Q..,...Q,Q...,..Q.]. n=2 has no boards.

Baseline: Try all n choices per row, then inspect completed boards: O(n^n n²) time with most candidates invalid.

1. Choose one queen per row, so row conflicts are impossible by construction. A future placement is legal iff its column and both diagonal identifiers are unoccupied.
2. At (row,col), row-col+n and row+col name the diagonals. Mark all three constraints, store the chosen column, and recurse to the next row. Any conflict can be pruned immediately because later queens cannot undo an attack between existing queens.
3. At row n, create strings from the chosen-column array. Undo all occupancy markers after returning. Building strings only at a solution keeps partial search state compact.

Correctness: Rows below the current row each have exactly one queen, no placed pair attacks, and occupancy arrays exactly describe those queens. Every valid board determines one row-by-row sequence.

```java
static List<List<String>> queens(int n) {
 if(n<0||n>12) throw new IllegalArgumentException("n outside 0..12");
 List<List<String>> out=new ArrayList<>();
 queenDfs(0,n,new int[n],new boolean[n],new boolean[2*n+1],new boolean[2*n+1],out);
 return out;
}
static void queenDfs(int row,int n,int[] chosen,boolean[] columns,boolean[] down,boolean[] up,List<List<String>> out) {
 if(row==n) {
  List<String> board=new ArrayList<>();
  for(int r=0;r<n;r++) { char[] line=new char[n]; Arrays.fill(line,'.'); line[chosen[r]]='Q'; board.add(new String(line)); }
  out.add(board); return;
 }
 for(int c=0;c<n;c++) {
  int d=row-c+n,u=row+c;
  if(columns[c]||down[d]||up[u]) continue;
  columns[c]=down[d]=up[u]=true; chosen[row]=c;
  queenDfs(row+1,n,chosen,columns,down,up,out);
  columns[c]=down[d]=up[u]=false;
 }
}
```

```kotlin
fun queens(n: Int): List<List<String>> {
 require(n in 0..12)
 val chosen = IntArray(n)
 val columns = BooleanArray(n)
 val down = BooleanArray(2 * n + 1)
 val up = BooleanArray(2 * n + 1)
 val out = mutableListOf<List<String>>()
 fun dfs(row: Int) {
  if (row == n) {
   out.add((0 until n).map { r ->
    val line = CharArray(n) { '.' }; line[chosen[r]] = 'Q'; String(line)
   })
   return
  }
  for (c in 0 until n) {
   val d = row - c + n
   val u = row + c
   if (columns[c] || down[d] || up[u]) continue
   columns[c] = true; down[d] = true; up[u] = true; chosen[row] = c
   dfs(row + 1)
   columns[c] = false; down[d] = false; up[u] = false
  }
 }
 dfs(0)
 return out
}
```

O(n·n! + S n²) conservative time after column pruning, for S output boards; diagonals prune further. O(n) auxiliary arrays/stack, O(Sn²) output characters. n=0 is constant.

- n=0 ⇒ [[]]
- n=1 ⇒ [[Q]]
- n=2 or n=3 ⇒ []
- n=4 ⇒ exactly two valid boards

Twist: To count only, increment a counter at each complete placement rather than building strings. Bit masks compress occupancy and speed candidate enumeration, but do not change the combinatorial search requirement.

Practice source: https://leetcode.com/problems/n-queens/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 950 180" role="img" aria-label="Increasing start indices enumerate subsets without rearranging the same membership."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">choose index 0</text><path d="M175,72 Q290,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="290" y="46" text-anchor="middle" font-size="12">choose index 1</text><path d="M365,72 Q480,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="480" y="46" text-anchor="middle" font-size="12">choose index 1</text><path d="M745,72 Q765,72 785,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="765" y="46" text-anchor="middle" font-size="12">emit; then undo</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">[] start=0</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">[1] start=1</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">[2] start=2</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">[1,2] start=2</div></foreignObject><rect x="785" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="790" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">record a copy at every node</div></foreignObject></svg></div><figcaption>Increasing start indices enumerate subsets without rearranging the same membership.</figcaption></figure>

## Additional practice

### [Combinations](https://leetcode.com/problems/combinations/) (Medium)

Hint: Increasing indices encode an unordered selection.

Approach: Choose next value above the last, stop at size k; prune when remaining candidates cannot fill missing positions. O(k·C(n,k)) output-sensitive time with optimized bounds/O(k) auxiliary space.

### [Subsets II](https://leetcode.com/problems/subsets-ii/) (Medium)

Hint: Skip equal choices at the same depth only.

Approach: Sort a copy and continue when i>start and a[i]==a[i-1]. Recurse with i+1. O(n2^n) worst-case/O(n) auxiliary plus output; deeper equal choices preserve multiplicity.

### [Palindrome Partitioning](https://leetcode.com/problems/palindrome-partitioning/) (Medium)

Hint: Each choice is a palindromic prefix of the remaining suffix.

Approach: Precompute palindrome intervals in O(n²), then DFS end positions with valid intervals. O(n²+n2^n) worst-case including output, O(n²) table plus path/stack.

### [Word Search](https://leetcode.com/problems/word-search/) (Medium)

Hint: The state is cell, next character index, and cells used on this path.

Approach: DFS matching four neighbors; mark before descent and restore on return. O(RC·3^L) standard upper bound after initial choices for word length L, O(L) stack plus visited state; no cell reuse.

### [Restore IP Addresses](https://leetcode.com/problems/restore-ip-addresses/) (Medium)

Hint: A choice consumes one to three digits and one segment slot.

Approach: Prune remaining length against remaining slots, reject leading-zero multi-digit segments and values>255. At most 3^4 branches times short parsing; output uses constant-length addresses.

### [Sudoku Solver](https://leetcode.com/problems/sudoku-solver/) (Hard)

Hint: Track legal candidates by row, column, and box.

Approach: Pick an empty cell with fewest candidates, try legal digits, mark/recurse/undo. Exponential worst case in empty cells; bit masks make checks constant. Define invalid or unsatisfiable input behavior.

## Check your reasoning

For combinations allowing repeated use of chosen a[i], what is the recursive start?

1. i
2. i+1
3. Always zero

Answer: 1. Keeping i allows this value again while preventing smaller values from creating reordered copies.

When can equal a[i] be skipped in unique permutations?

1. Always
2. When a[i-1] is equal and unused at this step
3. When a[i-1] is equal and used

Answer: 2. An unused earlier equal occurrence can represent the same choice. If it is already used, the later equal occurrence supplies needed multiplicity.

## Transfer
Generate all meal plans of exactly d days using dishes with positive costs. Total cost must equal budget, no two adjacent days may use the same dish, and order across days matters. Derive the search state and safe pruning.

Day order matters, so increasing dish indices would discard valid plans.

Track remaining budget and the previous dish identity.

State is day,remainingBudget,previousDish,path. Try every dish except previous; subtract its positive cost, recurse, and undo. At day d accept only zero remainder. Prune if remainder is below remainingDays×minimumCost or above remainingDays×maximumCost; these relaxed bounds are safe even though adjacency may make them unattainable. O(m^d·d) worst-case including output for m dishes, O(d) auxiliary state. Use Long for cost bounds and budget; additional memoization helps counting or feasibility, but enumeration still needs distinct paths.

## Recall
- Draw the decision tree before writing a helper signature.
- Explain which choice rule gives uniqueness and which condition gives pruning.
- Give a counterexample to overshoot pruning when negative values are allowed.

## Sources
- [LeetCode backtracking catalogue](https://leetcode.com/tag/backtracking/): Reference link; not retrieved in this run. Primary task statements and additional practice links.
- [N-Queens](https://leetcode.com/problems/n-queens/): Reference link; not retrieved in this run. Canonical nonattacking-placement enumeration task; this lesson specifies n=0 explicitly.


# Pattern 12: Tries: share prefixes and prune impossible continuations

When many words share a beginning, checking each word independently repeats the same character comparisons. A trie stores a path for each distinct prefix so one traversal answers exact-word, prefix, wildcard, or multiword board-search questions.

## Build the model
A trie is a rooted tree whose edges are labeled with symbols. The root represents the empty prefix. Following c, a, t from the root reaches the prefix cat. Nodes represent prefixes, not automatically complete words. If both car and cart are stored, the node for car needs an end-of-word marker even though it has children. Without that marker, searching ca might incorrectly succeed as a whole word just because a path exists. Conversely, a word can be a prefix of another word, so insertion must never stop merely because an earlier terminal marker is encountered.

Start with a simple dictionary interface: insert, exact search, and prefix search. Insertion creates missing child nodes while scanning the word, then marks its final node terminal. Exact search requires both an existing path and a terminal marker; prefix search requires only an existing path. Empty strings force a useful design decision: this lesson permits an empty word in the basic and wildcard dictionaries, represented by the root terminal marker, while replacement roots and board-search targets must be nonempty.

The symbol model is part of the contract. The code below deliberately uses lowercase Latin a through z so child lookup is an array index and has constant cost. It validates that restriction rather than silently treating all strings as ASCII. For arbitrary Unicode, choose whether symbols are UTF-16 code units, Unicode code points, or normalized grapheme clusters, then use maps or another suitable branching structure. A 26-way array is inappropriate without a bounded alphabet contract.

Wildcard search changes one deterministic edge into a set of possible edges. A dot can follow any existing child, but consumes exactly one character. The recursion state must include the current trie node and pattern position; a terminal marker is checked only when the entire pattern is consumed. A wildcard may multiply work, so it is wrong to claim every query is always linear in its pattern length.

Board search combines two trees of possibilities. Board adjacency proposes a next cell; the trie says whether the resulting letters remain a prefix of any desired word. A missing trie edge rejects every dictionary word sharing that failed route at once. Keep a visited marker only for the current board path, restore it on return, and clear a found terminal word when output should be unique. Prefix pruning reduces real work but does not eliminate exponential worst cases on repetitive boards.

## Invariant
At a trie node reached after consuming k symbols, the path from root spells exactly those k symbols. A terminal marker means that exact prefix is an inserted word. In product searches, the board path, trie path, and consumed character count describe the same prefix, while visited cells match only that active route. Missing edges prove no stored extension exists.

## Recognize it
- Queries repeatedly ask whether a string is a word or a prefix of any stored word.
- A dictionary is searched one character at a time, with early rejection when no prefix remains.
- Several target words are explored together in another search space, such as board paths.
- A wildcard represents several next-character alternatives, so search may branch at a prefix node.

## Execution trace
1. Insert car: root→c→a→r; mark r terminal.

Each edge extends the represented prefix by one symbol.

2. Insert cart: reuse c,a,r; append t; keep r terminal.

A terminal node can still be a proper prefix of another word.

3. search(ca): path exists, terminal=false ⇒ false.

Exact membership needs more than reachability.

4. startsWith(ca) ⇒ true; search(cart) ⇒ true.

Prefix and exact-word queries have different stopping conditions.

## Wrong turns
### Treat every existing prefix path as a complete stored word.
After inserting apple, the path app exists, but exact search(app) must be false. Prefix search(app) is true.

Store an independent terminal marker and distinguish exact search from prefix search.

### Keep board cells visited after returning from one search branch.
Board [a,b] with dictionary [ab,ba]: a first search can visit both cells; permanently marking them prevents the second starting position from finding ba.

Mark only on the active recursion route and unmark before returning; global terminal clearing is a separate deduplication decision.

### Build an exact and prefix dictionary (Easy to Medium)

Implement insert(word), search(word), and startsWith(prefix) for non-null lowercase a-z strings. Reject other symbols. Empty words are allowed, repeated insertion is idempotent, and startsWith("") is always true. This foundational exercise establishes the trie contract.

Insert apple: search(apple)=true, search(app)=false, startsWith(app)=true. Then insert app: search(app)=true.

Baseline: Store full words in a hash set: expected O(L) exact lookup but a prefix query scans all words, O(S) total characters in the worst case.

1. Represent each possible next letter with one child reference. The root stands for zero characters, so no special first-letter branch is needed.
2. Insertion follows or creates edges then marks terminal. Search and startsWith share a path-following helper; only search also requires the final terminal flag.
3. Validate an entire argument before mutating the structure. Otherwise inserting an invalid word such as ab! could partially add prefixes before throwing, violating clean operation behavior.

Correctness: Every inserted word has a terminal endpoint, and every created path is a prefix of some successfully inserted word. Repeated insertion changes no structure once the path exists.

```java
static class Trie {
 private static class TNode { TNode[] next=new TNode[26]; boolean terminal; }
 private final TNode root=new TNode();
 private void validate(String s) {
  Objects.requireNonNull(s);
  for(int i=0;i<s.length();i++) if(s.charAt(i)<'a'||s.charAt(i)>'z') throw new IllegalArgumentException("lowercase a-z only");
 }
 void insert(String word) {
  validate(word); TNode n=root;
  for(int i=0;i<word.length();i++) {
   int c=word.charAt(i)-'a';
   if(n.next[c]==null) n.next[c]=new TNode();
   n=n.next[c];
  }
  n.terminal=true;
 }
 private TNode follow(String s) {
  validate(s); TNode n=root;
  for(int i=0;i<s.length();i++) { n=n.next[s.charAt(i)-'a']; if(n==null) return null; }
  return n;
 }
 boolean search(String word) { TNode n=follow(word); return n!=null&&n.terminal; }
 boolean startsWith(String prefix) { return follow(prefix)!=null; }
}
```

```kotlin
class Trie {
 private class TNode { val next = arrayOfNulls<TNode>(26); var terminal = false }
 private val root = TNode()
 private fun validate(s: String) { require(s.all { it in 'a'..'z' }) }
 fun insert(word: String) {
  validate(word)
  var n = root
  for (c in word) {
   val index = c - 'a'
   if (n.next[index] == null) n.next[index] = TNode()
   n = n.next[index]!!
  }
  n.terminal = true
 }
 private fun follow(s: String): TNode? {
  validate(s)
  var n = root
  for (c in s) n = n.next[c - 'a'] ?: return null
  return n
 }
 fun search(word: String): Boolean = follow(word)?.terminal == true
 fun startsWith(prefix: String): Boolean = follow(prefix) != null
}
```

O(L) time per operation for L argument characters including validation. O(S) trie nodes for S total inserted characters; 26 references per node is a fixed alphabet factor.

- new trie startsWith("") ⇒ true; search("") ⇒ false
- insert(""); search("") ⇒ true
- insert apple twice ⇒ unchanged membership
- insert A ⇒ rejected

Twist: For deletion, clear terminal only after confirming the word exists, then optionally prune childless nonterminal ancestors. Shared prefix nodes cannot be deleted while other words depend on them.

Practice source: https://leetcode.com/problems/implement-trie-prefix-tree/

### Replace words by their shortest dictionary root (Medium)

Given a non-null list of nonempty lowercase a-z roots and a sentence of lowercase a-z words separated by exactly one space, replace each word with its shortest dictionary root prefix if one exists. Empty sentence is allowed. Reject malformed roots or sentence. Input is unchanged.

roots=[cat,bat,rat], sentence="the cattle was rattled" returns "the cat was rat".

Baseline: For each word try each root with a prefix comparison: O(W·D·L) worst case for W words, D roots and max compared length L.

1. Insert all roots into a trie. Multiple roots may share a prefix, so one word traversal tests them simultaneously. Validate all arguments before constructing the answer.
2. Walk each word from root. Stop at the first terminal node and return the consumed prefix: the first terminal is the shortest available root, even if longer roots also match.
3. If a child edge is absent, no root can match the remaining word; preserve the original word. Join replacements with the same single-space format.

Correctness: Before consuming each word character, the trie node matches exactly the already consumed prefix, and no shorter matching root has been skipped.

```java
static class RootNode { RootNode[] next=new RootNode[26]; boolean terminal; }
static String replaceRoots(List<String> roots,String sentence) {
 Objects.requireNonNull(sentence);
 if(!sentence.isEmpty() && !sentence.matches("[a-z]+( [a-z]+)*")) throw new IllegalArgumentException("invalid sentence");
 RootNode root=new RootNode();
 for(String word:roots) {
  if(word==null||!word.matches("[a-z]+")) throw new IllegalArgumentException("invalid root");
  RootNode n=root;
  for(char c:word.toCharArray()) { int i=c-'a'; if(n.next[i]==null)n.next[i]=new RootNode(); n=n.next[i]; }
  n.terminal=true;
 }
 if(sentence.isEmpty()) return "";
 String[] words=sentence.split(" ");
 for(int w=0;w<words.length;w++) {
  RootNode n=root;
  for(int i=0;i<words[w].length();i++) {
   n=n.next[words[w].charAt(i)-'a']; if(n==null) break;
   if(n.terminal) { words[w]=words[w].substring(0,i+1); break; }
  }
 }
 return String.join(" ",words);
}
```

```kotlin
class RootNode { val next = arrayOfNulls<RootNode>(26); var terminal = false }
fun replaceRoots(roots: List<String>, sentence: String): String {
 require(sentence.isEmpty() || sentence.matches(Regex("[a-z]+( [a-z]+)*")))
 val root = RootNode()
 for (word in roots) {
  require(word.matches(Regex("[a-z]+")))
  var n = root
  for (c in word) {
   val i = c - 'a'
   if (n.next[i] == null) n.next[i] = RootNode()
   n = n.next[i]!!
  }
  n.terminal = true
 }
 if (sentence.isEmpty()) return ""
 return sentence.split(" ").joinToString(" ") { word ->
  var n = root
  var replacement = word
  for (i in word.indices) {
   n = n.next[word[i] - 'a'] ?: break
   if (n.terminal) { replacement = word.substring(0, i + 1); break }
  }
  replacement
 }
}
```

O(S+T) time for S total root characters and T sentence characters, including validation and output assembly. O(S+T) trie/token/output space. The fixed regular expressions validate simple character structure.

- roots=[], sentence="hello" ⇒ "hello"
- roots=[a,ab], sentence="abc" ⇒ "a"
- empty sentence ⇒ empty string

Twist: If the longest root is required, continue past terminal nodes while recording the latest terminal position. Stopping at the first terminal would then be incorrect.

Practice source: https://leetcode.com/problems/replace-words/

### Dictionary lookup with single-character wildcards (Medium)

Implement addWord for lowercase a-z strings (empty allowed) and search for lowercase a-z plus dot, where dot matches exactly one arbitrary letter. Reject all other symbols. Search returns whether any inserted whole word matches; duplicate additions are harmless.

Insert bad,dad,mad: search(.ad)=true, search(b..)=true, search(..)=false.

Baseline: Store all words and scan each equal-length candidate against the pattern: O(DL) per query for D words and pattern length L.

1. Literal pattern characters follow one edge. A dot branches across existing children, which share the same consumed pattern length plus one. The helper state is node and index.
2. When index equals pattern length, return terminal. Reaching a prefix node alone is insufficient, and a dot cannot consume zero or multiple characters.
3. For a dot, stop as soon as any child succeeds; otherwise return false after trying all children. Validate the full query before searching so invalid later characters are rejected even if an early missing edge would otherwise return false.

Correctness: A helper call searches exactly the suffix pattern[index..] among word suffixes rooted at the current prefix node.

```java
static class WordDictionary {
 private static class TNode { TNode[] next=new TNode[26]; boolean terminal; }
 private final TNode root=new TNode();
 void addWord(String word) {
  Objects.requireNonNull(word);
  for(char c:word.toCharArray()) if(c<'a'||c>'z') throw new IllegalArgumentException("invalid word");
  TNode n=root;
  for(char c:word.toCharArray()) { int i=c-'a'; if(n.next[i]==null)n.next[i]=new TNode(); n=n.next[i]; }
  n.terminal=true;
 }
 boolean search(String pattern) {
  Objects.requireNonNull(pattern);
  for(char c:pattern.toCharArray()) if(c!='.'&&(c<'a'||c>'z')) throw new IllegalArgumentException("invalid pattern");
  return match(root,pattern,0);
 }
 private boolean match(TNode n,String p,int index) {
  if(index==p.length()) return n.terminal;
  char c=p.charAt(index);
  if(c!='.') { TNode child=n.next[c-'a']; return child!=null&&match(child,p,index+1); }
  for(TNode child:n.next) if(child!=null&&match(child,p,index+1)) return true;
  return false;
 }
}
```

```kotlin
class WordDictionary {
 private class TNode { val next = arrayOfNulls<TNode>(26); var terminal = false }
 private val root = TNode()
 fun addWord(word: String) {
  require(word.all { it in 'a'..'z' })
  var n = root
  for (c in word) {
   val i = c - 'a'
   if (n.next[i] == null) n.next[i] = TNode()
   n = n.next[i]!!
  }
  n.terminal = true
 }
 fun search(pattern: String): Boolean {
  require(pattern.all { it == '.' || it in 'a'..'z' })
  fun match(n: TNode, index: Int): Boolean {
   if (index == pattern.length) return n.terminal
   val c = pattern[index]
   if (c != '.') return n.next[c - 'a']?.let { match(it, index + 1) } ?: false
   for (child in n.next) if (child != null && match(child, index + 1)) return true
   return false
  }
  return match(root, 0)
 }
}
```

Insertion O(L). Literal-only search O(L). Wildcard search visits V compatible trie nodes with O(26V+L) work, bounded by trie size times the fixed alphabet factor and by the exponential pattern branching. O(L) recursive stack. Storage O(S) nodes for total inserted length S.

- empty dictionary search(.) ⇒ false
- addWord(""); search("") ⇒ true
- addWord(ab); search(a.) ⇒ true; search(a) ⇒ false

Twist: A star that matches zero or more characters changes the transition: either advance pattern without consuming or consume a trie edge without advancing the star. Memoize (node,index) to avoid repeated subproblems.

Practice source: https://leetcode.com/problems/design-add-and-search-words-data-structure/

### Find many dictionary words on a board (Hard)

Given a rectangular non-null board of lowercase a-z characters and non-null nonempty lowercase a-z dictionary words, return each dictionary word found by horizontal/vertical adjacent cells at most once. A cell cannot be reused within one word. Duplicate words are deduplicated. Empty board or zero columns returns empty. Reject malformed words/board; leave board unchanged.

board=[[o,a],[e,t]], words=[oat,oa,eat,oat] returns {oa,oat}; eat cannot follow adjacent e→a.

Baseline: Run independent DFS for each word: O(D·RC·3^L) worst-case time, repeating shared prefixes for D words.

1. Build one trie and store each full word at its terminal node. Start DFS from every cell, following a board letter only if the trie has that child. This combines prefix work across the dictionary.
2. After matching a cell, mark it on the active path. If the node has a terminal word, emit it and clear that field so other board routes do not duplicate the output. Continue searching because the same node may prefix a longer word.
3. Explore four unvisited neighbors and unmark the cell before return. Clearing terminal words is global answer deduplication; visited markers are local path constraints. They must not be confused.

Correctness: The active board route spells exactly the current trie prefix, contains no repeated cell, and every emitted word is removed only from the output eligibility marker, preserving longer descendants.

```java
static class BoardNode { BoardNode[] next=new BoardNode[26]; String word; }
static List<String> boardWords(char[][] board,String[] words) {
 BoardNode root=new BoardNode();
 for(String word:words) {
  if(word==null||!word.matches("[a-z]+")) throw new IllegalArgumentException("invalid word");
  BoardNode n=root;
  for(char c:word.toCharArray()) { int i=c-'a'; if(n.next[i]==null)n.next[i]=new BoardNode(); n=n.next[i]; }
  n.word=word;
 }
 int rows=board.length, cols=rows==0?0:board[0].length;
 for(char[] row:board) {
  if(row.length!=cols) throw new IllegalArgumentException("ragged board");
  for(char c:row) if(c<'a'||c>'z') throw new IllegalArgumentException("invalid board");
 }
 List<String> out=new ArrayList<>(); boolean[][] used=new boolean[rows][cols];
 for(int r=0;r<rows;r++) for(int c=0;c<cols;c++) boardDfs(board,r,c,root,used,out);
 return out;
}
static void boardDfs(char[][] b,int r,int c,BoardNode parent,boolean[][] used,List<String> out) {
 if(r<0||r>=b.length||c<0||c>=b[0].length||used[r][c]) return;
 BoardNode n=parent.next[b[r][c]-'a']; if(n==null) return;
 if(n.word!=null) { out.add(n.word); n.word=null; }
 used[r][c]=true;
 boardDfs(b,r-1,c,n,used,out); boardDfs(b,r+1,c,n,used,out);
 boardDfs(b,r,c-1,n,used,out); boardDfs(b,r,c+1,n,used,out);
 used[r][c]=false;
}
```

```kotlin
class BoardNode { val next = arrayOfNulls<BoardNode>(26); var word: String? = null }
fun boardWords(board: Array<CharArray>, words: Array<String>): List<String> {
 val root = BoardNode()
 for (word in words) {
  require(word.isNotEmpty() && word.all { it in 'a'..'z' })
  var n = root
  for (c in word) {
   val i = c - 'a'
   if (n.next[i] == null) n.next[i] = BoardNode()
   n = n.next[i]!!
  }
  n.word = word
 }
 val rows = board.size
 val cols = if (rows == 0) 0 else board[0].size
 require(board.all { row -> row.size == cols && row.all { it in 'a'..'z' } })
 val used = Array(rows) { BooleanArray(cols) }
 val out = mutableListOf<String>()
 fun dfs(r: Int, c: Int, parent: BoardNode) {
  if (r !in 0 until rows || c !in 0 until cols || used[r][c]) return
  val n = parent.next[board[r][c] - 'a'] ?: return
  n.word?.let { out.add(it); n.word = null }
  used[r][c] = true
  dfs(r - 1, c, n); dfs(r + 1, c, n); dfs(r, c - 1, n); dfs(r, c + 1, n)
  used[r][c] = false
 }
 for (r in 0 until rows) for (c in 0 until cols) dfs(r, c, root)
 return out
}
```

Build O(S) for S dictionary characters; search O(RC·4·3^(min(L,RC)-1)) conventional worst-case upper bound for nonempty board and max word length L, with substantial prefix pruning in practice. Add O(RC+S) validation. O(S+RC+min(L,RC)) auxiliary trie/visited/stack, excluding output references. Empty dictionary yields O(RC) search.

- empty board ⇒ []
- board=[[a]], words=[a,a,aa] ⇒ [a]
- board=[[a,b]], words=[aba] ⇒ [] because reuse is forbidden

Twist: If words may use diagonal neighbors, change branching to eight directions and update the worst-case bound accordingly. If counting routes instead of unique words, terminal clearing is invalid and counts may need Long or modular arithmetic.

Practice source: https://leetcode.com/problems/word-search-ii/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 1140 180" role="img" aria-label="A shared prefix can represent a word and still have longer descendants."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">c</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">a</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">r</text><path d="M745,72 Q765,72 785,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="765" y="46" text-anchor="middle" font-size="12">t</text><path d="M555,72 Q765,72 975,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="765" y="46" text-anchor="middle" font-size="12">t</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">root: empty prefix</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">c</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">ca</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">car: terminal</div></foreignObject><rect x="785" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="790" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">cart: terminal</div></foreignObject><rect x="975" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="980" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">cat: terminal</div></foreignObject></svg></div><figcaption>A shared prefix can represent a word and still have longer descendants.</figcaption></figure>

## Additional practice

### [Longest Word in Dictionary](https://leetcode.com/problems/longest-word-in-dictionary/) (Medium)

Hint: Only descend through terminal prefixes.

Approach: Insert all words, then DFS children whose nodes are terminal; update longest and lexicographic tie. O(S) trie work/O(S) storage; define alphabetical order.

### [Search Suggestions System](https://leetcode.com/problems/search-suggestions-system/) (Medium)

Hint: Each prefix needs its first three lexicographic completions.

Approach: Sort products then store up to three suggestions at each traversed prefix, or DFS ordered children at query time. O(S+sorting) preprocessing with fixed top-3 storage per node; output O(prefixLength).

### [Map Sum Pairs](https://leetcode.com/problems/map-sum-pairs/) (Medium)

Hint: Prefix aggregation needs a delta on replacement.

Approach: Store prior exact key values; insertion applies new-old delta to each prefix aggregate. O(L) insert/query, O(S) trie+key map; use Long for aggregate sums.

### [Maximum XOR of Two Numbers in an Array](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) (Medium)

Hint: A trie can index bits rather than letters.

Approach: For nonnegative Int values use bits 30..0; greedily choose opposite bit when available to maximize the highest undecided XOR bit. O(31n) time/O(31n) nodes; signed values require an explicit objective.

### [Concatenated Words](https://leetcode.com/problems/concatenated-words/) (Hard)

Hint: Trie transitions find candidate pieces; DP records reachable cut positions.

Approach: For each word test segmentation into at least two shorter dictionary words, excluding whole-word self-use. O(sum L²) typical character-traversal bound/O(S) trie plus per-word DP; avoid exponential raw recursion.

### [Palindrome Pairs](https://leetcode.com/problems/palindrome-pairs/) (Hard)

Hint: Reversed trie lookup can combine suffix matches with palindrome leftovers.

Approach: Insert reversed words, record palindromic remaining prefixes, then scan forward query words with terminal/palindrome checks. O(sum L²+output) with direct palindrome tests; duplicate/empty word indices require careful handling.

## Check your reasoning

After inserting car and cart, what must be true at the car node?

1. It is terminal and may have a child.
2. It cannot be terminal because it has a child.
3. Its child must be removed.

Answer: 1. Whole-word membership and ability to continue are independent properties.

Why does a wildcard query not always take O(L) time?

1. It must sort words first.
2. A dot can branch into many compatible prefixes.
3. Tries cannot store empty words.

Answer: 2. Literal lookup has one path; wildcards can explore a branching subtree, bounded by stored compatible nodes.

## Transfer
A router receives URL paths split into segments. Rules contain literal segments or a wildcard matching exactly one segment. Return whether any complete rule matches a request path; a rule ending early must not match longer paths. Design the index and search.

Treat whole segments as symbols rather than individual characters.

A terminal marker still distinguishes a complete rule from a prefix.

Build a trie with a map of literal segment children and a separate wildcard child. Query state is node and request-segment index; try the matching literal and wildcard edge, and accept only at the final index on a terminal node. O(V) visited compatible nodes and O(depth) stack, potentially branching per segment. Normalize/parse URLs before indexing under an explicit policy; prefix sharing and terminal logic are unchanged by the new alphabet.

## Recall
- Explain terminal versus prefix using app and apple.
- Derive wildcard state and its actual visited-node runtime.
- Explain why found-word clearing is global but board visited flags are path-local.

## Sources
- [LeetCode trie catalogue](https://leetcode.com/tag/trie/): Reference link; not retrieved in this run. Primary public problem contracts and practice taxonomy.
- [Word Search II](https://leetcode.com/problems/word-search-ii/): Reference link; not retrieved in this run. Canonical multiword board search; lowercase symbol and uniqueness contracts are made explicit here.


# Pattern 13: Graphs: reachability and shortest unweighted paths

Treat cells, words, or objects as states joined by legal moves. Explore each state deliberately instead of repeating the same search from every starting point. These worked problems start at Medium because constructing graph state from grids and objects adds complexity; begin with the one-node examples before increasing size.

## Build the model
A graph is a collection of states and connections. A state is called a vertex; a connection is called an edge. A road network is an obvious graph, but a grid is also a graph when a cell is a vertex and legal adjacent moves are edges. In a word puzzle, each allowed word can be a vertex and a one-letter transformation can be an edge. State modeling is the first decision: what facts must be known to determine the next legal moves? A position is sufficient for an ordinary maze; a position plus held keys may be necessary in a locked maze.

Start from the most basic question: can I get from one vertex to another? Keep a frontier of discovered vertices whose outgoing edges are not fully explored. Remove one, examine its neighbors, and add previously undiscovered neighbors. Mark a state when adding it, not when removing it. This ensures that many parents cannot enqueue the same state repeatedly before its first processing. A visited set is a semantic commitment: all later histories arriving at the same state are redundant for the answer being computed. If time, keys, remaining budget, or visited obligations affect future moves, position alone is not a complete state and such a commitment can discard valid solutions.

A stack gives depth-first search: continue down one branch, returning when it ends. A queue gives breadth-first search: finish the oldest discovered work first. Both discover reachable states. Only the FIFO queue gives the shortest number of edges in an unweighted graph without additional machinery. The reason is not that a queue somehow guesses the right route: every distance-d vertex is processed before any distance-(d+1) vertex. When a new neighbor is discovered from distance d, a shorter route would have had to come from an earlier layer and would already have discovered it.

Connected components arise by restarting a reachability traversal at each still-unvisited vertex. Each restart identifies one entire component. On a land/water grid, scanning all land cells and traversing from each unseen one counts islands. Marking does not create an island; it records that the island has already been counted.

Multi-source BFS places every initial source into the queue before expansion. This is equivalent, for distances, to connecting a virtual starting point to every source at the same cost. Each cell receives its distance from the nearest source, not the source encountered first in a separate sequential simulation. That distinction models simultaneous spreading correctly.

When cloning a graph, visited state becomes an original-to-copy map. Allocate and record a copy before exploring neighbors so cycles can refer back to an already allocated object. Values are not identities: two vertices with the same label can still be different vertices.

Count work on the modeled graph. With adjacency lists, each vertex is discovered once and each edge is inspected once per relevant endpoint, giving O(V+E). Grid degree is bounded, so traversal is linear in cells. If neighbor generation itself constructs many candidate words or copies strings, include that cost instead of writing O(V+E) without explaining what an edge costs.

## Invariant
Every frontier state has already been marked discovered and is reachable from the starting set. Every processed state's legal neighbors have been discovered. At termination no reachable state can remain undiscovered: on a hypothetical path to one, the first undiscovered state would follow a processed reachable predecessor. For BFS, frontier distances are nondecreasing, and first discovery is a shortest distance because all shorter predecessor layers were processed earlier. This proof assumes equal edge costs. For cloning, the map additionally preserves one distinct copy per discovered original; recording before expansion handles cycles and self-loops.

## Recognize it
- An answer depends on what can be reached through local moves: define vertices and edges, even if the input never says graph.
- You need the minimum number of equal-cost moves: FIFO breadth-first search explores distance layers.
- Several sources spread simultaneously: initialize one BFS queue with all sources at distance zero.
- Objects reference one another and can form cycles: store visited identity or an original-to-copy map before following outgoing references.

## Execution trace
1. Queue=[S]; distances={S:0}

The source is marked immediately.

2. Pop S; queue=[A,B]; distances A=B=1

Every one-edge neighbor is now discovered.

3. Pop A; queue=[B,C]; distance C=2

Distance-1 B remains ahead of new distance-2 work.

4. Pop B; queue=[C,T]; C already seen, distance T=2

A second route to C does not enqueue it again.

5. Pop C then T; queue=[]

All reachable vertices are processed; each first distance is minimal.

## Wrong turns
### Use DFS and return the first path as the shortest path.
Edges S-A, A-B, B-T and S-T. DFS taking A first finds length 3 although direct S-T has length 1.

Use BFS for equal-cost edges; use weighted shortest-path methods when costs differ.

### Mark visited only when a node is removed from the queue.
S connects to A and B; both connect to C. A and B can enqueue C before C is processed, repeating its work. A layered graph compounds the duplication.

Mark at enqueue time. The first BFS discovery already has a shortest distance.

### Clone graph nodes by label instead of identity.
Two distinct adjacent objects both labeled 7 collapse into one self-loop if a map keyed only by label is used.

Map original object identity to its one copy.

### Count islands (Medium)

Given a nonnull rectangular Int grid containing only 0 (water) and 1 (land), count components under four-direction adjacency. Empty grids or zero columns return 0. Do not mutate input. Rows are nonnull; rectangular shape and binary values are preconditions.

[[1,1,0],[0,1,0],[1,0,1]] => 3. Diagonal contact does not connect land.

Baseline: For each land cell, independently rediscover its whole component and deduplicate component sets: O((R*C)²) time in the worst case and O(R*C) space.

1. Scan every cell. An already seen land cell belongs to an island counted earlier.
2. At each unseen land cell, increment the answer and BFS its entire component.
3. Mark neighbors at enqueue time so every land cell enters one traversal exactly once.

Correctness: After each scan position, every seen land cell belongs to an island already counted, and each counted island is fully marked. No unseen island is counted twice.

```java
static int numIslands(int[][] grid) {
    Objects.requireNonNull(grid, "grid");
    int rows = grid.length;
    if (rows == 0) return 0;
    int cols = grid[0].length, answer = 0;
    boolean[][] seen = new boolean[rows][cols];
    int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};
    ArrayDeque<int[]> queue = new ArrayDeque<>();
    for (int r = 0; r < rows; r++) for (int c = 0; c < cols; c++) {
        if (grid[r][c] != 1 || seen[r][c]) continue;
        answer++;
        seen[r][c] = true;
        queue.add(new int[]{r, c});
        while (!queue.isEmpty()) {
            int[] cell = queue.remove();
            for (int d = 0; d < 4; d++) {
                int nr = cell[0] + dr[d], nc = cell[1] + dc[d];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1 && !seen[nr][nc]) {
                    seen[nr][nc] = true;
                    queue.add(new int[]{nr, nc});
                }
            }
        }
    }
    return answer;
}
```

```kotlin
fun numIslands(grid: Array<IntArray>): Int {
    val rows = grid.size
    if (rows == 0) return 0
    val cols = grid[0].size
    val seen = Array(rows) { BooleanArray(cols) }
    val dr = intArrayOf(-1, 1, 0, 0)
    val dc = intArrayOf(0, 0, -1, 1)
    val queue = ArrayDeque<Pair<Int, Int>>()
    var answer = 0
    for (r in 0 until rows) for (c in 0 until cols) {
        if (grid[r][c] != 1 || seen[r][c]) continue
        answer++
        seen[r][c] = true
        queue.add(r to c)
        while (queue.isNotEmpty()) {
            val (cr, cc) = queue.remove()
            for (d in 0 until 4) {
                val nr = cr + dr[d]
                val nc = cc + dc[d]
                if (nr in 0 until rows && nc in 0 until cols && grid[nr][nc] == 1 && !seen[nr][nc]) {
                    seen[nr][nc] = true
                    queue.add(nr to nc)
                }
            }
        }
    }
    return answer
}
```

O(R*C) time and auxiliary space for R rows and C columns, plus O(1) output. Iterative BFS avoids recursion-depth failure. Java null throws; Kotlin uses nonnull input.

- [] => 0
- [[0]] => 0
- [[1]] => 1
- [[1,0],[0,1]] => 2

Twist: For eight-direction adjacency change the neighbor list; the component proof is unchanged. For a streaming sequence of land additions, maintain components with union-find instead of retraversing the grid.

Practice source: https://leetcode.com/problems/number-of-islands/

### Clone a graph with cycles (Medium)

Given a nullable root of a graph whose Node has an integer value and an ordered mutable list of nonnull neighbor references, return a deep copy of every reachable node. Preserve neighbor order, duplicate edges and self-loops. Distinct nodes may share values. Null returns null. Do not mutate originals.

A(value=7) -> B(value=7), B -> A must produce distinct copies A' and B' joined cyclically, with no edge into the originals.

Baseline: Recursively copy each reference without remembering prior copies: does not terminate on a cycle and duplicates shared nodes even in a DAG.

1. Allocate a root copy and immediately map original root to it.
2. BFS originals. For each neighbor, allocate and enqueue a copy only if identity is absent from the map.
3. Append the mapped neighbor copy to the current copy in original neighbor order.

Correctness: The identity map contains exactly one new copy per discovered original. All edges of processed originals are copied to mapped objects, preserving order and multiplicity.

```java
static final class Node {
    final int value;
    final List<Node> neighbors = new ArrayList<>();
    Node(int value) { this.value = value; }
}
static Node cloneGraph(Node root) {
    if (root == null) return null;
    IdentityHashMap<Node, Node> copies = new IdentityHashMap<>();
    ArrayDeque<Node> queue = new ArrayDeque<>();
    copies.put(root, new Node(root.value));
    queue.add(root);
    while (!queue.isEmpty()) {
        Node current = queue.remove();
        for (Node neighbor : current.neighbors) {
            if (!copies.containsKey(neighbor)) {
                copies.put(neighbor, new Node(neighbor.value));
                queue.add(neighbor);
            }
            copies.get(current).neighbors.add(copies.get(neighbor));
        }
    }
    return copies.get(root);
}
```

```kotlin
class Node(val value: Int) {
    val neighbors: MutableList<Node> = mutableListOf()
}
fun cloneGraph(root: Node?): Node? {
    if (root == null) return null
    val copies = IdentityHashMap<Node, Node>()
    val queue = ArrayDeque<Node>()
    copies[root] = Node(root.value)
    queue.add(root)
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        for (neighbor in current.neighbors) {
            if (!copies.containsKey(neighbor)) {
                copies[neighbor] = Node(neighbor.value)
                queue.add(neighbor)
            }
            copies.getValue(current).neighbors.add(copies.getValue(neighbor))
        }
    }
    return copies[root]
}
```

Expected O(V+E) time for reachable vertices and neighbor-list entries, O(V) auxiliary map/queue space, and O(V+E) output space. Identity hashing assumptions apply.

- null => null
- one node without neighbors => distinct isolated copy
- self-loop => copied node points to itself

Twist: If Node stores additional mutable payload objects, copying only graph structure is no longer a full payload deep copy. Define which nested objects must also be cloned, and memoize shared payload identity when necessary.

Practice source: https://leetcode.com/problems/clone-graph/

### Simultaneous rotting spread (Medium)

Given a nonnull rectangular grid of 0 (empty), 1 (fresh), and 2 (rotten), each minute all rotten cells infect adjacent fresh cells in four directions. Return the fewest minutes until none remain fresh, or -1 if impossible. Empty/no-fresh grids return 0. Do not mutate input. Shape and values are preconditions.

[[2,1,1],[1,1,0],[0,1,1]] => 4. Spread from every initial rotten cell begins at minute 0.

Baseline: Repeatedly scan all cells to find the next minute's fresh infections, applying changes simultaneously on a copy: O((R*C)²) worst-case time and O(R*C) space.

1. Enqueue all rotten sources with time 0; count fresh cells.
2. BFS unvisited fresh neighbors, assigning parent time + 1 and decrementing fresh exactly once.
3. Track the largest assigned time. If fresh remains after traversal, some cells are unreachable.

Correctness: A discovered fresh cell is assigned its shortest distance from any initial rotten source. Remaining fresh counts precisely the not-yet-discovered fresh cells.

```java
static int orangesRotting(int[][] grid) {
    Objects.requireNonNull(grid, "grid");
    int rows = grid.length;
    if (rows == 0) return 0;
    int cols = grid[0].length, fresh = 0, minutes = 0;
    int[][] distance = new int[rows][cols];
    ArrayDeque<int[]> queue = new ArrayDeque<>();
    for (int r = 0; r < rows; r++) {
        Arrays.fill(distance[r], -1);
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 1) fresh++;
            if (grid[r][c] == 2) { distance[r][c] = 0; queue.add(new int[]{r,c}); }
        }
    }
    int[] dr = {-1,1,0,0}, dc = {0,0,-1,1};
    while (!queue.isEmpty()) {
        int[] cell = queue.remove();
        for (int d = 0; d < 4; d++) {
            int r = cell[0] + dr[d], c = cell[1] + dc[d];
            if (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] == 1 && distance[r][c] == -1) {
                distance[r][c] = distance[cell[0]][cell[1]] + 1;
                minutes = Math.max(minutes, distance[r][c]);
                fresh--;
                queue.add(new int[]{r,c});
            }
        }
    }
    return fresh == 0 ? minutes : -1;
}
```

```kotlin
fun orangesRotting(grid: Array<IntArray>): Int {
    val rows = grid.size
    if (rows == 0) return 0
    val cols = grid[0].size
    val distance = Array(rows) { IntArray(cols) { -1 } }
    val queue = ArrayDeque<Pair<Int, Int>>()
    var fresh = 0
    var minutes = 0
    for (r in 0 until rows) for (c in 0 until cols) {
        if (grid[r][c] == 1) fresh++
        if (grid[r][c] == 2) { distance[r][c] = 0; queue.add(r to c) }
    }
    val dr = intArrayOf(-1,1,0,0)
    val dc = intArrayOf(0,0,-1,1)
    while (queue.isNotEmpty()) {
        val (cr, cc) = queue.remove()
        for (d in 0 until 4) {
            val r = cr + dr[d]
            val c = cc + dc[d]
            if (r in 0 until rows && c in 0 until cols && grid[r][c] == 1 && distance[r][c] == -1) {
                distance[r][c] = distance[cr][cc] + 1
                minutes = maxOf(minutes, distance[r][c])
                fresh--
                queue.add(r to c)
            }
        }
    }
    return if (fresh == 0) minutes else -1
}
```

O(R*C) time and auxiliary space, O(1) output. Time labels assume a feasible in-memory grid whose number of cells fits Int.

- [] => 0
- [[1]] => -1
- [[2]] => 0
- [[2,1,1,2]] => 1

Twist: If infection times differ by edge, FIFO layers no longer model time. Use a priority queue and Dijkstra relaxation from all sources when all transmission costs are nonnegative.

Practice source: https://leetcode.com/problems/rotting-oranges/

### Shortest word transformation (Hard)

Given nonempty same-length begin and end strings, and a nonnull list of same-length words, all containing only lowercase English a-z, return the number of words in a shortest transformation chain. Each step changes exactly one letter; every word after begin must be in the list. Return 0 if no chain exists. begin=end returns 1 even if absent from the list. Do not mutate the caller's list. Alphabet and equal lengths are explicit preconditions.

hit -> cog using [hot,dot,dog,lot,log,cog] => 5: hit,hot,dot,dog,cog.

Baseline: Build adjacency by comparing every pair of dictionary words (O(N²L)), then BFS; graph storage can be O(N²).

1. Treat words as vertices. A one-letter substitution is an equal-cost edge.
2. Generate neighbors on demand by replacing each position with each of 26 letters. Only dictionary members are valid next states.
3. Remove a word from a copied remaining set when enqueuing it so it is visited once.
4. Process queue layers and count words, starting at 1 for begin. Return upon discovering end.

Correctness: Every queued word is reachable with its layer's number of words, and no shorter chain can be discovered later. Remaining contains exactly dictionary words not yet discovered.

```java
static int ladderLength(String begin, String end, List<String> words) {
    Objects.requireNonNull(begin, "begin"); Objects.requireNonNull(end, "end");
    Objects.requireNonNull(words, "words");
    if (begin.equals(end)) return 1;
    Set<String> remaining = new HashSet<>(words);
    if (!remaining.contains(end)) return 0;
    remaining.remove(begin);
    ArrayDeque<String> queue = new ArrayDeque<>(); queue.add(begin);
    int length = 1;
    while (!queue.isEmpty()) {
        int layer = queue.size();
        for (int k = 0; k < layer; k++) {
            char[] chars = queue.remove().toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char saved = chars[i];
                for (char ch = 'a'; ch <= 'z'; ch++) {
                    if (ch == saved) continue;
                    chars[i] = ch;
                    String next = new String(chars);
                    if (remaining.remove(next)) {
                        if (next.equals(end)) return length + 1;
                        queue.add(next);
                    }
                }
                chars[i] = saved;
            }
        }
        length++;
    }
    return 0;
}
```

```kotlin
fun ladderLength(begin: String, end: String, words: List<String>): Int {
    if (begin == end) return 1
    val remaining = words.toHashSet()
    if (end !in remaining) return 0
    remaining.remove(begin)
    val queue = ArrayDeque<String>()
    queue.add(begin)
    var length = 1
    while (queue.isNotEmpty()) {
        val layer = queue.size
        repeat(layer) {
            val chars = queue.remove().toCharArray()
            for (i in chars.indices) {
                val saved = chars[i]
                for (ch in 'a'..'z') {
                    if (ch == saved) continue
                    chars[i] = ch
                    val next = String(chars)
                    if (remaining.remove(next)) {
                        if (next == end) return length + 1
                        queue.add(next)
                    }
                }
                chars[i] = saved
            }
        }
        length++
    }
    return 0
}
```

For N dictionary words of length L, expected O(N*26*L² + N*L) time including O(L) String creation/hash per candidate, and O(N*L) auxiliary storage including queued generated strings. Extra transient characters per word use O(L) space. Hash membership is expected-time.

- begin=a, end=a, words=[] => 1
- begin=a, end=c, words=[c] => 2
- hit,cog,[hot,dot,dog] => 0

Twist: For large search frontiers use bidirectional BFS: maintain two complete frontier layers and expand the smaller, detecting an edge into the opposite frontier. Track each side's distances carefully so the sum counts edges, then add 1 for words. Worst-case bounds need not improve, though practical exploration often shrinks. To return all shortest chains, retain all predecessor edges that respect shortest-distance layers and enumerate afterward.

Practice source: https://leetcode.com/problems/word-ladder/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 950 180" role="img" aria-label="FIFO BFS processes one complete distance layer before the next."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">discover</text><path d="M175,72 Q290,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="290" y="46" text-anchor="middle" font-size="12">discover</text><path d="M365,72 Q480,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="480" y="46" text-anchor="middle" font-size="12">discover once</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">already seen</text><path d="M555,72 Q670,72 785,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="670" y="46" text-anchor="middle" font-size="12">first route is shortest</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">S distance 0</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">A distance 1</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">B distance 1</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">C distance 2</div></foreignObject><rect x="785" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="790" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">T distance 2</div></foreignObject></svg></div><figcaption>FIFO BFS processes one complete distance layer before the next.</figcaption></figure>

## Additional practice

### [Find if Path Exists in Graph](https://leetcode.com/problems/find-if-path-exists-in-graph/) (Easy)

Hint: Start with ordinary reachability and a complete vertex state.

Approach: Build undirected adjacency lists; BFS/DFS from source, marking at discovery. O(V+E) time/space. source=destination is reachable without any edge.

### [Flood Fill](https://leetcode.com/problems/flood-fill/) (Easy)

Hint: A component is constrained by its original color.

Approach: Traverse neighbors matching the original color and recolor. O(R*C) worst case. If new color equals old color, return immediately or use visited to avoid rediscovering cells.

### [Max Area of Island](https://leetcode.com/problems/max-area-of-island/) (Medium)

Hint: Count nodes in each discovered component.

Approach: Run one traversal per unseen land cell, accumulate each component area and take the maximum. O(R*C) time/space; do not combine disconnected islands.

### [01 Matrix](https://leetcode.com/problems/01-matrix/) (Medium)

Hint: Distances are to the nearest zero; reverse the sources.

Approach: Initialize every zero at distance 0 and multi-source BFS over all cells. O(R*C) time/space. Define behavior for no-zero inputs explicitly if expanding the original contract.

### [Pacific Atlantic Water Flow](https://leetcode.com/problems/pacific-atlantic-water-flow/) (Medium)

Hint: Reverse directed reachability from the destination borders.

Approach: From each ocean border, traverse to equal or higher neighbors, then intersect the visited sets. O(R*C) time/space; reversing the inequality is essential.

### [Open the Lock](https://leetcode.com/problems/open-the-lock/) (Medium)

Hint: A lock combination is a vertex with eight neighbors.

Approach: BFS from 0000 excluding dead states; generate one-wheel ±1 moves with wraparound. O(S*D) transitions for S reachable states and D digits, plus state-string costs. Reject a dead starting state.

### [Shortest Path in a Grid with Obstacles Elimination](https://leetcode.com/problems/shortest-path-in-a-grid-with-obstacles-elimination/) (Hard)

Hint: Position plus remaining eliminations determines future moves.

Approach: BFS (r,c,kRemaining), marking full states; O(R*C*K) time/space. A stronger remaining-budget state at equal or smaller distance can dominate a weaker one, but position alone cannot.

## Check your reasoning

A move costs either 1 or 7. Does ordinary FIFO BFS minimize total cost?

1. Yes, because every node is marked once.
2. No; its proof minimizes edges, so use an algorithm supporting weights.
3. Yes, if you sort adjacency lists.

Answer: 2. Fewest edges and smallest weight can differ. Dijkstra handles nonnegative weights.

Two paths reach the same cell, but one holds a key. Can visited use only the cell?

1. Always.
2. Only if keys cannot affect any future legal move or objective.
3. Never, even in an ordinary open grid.

Answer: 2. Equivalent states must have equivalent future options. With doors, include the key set in the state.

## Transfer
Several charging stations are placed in an unweighted warehouse grid with blocked cells. For every open cell, return the minimum walking steps to any station, or -1 if unreachable.

Running one independent search per cell repeats almost all work.

Reverse the perspective: start every station at distance zero.

Initialize all station cells as discovered with distance 0 in one queue. BFS through legal open neighbors and assign distance parent+1 on first discovery. Queue layering proves minimum distance from the whole starting set. O(R*C) time and space; absent stations leave all distances -1.

## Recall
- Model a word transformation graph without drawing every possible edge.
- Explain why marking on enqueue prevents duplicate frontier work.
- Derive the simultaneous-spread algorithm from nearest-source distance.
- Show a cyclic two-node graph and the moment each clone enters the map.
- Give a maze where position alone is insufficient state.

## Sources
- [Princeton graph algorithms](https://algs4.cs.princeton.edu/40graphs/): Primary algorithm reference link; external content not retrieved during this pass. Practice links identify problems, not company frequency.


# Pattern 14: Dependency order and dynamic connectivity

Two common graph questions require different information: directed prerequisites ask which work is ready next, while undirected connectivity asks which vertices already share a component. Learn both representations and the boundary between them. These problems start at Medium because they assume the graph basics in lesson 13.

## Build the model
A graph question can ask for an ordering or for connectivity; those are different objectives. If task B needs task A, draw A -> B. The arrow describes a prerequisite, not merely an undirected connection. A valid execution list must place A before B for every arrow. Such a list is called a topological ordering. Many orderings can be valid; requiring one particular numerical order is an extra constraint.

Ask the simplest scheduling question: which task could run now? A task with no unfinished prerequisites is ready. Store for every task its indegree, the number of incoming prerequisite edges not yet removed. Put all zero-indegree tasks in a queue. Remove a ready task, append it to the result, and decrement indegrees of its dependents. When one reaches zero, every prerequisite is finished, so it becomes ready. This is Kahn's algorithm. It repeatedly removes sources from the remaining directed graph.

Why does this detect cycles? In a nonempty directed acyclic graph there is a source: otherwise repeatedly following an incoming edge in a finite graph would eventually revisit a vertex and form a cycle. Conversely, a directed cycle cannot lose its final incoming cycle edge through source removal. If the queue empties while vertices remain, the remaining graph contains a cycle. Some leftover vertices may only depend on that cycle; do not claim every leftover vertex belongs to one. A processed-count check is enough for feasibility, while a saved output list supplies an ordering.

Now change the question. Suppose undirected edges are added over time and you ask whether u and v are connected. The exact route is irrelevant. Partition vertices into sets, one for each connected component. A disjoint-set union structure, also called DSU or union-find, represents each set with a rooted parent tree. find(x) follows parents to the representative root. Two vertices are connected exactly when their roots are equal. union(a,b) links two different roots. Linking by component size keeps trees shallow; path compression rewires visited parent links toward the root without changing membership.

This parent tree is an implementation device, not the original graph. Its links need not correspond to input edges, so it cannot reconstruct a graph route. Its root is an arbitrary representative that may change after merging. Store component-wide metadata such as size at roots, and always query through find before reading it. The amortized cost with union by size and compression is inverse Ackermann, effectively tiny but not literally a strict O(1) worst-case guarantee per operation.

An undirected edge whose endpoints are already connected closes a cycle: an old path plus the new edge forms that cycle. That is not a directed-cycle test. In a directed diamond A->B, A->C, B->D, C->D, ignoring directions makes an undirected cycle even though the dependency graph is a valid DAG. Choose DSU only when undirected equivalence of components is the needed state.

Grid merging combines these ideas with modeling. Build components of existing land. For each candidate water cell, gather the roots of its land neighbors, deduplicate them, and add their sizes plus one. Two neighboring cells may already belong to the same island; summing both would invent extra land. Because only one flip is allowed, evaluate each candidate against the unchanged original components rather than permanently merging every candidate.

## Invariant
For Kahn's algorithm, indegree[v] counts precisely the incoming edges from vertices not yet processed. A queued vertex has zero such edges, so appending it never violates an edge to an earlier task. At termination, processing every vertex proves the produced list satisfies all dependencies; a nonempty residue with no source proves a cycle exists. For DSU, vertices have the same representative exactly when the processed undirected edges connect them. Union merges precisely two previously distinct equivalence classes, and compression preserves class membership. Component sizes are valid at representatives only.

## Recognize it
- A must happen before B: orient A -> B and track unfinished prerequisites, rather than sorting labels.
- You need a valid ordering satisfying all directed edges: topological ordering exists exactly for a directed acyclic graph.
- Undirected edges arrive and you only need component membership: disjoint-set union merges groups without storing paths.
- A proposed new link or cell joins several existing components: combine component sizes, deduplicating roots.

## Execution trace
1. Edges 0->1, 0->2, 1->3, 2->3; indegree=[0,1,1,2]; queue=[0]

Only task 0 is initially ready.

2. Process 0: indegree=[0,0,0,2]; queue=[1,2]; output=[0]

Tasks 1 and 2 lose their last unfinished prerequisite.

3. Process 1: indegree[3]=1; queue=[2]; output=[0,1]

Task 3 is still blocked by task 2.

4. Process 2: indegree[3]=0; queue=[3]; output=[0,1,2]

Every prerequisite of 3 is now complete.

5. Process 3: output=[0,1,2,3], processed=4

All vertices processed, so the ordering is valid.

6. DSU edges (1,2), (2,3), proposed (1,3): roots of 1 and 3 already equal

For an undirected graph, the proposed edge creates a cycle.

## Wrong turns
### Use DSU to detect cycles in a directed dependency graph.
A->B, A->C, B->D, C->D is a DAG. Ignoring directions, the final edge joins already-connected endpoints and DSU falsely flags a directed cycle.

Use indegree removal or DFS active-stack colors for directed cycles.

### Sum the component size of every land neighbor when flipping a water cell.
[[1,1],[1,0]]: the three existing land cells belong to the same size-3 component. The bottom-right cell has two land neighbors; counting the same component twice suggests 7 instead of 4.

Collect distinct representative roots, then sum each size once.

### Call every vertex left after Kahn's queue empties a cycle vertex.
A->B, B->A, B->C leaves A, B, and C unprocessed. C is blocked by the cycle but is not itself on one.

Use the leftover count only for feasibility unless a separate cycle/SCC analysis identifies exact cycle membership.

### Can every course be completed? (Medium)

There are n courses labeled 0..n-1, n >= 0. A prerequisite pair [course, prerequisite] requires prerequisite before course. Return whether all courses can be completed. Pairs contain valid labels; duplicate pairs are allowed and counted consistently. Inputs are nonnull and not mutated. n=0 with no pairs returns true.

n=2, pairs=[[1,0]] => true; pairs=[[1,0],[0,1]] => false.

Baseline: For each course separately, explore every prerequisite path while tracking its current path. Repeating path exploration can be exponential without memoization; a proper DFS coloring alternative is O(n+m).

1. Orient each pair prerequisite -> course. Count all incoming edges.
2. Enqueue zero-indegree courses, then remove their outgoing edges when processed.
3. Return true exactly when processed count equals n.

Correctness: A course is enqueued exactly when all its prerequisites have been processed. Removing all n courses certifies feasibility; any nonempty residue contains a directed cycle.

```java
static boolean canFinish(int n, int[][] prerequisites) {
    if (n < 0) throw new IllegalArgumentException("negative n");
    Objects.requireNonNull(prerequisites, "prerequisites");
    List<List<Integer>> out = new ArrayList<>();
    for (int i = 0; i < n; i++) out.add(new ArrayList<>());
    int[] indegree = new int[n];
    for (int[] pair : prerequisites) { out.get(pair[1]).add(pair[0]); indegree[pair[0]]++; }
    ArrayDeque<Integer> queue = new ArrayDeque<>();
    for (int i = 0; i < n; i++) if (indegree[i] == 0) queue.add(i);
    int processed = 0;
    while (!queue.isEmpty()) {
        int current = queue.remove(); processed++;
        for (int next : out.get(current)) if (--indegree[next] == 0) queue.add(next);
    }
    return processed == n;
}
```

```kotlin
fun canFinish(n: Int, prerequisites: Array<IntArray>): Boolean {
    require(n >= 0)
    val out = Array(n) { mutableListOf<Int>() }
    val indegree = IntArray(n)
    for (pair in prerequisites) { out[pair[1]].add(pair[0]); indegree[pair[0]]++ }
    val queue = ArrayDeque<Int>()
    for (i in 0 until n) if (indegree[i] == 0) queue.add(i)
    var processed = 0
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        processed++
        for (next in out[current]) { indegree[next]--; if (indegree[next] == 0) queue.add(next) }
    }
    return processed == n
}
```

O(n+m) time and auxiliary space for n courses and m prerequisite entries, including duplicate edges; O(1) output space. Int indegrees assume in-memory edge counts fit Int.

- n=0, [] => true
- n=1, [[0,0]] => false
- n=2, [[1,0],[1,0]] => true

Twist: If you must identify the actual cycle, Kahn's leftover set is insufficient. Use DFS colors with parent reconstruction or strongly connected components. For task durations, a topological DP can propagate earliest finish times.

Practice source: https://leetcode.com/problems/course-schedule/

### Return a dependency-respecting course order (Medium)

Using the same valid nonnull [course, prerequisite] pairs, return any ordering of all n >= 0 courses respecting every pair. If a cycle prevents completion, return an empty array. For n=0 the valid order is also empty; callers needing to distinguish cases must retain n. No input mutation.

n=4, [[1,0],[2,0],[3,1],[3,2]] => [0,1,2,3] or [0,2,1,3].

Baseline: Enumerate course permutations and test every prerequisite: O(n!*(n+m)) time, O(n) permutation storage.

1. Reuse the ready-course indegree state from feasibility.
2. Append each removed ready course to an output array. Every prerequisite of that course is already earlier.
3. If fewer than n courses were appended, discard the partial result because it is not a complete feasible order.

Correctness: The current output respects every edge between its members, and no unprocessed course has an edge into a processed course that was unmet when appended.

```java
static int[] findOrder(int n, int[][] prerequisites) {
    if (n < 0) throw new IllegalArgumentException("negative n");
    Objects.requireNonNull(prerequisites, "prerequisites");
    List<List<Integer>> out = new ArrayList<>();
    for (int i = 0; i < n; i++) out.add(new ArrayList<>());
    int[] indegree = new int[n], order = new int[n];
    for (int[] pair : prerequisites) { out.get(pair[1]).add(pair[0]); indegree[pair[0]]++; }
    ArrayDeque<Integer> queue = new ArrayDeque<>();
    for (int i = 0; i < n; i++) if (indegree[i] == 0) queue.add(i);
    int count = 0;
    while (!queue.isEmpty()) {
        int current = queue.remove(); order[count++] = current;
        for (int next : out.get(current)) if (--indegree[next] == 0) queue.add(next);
    }
    return count == n ? order : new int[0];
}
```

```kotlin
fun findOrder(n: Int, prerequisites: Array<IntArray>): IntArray {
    require(n >= 0)
    val out = Array(n) { mutableListOf<Int>() }
    val indegree = IntArray(n)
    for (pair in prerequisites) { out[pair[1]].add(pair[0]); indegree[pair[0]]++ }
    val queue = ArrayDeque<Int>()
    for (i in 0 until n) if (indegree[i] == 0) queue.add(i)
    val order = IntArray(n)
    var count = 0
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        order[count++] = current
        for (next in out[current]) { indegree[next]--; if (indegree[next] == 0) queue.add(next) }
    }
    return if (count == n) order else IntArray(0)
}
```

O(n+m) time and auxiliary space, with O(n) output. A FIFO queue returns one valid order but makes no lexicographic-minimum promise.

- n=0, [] => []
- n=3, [] => any permutation such as [0,1,2]
- n=1, [[0,0]] => []

Twist: For the lexicographically smallest order, replace the FIFO queue with a min-priority queue. Always choosing the smallest currently valid next task yields the smallest order, in O(m+n log n) time. For uniqueness, check whether more than one ready course exists at any step of a successful traversal.

Practice source: https://leetcode.com/problems/course-schedule-ii/

### Find the redundant undirected edge (Medium)

A nonnull edge list describes a connected undirected simple graph formed from a tree on labels 1..n plus exactly one added edge. There are n edges, n >= 3, each with valid labels and distinct endpoints. Return the edge whose removal restores a tree, choosing the latest one in input order among valid choices. Input is not mutated.

[[1,2],[1,3],[2,3]] => [2,3]. Before this edge, 2 already connects to 3 through 1.

Baseline: Before adding each edge, BFS its endpoints in the graph built so far. O(n²) time and O(n) space for this sparse input.

1. Initialize every label in its own DSU component.
2. For each edge, compare roots. Different roots means it joins components; merge them.
3. Equal roots means an earlier path already exists. Adding this edge closes the unique cycle, so it is the last input edge on that cycle and satisfies the requested tie rule.

Correctness: Before an edge, DSU classes equal connectivity through earlier edges. The first failed union is precisely the edge whose endpoints are already connected. Under the tree-plus-one-edge promise, it is the only failed union.

```java
static final class DSU {
    final int[] parent, size;
    DSU(int n) {
        parent = new int[n]; size = new int[n];
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    }
    int find(int x) {
        while (x != parent[x]) { parent[x] = parent[parent[x]]; x = parent[x]; }
        return x;
    }
    boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra; size[ra] += size[rb]; return true;
    }
}
static int[] findRedundantConnection(int[][] edges) {
    Objects.requireNonNull(edges, "edges");
    DSU dsu = new DSU(edges.length + 1);
    for (int[] edge : edges) if (!dsu.union(edge[0], edge[1])) return new int[]{edge[0], edge[1]};
    throw new IllegalArgumentException("tree plus one edge required");
}
```

```kotlin
class DSU(n: Int) {
    private val parent = IntArray(n) { it }
    val size = IntArray(n) { 1 }
    fun find(value: Int): Int {
        var x = value
        while (x != parent[x]) { parent[x] = parent[parent[x]]; x = parent[x] }
        return x
    }
    fun union(a: Int, b: Int): Boolean {
        var ra = find(a)
        var rb = find(b)
        if (ra == rb) return false
        if (size[ra] < size[rb]) { val t = ra; ra = rb; rb = t }
        parent[rb] = ra
        size[ra] += size[rb]
        return true
    }
}
fun findRedundantConnection(edges: Array<IntArray>): IntArray {
    val dsu = DSU(edges.size + 1)
    for (edge in edges) if (!dsu.union(edge[0], edge[1])) return intArrayOf(edge[0], edge[1])
    throw IllegalArgumentException("tree plus one edge required")
}
```

O(n alpha(n)) amortized time for n edges and labels, O(n) auxiliary space, O(1) output. alpha is inverse Ackermann; path halving and union by size establish this bound.

- [[1,2],[2,3],[1,3]] => [1,3]
- [[1,2],[2,3],[3,4],[1,4],[1,5]] => [1,4]

Twist: For directed redundant-edge variants, indegree-two conflicts and direction matter; this undirected DSU proof does not apply unchanged. For online deletions, ordinary DSU cannot split components; offline reversal or rollback structures may be needed.

Practice source: https://leetcode.com/problems/redundant-connection/

### Largest island after at most one flip (Hard)

Given a nonnull square binary Int grid with side n in [0,500], change at most one 0 to 1 and return the maximum possible four-direction island area. Empty returns 0; all land returns n². Input is not mutated. Square shape and 0/1 values are preconditions.

[[1,0],[0,1]] => 3. A flip connects both distinct size-1 land components. [[1,1],[1,0]] => 4, counting the shared neighboring component only once.

Baseline: For each zero, temporarily flip it in a copy and traverse the whole grid to find the largest component: O(n^4) time and O(n²) space.

1. Create one DSU position per cell and merge adjacent existing land only.
2. Record the largest existing land component, covering the all-land case.
3. For each zero, collect distinct roots of adjacent land components. Candidate area is 1 plus each distinct size.
4. Take the largest candidate without merging zeros permanently; only one flip may occur.

Correctness: DSU represents exactly original land components. Every island created by one flip consists of that cell and the distinct neighboring components, so the candidate formula is both achievable and complete.

```java
static final class DSU {
    final int[] parent, size;
    DSU(int n) {
        parent = new int[n]; size = new int[n];
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    }
    int find(int x) {
        while (x != parent[x]) { parent[x] = parent[parent[x]]; x = parent[x]; }
        return x;
    }
    boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra; size[ra] += size[rb]; return true;
    }
}
static int largestIsland(int[][] grid) {
    Objects.requireNonNull(grid, "grid");
    int n = grid.length;
    if (n > 500) throw new IllegalArgumentException("n too large");
    if (n == 0) return 0;
    DSU dsu = new DSU(n * n);
    for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) if (grid[r][c] == 1) {
        if (r + 1 < n && grid[r+1][c] == 1) dsu.union(r*n+c, (r+1)*n+c);
        if (c + 1 < n && grid[r][c+1] == 1) dsu.union(r*n+c, r*n+c+1);
    }
    int answer = 0;
    int[] dr = {-1,1,0,0}, dc = {0,0,-1,1};
    for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
        if (grid[r][c] == 1) { answer = Math.max(answer, dsu.size[dsu.find(r*n+c)]); continue; }
        Set<Integer> roots = new HashSet<>();
        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d], nc = c + dc[d];
            if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 1) roots.add(dsu.find(nr*n+nc));
        }
        int area = 1;
        for (int root : roots) area += dsu.size[root];
        answer = Math.max(answer, area);
    }
    return answer;
}
```

```kotlin
class DSU(n: Int) {
    private val parent = IntArray(n) { it }
    val size = IntArray(n) { 1 }
    fun find(value: Int): Int {
        var x = value
        while (x != parent[x]) { parent[x] = parent[parent[x]]; x = parent[x] }
        return x
    }
    fun union(a: Int, b: Int): Boolean {
        var ra = find(a)
        var rb = find(b)
        if (ra == rb) return false
        if (size[ra] < size[rb]) { val t = ra; ra = rb; rb = t }
        parent[rb] = ra
        size[ra] += size[rb]
        return true
    }
}
fun largestIsland(grid: Array<IntArray>): Int {
    val n = grid.size
    require(n <= 500)
    if (n == 0) return 0
    val dsu = DSU(n * n)
    for (r in 0 until n) for (c in 0 until n) if (grid[r][c] == 1) {
        if (r + 1 < n && grid[r+1][c] == 1) dsu.union(r*n+c, (r+1)*n+c)
        if (c + 1 < n && grid[r][c+1] == 1) dsu.union(r*n+c, r*n+c+1)
    }
    var answer = 0
    val dr = intArrayOf(-1,1,0,0)
    val dc = intArrayOf(0,0,-1,1)
    for (r in 0 until n) for (c in 0 until n) {
        if (grid[r][c] == 1) { answer = maxOf(answer, dsu.size[dsu.find(r*n+c)]); continue }
        val roots = mutableSetOf<Int>()
        for (d in 0 until 4) {
            val nr = r + dr[d]
            val nc = c + dc[d]
            if (nr in 0 until n && nc in 0 until n && grid[nr][nc] == 1) roots.add(dsu.find(nr*n+nc))
        }
        var area = 1
        for (root in roots) area += dsu.size[root]
        answer = maxOf(answer, area)
    }
    return answer
}
```

O(n² alpha(n²)) amortized time and O(n²) auxiliary space; root deduplication has at most four entries per candidate and O(1) expected cost. O(1) output. n <= 500 keeps area and flattened indices within Int.

- [] => 0
- [[0]] => 1
- [[1]] => 1
- [[1,1],[1,1]] => 4
- [[0,0],[0,0]] => 1

Twist: Allowing two flips invalidates independent one-cell evaluation: the two new cells can connect to each other and join components neither reaches alone. For [[1,0],[0,0]], two flips can grow the island to 3, while a one-flip evaluation sees at most 2. Model pair interactions or derive a different constrained algorithm.

Practice source: https://leetcode.com/problems/making-a-large-island/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 950 180" role="img" aria-label="Indegree removal exposes ready work; DSU answers a different undirected membership question."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">prerequisite</text><path d="M175,72 Q290,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="290" y="46" text-anchor="middle" font-size="12">prerequisite</text><path d="M365,72 Q480,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="480" y="46" text-anchor="middle" font-size="12">prerequisite</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">prerequisite</text><path d="M175,72 Q480,72 785,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="480" y="46" text-anchor="middle" font-size="12">remove outgoing edges</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">task 0: indegree 0</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">task 1: indegree 1</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">task 2: indegree 1</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">task 3: indegree 2</div></foreignObject><rect x="785" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="790" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">ready queue after 0: [1,2]</div></foreignObject></svg></div><figcaption>Indegree removal exposes ready work; DSU answers a different undirected membership question.</figcaption></figure>

## Additional practice

### [Find the Town Judge](https://leetcode.com/problems/find-the-town-judge/) (Easy)

Hint: Track directed incoming and outgoing counts; no traversal is required.

Approach: Find the unique vertex with incoming n-1 and outgoing 0 under distinct trust-pair assumptions. O(n+m) time, O(n) space. This shows that directed graphs do not always require topology.

### [Number of Provinces](https://leetcode.com/problems/number-of-provinces/) (Medium)

Hint: Connectivity is symmetric and no path details are needed.

Approach: Union every connected pair in the matrix and count roots. O(n² alpha(n)) time and O(n) auxiliary space; DFS is also appropriate for a static graph.

### [Accounts Merge](https://leetcode.com/problems/accounts-merge/) (Medium)

Hint: An email is an identity link, while a display name is not.

Approach: Map each email to an account index and union accounts sharing an email; group emails by final representative and sort each group. O(E alpha(A)+E log E) typical bound plus string costs. Equal names alone must not merge accounts.

### [Alien Dictionary](https://leetcode.com/problems/alien-dictionary/) (Hard)

Hint: Only the first differing character between adjacent sorted words creates an order edge.

Approach: Build a directed character graph, reject longer-before-prefix pairs, deduplicate edges, then topologically sort. O(total characters + alphabet + edges), plus queue choice costs. A cycle makes the order impossible; absent constraints permit multiple orders.

### [Minimum Height Trees](https://leetcode.com/problems/minimum-height-trees/) (Medium)

Hint: Peel undirected leaves; compare this to directed source removal.

Approach: For a tree, repeatedly remove all leaves until at most two centers remain. O(n) time/space. This is not general topological sorting: an undirected tree has no directed prerequisites.

### [Number of Operations to Make Network Connected](https://leetcode.com/problems/number-of-operations-to-make-network-connected/) (Medium)

Hint: Count components and spare undirected edges.

Approach: If fewer than n-1 cables exist, return -1. Otherwise DSU component count c implies c-1 reconnections. O((n+m) alpha(n)) time, O(n) space. Reusing a redundant cable is permitted by the problem contract.

### [Parallel Courses III](https://leetcode.com/problems/parallel-courses-iii/) (Hard)

Hint: A prerequisite DAG supports longest-path dynamic programming.

Approach: Set earliestFinish[v] to duration[v] initially; in topological order relax finish[next]=max(finish[next],finish[cur]+duration[next]). Return maximum finish. O(n+m) time/space; use Long for large total durations. A cycle means the assumed schedule is invalid.

## Check your reasoning

Kahn processes 7 of 9 vertices and its queue becomes empty. What follows?

1. All remaining vertices are on one cycle.
2. The directed graph contains a cycle; remaining vertices may also be blocked downstream.
3. The input only needs a different queue order.

Answer: 2. No ready vertex remains, so the nonempty residual graph has a cycle. Queue order cannot fix a dependency cycle.

Why must an island-flip candidate deduplicate neighbor roots?

1. The grid contains duplicate coordinates.
2. Two adjacent land neighbors may already connect around another route.
3. DSU root sizes are always too large.

Answer: 2. Each existing component contributes its area once even if it touches the candidate on several sides.

## Transfer
A build tool receives directed package prerequisites and undirected cable additions between build workers. It needs a feasible package build order and whether two workers currently communicate. Choose state and algorithms for each query.

An arrow imposes order; a cable expresses symmetric reachability.

Ask whether your state needs readiness counts or equivalence classes.

Use directed adjacency and indegrees with Kahn for package order, O(P+D) time/space for P packages and D dependency edges. Use DSU for workers: union on cable additions and compare find roots for connectivity; O((W+Q) alpha(W)) total for initialization plus Q operations, O(W) space. DSU cannot answer directed prerequisite feasibility or show the actual cable path.

## Recall
- Derive why a nonempty DAG has a zero-indegree vertex.
- Explain why leftover Kahn vertices need not all lie on cycles.
- State the DSU membership invariant and where size metadata is valid.
- Draw a DAG that DSU would incorrectly classify as cyclic.
- Explain why evaluating one island flip must leave other candidate water cells unmerged.

## Sources
- [Princeton directed graphs](https://algs4.cs.princeton.edu/42digraph/): Primary reference link for directed graph concepts; content not retrieved during this pass.
- [Princeton union-find](https://algs4.cs.princeton.edu/15uf/): Primary reference link for connectivity data structures; content not retrieved during this pass. No employer frequency claims.


# Pattern 15: Shortest paths: matching the frontier to edge costs

Minimizing the number of moves and minimizing total cost agree only when every move has equal cost. Derive the right frontier discipline from the weight assumptions, and expand the state when a limited resource affects future options.

## Build the model
A shortest-path problem asks for the cheapest legal route between states. First define its cost. If every move costs one, route cost equals number of edges, and BFS layers already sort that cost. If one move costs 100 while another costs 1, a route with more edges may be cheaper. The data structure is not the starting point: the ordering property required by the proof is.

Begin with distances. dist[v] is the smallest cost of any route to v discovered so far. Initialize the source to zero and every other vertex to infinity, a sentinel meaning no route discovered. For an edge u->v of weight w, a route to u can be extended to v, so compare dist[u]+w with dist[v]. Replacing a larger tentative value with this smaller candidate is called relaxation. A tentative value is an upper bound on the true optimum because it corresponds to an actual discovered route, not an optimistic guess.

For nonnegative weights, Dijkstra repeatedly takes the currently smallest tentative distance from a min-priority queue. Why can this vertex be settled? Consider a hypothetical cheaper route to it. On that route, the first not-yet-settled vertex has a settled predecessor whose outgoing edge was already relaxed. Its tentative distance would be no greater than the cost of that prefix, and nonnegative remaining edges cannot reduce that prefix to less than the vertex just selected. The supposed cheaper route contradicts the minimum selection. Negative edges destroy the last step of this argument.

A practical heap can contain several entries for one vertex because a new improvement is pushed without deleting the old entry. Store both vertex and candidate distance. When popping, discard entries whose saved distance differs from the current dist[vertex]. Such entries are stale, not new work. Marking a vertex visited when first enqueued is wrong for weighted graphs: the first discovered route need not be cheapest. Settlement happens when its minimum current distance is removed from the correctly ordered frontier.

When weights are only zero or one, relaxation from distance d produces only d or d+1. A deque can preserve order: put a zero-edge improvement at the front, and a one-edge improvement at the back. The deque replaces the general min-heap because the range of possible next distances has special structure. Continue using relaxation and stale-entry checks; an ordinary “visited on enqueue” BFS proof does not transfer to zero-cost edges.

Constraints can change the state even when edge weights stay nonnegative. In a flight network, a cheap arrival using all allowed flights may be useless while a more expensive arrival with one flight used can still reach the destination. The airport alone is not enough state. One approach is (airport, flightsUsed). A simpler bounded Bellman-Ford recurrence lets layer e contain the minimum costs using at most e edges. To build the next layer, read exclusively from the previous layer and write into a copy. Reading updated values from the same pass can silently chain several flights and violate the budget.

A stop is an intermediate airport. At most K stops allows at most K+1 edges, while direct source-to-destination uses one edge and zero stops. Clarify this off-by-one rule with examples before coding. Finally, no finite minimum exists when a reachable negative-cost cycle can be repeated on a route to the target without a limiting resource. Report the condition under the problem's contract instead of returning an arbitrary finite distance.

## Invariant
All finite tentative distances correspond to actual legal paths. BFS first discovery is final only under equal edge costs. Dijkstra settles a vertex only when its smallest nonstale heap entry is removed, relying on nonnegative edges. Zero-one BFS maintains a deque in nondecreasing current candidate cost by putting zero-cost extensions before cost-one extensions. In bounded Bellman-Ford, after e passes the array contains exact optima among paths of at most e edges, because the next pass extends only previous-layer paths. These are separate correctness arguments; a universal visited rule cannot replace them.

## Recognize it
- Equal-cost moves: BFS orders states by distance in edges.
- Nonnegative varying costs: always expand the smallest tentative total distance with Dijkstra.
- Edge costs are exactly zero or one: a deque maintains the necessary order more cheaply than a heap.
- The route has a maximum number of edges or stops: carry the resource dimension or update from a frozen previous layer.
- Negative edges: Dijkstra's settlement proof fails; consider Bellman-Ford and explicitly address reachable negative cycles.

## Execution trace
1. dist[S]=0, dist[A]=∞, dist[B]=∞; heap=[(0,S)]

Only the empty source route is known.

2. Pop S; dist[A]=8, dist[B]=1; heap=[(1,B),(8,A)]

Both entries are real paths; neither first push settles A.

3. Pop B at 1; candidate A=1+1=2; heap=[(2,A),(8,A)]

Relaxation replaces an expensive tentative route.

4. Pop A at 2; settle A

Nonnegative edges and minimum extraction certify its optimum.

5. Pop A at 8; discard because 8 != dist[A]

The old entry is stale and must not expand edges again.

6. Flight budget 1 edge: previous=[0,∞,∞], next after edges 0->1,1->2 is [0,1,∞]

Frozen reads prevent illegally chaining two edges in one pass.

## Wrong turns
### Use ordinary BFS to minimize weighted cost.
Edges S->T cost 10, S->A cost 1, A->T cost 1. BFS discovers the direct one-edge route first, but the cheapest route costs 2.

For nonnegative varying weights, use a min-heap of total distances.

### Mark a weighted vertex permanently visited when first pushed.
S->A cost 8, S->B cost 1, B->A cost 1. First discovery sets A=8, but the optimum is 2.

Allow improvements; settle only when removing the smallest nonstale candidate.

### Update the same distance array in one constrained-flight pass.
Edges 0->1 cost 1 and 1->2 cost 1, K=0 stops. An in-place pass can set dist[1]=1 then dist[2]=2, illegally using two flights where only one is allowed.

Read the frozen previous layer and write into a copied next layer.

### Apply Dijkstra when a reachable negative edge exists.
S->A cost 1, S->B cost 2, B->A cost -5. Settling A at 1 before exploring B misses the better cost -3 under the settlement proof.

Use an algorithm whose proof allows negative edges; address negative cycles and any edge budget.

### Minimum edges in an unweighted network (Easy)

Given n >= 1 vertices labeled 0..n-1, a nonnull array of valid undirected edge pairs, and valid source/target labels, return the minimum number of edges on a source-to-target path, or -1 when unreachable. This is an original distance variation of ordinary reachability. Self-loops and repeated edges are allowed. Inputs are not mutated.

n=4, edges=[[0,1],[1,2],[0,2]], source=0,target=2 => 1. target=3 => -1.

Baseline: Enumerate all simple paths and keep the shortest: exponential time in the worst case, O(n) active path storage. A reachable walk with a cycle can always be shortened by deleting that cycle.

1. Build undirected adjacency lists.
2. Initialize distances to -1 and enqueue source at 0.
3. Assign each unseen neighbor distance current+1 on enqueue. FIFO ordering means first discovery is the minimum edge count.

Correctness: Queue distances are nondecreasing. Any hypothetical shorter route to a newly discovered vertex would have had a predecessor processed earlier, contradicting that it was unseen.

```java
static int shortestEdges(int n, int[][] edges, int source, int target) {
    if (n < 1) throw new IllegalArgumentException("positive n required");
    Objects.requireNonNull(edges, "edges");
    List<List<Integer>> out = new ArrayList<>();
    for (int i = 0; i < n; i++) out.add(new ArrayList<>());
    for (int[] edge : edges) { out.get(edge[0]).add(edge[1]); out.get(edge[1]).add(edge[0]); }
    int[] distance = new int[n]; Arrays.fill(distance, -1); distance[source] = 0;
    ArrayDeque<Integer> queue = new ArrayDeque<>(); queue.add(source);
    while (!queue.isEmpty()) {
        int current = queue.remove();
        if (current == target) return distance[current];
        for (int next : out.get(current)) if (distance[next] == -1) {
            distance[next] = distance[current] + 1; queue.add(next);
        }
    }
    return -1;
}
```

```kotlin
fun shortestEdges(n: Int, edges: Array<IntArray>, source: Int, target: Int): Int {
    require(n >= 1)
    val out = Array(n) { mutableListOf<Int>() }
    for (edge in edges) { out[edge[0]].add(edge[1]); out[edge[1]].add(edge[0]) }
    val distance = IntArray(n) { -1 }
    distance[source] = 0
    val queue = ArrayDeque<Int>()
    queue.add(source)
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        if (current == target) return distance[current]
        for (next in out[current]) if (distance[next] == -1) {
            distance[next] = distance[current] + 1
            queue.add(next)
        }
    }
    return -1
}
```

O(n+m) time and auxiliary space for n vertices and m edge entries; O(1) output. Distances fit Int because a shortest simple path has at most n-1 edges.

- n=1,edges=[],source=target=0 => 0
- n=2,edges=[],source=0,target=1 => -1
- n=2,edges=[[0,0],[0,1]],source=0,target=1 => 1

Twist: For directed edges, add only the forward adjacency. To reconstruct one shortest path, record parent[next]=current at discovery and reverse the parent chain after reaching target.

Practice source: https://leetcode.com/problems/find-if-path-exists-in-graph/

### Network delay with nonnegative weights (Medium)

There are n nodes labeled 1..n, 1 <= n <= 100_000, and a nonnull list of directed [u,v,weight] edges with weight in [0,1_000_000_000] and at most 200_000 entries. Starting at valid source k, return the minimum time by which all nodes receive a signal, or -1 if any are unreachable. Return Long; no input mutation. Labels, weight range, and entry shape are preconditions.

n=3, k=1, edges=[[1,2,8],[1,3,1],[3,2,1]] => 2. Node 2 is reached via node 3.

Baseline: Repeated full-edge Bellman-Ford relaxation for n-1 rounds: O(n*m) time, O(n) distance space.

1. Build directed adjacency lists and start dist[k]=0.
2. Use a min-priority queue of (candidateDistance,node). Discard stale candidates.
3. Relax every outgoing edge of the smallest current candidate. Nonnegative weights certify its settled distance.
4. If any distance remains infinite, return -1; otherwise the slowest node determines the total delay.

Correctness: Each settled nonstale minimum has its true shortest distance. Every finite tentative distance is the cost of an actual source path.

```java
static long networkDelayTime(int[][] times, int n, int k) {
    Objects.requireNonNull(times, "times");
    if (n < 1 || n > 100_000) throw new IllegalArgumentException("n out of range");
    List<List<int[]>> out = new ArrayList<>();
    for (int i = 0; i <= n; i++) out.add(new ArrayList<>());
    for (int[] edge : times) out.get(edge[0]).add(new int[]{edge[1], edge[2]});
    long[] dist = new long[n + 1]; Arrays.fill(dist, Long.MAX_VALUE); dist[k] = 0;
    PriorityQueue<long[]> heap = new PriorityQueue<>((a,b) -> Long.compare(a[0],b[0]));
    heap.add(new long[]{0,k});
    while (!heap.isEmpty()) {
        long[] entry = heap.remove(); long cost = entry[0]; int current = (int)entry[1];
        if (cost != dist[current]) continue;
        for (int[] edge : out.get(current)) {
            long nextCost = cost + edge[1];
            if (nextCost < dist[edge[0]]) { dist[edge[0]] = nextCost; heap.add(new long[]{nextCost, edge[0]}); }
        }
    }
    long answer = 0;
    for (int i = 1; i <= n; i++) { if (dist[i] == Long.MAX_VALUE) return -1; answer = Math.max(answer, dist[i]); }
    return answer;
}
```

```kotlin
fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Long {
    require(n in 1..100_000)
    val out = Array(n + 1) { mutableListOf<IntArray>() }
    for (edge in times) out[edge[0]].add(intArrayOf(edge[1], edge[2]))
    val dist = LongArray(n + 1) { Long.MAX_VALUE }
    dist[k] = 0L
    val heap = PriorityQueue<LongArray> { a, b -> a[0].compareTo(b[0]) }
    heap.add(longArrayOf(0L, k.toLong()))
    while (heap.isNotEmpty()) {
        val entry = heap.remove()
        val cost = entry[0]
        val current = entry[1].toInt()
        if (cost != dist[current]) continue
        for (edge in out[current]) {
            val nextCost = cost + edge[1].toLong()
            if (nextCost < dist[edge[0]]) { dist[edge[0]] = nextCost; heap.add(longArrayOf(nextCost, edge[0].toLong())) }
        }
    }
    var answer = 0L
    for (i in 1..n) { if (dist[i] == Long.MAX_VALUE) return -1L; answer = maxOf(answer, dist[i]) }
    return answer
}
```

O(n + m log(m+1)) time and O(n+m) auxiliary space for n nodes and m edges, including stale heap entries. With arbitrary parallel edges, log(m+1) is more precise than assuming a heap of at most n entries. O(1) output. Long arithmetic is safe under stated bounds.

- n=1,k=1,times=[] => 0
- n=2,k=1,times=[] => -1
- n=2,k=1,times=[[1,2,0]] => 0

Twist: To find a route rather than all-node delay, retain predecessor pointers on successful relaxations. If only one target matters, return when its nonstale entry is popped, never when it is first inserted.

Practice source: https://leetcode.com/problems/network-delay-time/

### Cheapest flight with a stop budget (Medium)

Given 1 <= n <= 1000 airports labeled 0..n-1, a nonnull list of directed [from,to,price] flights with prices in [0,1_000_000_000], valid src/dst, and K in [0,1000], return the minimum Long fare using at most K intermediate stops (K+1 flights), or -1 if impossible. src=dst permits the empty zero-cost route. No input mutation. Valid labels and flight shape are preconditions.

n=3, flights=[[0,1,1],[1,2,1],[0,2,8]], src=0,dst=2: K=0 => 8, K=1 => 2.

Baseline: Enumerate all flight sequences up to K+1 edges: exponential in the budget and branching factor; O(K) recursion storage apart from the graph.

1. Let prev[v] be the minimum fare using at most e edges. Initially only src is reachable with e=0.
2. Copy prev to next, retaining paths that use fewer edges.
3. For every flight u->v, relax next[v] from prev[u]+price, reading only the previous array.
4. After K+1 passes, return the dst entry unless infinite.

Correctness: After pass e, distances are exact minima among paths with at most e edges. Every legal nonempty path in the next layer is a previous-layer path plus one final edge.

```java
static long cheapestFlight(int n, int[][] flights, int src, int dst, int stops) {
    Objects.requireNonNull(flights, "flights");
    if (n < 1 || n > 1000 || stops < 0 || stops > 1000) throw new IllegalArgumentException("bounds violated");
    long[] previous = new long[n]; Arrays.fill(previous, Long.MAX_VALUE); previous[src] = 0;
    for (int used = 1; used <= stops + 1; used++) {
        long[] next = previous.clone();
        for (int[] flight : flights) {
            if (previous[flight[0]] == Long.MAX_VALUE) continue;
            next[flight[1]] = Math.min(next[flight[1]], previous[flight[0]] + flight[2]);
        }
        previous = next;
    }
    return previous[dst] == Long.MAX_VALUE ? -1 : previous[dst];
}
```

```kotlin
fun cheapestFlight(n: Int, flights: Array<IntArray>, src: Int, dst: Int, stops: Int): Long {
    require(n in 1..1000 && stops in 0..1000)
    var previous = LongArray(n) { Long.MAX_VALUE }
    previous[src] = 0L
    for (used in 1..stops + 1) {
        val next = previous.copyOf()
        for (flight in flights) {
            if (previous[flight[0]] == Long.MAX_VALUE) continue
            next[flight[1]] = minOf(next[flight[1]], previous[flight[0]] + flight[2].toLong())
        }
        previous = next
    }
    return if (previous[dst] == Long.MAX_VALUE) -1L else previous[dst]
}
```

O((K+1)*(n+m)) time because every pass copies n distances and scans m flights; O(n) working space, excluding input, and O(1) output. Bound K <= 1000 and prices <= 1e9 keep totals safely within Long.

- src=dst, flights=[] => 0
- n=2,src=0,dst=1,flights=[] => -1
- two-edge-only route with K=0 => -1

Twist: If the requirement is exactly K stops, do not carry previous distances into the next layer: initialize next to infinity each pass, and take exactly K+1 transitions. If reconstructing routes, retain layer-specific predecessors, because one parent per airport loses the budget dimension.

Practice source: https://leetcode.com/problems/cheapest-flights-within-k-stops/

### Minimum obstacles to remove (Hard)

Given a nonnull rectangular binary grid with R,C >= 1 and R*C <= 1_000_000, move four directions from top-left to bottom-right. Entering a 1 cell removes that obstacle and costs 1; entering 0 costs 0. Endpoints are 0. Return the minimum obstacles removed, not minimum moves. No input mutation. Shape, size, values, and endpoint conditions are preconditions.

[[0,1,1],[1,1,0],[1,1,0]] => 2. A longer route can still be preferable if it crosses fewer obstacles.

Baseline: Use ordinary Dijkstra on this grid: O(R*C log(R*C)) time and O(R*C) space. It is correct but ignores the special 0/1 edge weights.

1. View the destination cell value as the directed edge entry cost.
2. Maintain best obstacle counts. A zero-cost improvement has the same distance as the current state and goes to the deque front.
3. A one-cost improvement goes to the back, after the same-distance work.
4. Pop in nondecreasing candidate distance, discard stale entries, and return when the destination is popped nonstale.

Correctness: Deque candidates remain ordered by cost. Relaxation creates only cost d or d+1 from a current minimum d; front/back placement preserves the ordering needed for settlement.

```java
static int minimumObstacles(int[][] grid) {
    Objects.requireNonNull(grid, "grid");
    int rows = grid.length, cols = grid[0].length;
    int[][] dist = new int[rows][cols];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    dist[0][0] = 0;
    ArrayDeque<int[]> deque = new ArrayDeque<>(); deque.addFirst(new int[]{0,0,0});
    int[] dr = {-1,1,0,0}, dc = {0,0,-1,1};
    while (!deque.isEmpty()) {
        int[] entry = deque.removeFirst(); int r = entry[0], c = entry[1], cost = entry[2];
        if (cost != dist[r][c]) continue;
        if (r == rows - 1 && c == cols - 1) return cost;
        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d], nc = c + dc[d];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
            int nextCost = cost + grid[nr][nc];
            if (nextCost < dist[nr][nc]) {
                dist[nr][nc] = nextCost;
                int[] next = {nr,nc,nextCost};
                if (grid[nr][nc] == 0) deque.addFirst(next); else deque.addLast(next);
            }
        }
    }
    return -1;
}
```

```kotlin
fun minimumObstacles(grid: Array<IntArray>): Int {
    val rows = grid.size
    val cols = grid[0].size
    val dist = Array(rows) { IntArray(cols) { Int.MAX_VALUE } }
    dist[0][0] = 0
    val deque = ArrayDeque<IntArray>()
    deque.addFirst(intArrayOf(0,0,0))
    val dr = intArrayOf(-1,1,0,0)
    val dc = intArrayOf(0,0,-1,1)
    while (deque.isNotEmpty()) {
        val entry = deque.removeFirst()
        val r = entry[0]
        val c = entry[1]
        val cost = entry[2]
        if (cost != dist[r][c]) continue
        if (r == rows - 1 && c == cols - 1) return cost
        for (d in 0 until 4) {
            val nr = r + dr[d]
            val nc = c + dc[d]
            if (nr !in 0 until rows || nc !in 0 until cols) continue
            val nextCost = cost + grid[nr][nc]
            if (nextCost < dist[nr][nc]) {
                dist[nr][nc] = nextCost
                val next = intArrayOf(nr,nc,nextCost)
                if (grid[nr][nc] == 0) deque.addFirst(next) else deque.addLast(next)
            }
        }
    }
    return -1
}
```

O(R*C) time and auxiliary space because the implicit grid graph has O(R*C) vertices and edges and zero-one BFS performs linear relaxation work; O(1) output. A shortest simple route crosses at most R*C cells, so cost fits Int under the contract.

- [[0]] => 0
- [[0,0,0]] => 0
- [[0,1,0]] => 1

Twist: If values can be 0, 1, or 2, front/back placement no longer preserves the required order; use a min-heap Dijkstra implementation, or a carefully derived bounded-integer bucket algorithm. If you minimize moves subject to an obstacle-removal budget instead, BFS over (r,c,budgetRemaining).

Practice source: https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 950 180" role="img" aria-label="A min-heap corrects an early expensive discovery; FIFO edge count would choose the direct route."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">cost 8</text><path d="M175,72 Q290,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="290" y="46" text-anchor="middle" font-size="12">cost 1</text><path d="M555,72 Q385,15 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="20" text-anchor="middle" font-size="12">cost 1</text><path d="M175,72 Q385,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">relax outgoing edges</text><path d="M745,72 Q765,72 785,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="765" y="46" text-anchor="middle" font-size="12">pop B; improve A; later skip stale 8</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">S distance 0</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">A tentative 8, later 2</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">B distance 1</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">heap after S: [(1,B),(8,A)]</div></foreignObject><rect x="785" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="790" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">after B: [(2,A),(8,A)]</div></foreignObject></svg></div><figcaption>A min-heap corrects an early expensive discovery; FIFO edge count would choose the direct route.</figcaption></figure>

## Additional practice

### [Shortest Path in Binary Matrix](https://leetcode.com/problems/shortest-path-in-binary-matrix/) (Medium)

Hint: All allowed eight-direction moves have equal cost.

Approach: BFS through zero cells, marking on enqueue. Return number of cells in route, so source distance is 1; reject blocked endpoints. O(n²) time/space.

### [Path With Minimum Effort](https://leetcode.com/problems/path-with-minimum-effort/) (Medium)

Hint: Path aggregation is a maximum, rather than a sum.

Approach: Dijkstra with candidate=max(currentEffort,abs(height[u]-height[v])). This aggregation is monotone under extension, enabling settlement. O(R*C log(R*C)) time/space; widen arithmetic before subtraction.

### [Swim in Rising Water](https://leetcode.com/problems/swim-in-rising-water/) (Hard)

Hint: The cost of a route is its largest required elevation.

Approach: Use minimax Dijkstra with max(currentLevel,nextElevation), or binary-search a level with reachability. Heap version O(n² log n) time and O(n²) space. Distinguish it from additive travel cost.

### [Minimum Cost to Make at Least One Valid Path in a Grid](https://leetcode.com/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/) (Hard)

Hint: Following an arrow costs zero; choosing a different direction costs one.

Approach: Use zero-one BFS with edge cost determined by the current cell's arrow. O(R*C) time/space. Source-based edge cost differs from destination obstacle cost.

### [Find the City With the Smallest Number of Neighbors at a Threshold Distance](https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/) (Medium)

Hint: Many all-pairs distance queries can justify an all-pairs algorithm.

Approach: Use Floyd-Warshall in O(n³) time/O(n²) space for small n, or run Dijkstra from each source on nonnegative sparse graphs. Apply the specified tie rule and decide whether self counts.

### [Bellman-Ford negative-cycle extension](https://leetcode.com/problems/network-delay-time/) (Advanced)

Hint: This is an original extension, not the linked problem's nonnegative contract.

Approach: Relax edges n-1 times from a source, guarding infinity. A further improvement identifies influence of a source-reachable negative cycle; propagate that influence to mark unbounded targets. O(n*m) time, O(n+m) space with influence traversal. A cycle irrelevant to the target does not make that target's minimum unbounded.

### [Shortest Path Visiting All Nodes](https://leetcode.com/problems/shortest-path-visiting-all-nodes/) (Hard)

Hint: The route must remember which obligations it has met.

Approach: BFS over (currentNode,visitedMask), initialized from every possible start. O((n+m)*2^n) time and O(n*2^n) space for small n. Different masks at the same node are different states.

## Check your reasoning

With K=0 allowed intermediate stops, how many flight edges may a route contain?

1. Zero.
2. At most one.
3. At most two.

Answer: 2. A direct flight has no intermediate airport and one edge.

Why skip a heap entry (cost,node) when cost differs from dist[node]?

1. A better discovered route made the older candidate stale.
2. The graph necessarily has a negative cycle.
3. The node must be unreachable.

Answer: 1. The implementation pushes improvements and leaves old entries in the heap. The distance comparison prevents duplicate expansion.

## Transfer
A robot moves in a warehouse. Entering a free aisle costs 0 energy and entering a charging-door cell costs 1 energy. Find the smallest energy from the entrance to exit; there is no limit on moves. Then explain what changes if the robot can open at most two doors but must minimize walking steps.

Energy and move count are different objectives.

For the second version, how does remaining door budget affect legal future moves?

For minimum energy with 0/1 entry costs, use zero-one BFS, front-pushing zero-cost improvements and back-pushing one-cost improvements: O(R*C) time/space. For minimum steps under two doors, use ordinary BFS over (r,c,doorsUsed) with at most 3*R*C states. The complete state distinguishes arrivals with different remaining capabilities. A single distance per cell would discard useful routes.

## Recall
- Give a weighted counterexample to visited-on-enqueue.
- Derive why nonnegative weights justify Dijkstra settlement.
- Simulate a zero-cost edge placed at the front of a deque.
- Explain why cloning the distance array enforces a flight count.
- Distinguish unreachable targets from an unbounded negative-cycle objective.

## Sources
- [Princeton shortest paths](https://algs4.cs.princeton.edu/44sp/): Primary algorithms reference link; external page not retrieved in this pass. No company question-frequency claims.


# Pattern 16: Greedy choices and exchange arguments

Commit to one local choice only after proving an optimal solution can be transformed to contain it.

## Build the model
Start with an everyday decision: you have cookies of different sizes and children with minimum acceptable sizes. Giving a very large cookie to a child who would accept a small one feels wasteful. But “feels wasteful” is not yet an algorithm. We need to show that the smaller adequate cookie can replace the larger one in an optimal assignment without reducing the number of satisfied children. That replacement is an **exchange**.

A greedy algorithm makes a choice and does not revisit it. Backtracking explores alternatives; dynamic programming compares alternatives while sharing repeated subproblems. Greedy avoids those alternatives entirely, so its proof has to justify discarding them. Its speed comes from a structural theorem, not from confidence.

To discover a rule, write two plausible choices and invent the smallest input on which they differ. In interval selection, sorting by earliest start is plausible, but `[0,100], [1,2], [2,3]` makes it fail when touching endpoints are allowed: the first interval blocks two others. Sorting by earliest finish leaves at least as much room for the future. Replace the first interval of an optimal schedule with the earliest-finishing compatible interval. The remaining selected intervals still fit. Repeat the argument on the suffix.

There are two common proof shapes. In an exchange argument, transform some optimal solution to agree with your first choice, then solve the smaller instance. In a stays-ahead argument, compare prefixes: after each processed item, your summary dominates every competitor's summary. For reachability in an array, the largest reachable index is enough; paths reaching an earlier boundary cannot enable anything the farther-reaching prefix cannot.

Sometimes one scan cannot satisfy all local constraints. Candy assignment has a left-neighbor requirement and a right-neighbor requirement. A left-to-right pass computes the minimum needed for one direction. A right-to-left pass computes the other. Taking the larger requirement at every position satisfies both, and each requirement is a necessary lower bound. This is a greedy construction backed by a minimality proof.

Always separate the **ordering** from the **choice**. Sorting is preprocessing; it does not itself prove optimality. Write the objective, the exchange, and the feasibility conditions. If the objective changes from number of intervals to total interval value, earliest finish is no longer sufficient: `[0,2]` worth 100 beats `[0,1]` worth 1 plus `[1,2]` worth 1. The problem has become weighted scheduling and generally needs DP.

## Invariant
After each irreversible decision, there exists an optimal solution whose decisions agree with the chosen prefix, or the maintained summary dominates every reachable competitor. State which form applies to the specific problem. Initialization has an empty prefix; maintenance follows from a feasibility-preserving exchange or a dominance comparison; termination converts the prefix theorem into global optimality.

## Recognize it
- A decision permanently consumes a resource, and the remaining problem has the same structure. Look for an exchange argument, not a keyword.
- Sorted thresholds, earliest finishes, and dominance can make one partial solution at least as good for every possible future.
- If a locally attractive choice can harm future feasibility, a greedy rule needs repair or the problem needs DP.

## Execution trace
1. greed=[1,2,3], cookies=[1,1,3]; next child i=0

Sort both sequences. The smallest unsatisfied threshold is 1.

2. cookie 1 satisfies child 1; i=1, count=1

Use the smallest adequate resource. Any solution using a larger one can exchange it.

3. next cookie 1 < threshold 2; skip cookie; i=1

This cookie cannot satisfy this child or any later child, so discarding it is safe.

4. cookie 3 satisfies threshold 2; i=2, count=2; no cookies remain

Two children are satisfied. Assigning 3 to threshold 3 instead cannot increase the count.

## Wrong turns
### Choose the largest immediate reward without an exchange proof
Coin denominations [1,3,4], amount 6: taking 4 first produces 4+1+1 (3 coins), while 3+3 uses 2. Local denomination size does not dominate the future remainder.

Use amount-based DP for arbitrary denominations, or prove a special property of the denomination system.

### Reuse an unweighted greedy rule after adding weights
Intervals [0,1] value 1, [1,2] value 1, [0,2] value 100: earliest finish maximizes count but selects total value 2 instead of 100.

Define the objective first. Weighted scheduling needs predecessor compatibility and a choose/skip DP; an unweighted exchange does not preserve value.

### Assign cookies (Easy)

Given nonnegative minimum sizes greed and nonnegative cookie sizes, maximize the number of satisfied children. Each cookie can be assigned to at most one child. Empty arrays are valid. Inputs are not mutated.

greed=[1,2,3], sizes=[1,1,3] → 2. The two size-1 cookies cannot both help threshold 2.

Baseline: Try all assignments, exponentially many in the numbers of children and cookies; store a used-cookie set in O(m) space.

1. Sort copies of both arrays, so the next child has the smallest unmet threshold.
2. If a cookie is too small for that child, it is too small for every remaining child. Discard it.
3. Otherwise assign it. In an optimal assignment using a larger cookie for this child, exchange the two cookies; any other child previously using the smaller cookie can accept the larger one.
4. After all cookies or children are exhausted, the agreeing optimal assignment proves the count is maximal.

Correctness: There exists an optimal assignment agreeing with the matches made so far. Skipped undersized cookies cannot satisfy any remaining child.

```java
static int cookies(int[] greed, int[] sizes) {
    int[] g=greed.clone(), s=sizes.clone();
    Arrays.sort(g); Arrays.sort(s);
    int i=0;
    for(int size:s) if(i<g.length && size>=g[i]) i++;
    return i;
}
```

```kotlin
fun cookies(greed: IntArray, sizes: IntArray): Int {
    val g=greed.sortedArray(); val s=sizes.sortedArray()
    var i=0
    for(size in s) if(i<g.size && size>=g[i]) i++
    return i
}
```

For n children and m cookies: O(n log n + m log m) time, O(n+m) auxiliary space for copies plus sort workspace. Output is O(1).

- [], [1] → 0
- [2], [1] → 0
- [1,1], [1,1] → 2

Twist: If each child has a different reward, maximizing count and maximizing reward diverge. Re-derive the objective; the threshold exchange alone is insufficient.

Practice source: https://leetcode.com/problems/assign-cookies/

### Jump game: reachable frontier (Medium)

A nonnegative array a gives the maximum forward jump length from each index. Decide whether the last index is reachable from index 0. Empty input returns false. Do not mutate the array.

[2,3,1,1,4] → true; [3,2,1,0,4] → false.

Baseline: Enumerate every possible jump path: exponential worst-case time, O(n) recursive depth. A visited-state DP improves this to O(n²) time and O(n) space.

1. Reachable indices form a prefix: from any reachable index you can choose any smaller allowed forward step.
2. Store far, the largest reachable index using processed positions. Process i only when i≤far.
3. Update far with i+a[i], promoting before addition. If far covers the end, report true.
4. If i exceeds far, no processed reachable position can cross the gap, so no future position can help.

Correctness: Before processing i, all positions through far are reachable, and far is the maximum endpoint generated by processed reachable positions. The endpoint only grows.

```java
static boolean canJump(int[] a) {
    if(a.length==0) return false;
    long far=0;
    for(int i=0;i<a.length;i++) {
        if(i>far) return false;
        far=Math.max(far,(long)i+a[i]);
        if(far>=a.length-1) return true;
    }
    return false;
}
```

```kotlin
fun canJump(a: IntArray): Boolean {
    if(a.isEmpty()) return false
    var far=0L
    for(i in a.indices) {
        if(i.toLong()>far) return false
        far=maxOf(far,i.toLong()+a[i])
        if(far>=a.lastIndex.toLong()) return true
    }
    return false
}
```

O(n) time, O(1) auxiliary and output space. far uses 64-bit arithmetic.

- [0] → true
- [0,1] → false
- [] → false

Twist: To minimize jumps, count expansions of BFS-like reachable layers. Merely returning far loses how many jumps were needed; retain the current layer end and the next farthest end.

Practice source: https://leetcode.com/problems/jump-game/

### Partition labels (Medium)

Partition a lowercase-English-letter string into the maximum number of nonempty contiguous parts such that each letter appears in at most one part. Return part lengths. Empty input returns an empty list.

ababcbacadefegdehijhklij → [9,7,8].

Baseline: Try every cut set (2^(n−1) for nonempty strings) and validate whether letters cross cuts. O(n·2^n) time, O(n) working space.

1. Precompute the last occurrence of every letter. Any part containing position i must extend through last[s[i]].
2. Scan and extend end to the largest required last position seen in the current part.
3. When i=end, every letter in the current part has finished. Cutting is feasible.
4. No earlier cut was feasible, so cutting immediately leaves the maximum opportunity for later parts.

Correctness: end is the farthest last occurrence of all letters read in the current part. No valid cut exists inside that unfinished closure.

```java
static List<Integer> partitions(String s) {
    int[] last=new int[26];
    for(int i=0;i<s.length();i++) last[s.charAt(i)-'a']=i;
    List<Integer> out=new ArrayList<>(); int start=0,end=0;
    for(int i=0;i<s.length();i++) {
        end=Math.max(end,last[s.charAt(i)-'a']);
        if(i==end){out.add(i-start+1);start=i+1;}
    }
    return out;
}
```

```kotlin
fun partitions(s: String): List<Int> {
    val last=IntArray(26)
    for(i in s.indices) last[s[i]-'a']=i
    val out=mutableListOf<Int>(); var start=0; var end=0
    for(i in s.indices) {
        end=maxOf(end,last[s[i]-'a'])
        if(i==end){out.add(i-start+1);start=i+1}
    }
    return out
}
```

O(n+26) time, O(26) auxiliary space, O(p) output for p parts. The fixed alphabet is an explicit contract.

- empty string → []
- aaaa → [4]
- abc → [1,1,1]

Twist: For arbitrary Unicode code points, index by code points and store their last positions in a map; define whether returned lengths count code points or UTF-16 units.

Practice source: https://leetcode.com/problems/partition-labels/

### Candy: two directional lower bounds (Hard)

Give each child at least one candy. A child with a strictly higher rating than an immediate neighbor must receive more candies than that neighbor. Minimize the total. Ratings may be any Int; empty input returns 0. Input is not mutated.

[1,0,2] → 5 with [2,1,2]. [1,2,2] → 4 with [1,2,1]. Equal ratings impose no ordering.

Baseline: Enumerate positive candy vectors and test constraints. With n children a solution never needs more than n per child, but n^n candidates are impractical; O(n) validation each.

1. A left-to-right increasing run forces candies 1,2,3,… . Compute these necessary left requirements.
2. Scan right to left with a scalar right requirement, incrementing when the current rating exceeds the next.
3. At each index take max(left[i],right). Both directional requirements are necessary; the maximum satisfies both.
4. Sum in long/Long. Each chosen amount equals the smallest value satisfying the two independent lower bounds, proving total minimality.

Correctness: left[i] is the minimum candy amount satisfying all constraints from the left. During the reverse scan right is the analogous minimum from the right. Their pointwise maximum is feasible and no feasible vector can be smaller at any index.

```java
static long candy(int[] rating) {
    int n=rating.length; if(n==0) return 0L;
    int[] left=new int[n]; Arrays.fill(left,1);
    for(int i=1;i<n;i++) if(rating[i]>rating[i-1]) left[i]=left[i-1]+1;
    int right=1; long total=left[n-1];
    for(int i=n-2;i>=0;i--) {
        right=rating[i]>rating[i+1]?right+1:1;
        total+=Math.max(left[i],right);
    }
    return total;
}
```

```kotlin
fun candy(rating: IntArray): Long {
    val n=rating.size; if(n==0) return 0L
    val left=IntArray(n){1}
    for(i in 1 until n) if(rating[i]>rating[i-1]) left[i]=left[i-1]+1
    var right=1; var total=left[n-1].toLong()
    for(i in n-2 downTo 0) {
        right=if(rating[i]>rating[i+1]) right+1 else 1
        total+=maxOf(left[i],right).toLong()
    }
    return total
}
```

O(n) time, O(n) auxiliary space. The total can be Θ(n²), so it uses 64 bits. Output is one number.

- [] → 0
- [7] → 1
- [1,2,2] → 4
- [3,2,1] → 6

Twist: A circular arrangement adds an edge from the last child to the first. The linear proof no longer covers every neighbor relation; start from a global minimum or use an ordering-based graph assignment.

Practice source: https://leetcode.com/problems/candy/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="A greedy proof exchanges a choice in an optimal solution, then recurses on the remaining instance."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">exchange</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">feasible</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">repeat</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Optimal solution O</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Swap first choice</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Same-quality O′</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Smaller remaining task</div></foreignObject></svg></div><figcaption>A greedy proof exchanges a choice in an optimal solution, then recurses on the remaining instance.</figcaption></figure>

## Additional practice

### [Non-overlapping intervals](https://leetcode.com/problems/non-overlapping-intervals/) (Medium)

Hint: Which first selected interval leaves the most future room?

Approach: Sort by finish, keep an interval if start≥previous end, count deletions. Exchange the first interval of any optimal set with the earliest-finishing interval. O(n log n) time; O(n) space if copying input.

### [Jump game II](https://leetcode.com/problems/jump-game-ii/) (Medium)

Hint: Turn reachable frontiers into layers.

Approach: Track current layer end and farthest next end. When reaching the layer end, commit one jump. Detect unreachable gaps if the contract allows them. O(n) time, O(1) auxiliary space.

### [Gas station](https://leetcode.com/problems/gas-station/) (Medium)

Hint: What does a failed candidate tell you about every interior start?

Approach: Maintain net total and candidate tank. If tank falls below zero at i, no station since the current candidate can reach i+1; reset to i+1. Total≥0 ensures the surviving candidate works. O(n) time and O(1) space with 64-bit sums.

### [Task scheduler](https://leetcode.com/problems/task-scheduler/) (Medium)

Hint: The most frequent task creates forced gaps.

Approach: For unit tasks with a common cooldown k, the lower bound is max(N,(f−1)(k+1)+c), with c tasks attaining maximum frequency f. Argue construction or simulate with a heap/cooldown queue. O(N+alphabet) counting time; cooldown assumptions matter.

### [Merge triplets to form target triplet](https://leetcode.com/problems/merge-triplets-to-form-target-triplet/) (Medium)

Hint: Can any coordinate exceeding the target ever be undone?

Approach: Discard triplets exceeding a target coordinate. For each coordinate mark whether an eligible triplet reaches the target there. Coordinatewise maxima combine independently. O(n) time and O(1) space.

### [Maximum performance of a team](https://leetcode.com/problems/maximum-performance-of-a-team/) (Hard)

Hint: Freeze the bottleneck efficiency, then optimize speeds.

Approach: Sort engineers by descending efficiency; keep at most k largest speeds using a min-heap and a running sum. Evaluate sum×current efficiency. O(n log n+n log k) time, O(n+k) space including sorted records. Use 64-bit arithmetic and apply modulus only to the final maximum.

## Check your reasoning

Why is earliest finish valid for unweighted interval count?

1. It always selects the shortest interval
2. An optimal first interval can be replaced without reducing future feasibility
3. Sorting automatically proves optimality

Answer: 2. The exchange preserves count and leaves an endpoint no later, so every later selected interval remains feasible.

The jump array can contain negative maximum jumps. Can the same prefix proof be used?

1. Yes, unchanged
2. No; first define the new movement contract and reachability structure
3. Yes, if we sort it

Answer: 2. The proof uses the ability to choose any forward distance from zero through a nonnegative maximum. Changing the movement model can destroy prefix reachability.

## Transfer
A camera battery can cover any interval of time up to its charge length. Given start/end times for shows on one camera, choose the largest number of complete nonoverlapping shows. Touching endpoints are allowed. Now each show has a different revenue: does the same method maximize revenue?

Ignore revenue first. Compare two possible first shows by their end times.

Try one long high-revenue show versus two short low-revenue shows.

For count, sort by ending time and greedily accept compatible shows: exchange any optimal first show with the earliest-finishing one. O(n log n) time. Revenue breaks the count-preserving exchange; sort by end, find each compatible predecessor by binary search and use dp[i]=max(dp[i−1],value[i]+dp[p(i)]), O(n log n) time and O(n) space.

## Recall
- Derive the cookie exchange without referring to the code.
- Give a minimum-size counterexample to largest-coin-first.
- Explain why max(left,right), rather than their sum, is the minimal candy assignment.
- Distinguish a greedy choice from the data structure used to implement it.

## Sources
- [MIT 6.006 course materials](https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-fall-2011/): Reference link; not retrieved in this run. Primary algorithm-course reference for correctness and optimization; live retrieval status is documented separately.
- [Princeton Algorithms: minimum spanning trees](https://algs4.cs.princeton.edu/43mst/): Reference link; not retrieved in this run. Further study in greedy proofs and cut properties; not evidence of company question frequency.


# Pattern 17: Linear dynamic programming: decisions, states, and dependencies

Dynamic programming begins with a decision tree, not a table. Imagine solving a problem by asking what the final or next decision could be, recursively solving what remains, and combining the results. For climbing stairs, the final step was either one step or two steps; for robbery, a house is either taken or skipped. These choices generate many recursive calls, and different paths often ask the exact same remaining question. Recomputing that question is the waste that DP removes.

## Build the model
Dynamic programming begins with a decision tree, not a table. Imagine solving a problem by asking what the final or next decision could be, recursively solving what remains, and combining the results. For climbing stairs, the final step was either one step or two steps; for robbery, a house is either taken or skipped. These choices generate many recursive calls, and different paths often ask the exact same remaining question. Recomputing that question is the waste that DP removes.

A state is a complete description of a subproblem. It must preserve everything the future needs and forget everything else. If the future depends only on how many stairs remain, the index is sufficient. If a stock trade depends on whether you hold a share, an index alone is insufficient. Two histories may share a state only if every possible future continuation has the same legal choices and consequences from both histories. This is the central design test; a clever recurrence cannot repair a state that merges unlike futures.

Memoization stores the answer when a recursive state is first solved. The recursion still expresses the decision tree, but every distinct state is computed once. Count states and work per state to derive complexity; do not assume every memoized recursion is linear. Bottom-up DP evaluates the same dependency graph in an order where every needed predecessor is already known. Stairs depend on two smaller indices, so left-to-right evaluation works. Coin change for unlimited positive coins depends on smaller amounts, so increasing amount works. A recurrence that points forward instead may need reverse iteration or a different state definition.

Base values are semantic answers to the smallest subproblems. There is one way to climb zero stairs: take no steps. There are zero coins needed to make amount zero. There is zero available reward before the first job. An impossible positive amount is different from a legitimate zero-cost solution and needs an explicit sentinel. Never add one to an infinite sentinel without checking reachability: it can overflow into a value that incorrectly looks excellent.

Memory reduction is a later proof step. If a state depends only on the last two values, the rest of the table can be discarded after use. If the output includes a reconstruction, those discarded choices may be needed again, so keep parents or recompute them. In one-dimensional knapsack, loop direction controls whether the current item can be reused: ascending amounts allow unlimited reuse, descending amounts prevent it. The code's loop order is part of the mathematical model.

The hard scheduling example combines DP with sorting and binary search. Sorting jobs by finish time exposes a prefix of compatible earlier jobs. Taking a job contributes its reward plus the optimum on that compatible prefix; skipping it leaves the previous optimum. This is why earliest-finish greedy, although correct for maximum count, fails for weighted rewards. Learn to identify the precise decision and sufficient predecessor state, then let the recurrence select the implementation.

## Invariant
Each state answers a precise subproblem, and every transition partitions its feasible solutions into exhaustive choices. Base values describe empty or impossible subproblems accurately. Bottom-up order guarantees dependencies are solved before use; memory compression preserves every dependency until its final consumer.

## Recognize it
- A recursive take/skip or last-choice search repeats the same remaining subproblem.
- An optimum for a prefix can be expressed using a few smaller-prefix optima.
- The legal next choices depend on a compact state rather than the entire history.

## Execution trace
1. House rewards [2,7,9,3,1]; initially two=0,one=0

The empty prefix has reward zero; no selection is mandatory.

2. After 2: current=max(0,0+2)=2; two=0,one=2

Taking the first house beats skipping it.

3. After 7: current=max(2,0+7)=7; two=2,one=7

Taking 7 excludes adjacent 2.

4. After 9: current=max(7,2+9)=11; two=7,one=11

Taking 9 combines with the best prefix ending before its neighbor.

5. After 3 then 1: best values 11 then 12

Skip 3; then combine 1 with the earlier optimum 11 to choose indices 0,2,4.

## Wrong turns
### Always take the currently largest house.
[2,3,2]: taking 3 gives 3, but taking both 2s gives 4.

Compare take and skip for every prefix rather than committing to a local reward.

### Use largest-coin-first greedy for minimum coin count.
coins=[1,3,4],amount=6: greedy uses 4+1+1 (3 coins), while 3+3 uses 2.

Evaluate every possible final coin using solved predecessor amounts.

### Use earliest-finish scheduling for weighted reward.
Jobs [1,2,5],[2,3,5],[1,3,20]: earliest finish earns 10, but the longer job earns 20.

Use compatible-prefix take/skip DP; the unweighted exchange proof does not preserve reward.

### Use an ascending one-dimensional 0/1 subset loop.
One item of value 3 and target 6: ascending updates dp[3] then dp[6], reusing the same item twice.

For one-use items iterate amounts downward so dp[x-item] still describes the previous item layer.

### Climbing Stairs: From Decision Tree to Two Values (Easy)

For an integer n in 0..91, return as long the number of distinct sequences of one-step and two-step moves that climb exactly n stairs. n=0 has one empty sequence. Throw outside this range because larger answers can overflow long.

n=4 => 5: 1111,112,121,211,22.

Baseline: Recursively try a final step of size one or two without caching: exponential time and O(n) recursion depth.

1. Partition all valid sequences by their last move. Removing a final one leaves a sequence for n-1; removing a final two leaves one for n-2. The groups are disjoint and exhaustive.
2. Set ways(0)=1 and ways(1)=1. Memoization would compute n+1 states once; bottom-up evaluates ways(i)=ways(i-1)+ways(i-2).
3. Only the previous two states are needed, so update two long values. The n<=91 contract keeps every stored sum representable.

Correctness: Before computing step i, previous=ways(i-2) and current=ways(i-1). Their sum is ways(i) by the disjoint final-step partition.

```java
static long climbStairs(int n) {
    if(n<0 || n>91) throw new IllegalArgumentException("n must be in 0..91");
    if(n<=1) return 1;
    long previous=1,current=1;
    for(int i=2;i<=n;i++) {
        long next=previous+current; previous=current; current=next;
    }
    return current;
}
```

```kotlin
fun climbStairs(n: Int): Long {
    require(n in 0..91) { "n must be in 0..91" }
    if (n <= 1) return 1L
    var previous = 1L; var current = 1L
    for (i in 2..n) {
        val next = previous + current; previous = current; current = next
    }
    return current
}
```

O(n) time, O(1) auxiliary space, no recursion. BigInteger permits larger exact answers but changes arithmetic cost.

- n=0 => 1
- n=1 => 1
- n=4 => 5
- n=91 => 7540113804746346429
- n=92 => exception

Twist: If step sizes are {1,3,5}, ways(i) sums ways(i-s) for legal s and ways(0)=1. If individual stairs are blocked, set their ways to zero; two-state compression may no longer be enough.

Practice source: https://leetcode.com/problems/climbing-stairs/

### House Robber: Take or Skip a Prefix Endpoint (Medium)

Given a non-null int array of house rewards, choose any subset with no adjacent indices and return the largest total reward as long. Choosing none is allowed, so empty or entirely negative input returns 0. Input is unchanged.

[2,7,9,3,1] => 12 by choosing indices 0,2,4.

Baseline: Enumerate take/skip subsets recursively, disallowing adjacent takes: exponential time/O(n) recursion depth.

1. Let best(i) mean the greatest reward from the first i houses. If the final house is skipped, the answer is best(i-1).
2. If the final house is taken, its neighbor is excluded, leaving reward[i-1]+best(i-2). Take the maximum of these two exhaustive choices.
3. Use base best(0)=0 and a virtual best(-1)=0. Maintain the previous two prefix optima; negative rewards are naturally skipped.

Correctness: Before each reward x, one is the optimum of all processed houses and two is the optimum before their last house. The recurrence considers every feasible solution according to whether x is selected.

```java
static long rob(int[] rewards) {
    long two=0,one=0;
    for(int x:rewards) {
        long current=Math.max(one,two+x); two=one; one=current;
    }
    return one;
}
```

```kotlin
fun rob(rewards: IntArray): Long {
    var two = 0L; var one = 0L
    for (x in rewards) {
        val current = maxOf(one, two + x); two = one; one = current
    }
    return one
}
```

O(n) time, O(1) auxiliary space. Total reward is long; no recursion or mutation.

- [] => 0
- [-3,-1] => 0
- [5,1,5] => 10
- [2147483647,0,2147483647] => 4294967294

Twist: For houses on a circle, first and last are adjacent. For n>=2 take the max of solving [0,n-2] and [1,n-1]; handle n=1 separately. To return chosen indices, keep a DP table and reconstruct take/skip decisions.

Practice source: https://leetcode.com/problems/house-robber/

### Coin Change: Minimum Cost With Impossible States (Medium)

Given a non-null int array of strictly positive coin denominations and amount in 0..1,000,000, return the minimum number of coins making exactly amount using unlimited copies, or -1 if impossible. Empty coins make only amount zero. Throw for invalid amount or nonpositive coin. Input is unchanged.

coins=[1,2,5], amount=11 => 3 via 5+5+1; coins=[2],amount=3 => -1.

Baseline: Try all possible next coins recursively until the remainder is zero or negative: exponential branching; positive coins guarantee finite depth.

1. Let dp[x] be the minimum coins for exact amount x. For a last coin c<=x, the preceding amount is x-c, so consider dp[x-c]+1 only when that amount is reachable.
2. Set dp[0]=0; initialize all positive amounts to amount+1, which exceeds any valid minimum because every coin is at least one.
3. Iterate x upward so all dependencies x-c are ready. Unlimited reuse is intended: a denomination can appear in many consecutive predecessor steps. Return -1 if the final sentinel remains.

Correctness: When computing dp[x], every smaller amount is already optimal. Every exact representation ends with some coin, and removing it gives one of the predecessor states examined.

```java
static int coinChange(int[] coins,int amount) {
    if(amount<0 || amount>1_000_000) throw new IllegalArgumentException("amount");
    for(int c:coins) if(c<=0) throw new IllegalArgumentException("positive coins required");
    int unreachable=amount+1;
    int[] dp=new int[amount+1]; Arrays.fill(dp,unreachable); dp[0]=0;
    for(int x=1;x<=amount;x++) {
        for(int c:coins) if(c<=x && dp[x-c]!=unreachable)
            dp[x]=Math.min(dp[x],dp[x-c]+1);
    }
    return dp[amount]==unreachable ? -1 : dp[amount];
}
```

```kotlin
fun coinChange(coins: IntArray, amount: Int): Int {
    require(amount in 0..1_000_000) { "amount" }
    for (c in coins) require(c > 0) { "positive coins required" }
    val unreachable = amount + 1
    val dp = IntArray(amount + 1) { unreachable }; dp[0] = 0
    for (x in 1..amount) {
        for (c in coins) if (c <= x && dp[x - c] != unreachable)
            dp[x] = minOf(dp[x], dp[x - c] + 1)
    }
    return if (dp[amount] == unreachable) -1 else dp[amount]
}
```

O(Ac) time for amount A and c coin entries, O(A) auxiliary space. This is pseudo-polynomial in the numeric amount, not polynomial in the bit length of A.

- coins=[],amount=0 => 0
- coins=[],amount=3 => -1
- coins=[2],amount=3 => -1
- coins=[1,3,4],amount=6 => 2

Twist: To count unordered combinations, use coins as the outer loop and amounts ascending; to count ordered sequences, amounts outermost counts different orders separately. For one-use coins, amounts must descend when compressing the item dimension.

Practice source: https://leetcode.com/problems/coin-change/

### Weighted Job Scheduling: Compatible Prefix DP (Hard)

Given a non-null array of jobs [start,end,reward] with start<end and int rewards, return the maximum long reward from mutually nonoverlapping half-open jobs. Touching jobs are compatible. Negative rewards are allowed and may be skipped; empty input returns 0. Do not mutate input; invalid endpoints throw.

[[1,3,50],[2,4,10],[3,5,40],[3,6,70]] => 120 by choosing [1,3,50] and [3,6,70].

Baseline: Enumerate job subsets and test compatibility: exponential time, with O(n) recursion state.

1. Sort copied job references by end. Define dp[i] as the optimum using the first i jobs in this order.
2. For job i, binary-search among earlier jobs for the count p whose ends are <=start[i]. Taking job i allows exactly that compatible prefix, giving reward[i]+dp[p]; skipping gives dp[i].
3. Store dp[i+1]=max(skip,take). Every optimum either contains the current job or not. Sorting ensures that all jobs in a feasible earlier prefix finish before it starts.

Correctness: Before processing job i, dp[0..i] holds exact prefix optima. The binary search returns the full earlier compatible prefix, so taking this job combines independent feasible choices and skipping preserves the prior optimum.

```java
static long maxJobReward(int[][] jobs) {
    int[][] a=jobs.clone();
    for(int[] j:a) if(j[0]>=j[1]) throw new IllegalArgumentException("nonempty job required");
    Arrays.sort(a,Comparator.comparingInt((int[] j)->j[1]).thenComparingInt(j->j[0]));
    long[] dp=new long[a.length+1];
    for(int i=0;i<a.length;i++) {
        int lo=0,hi=i;
        while(lo<hi) {
            int mid=lo+(hi-lo)/2;
            if(a[mid][1]<=a[i][0]) lo=mid+1; else hi=mid;
        }
        dp[i+1]=Math.max(dp[i],(long)a[i][2]+dp[lo]);
    }
    return dp[a.length];
}
```

```kotlin
fun maxJobReward(jobs: Array<IntArray>): Long {
    for (j in jobs) require(j[0] < j[1]) { "nonempty job required" }
    val a = jobs.sortedWith(compareBy<IntArray> { it[1] }.thenBy { it[0] })
    val dp = LongArray(a.size + 1)
    for (i in a.indices) {
        var lo = 0; var hi = i
        while (lo < hi) {
            val mid = lo + (hi - lo) / 2
            if (a[mid][1] <= a[i][0]) lo = mid + 1 else hi = mid
        }
        dp[i + 1] = maxOf(dp[i], a[i][2].toLong() + dp[lo])
    }
    return dp[a.size]
}
```

O(n log n) time for sorting and n predecessor searches; O(n) copied references, sorting storage, and DP table. No recursion; output is scalar.

- [] => 0
- [[1,2,-5]] => 0
- [[1,2,5],[2,3,6]] => 11
- [[1,3,20],[1,2,5],[2,3,5]] => 20

Twist: If at most k jobs may be selected, add a selected-count dimension: dp[i][k]=max(dp[i-1][k],reward+dp[p][k-1]). Precompute predecessors once; O(nk+n log n) time and O(nk) space, reducible by count layers.

Practice source: https://leetcode.com/problems/maximum-profit-in-job-scheduling/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="The take/skip recurrence collapses an exponential decision tree into a prefix dependency graph."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">case: final house absent</text><path d="M175,72 Q290,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="290" y="46" text-anchor="middle" font-size="12">case: final house present</text><path d="M365,72 Q480,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="480" y="46" text-anchor="middle" font-size="12">exhaustive disjoint cases</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">two predecessor answers suffice</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">best(i): optimum for first i houses</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Skip final house: best(i−1)</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">Take final house: reward[i−1] + best(i−2)</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">best(i) = max(skip, take)</div></foreignObject></svg></div><figcaption>The take/skip recurrence collapses an exponential decision tree into a prefix dependency graph.</figcaption></figure>

## Additional practice

### [Min Cost Climbing Stairs](https://leetcode.com/problems/min-cost-climbing-stairs/) (Easy)

Hint: The state is minimum cost to reach a boundary, not necessarily to stand on a stair.

Approach: Let dp[i] be cost to reach step i; transition from i-1 and i-2 by paying the departure step. O(n), O(1) after compression; define the two starting positions.

### [Decode Ways](https://leetcode.com/problems/decode-ways/) (Medium)

Hint: Partition decodings by whether the final code uses one or two digits.

Approach: A nonzero final digit contributes dp[i-1]; a valid 10..26 pair contributes dp[i-2]. O(n), O(1). Leading zero produces no standalone decoding; use a bounded answer contract or BigInteger.

### [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/) (Medium)

Hint: The next choice depends on the last chosen value, not just how many were chosen.

Approach: Foundation DP: dp[i]=1+max(dp[j]) for j<i with a[j]<a[i], O(n²)/O(n). Advanced tails array stores minimum possible tail per length, using lower bound for O(n log n); tails are not necessarily the actual subsequence.

### [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/) (Medium)

Hint: A partition exists exactly when a subset reaches half the total.

Approach: For nonnegative values and even total, run boolean 0/1 knapsack to total/2 with descending sums. O(nT), O(T), pseudo-polynomial; ascending sums incorrectly reuse one-use items.

### [Coin Change II](https://leetcode.com/problems/coin-change-ii/) (Medium)

Hint: Unordered combinations and ordered sequences need different loop nesting.

Approach: Initialize ways[0]=1. For each denomination, update amounts ascending; each combination is counted when its largest processed denomination becomes available. O(cA), O(A); duplicate denominations must be deduplicated and counts need an overflow contract.

### [Best Time to Buy and Sell Stock with Cooldown](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/) (Medium)

Hint: Index alone is not a sufficient state: holding and cooldown change legal actions.

Approach: Track holding, sold-today, and resting states, updating from previous-day values only. O(n), O(1); impossible holding states use safe negative infinity or nullable reachability.

### [Edit Distance](https://leetcode.com/problems/edit-distance/) (Medium)

Hint: A pair of consumed-prefix lengths identifies the remaining comparison.

Approach: DP over two indices with insert/delete/replace transitions; O(mn) time and O(mn) space, reducible to O(min(m,n)) when only distance is needed. Detailed in the grid/string DP lesson.

## Check your reasoning

Why does an ascending sum loop fail for one-use subset sum?

1. It ignores item values
2. It may reuse the current item through a newly updated smaller sum
3. It is slower than descending iteration

Answer: 2. For a single item 3, ascending updates reach sum 3 and then sum 6 in the same item pass. Descending iteration keeps predecessors in the previous-item layer.

For weighted interval scheduling, what is the take transition after sorting by end?

1. reward[i] + best[latest compatible prefix]
2. reward[i] + best[i−1] always
3. Always select the earliest-finishing job

Answer: 1. The whole previous prefix may contain conflicting jobs. The compatible-prefix boundary, found by binary search, identifies exactly which solved optimum can safely precede this job.

## Transfer
A row of machines offers integer rewards. If you activate machine i, the next two machines must remain inactive. Find the maximum reward, allowing no activation.

Define the answer for the first i machines.

How far back is the last prefix that can coexist with taking the current machine?

Let dp[i] be the optimum for the first i machines, with dp[0]=0 and out-of-range negative prefixes treated as zero. For current machine i-1, skip gives dp[i-1], take gives reward[i-1]+dp[max(0,i-3)]. Thus dp[i]=max of these choices. O(n) time and O(n) table, compressible to three prior states in O(1) space; keep the table for reconstruction.

## Recall
- After one day: derive the maintained state from the brute-force repeated work without looking at code.
- After three days: explain the counterexample to each wrong turn and repair it.
- After one week: solve the transfer problem, state the invariant aloud, and compare both JVM implementations.

## Sources
- [Climbing Stairs: From Decision Tree to Two Values](https://leetcode.com/problems/climbing-stairs/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [House Robber: Take or Skip a Prefix Endpoint](https://leetcode.com/problems/house-robber/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Coin Change: Minimum Cost With Impossible States](https://leetcode.com/problems/coin-change/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.
- [Weighted Job Scheduling: Compatible Prefix DP](https://leetcode.com/problems/maximum-profit-in-job-scheduling/): Reference link; not retrieved in this run. Canonical problem statement and constraints; used to identify the technique and offer optional practice, not as evidence of company frequency.


# Pattern 18: Dynamic programming on grids and strings

Define an answer for every pair of boundaries, derive transitions from the final decision, then choose a dependency-safe evaluation order.

## Build the model
Imagine walking on a grid, allowed only to move right or down. The number of ways to reach a cell is not a property of the most recent route: every route ending immediately above can step down, and every route ending immediately to the left can step right. Those two sets are disjoint because their final steps differ. Therefore the count is the sum of the two neighboring counts. The recurrence follows from a partition of all valid solutions.

Now replace rows and columns with positions in two strings. A cell (i,j) can mean the answer for the first i characters of one string and the first j characters of another. These are **prefix lengths**, not character indices. There are m+1 possible lengths for a string of m characters because length zero represents the empty prefix. A large fraction of DP bugs come from mixing prefix lengths with indices. The current final characters, when they exist, are a[i−1] and b[j−1].

A recurrence is a proof written as a computation. For a longest common subsequence, ask whether an optimal answer uses the two final characters together. If they match, an optimal answer can pair them and append one to a solution for the shorter prefixes. If they differ, a common subsequence cannot use both mismatching final characters together, so at least one is excluded. Compare those two possibilities. Unlike a substring, a subsequence may skip positions, so a sliding window cannot describe the state.

Edit distance changes the objective: instead of maximizing matched characters, minimize paid operations. The last operation can delete a source character, insert a target character, or replace/match the final characters. Each option leads to a smaller pair of prefixes. The base cases describe converting to or from the empty string. They are not arbitrary initialization constants.

Write the state definition before the recurrence, then list every dependency. Draw arrows from a state to the smaller states it needs. A correct bottom-up order visits every dependency first. Right/down path counting can be evaluated row by row because above belongs to the preceding row and left belongs to the current row. One-dimensional storage works only when you know which entries still mean “previous row” and which mean “current row.” Save a diagonal value before overwriting it if a transition needs that diagonal.

Pattern matching with dot and star illustrates why guessing code from a table shape fails. The pattern is a small language. A star modifies the preceding atom, so consuming zero repetitions moves back two pattern positions. Consuming one more matching character moves back one string position while retaining the pattern position. The recurrence must encode the grammar, including empty-string matches. A table of the right dimensions with the wrong semantics is still wrong.

These examples use finite inputs and exact arithmetic within stated bounds. Counts can grow combinatorially: bigger grid constraints need a modulus or arbitrary-precision integers. Cost minimization needs explicit impossible-state sentinels when not every transition is valid. Reconstructing an actual sequence requires retaining decisions or a separate reconstruction pass; the optimized score-only table may not contain enough history.

## Invariant
dp[i][j] denotes the exact answer for the stated pair of prefixes or coordinates. Every transition enumerates an exhaustive set of final decisions, with no missing valid solution and no unjustified double counting. The evaluation order computes dependencies before their consumers. A space optimization must preserve this meaning even while entries change from previous-row to current-row values.

## Recognize it
- Two independently changing boundaries determine the remaining task: two string prefixes, a row and column, or an item count and capacity.
- Different paths reach the same state; their future depends on the state, not the route taken to it.
- A local choice can be suboptimal, but all choices reduce to smaller states that can be compared or combined.

## Execution trace
1. LCS a="ab", b="ac"; row0=[0,0,0]

An empty source has no common characters with any target prefix.

2. Process a[0]=a: dp[1][1]=1, dp[1][2]=1; row1=[0,1,1]

a matches a, so diagonal+1. Against c, compare skipping c or skipping a.

3. Process a[1]=b, j=1: old up=1, new left=0; dp[2][1]=1

b and a differ; exclude one of the final characters and keep the best.

4. j=2: b≠c; max(up=1,left=1)=1; row2=[0,1,1]

All required smaller states are final. LCS length is 1, achieved by "a".

## Wrong turns
### Treat subsequence as contiguous substring
a="abc", b="ac": longest common subsequence length is 2 (a,c), while the longest common contiguous substring length is 1.

Subsequence DP can skip either final character. Substring DP requires a matching suffix and resets to zero on mismatch.

### Initialize every empty-prefix answer to zero
Edit distance from "" to "abc" is 3, not 0. Regex empty string against "a*" is true, while against "a" is false.

Derive base cases from the contract and grammar before writing general transitions.

### Overwrite the previous row without saving a diagonal
In LCS of "a" and "aa", using an already updated left value as the previous-row diagonal can count the same source character twice and return 2.

Track diagonal before assigning dp[j]. Distinguish old up, new left, and saved diagonal at each step.

### Count right/down grid paths (Easy foundation)

An m by n grid has no obstacles. Starting at the top-left, move only right or down and count paths to the bottom-right. Require 1≤m,n≤20, so the answer fits Long. Inputs are dimensions, not a mutable grid.

m=3,n=3 → 6; m=1,n=4 → 1.

Baseline: Recursively branch right/down from every position. O(2^(m+n)) loose time bound and O(m+n) stack, repeatedly solving the same suffix grids.

1. Let dp[r][c] count ways to reach (r,c). Every non-start path arrives from above or left, and these final-step classes are disjoint.
2. The first row and first column each have one path. Initialize a one-row array to ones.
3. At each later row, dp[c] still holds the old above value; dp[c−1] already holds the new left value. Add them.
4. After the last row, dp[n−1] counts exactly all paths to the destination.

Correctness: During row r, positions before c hold row-r counts and positions from c onward hold row-(r−1) counts. Adding old above and new left implements the exhaustive final-step partition.

```java
static long paths(int m,int n) {
    if(m<1 || m>20 || n<1 || n>20) throw new IllegalArgumentException();
    long[] dp=new long[n]; Arrays.fill(dp,1L);
    for(int r=1;r<m;r++) for(int c=1;c<n;c++) dp[c]+=dp[c-1];
    return dp[n-1];
}
```

```kotlin
fun paths(m: Int,n: Int): Long {
    require(m in 1..20 && n in 1..20)
    val dp=LongArray(n){1L}
    for(r in 1 until m) for(c in 1 until n) dp[c]+=dp[c-1]
    return dp[n-1]
}
```

O(mn) time, O(n) auxiliary space and O(1) output. Use the smaller dimension for O(min(m,n)) space if desired. The maximum count C(38,19) fits 64 bits.

- 1×1 → 1: the empty sequence of moves is one path
- 1×20 → 1
- 2×2 → 2

Twist: With obstacles, set the current cell count to zero whenever blocked. If the start is blocked, no path exists. For larger dimensions choose BigInteger or a specified modulus.

Practice source: https://leetcode.com/problems/unique-paths/

### Longest common subsequence (Medium)

Return the length of a longest common subsequence of two strings. You may skip characters but may not reorder them. Empty strings are valid. This snippet compares UTF-16 Char units; use code-point arrays for a different character contract. Inputs are immutable.

"abcde", "ace" → 3. The common sequence skips b and d.

Baseline: Try every subsequence of the first string and check whether it occurs in the second: O((m+n)2^m) loose time bound and O(m) choice state.

1. Define dp[i][j] as the optimum for prefixes a[0,i) and b[0,j). Empty-prefix states are 0.
2. If the final characters match, pair them and add 1 to dp[i−1][j−1]. An optimal solution can be chosen to pair these equal final characters.
3. If they differ, at least one final character is unused; take max(dp[i−1][j],dp[i][j−1]).
4. Keep one row. Before overwriting dp[j], save its old value as the next diagonal.

Correctness: Before processing column j in row i, dp[j] is the previous-row answer, dp[j−1] is the current-row answer, and diagonal is the previous-row previous-column answer. The final-character case split is exhaustive.

```java
static int lcs(String a,String b) {
    int[] dp=new int[b.length()+1];
    for(int i=1;i<=a.length();i++) {
        int diagonal=0;
        for(int j=1;j<=b.length();j++) {
            int old=dp[j];
            dp[j]=a.charAt(i-1)==b.charAt(j-1)?diagonal+1:Math.max(dp[j],dp[j-1]);
            diagonal=old;
        }
    }
    return dp[b.length()];
}
```

```kotlin
fun lcs(a: String,b: String): Int {
    val dp=IntArray(b.length+1)
    for(i in 1..a.length) {
        var diagonal=0
        for(j in 1..b.length) {
            val old=dp[j]
            dp[j]=if(a[i-1]==b[j-1]) diagonal+1 else maxOf(dp[j],dp[j-1])
            diagonal=old
        }
    }
    return dp[b.length]
}
```

O(mn) time, O(n) auxiliary space, where m and n are UTF-16 lengths. Swapping strings can reduce memory to O(min(m,n)). Output is O(1).

- "", "abc" → 0
- "a", "aa" → 1
- "abc", "def" → 0

Twist: To return one actual subsequence, retain a full table and backtrack by equal-character diagonal or the larger neighbor; define deterministic tie-breaking. Linear-space reconstruction requires a more advanced divide-and-conquer method.

Practice source: https://leetcode.com/problems/longest-common-subsequence/

### Edit distance (Medium to advanced)

Return the minimum number of unit-cost insertions, deletions and replacements required to convert string a to b. Empty strings are valid. Compare UTF-16 Char units. Do not mutate input.

"horse" → "ros" costs 3; "" → "abc" costs 3.

Baseline: Recursively try every final operation without caching. Exponential time in m+n, O(m+n) recursion space; repeated prefix pairs dominate the work.

1. Let dp[i][j] be the minimum cost to turn a prefix of length i into a target prefix of length j.
2. The empty-source row is j insertions; the empty-target column is i deletions.
3. Delete the final source character (above+1), insert the final target character (left+1), or replace/match the final pair (diagonal+cost). Take the minimum.
4. Evaluate row by row and preserve the old diagonal before overwriting. Each valid edit script ends in one of the enumerated operations, and each transition constructs a valid script.

Correctness: Each stored state is the minimum over all scripts for its prefix pair. Previous-row values, current left, and saved diagonal correspond exactly to delete, insert, and pair transitions.

```java
static int editDistance(String a,String b) {
    int[] dp=new int[b.length()+1];
    for(int j=0;j<dp.length;j++) dp[j]=j;
    for(int i=1;i<=a.length();i++) {
        int diagonal=dp[0]; dp[0]=i;
        for(int j=1;j<=b.length();j++) {
            int old=dp[j];
            int cost=a.charAt(i-1)==b.charAt(j-1)?0:1;
            dp[j]=Math.min(diagonal+cost,Math.min(old+1,dp[j-1]+1));
            diagonal=old;
        }
    }
    return dp[b.length()];
}
```

```kotlin
fun editDistance(a: String,b: String): Int {
    val dp=IntArray(b.length+1){it}
    for(i in 1..a.length) {
        var diagonal=dp[0]; dp[0]=i
        for(j in 1..b.length) {
            val old=dp[j]
            val cost=if(a[i-1]==b[j-1]) 0 else 1
            dp[j]=minOf(diagonal+cost,old+1,dp[j-1]+1)
            diagonal=old
        }
    }
    return dp[b.length]
}
```

O(mn) time and O(n) auxiliary space for UTF-16 lengths m,n. Output is one integer. This unit-cost distance never exceeds max(m,n), though allocation limits constrain realistic inputs.

- "same", "same" → 0
- "abc", "" → 3
- "a", "b" → 1

Twist: If adjacent transposition is allowed, the transition set changes. If costs depend on context, two prefix lengths might no longer be sufficient state. Re-derive rather than replacing a constant blindly.

Practice source: https://leetcode.com/problems/edit-distance/

### Full regular-expression matching with dot and star (Hard)

Match the entire lowercase-English-letter string s against pattern p, whose atoms are lowercase letters or dot, optionally followed by star. Dot matches one character; x* matches zero or more x characters. Pattern syntax must be valid: no leading star or repeated star; invalid syntax throws. Empty string and pattern are valid. This is a restricted regex language, not the full Java regex engine.

s="aab", p="c*a*b" → true: zero c, two a, one b. s="ab", p=".*c" → false.

Baseline: Backtrack over every repetition split. Ambiguous repeated atoms can create exponential search and O(m+n) recursion depth.

1. Define dp[i][j] as whether the first i text characters match the first j pattern characters in full. Set dp[0][0]=true.
2. An ordinary atom needs a matching final character and dp[i−1][j−1].
3. For star, zero copies ignore atom+star: dp[i][j−2]. One more matching copy consumes a text character but keeps the same pattern prefix: dp[i−1][j].
4. Allow i=0 in every pattern column so zero-copy stars can construct empty matches. Fill rows from small to large, columns from left to right. Validate syntax before j−2 indexing.

Correctness: The table exactly represents full-prefix matches. The star cases partition matches by whether they use zero copies or at least one copy of the final atom; the latter removes one matched text character and remains in the same star state.

```java
static boolean regex(String s,String p) {
    for(int j=0;j<p.length();j++) {
        char c=p.charAt(j);
        if(c=='*') {if(j==0 || p.charAt(j-1)=='*') throw new IllegalArgumentException();}
        else if(c!='.' && (c<'a' || c>'z')) throw new IllegalArgumentException();
    }
    for(int i=0;i<s.length();i++) if(s.charAt(i)<'a'||s.charAt(i)>'z') throw new IllegalArgumentException();
    boolean[][] dp=new boolean[s.length()+1][p.length()+1]; dp[0][0]=true;
    for(int i=0;i<=s.length();i++) for(int j=1;j<=p.length();j++) {
        char atom=p.charAt(j-1);
        if(atom=='*') {
            char prev=p.charAt(j-2);
            dp[i][j]=dp[i][j-2] || (i>0 && (prev=='.'||prev==s.charAt(i-1)) && dp[i-1][j]);
        } else dp[i][j]=i>0 && (atom=='.'||atom==s.charAt(i-1)) && dp[i-1][j-1];
    }
    return dp[s.length()][p.length()];
}
```

```kotlin
fun regex(s: String,p: String): Boolean {
    for(j in p.indices) {
        val c=p[j]
        if(c=='*') require(j>0 && p[j-1]!='*')
        else require(c=='.' || c in 'a'..'z')
    }
    require(s.all { it in 'a'..'z' })
    val dp=Array(s.length+1){BooleanArray(p.length+1)}; dp[0][0]=true
    for(i in 0..s.length) for(j in 1..p.length) {
        val atom=p[j-1]
        dp[i][j]=if(atom=='*') {
            val prev=p[j-2]
            dp[i][j-2] || (i>0 && (prev=='.'||prev==s[i-1]) && dp[i-1][j])
        } else i>0 && (atom=='.'||atom==s[i-1]) && dp[i-1][j-1]
    }
    return dp[s.length][p.length]
}
```

O((m+1)(n+1)) time and auxiliary space; m is text length, n pattern length. Output is boolean. A two-row optimization is possible because zero-copy stars refer left in the same row and positive copies refer above.

- "", "" → true
- "", "a*b*" → true
- "a", "" → false
- "ab", ".*c" → false

Twist: Wildcard glob star matches any sequence by itself, so its recurrence skips one pattern character, not atom+star. Identical punctuation does not imply identical grammar.

Practice source: https://leetcode.com/problems/regular-expression-matching/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 760 180" role="img" aria-label="Each grid/string state depends on smaller boundary pairs; evaluate the arrows before their destination."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q385,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">diagonal</text><path d="M365,72 Q480,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="480" y="46" text-anchor="middle" font-size="12">above</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">left</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">dp[i−1][j−1]</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">dp[i−1][j]</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">dp[i][j−1]</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">dp[i][j]</div></foreignObject></svg></div><figcaption>Each grid/string state depends on smaller boundary pairs; evaluate the arrows before their destination.</figcaption></figure>

## Additional practice

### [Unique paths II](https://leetcode.com/problems/unique-paths-ii/) (Medium)

Hint: An obstacle has zero ways to be entered.

Approach: Use row-compressed path DP, setting blocked cells to zero, including the start. O(mn) time, O(n) auxiliary space; establish count overflow policy.

### [Minimum path sum](https://leetcode.com/problems/minimum-path-sum/) (Medium)

Hint: Replace sum of counts with minimum of predecessor costs.

Approach: dp[r][c]=grid[r][c]+min(above,left); missing predecessors are impossible, not zero. O(mn) time and O(n) auxiliary space. Negative values are fine in this acyclic movement model.

### [Distinct subsequences](https://leetcode.com/problems/distinct-subsequences/) (Hard)

Hint: Count choices of source positions, not just the best length.

Approach: dp[i][j]=dp[i−1][j]+(source[i−1]==target[j−1]?dp[i−1][j−1]:0). Empty target has one construction. One-row target updates must run backwards. O(mn) time, O(n) space; counts require appropriate numeric bounds.

### [Interleaving string](https://leetcode.com/problems/interleaving-string/) (Medium)

Hint: The consumed target length equals i+j.

Approach: dp[i][j] says whether prefixes of the first two strings form the target prefix of length i+j. Try consuming a matching character from either source. Reject total-length mismatch first. O(mn) time and O(n) space.

### [Longest palindromic subsequence](https://leetcode.com/problems/longest-palindromic-subsequence/) (Medium)

Hint: Now the state describes an interval of one string.

Approach: dp[l][r] is the best inside [l,r]. Equal endpoints may contribute 2 plus the inside; otherwise exclude one endpoint. Iterate by increasing interval length. O(n²) time and space; empty interval contributes 0.

### [Burst balloons](https://leetcode.com/problems/burst-balloons/) (Hard)

Hint: The final balloon in an interval has stable outside neighbors.

Approach: Pad with sentinel 1s; dp[l][r] is the best score for bursting strictly inside (l,r). Try each last k: dp[l][k]+dp[k][r]+a[l]*a[k]*a[r]. Evaluate short intervals before long ones. O(n³) time, O(n²) space; use long if bounds require.

## Check your reasoning

In row-compressed LCS, which value must be saved before dp[j] is overwritten?

1. The previous row’s previous-column value
2. Only the current left value
3. The whole source string reversed

Answer: 1. The matching transition needs the old diagonal. dp[j−1] already means the current row, so using it can reuse a source character.

Why does empty target have one construction in subsequence-counting DP?

1. There is exactly one choice: select no positions
2. It is a sentinel with no meaning
3. Every source character creates another empty result

Answer: 1. Counting DP counts choices of position sets. The empty set is one valid choice, including when the source is empty.

## Transfer
A migration tool converts a sequence of schema tokens into another using insert, delete or rename, each with a given nonnegative fixed cost. Derive the cheapest conversion. Then suppose renaming a token costs less if the previous action was also a rename: what changes?

Define the answer for every pair of consumed token-prefix lengths.

Does a prefix pair alone determine the future cost after the discount rule is added?

With fixed context-free costs, use edit-distance DP: min(delete cost+above, insert cost+left, rename/match cost+diagonal). Base rows/columns are accumulated insertion/deletion costs. O(mn) time and O(n) score-only space. A previous-action discount requires an additional state for the last action type, or another sufficient summary of discount eligibility. The old two-dimensional state merges histories with different future costs and is no longer sufficient.

## Recall
- Derive the grid-path recurrence by partitioning paths according to their final step.
- Draw old up, new left and saved diagonal for one LCS row update.
- Explain the edit-distance empty row without reading code.
- Derive zero and positive repetitions for star from the pattern grammar.
- What changes between maximizing a subsequence length and counting subsequences?

## Sources
- [MIT 6.006 dynamic programming materials](https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-spring-2020/): Reference link; not retrieved in this run. Primary course reference for state definitions and recurrence derivation; current retrieval is not verified.
- [Kotlin strings](https://kotlinlang.org/docs/strings.html): Reference link; not retrieved in this run. Language reference for string and character representation; see research ledger for verification status.


# Pattern 19: Range queries: precompute reusable pieces, then update only dependencies

A single interval query can scan its elements. Thousands of overlapping queries repeat the same additions or comparisons. Prefix arrays, Fenwick trees, and segment trees store reusable summaries; the right choice depends on whether values change and which operation the query combines.

## Build the model
First fix the interval convention. This lesson uses half-open [left,right): include left, exclude right. Its length is right-left, the full array is [0,n), and an empty interval has left==right. Prefix P[k] aggregates the first k elements, so P[0] is the identity and a range sum is P[right]-P[left]. Writing the convention before the algorithm prevents a large class of off-by-one mistakes.

A static prefix array works because addition has an inverse. The unwanted left prefix can be subtracted. Maximum has no such inverse: if prefix maxima are [5,5], those summaries cannot reveal whether the second value is 1 or 4. Associativity alone permits combining summaries, but subtraction requires more structure. This distinction explains why a prefix-max array cannot replace a segment tree.

Updates expose the weakness of complete prefixes. Changing a[0] affects every later prefix. A Fenwick tree stores selected blocks instead: at one-based index i, the stored interval ends at i and has length lowbit(i)=i & -i. For i=12, binary 1100, lowbit is 4, so the block covers one-based positions 9..12. A prefix query repeatedly subtracts lowbit to collect disjoint blocks; a point update repeatedly adds lowbit to visit every stored block containing that point. Each step changes a binary boundary, yielding O(log n) work. One-based indexing is essential: lowbit(0)=0 would never advance.

A segment tree explicitly stores summaries of a hierarchy of intervals. A leaf describes one element; a parent combines two child summaries. Changing one leaf invalidates only its ancestors. A query selects disjoint stored intervals whose union is the requested range. This works for any associative combine operation with an identity; commutativity is unnecessary if left-to-right order is preserved. The maximum implementation below uses an iterative 2n layout and a nonempty query result initialized to Long.MIN_VALUE. Empty intervals return null because no maximum exists.

Coordinate compression changes the axis. When only ordering matters, replace values by their ranks among distinct sorted values. Negative numbers and large gaps do not matter; equality must retain the same rank. Scanning an array from right to left then allows a frequency Fenwick tree to count how many already seen values have smaller ranks. This is not a spatial range query over original indices; it is a range query over value order.

Numeric contracts matter for sums. Long is wider than Int but still finite. The sum examples require every aggregate and intermediate subtraction/update to fit signed Long; they do not silently promise arbitrary-precision arithmetic. Maximum and rank counts have different numeric risks and should be analyzed separately.

## Invariant
Each stored summary corresponds to a precisely defined set of original elements. A query combines disjoint summaries covering exactly the requested interval, with neither gaps nor double counting. An update changes exactly the summaries whose sets contain the modified point. For compression, rank order and equality match original value order, so prefix frequency answers preserve the original comparison meaning.

## Recognize it
- Many queries ask for an aggregate over contiguous intervals of the same array.
- Static sums allow subtracting two prefixes, but point updates would invalidate many prefixes.
- The aggregate is associative, such as sum, minimum, maximum, gcd, or matrix multiplication: fixed interval summaries may compose.
- A value-order question such as counts smaller than a threshold can become a prefix query after coordinate compression.

## Execution trace
1. Values [3,-1,4,2]; one-based Fenwick blocks: bit[1]=3, bit[2]=2, bit[3]=4, bit[4]=8.

Each bit entry sums its lowbit-sized block ending at that index.

2. Query [1,4): prefix(4)=bit[4]=8; prefix(1)=bit[1]=3; result=5.

Subtract the first element to keep -1+4+2.

3. add external index 1 by +5: internal path 2→4; bit[2]=7, bit[4]=13.

Only stored blocks containing original index 1 change; bit[1] and bit[3] stay fixed.

4. Values now [3,4,4,2]; query [1,4)=13-3=10; query [2,2)=0.

The same decomposition remains valid after updating its exact dependencies.

## Wrong turns
### Compute a range maximum by subtracting two prefix maxima.
Arrays [5,1] and [5,4] both have prefix maxima [5,5]. Their range [1,2) maxima are 1 and 4, so no subtraction of those equal summaries can recover the answer.

Use a segment tree for mutable associative aggregates, or an appropriate sparse table for static idempotent queries.

### Use index zero directly in a one-based Fenwick update loop.
With i=0, i & -i=0. The update i+=lowbit(i) stays at zero forever and the loop does not terminate.

Translate external index p to internal p+1; prefix queries receive a count of elements and stop at zero.

### Static range sums with an empty prefix (Easy)

Construct RangeSum from a non-null Long array, then answer sum(left,right) over half-open intervals with 0≤left≤right≤n. Invalid bounds throw. Empty intervals return 0, including [0,0) on empty input. Copy required summaries so later caller mutation has no effect. Assume all prefix sums and returned differences fit signed Long.

[3,-1,4,2], sum(1,4)=5; sum(2,2)=0.

Baseline: Scan each requested range: O(length) per query, O(1) extra state; Q full-range queries cost O(Qn).

1. Define prefix[k] as the sum of the first k values. Start with prefix[0]=0, then compute prefix[i+1]=prefix[i]+a[i]. This also gives a valid representation for empty input.
2. The sum through right contains the unwanted first left values exactly once. Subtract prefix[left] to retain precisely [left,right). The half-open convention makes both endpoints direct prefix indices.
3. Check both ordering and bounds before indexing. A precomputed prefix snapshot means subsequent caller array mutation does not silently change the query object.

Correctness: prefix[k] equals the sum of a[0] through a[k-1] from construction time, so subtracting two nested prefixes leaves exactly the requested interval.

```java
static class RangeSum {
 private final long[] prefix;
 RangeSum(long[] values) {
  prefix=new long[values.length+1];
  for(int i=0;i<values.length;i++) prefix[i+1]=prefix[i]+values[i];
 }
 long sum(int left,int right) {
  int n=prefix.length-1;
  if(left<0||left>right||right>n) throw new IndexOutOfBoundsException();
  return prefix[right]-prefix[left];
 }
}
```

```kotlin
class RangeSum(values: LongArray) {
 private val prefix = LongArray(values.size + 1)
 init { for (i in values.indices) prefix[i + 1] = prefix[i] + values[i] }
 fun sum(left: Int, right: Int): Long {
  require(left >= 0 && left <= right && right < prefix.size)
  return prefix[right] - prefix[left]
 }
}
```

O(n) construction time and space; O(1) query. Long arithmetic is assumed to fit by contract; arbitrary precision would change arithmetic cost.

- []; sum(0,0) ⇒ 0
- [-5]; sum(0,1) ⇒ -5
- [3,-1,4,2]; sum(0,4) ⇒ 8

Twist: For a 2D static grid, build a prefix table and use inclusion-exclusion of four corners. Explain the overlap sign rather than memorizing it; construction O(RC), query O(1).

Practice source: https://leetcode.com/problems/range-sum-query-immutable/

### Point additions and range sums with a Fenwick tree (Medium)

Construct Fenwick from a non-null Long array. Support add(index,delta) and sum(left,right) over half-open intervals. Reject invalid indices or bounds; empty ranges sum to 0 and empty construction is allowed. The original array is not retained or mutated. Assume all values, stored block sums, query intermediates, and update arithmetic fit signed Long.

[3,-1,4,2]; add(1,5) changes the represented array to [3,4,4,2]; sum(1,4)=10.

Baseline: Rebuild a prefix array after every update: O(n) update and O(1) query. Or store raw values: O(1) update and O(n) query.

1. Use one-based bit[i] to store the block of length lowbit(i) ending at i. A point update belongs to successive ancestor blocks at i+=lowbit(i), so it touches only logarithmically many summaries.
2. To sum the first end elements, take bit[end], then end-=lowbit(end), repeating until zero. Blocks are disjoint because each step removes the rightmost block of the unprocessed prefix.
3. Range sum remains prefix(right)-prefix(left). Validate external indices before translating index+1; this prevents zero-based loops and partial invalid updates.

Correctness: bit[i] is always the sum of the represented values in one-based interval [i-lowbit(i)+1,i]; an update changes every and only containing block.

```java
static class Fenwick {
 private final int n; private final long[] bit;
 Fenwick(long[] values) {
  n=values.length; bit=new long[n+1];
  for(int i=0;i<n;i++) add(i,values[i]);
 }
 void add(int index,long delta) {
  if(index<0||index>=n) throw new IndexOutOfBoundsException();
  for(long i=(long)index+1;i<=n;i+=i&-i) bit[(int)i]+=delta;
 }
 private long prefix(int end) {
  long sum=0;
  for(int i=end;i>0;i-=i&-i) sum+=bit[i];
  return sum;
 }
 long sum(int left,int right) {
  if(left<0||left>right||right>n) throw new IndexOutOfBoundsException();
  return prefix(right)-prefix(left);
 }
}
```

```kotlin
class Fenwick(values: LongArray) {
 private val n = values.size
 private val bit = LongArray(n + 1)
 init { for (i in values.indices) add(i, values[i]) }
 fun add(index: Int, delta: Long) {
  require(index in 0 until n)
  var i = index.toLong() + 1
  while (i <= n.toLong()) { bit[i.toInt()] += delta; i += i and -i }
 }
 private fun prefix(end: Int): Long {
  var i = end
  var sum = 0L
  while (i > 0) { sum += bit[i]; i -= i and -i }
  return sum
 }
 fun sum(left: Int, right: Int): Long {
  require(left >= 0 && left <= right && right <= n)
  return prefix(right) - prefix(left)
 }
}
```

This simple construction is O(n log(n+1)); each update/query O(log(n+1)), space O(n). Empty queries are O(1). A linear Fenwick build is possible but not needed for the operation proof.

- empty; sum(0,0) ⇒ 0
- [1]; add(0,-3); sum(0,1) ⇒ -2
- negative input values are valid

Twist: For point assignment set(index,value), retain current values and compute delta=value-old under the same numeric contract. Range-add/range-sum needs two Fenwick trees or a lazy segment tree; one point-update tree is insufficient.

Practice source: https://leetcode.com/problems/range-sum-query-mutable/

### Mutable range maximum with an iterative segment tree (Medium to Hard)

Construct SegmentMax from a non-null Long array. Support set(index,value) and maximum(left,right) over half-open intervals. Empty intervals return null; invalid indices/bounds throw. Empty construction is allowed. Input is not mutated or retained. The 2n storage size must fit Int indexing and allocation. Long extremes are supported.

[-5,7,2,-1]; maximum(0,4)=7; set(1,-8); maximum(0,3)=2.

Baseline: Rescan each interval: O(n) worst-case query and O(1) assignment. A prefix maximum cannot remove an unwanted prefix.

1. Place original values at leaves tree[n+i], then build each parent as max of its two children. The iterative 2n layout works without rounding n to a power of two. In the compact 2n layout, an internal node can wrap around the original-index boundary when n is not a power of two; the iterative query still selects disjoint nodes covering exactly the desired leaves. A padded power-of-two layout makes every node a literal contiguous original-index interval.
2. For query leaf boundaries l=left+n and r=right+n, consume l when it is a right child, consume r-1 when r is an exclusive right boundary after a left child, then move both boundaries to their parents. The consumed nodes are disjoint.
3. For assignment change one leaf then recompute its ancestors. For a nonempty maximum initialize best to Long.MIN_VALUE; return null before the loop for an empty interval so a real Long.MIN_VALUE remains distinguishable from absence.

Correctness: Every internal node stores the maximum of its descendant leaves. During a query, already consumed nodes plus the still-uncovered boundary region partition the target interval.

```java
static class SegmentMax {
 private final int n; private final long[] tree;
 SegmentMax(long[] values) {
  n=values.length; tree=new long[Math.multiplyExact(2,n)];
  System.arraycopy(values,0,tree,n,n);
  for(int i=n-1;i>0;i--) tree[i]=Math.max(tree[2*i],tree[2*i+1]);
 }
 void set(int index,long value) {
  if(index<0||index>=n) throw new IndexOutOfBoundsException();
  int p=index+n; tree[p]=value;
  while(p>1) { p/=2; tree[p]=Math.max(tree[2*p],tree[2*p+1]); }
 }
 Long maximum(int left,int right) {
  if(left<0||left>right||right>n) throw new IndexOutOfBoundsException();
  if(left==right) return null;
  int l=left+n,r=right+n; long best=Long.MIN_VALUE;
  while(l<r) {
   if((l&1)==1) best=Math.max(best,tree[l++]);
   if((r&1)==1) best=Math.max(best,tree[--r]);
   l/=2; r/=2;
  }
  return best;
 }
}
```

```kotlin
class SegmentMax(values: LongArray) {
 private val n = values.size
 private val tree = LongArray(Math.multiplyExact(2, n))
 init {
  values.copyInto(tree, n)
  for (i in n - 1 downTo 1) tree[i] = maxOf(tree[2 * i], tree[2 * i + 1])
 }
 fun set(index: Int, value: Long) {
  require(index in 0 until n)
  var p = index + n
  tree[p] = value
  while (p > 1) { p /= 2; tree[p] = maxOf(tree[2 * p], tree[2 * p + 1]) }
 }
 fun maximum(left: Int, right: Int): Long? {
  require(left >= 0 && left <= right && right <= n)
  if (left == right) return null
  var l = left + n
  var r = right + n
  var best = Long.MIN_VALUE
  while (l < r) {
   if ((l and 1) == 1) { best = maxOf(best, tree[l]); l++ }
   if ((r and 1) == 1) { r--; best = maxOf(best, tree[r]) }
   l /= 2; r /= 2
  }
  return best
 }
}
```

O(n) construction time and storage; O(log(n+1)) assignment/query, O(1) for an empty query. Iterative operations use O(1) auxiliary work space.

- empty; maximum(0,0) ⇒ null
- [Long.MIN_VALUE]; maximum(0,1) ⇒ Long.MIN_VALUE
- [-3,-2,-7]; maximum(0,3) ⇒ -2

Twist: For string concatenation or matrix multiplication, preserve order with separate left and right accumulators: append left selections and prepend right selections, then combine. A single commutative max accumulator hides this issue. Range updates need lazy propagation.

Practice source: https://leetcode.com/problems/range-sum-query-mutable/

### Count strictly smaller values to the right (Hard)

Given a non-null Int array, return an Int array answer where answer[i] counts indices j>i with a[j]<a[i]. Equal values do not count. Empty input returns empty. Input is unchanged.

[5,2,6,1] returns [2,1,1,0]; [2,2] returns [0,0].

Baseline: Compare every pair i<j: O(n²) time, O(n) output space.

1. Process positions right to left. Previously processed values are exactly the suffix to the current position, so the query becomes a dynamic count of seen values below a threshold.
2. Sort a copy and retain distinct values to assign ranks. A frequency Fenwick tree indexed by rank stores how many seen values have each rank. Query the prefix of ranks strictly below the current rank before inserting the current occurrence.
3. Duplicates share a rank. Querying before insertion excludes the current index, and excluding the current rank excludes equal values. Each returned count is at most n-1 and fits Int.

Correctness: Before processing i the frequency tree contains exactly a[i+1..n); compression preserves strict ordering and equalities, so prefix(rank(a[i])) counts exactly qualifying suffix indices.

```java
static int[] countSmaller(int[] a) {
 int[] sorted=a.clone(); Arrays.sort(sorted); int unique=0;
 for(int value:sorted) if(unique==0||sorted[unique-1]!=value) sorted[unique++]=value;
 int[] bit=new int[unique+1], out=new int[a.length];
 for(int i=a.length-1;i>=0;i--) {
  int rank=Arrays.binarySearch(sorted,0,unique,a[i]); int count=0;
  for(int p=rank;p>0;p-=p&-p) count+=bit[p];
  out[i]=count;
  for(long p=(long)rank+1;p<=unique;p+=p&-p) bit[(int)p]++;
 }
 return out;
}
```

```kotlin
fun countSmaller(a: IntArray): IntArray {
 val sorted = a.sortedArray()
 var unique = 0
 for (value in sorted) {
  if (unique == 0 || sorted[unique - 1] != value) sorted[unique++] = value
 }
 val bit = IntArray(unique + 1)
 val out = IntArray(a.size)
 for (i in a.lastIndex downTo 0) {
  val rank = Arrays.binarySearch(sorted, 0, unique, a[i])
  var p = rank
  var count = 0
  while (p > 0) { count += bit[p]; p -= p and -p }
  out[i] = count
  var update = rank.toLong() + 1
  while (update <= unique.toLong()) { bit[update.toInt()]++; update += update and -update }
 }
 return out
}
```

O(n log(n+1)) sorting/query/update time; O(n) compressed copy and output plus O(u) frequency tree for u distinct values. Counts fit Int because they count at most n-1 array positions.

- [] ⇒ []
- [2,2] ⇒ [0,0]
- [-1,-2,-3] ⇒ [2,1,0]
- [Int.MAX_VALUE,Int.MIN_VALUE] ⇒ [1,0]

Twist: For counts ≤current, query through its rank instead of stopping before it. For inversion total sum the individual counts using Long; the total can reach n(n-1)/2.

Practice source: https://leetcode.com/problems/count-of-smaller-numbers-after-self/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 1330 180" role="img" aria-label="Fenwick prefix(13) decomposes the first 13 elements into disjoint blocks."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">take lowbit block</text><path d="M175,72 Q290,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="290" y="46" text-anchor="middle" font-size="12">subtract 1</text><path d="M555,72 Q575,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="575" y="46" text-anchor="middle" font-size="12">take lowbit block</text><path d="M555,72 Q670,72 785,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="670" y="46" text-anchor="middle" font-size="12">subtract 4</text><path d="M935,72 Q955,72 975,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="955" y="46" text-anchor="middle" font-size="12">take lowbit block</text><path d="M935,72 Q1050,72 1165,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="1050" y="46" text-anchor="middle" font-size="12">subtract 8</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">prefix end 13 (1101)</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">block 13..13, length 1</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">prefix end 12 (1100)</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">block 9..12, length 4</div></foreignObject><rect x="785" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="790" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">prefix end 8 (1000)</div></foreignObject><rect x="975" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="980" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">block 1..8, length 8</div></foreignObject><rect x="1165" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="1170" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">end 0</div></foreignObject></svg></div><figcaption>Fenwick prefix(13) decomposes the first 13 elements into disjoint blocks.</figcaption></figure>

## Additional practice

### [Range Sum Query 2D - Immutable](https://leetcode.com/problems/range-sum-query-2d-immutable/) (Medium)

Hint: Four prefixes use inclusion-exclusion.

Approach: P[r][c] sums a rectangle from origin; query uses bottom-right minus top strip minus left strip plus their overlap. O(RC) build/O(1) query/O(RC) space; use Long.

### [NumArray Mutable Range Sum](https://leetcode.com/problems/range-sum-query-mutable/) (Medium)

Hint: Assignment must become a delta or leaf replacement.

Approach: Fenwick plus retained values supports set through value-old, or segment tree replaces leaf and ancestors. O(log n) operations/O(n) space; clearly define interval endpoints.

### [Reverse Pairs](https://leetcode.com/problems/reverse-pairs/) (Hard)

Hint: The threshold is twice a value, not merely its rank.

Approach: Scan and query counts satisfying a[i]>2*a[j] using compressed values and binary-search bounds, or use merge-sort counting. O(n log n)/O(n); multiply using Long.

### [Count of Range Sum](https://leetcode.com/problems/count-of-range-sum/) (Hard)

Hint: Prefix sums transform a subarray range into a prior-prefix interval.

Approach: For current prefix p, count prior prefixes in [p-upper,p-lower] using compressed coordinates and a Fenwick tree, then insert p. O(n log n)/O(n); include initial zero prefix and use Long.

### [The Skyline Problem](https://leetcode.com/problems/the-skyline-problem/) (Hard)

Hint: Event coordinates compress a huge horizontal domain.

Approach: Sort building edge events and maintain active height counts/max via an ordered multiset or heap. O(n log n)/O(n); process all events at the same x together. This is a useful contrast: not every compressed-coordinate task needs a Fenwick tree.

### [Falling Squares](https://leetcode.com/problems/falling-squares/) (Hard)

Hint: Range maximum followed by range assignment needs more than point updates.

Approach: Compress square interval endpoints, query covered maximum, then assign that range to max+size using a lazy segment tree. O(n log n)/O(n); model geometric half-open spans so merely touching squares do not overlap.

## Check your reasoning

Which property allows rangeSum(l,r)=prefix(r)-prefix(l)?

1. Addition has an inverse for the unwanted prefix.
2. All values are nonnegative.
3. All arrays have even length.

Answer: 1. Negative values are fine; the subtraction law follows from additive cancellation, which maximum does not have.

In count-smaller, why query prefix(rank) before inserting rank+1?

1. To exclude equal ranks and the current occurrence.
2. To reverse the input permanently.
3. To avoid sorting.

Answer: 1. Internal rank+1 represents the current value; prefix(rank) includes only strictly smaller coordinates, and insertion afterward limits state to j>i.

## Transfer
A live leaderboard assigns each known player a score. Support point score replacement and ask for the maximum score among player IDs in an interval. Later product asks for the number of players whose score is below a threshold. Explain whether the same index answers both efficiently.

The first query orders players by ID. The second orders them by score.

A segment maximum loses the distribution below its maximum.

A segment tree indexed by player ID supports replacement and interval maximum in O(log n), storing one maximum per node. It cannot answer score-threshold counts from those maxima alone because many distributions share the same maximum. For a global threshold count with known score coordinates, maintain a separate frequency Fenwick tree over compressed scores, decrementing the old rank and incrementing the new rank on replacement. If new scores arrive outside known coordinates, use an order-statistic balanced tree or rebuild compression; for both an ID interval and score threshold, a two-dimensional data structure is required.

## Recall
- Draw the one-based block represented by bit[12] and derive both update and query transitions.
- Explain why associativity is enough for a segment tree but insufficient for subtracting prefixes.
- Implement strict-smaller counts and then change the boundary for smaller-or-equal.

## Sources
- [LeetCode immutable range sums](https://leetcode.com/problems/range-sum-query-immutable/): Reference link; not retrieved in this run. Primary static range-sum contract; this lesson uses explicit half-open endpoints.
- [LeetCode mutable range sums](https://leetcode.com/problems/range-sum-query-mutable/): Reference link; not retrieved in this run. Primary point-update/range-query exercise, adapted to sum and maximum.
- [Count of Smaller Numbers After Self](https://leetcode.com/problems/count-of-smaller-numbers-after-self/): Reference link; not retrieved in this run. Primary strict-smaller suffix-count task.


# Pattern 20: Bit manipulation and state compression

Represent yes/no facts directly in binary digits, cancel paired information, or compress a small chosen set into one integer. Derive each operation from its meaning before using a formula.

## Build the model
A bit is one binary digit, either 0 or 1. A nonnegative integer is a row of such digits. For example 13 is binary 1101 because it contains 8 + 4 + 1. Number the positions from the right, starting at zero. Bit i represents the value 2^i. A mask can instead use position i to mean “item i has been selected.” Then mask 1101 represents the set {0, 2, 3}; its numeric value is merely a compact encoding of that set. Do not confuse a value's magnitude with the membership meaning you choose for its bits.

Work one column at a time. AND produces 1 only when both inputs have 1. OR produces 1 when either has 1. XOR produces 1 when the inputs differ. Therefore x XOR x = 0 and x XOR 0 = x. XOR is associative and commutative, so a sequence containing pairs cancels independently of order. That is a precise frequency-parity fact. It does not recover arbitrary frequency counts: three copies leave the original bit pattern because three is odd. An XOR accumulator is useful only when the output can be derived from the parity information it retains.

In Java use &, |, ^, ~, <<, >>, and >>>. In Kotlin use and, or, xor, inv(), shl, shr, and ushr. Parenthesize expressions instead of trusting mixed operator precedence. A membership test is (mask & (1 << i)) != 0 in Java, or (mask and (1 shl i)) != 0 in Kotlin. Adding membership is OR; removing membership is AND with the complemented single-bit mask. XOR toggles membership, which removes a present bit but adds an absent one. That distinction matters.

A signed Int has 32 bits, including its sign representation. XOR cancellation still works for negative values because it operates on the complete fixed-width bit pattern. Arithmetic right shift copies the sign bit; unsigned right shift fills with zeros. A positive-power mask represented with Int is most straightforward for positions 0 through 30. Java and Kotlin/JVM mask shift distances for Int operations, so shifting by 32 does not create a thirty-third bit. Use Long when the intended universe is wider, and still respect its finite width.

Two small identities are worth deriving. If x is positive, subtracting one turns its lowest 1 into 0 and changes lower zero bits to ones. Consequently x AND (x - 1) removes exactly the lowest set bit. In fixed-width two's complement, x AND (-x) isolates that bit, including the sign bit when it is the only set bit. These are representations with proof, not incantations.

Finally, a bitmask does not make exponential search polynomial. A subset of n elements has 2^n possible masks. Compression makes state storage and transitions convenient. The final assignment problem combines that representation with dynamic programming, merging all histories that selected the same items. Its optimization comes from equivalent future choices, not from the presence of bitwise symbols.

## Invariant
Always say what each bit or accumulator means. For parity accumulation, after consuming the first k values, bit b equals the parity of the number of consumed values having bit b set. Appending a value flips exactly its set columns, preserving that meaning. Equal paired values flip each of their columns twice and disappear. For a subset mask, bit j is 1 exactly when item j is used. A transition may set a previously zero bit; then the number of chosen items increases by exactly one. In dynamic programming, dp[mask] is the best cost among every partial assignment having precisely that used set. These invariants are different; using XOR cancellation does not justify a subset optimization or vice versa.

## Recognize it
- Every value occurs an even number of times except a few: XOR cancels equal pairs without storing a frequency table.
- The question manipulates membership of a small fixed universe: one bit per element can encode the entire chosen set.
- You need to count, isolate, or remove set bits: inspect how the lowest 1 changes under subtraction.
- You see subset enumeration and n is roughly 20 or smaller: an exponential bitmask state may be feasible; inspect the actual memory/time bound first.

## Execution trace
1. Single Number input [4, 1, 4, 2, 2]; accumulator = 0

No values consumed, so every bit has even parity.

2. Consume 4 => 4; consume 1 => 5 (0101)

The accumulator stores column parity, not the largest or most recent value.

3. Consume second 4 => 1

Both copies of 4 cancel; 1 remains.

4. Consume 2 => 3; consume second 2 => 1

The second pair cancels, leaving the sole unpaired value.

5. Assignment a=[1,2], b=[2,3]: dp[00]=0; dp[01]=3; dp[10]=2

The first a value is assigned to either b position, recording which one was used.

6. dp[11]=min(3+(2 XOR 3), 2+(2 XOR 2)) = min(4,2)=2

Different histories converge to the same completed set; retain the minimum.

## Wrong turns
### XOR all values to find the value that appears once even if the others occur three times.
[2, 2, 2, 3] XORs to 1, not the unique value 3. Three copies have odd parity and do not disappear.

For triplicates, count each bit modulo 3, or derive a two-register finite-state machine. State the frequency assumption first.

### Use XOR as an unconditional “remove this element” operation.
mask = 0100, remove item 1 using XOR 0010 gives 0110, adding an absent item.

Use mask & ~(1 << i), or prove membership before toggling.

### Assume a compact mask makes an arbitrary large subset problem cheap.
n = 40 needs 2^40 states, over one trillion, despite each state fitting in one Long.

Compute state count before allocation. Find a different structure, meet-in-the-middle, or a valid pruning argument.

### One unpaired integer (Easy)

Given a nonnull, nonempty Int array, exactly one value occurs once and every other distinct value occurs twice. Return the unpaired value. Values may be negative. Do not mutate the array. The frequency promise is a precondition, not validated by the implementation.

[4,1,4,2,2] => 1; paired 4 and 2 cancel.

Baseline: Count each value in a hash map, then find count 1: expected O(n) time and O(n) space. A pairwise search needs O(n²) time and O(1) extra space.

1. Each paired value contributes an even number of occurrences to every bit column.
2. Use one XOR accumulator to remember column parity rather than complete counts.
3. XOR all values. The only uncancelled bit pattern is the once-occurring integer.

Correctness: After each prefix, accumulator equals the XOR of that prefix. By associativity, all full pairs cancel at the end and leave the unique value.

```java
static int singleNumber(int[] nums) {
    Objects.requireNonNull(nums, "nums");
    if (nums.length == 0) throw new IllegalArgumentException("nonempty required");
    int answer = 0;
    for (int x : nums) answer ^= x;
    return answer;
}
```

```kotlin
fun singleNumber(nums: IntArray): Int {
    require(nums.isNotEmpty()) { "nonempty required" }
    var answer = 0
    for (x in nums) answer = answer xor x
    return answer
}
```

O(n) time for n values; O(1) extra space, no output collection. Java null throws; Kotlin nonnull parameters reject null.

- [0] => 0
- [-1,2,2] => -1
- [Int.MIN_VALUE,7,7] => Int.MIN_VALUE

Twist: If all other values appear three times, parity loses necessary information. Count each of the 32 bit columns modulo 3, then assemble the remaining pattern; O(32n) time, O(1) space.

Practice source: https://leetcode.com/problems/single-number/

### Count set bits for every prefix value (Easy)

Given n in [0, 1_000_000], return an Int array answer of length n+1 with answer[x] equal to the number of 1 bits in nonnegative x. The bound explicitly limits allocation. There is no input mutation.

n=5 => [0,1,1,2,1,2], corresponding to 0,1,10,11,100,101.

Baseline: For each x, inspect every binary position independently: O(n log(n+1)) time and O(n) output space.

1. Deleting the lowest set bit produces a smaller integer with exactly one fewer 1.
2. Thus count[x] = count[x & (x-1)] + 1. Initialize count[0]=0.
3. Process x increasingly so the smaller dependency is already available.

Correctness: After finishing x, count[0..x] is correct. The recurrence removes exactly one 1 and references a smaller correct value.

```java
static int[] countBits(int n) {
    if (n < 0 || n > 1_000_000) throw new IllegalArgumentException("n out of range");
    int[] count = new int[n + 1];
    for (int x = 1; x <= n; x++) count[x] = count[x & (x - 1)] + 1;
    return count;
}
```

```kotlin
fun countBits(n: Int): IntArray {
    require(n in 0..1_000_000) { "n out of range" }
    val count = IntArray(n + 1)
    for (x in 1..n) count[x] = count[x and (x - 1)] + 1
    return count
}
```

O(n+1) time and O(n+1) output space; O(1) auxiliary space excluding the required result. Fixed-width Int operations are constant time.

- n=0 => [0]
- n=1 => [0,1]
- n=8 => [0,1,1,2,1,2,2,3,1]

Twist: For one arbitrary signed Int, repeatedly clear the lowest set bit until zero. At most 32 iterations count the complete two's-complement representation, including the sign bit.

Practice source: https://leetcode.com/problems/counting-bits/

### Two unpaired integers (Medium)

Given a nonnull Int array, exactly two distinct values occur once each; every other value occurs twice. Return the two unique values in ascending order. Negative values are allowed. Input is not mutated. The frequency promise is assumed.

[1,2,1,3,2,5] => [3,5]. All XOR = 6 (0110); bit 1 separates 3 and 5.

Baseline: A frequency map uses expected O(n) time and O(n) space. Sorting a copy uses O(n log n) time and O(n) space.

1. XORing everything yields a XOR b. It is nonzero because a and b are distinct.
2. Choose any set bit in this result; at this position a and b differ. Isolate the lowest one with diff & -diff.
3. Partition values by that bit and XOR within each group. Equal pairs stay in one group and cancel; each unique value lands in a different group.
4. Sort the two recovered values for the specified output contract.

Correctness: Within each partition, paired equal values cancel. Since the chosen bit differs between the two singletons, exactly one uncancelled value remains in each partition.

```java
static int[] twoSingles(int[] nums) {
    Objects.requireNonNull(nums, "nums");
    int diff = 0;
    for (int x : nums) diff ^= x;
    if (diff == 0) throw new IllegalArgumentException("promise violated");
    int bit = diff & -diff;
    int a = 0, b = 0;
    for (int x : nums) {
        if ((x & bit) == 0) a ^= x;
        else b ^= x;
    }
    return new int[] {Math.min(a, b), Math.max(a, b)};
}
```

```kotlin
fun twoSingles(nums: IntArray): IntArray {
    var diff = 0
    for (x in nums) diff = diff xor x
    require(diff != 0) { "promise violated" }
    val bit = diff and -diff
    var a = 0
    var b = 0
    for (x in nums) {
        if ((x and bit) == 0) a = a xor x
        else b = b xor x
    }
    return intArrayOf(minOf(a, b), maxOf(a, b))
}
```

O(n) time across two scans; O(1) auxiliary and output space. The negation of Int.MIN_VALUE wraps on JVM by design; diff & -diff still isolates the sign bit correctly.

- [0,1] => [0,1]
- [-1,0,2,2] => [-1,0]
- [Int.MIN_VALUE,0] => [Int.MIN_VALUE,0]

Twist: If three values are unique, the total XOR and a single separating bit do not in general isolate all three. A hash map is a clear general solution; do not extrapolate the two-value partition proof.

Practice source: https://leetcode.com/problems/single-number-iii/

### Minimum XOR assignment (Hard)

Given nonnull arrays a and b of equal length n, 0 <= n <= 14, values in [0, 10_000_000], assign every b position to exactly one a position. Return the minimum sum of (a[i] XOR assignedB[i]) as Long. Equal values at different indices are separate items. Inputs are not mutated. Empty arrays return 0.

a=[1,2], b=[2,3] => 2 by assigning [3,2]: (1 XOR 3)+(2 XOR 2)=2+0. The original order costs 3+1=4.

Baseline: Enumerate all n! permutations of b and evaluate each in O(n), giving O(n*n!) time and O(n) recursion/path space.

1. Fix the order of a. After k assignments, only which b indices were used determines the remaining choices.
2. Encode used indices as mask. The next a index is popcount(mask), so no second state coordinate is needed.
3. Define dp[mask] as the minimum partial sum for exactly that used set; initialize only dp[0]=0.
4. For each zero bit j, set it and add a[k] XOR b[j]. Retain the minimum when histories merge.
5. Every assignment reaches the all-ones mask. Each transition adds a bit and increases the mask numerically, so increasing-mask iteration respects dependencies.

Correctness: Before processing a mask, dp[mask] is minimal over all ways to assign the first popcount(mask) elements to its used indices. Every predecessor is smaller and has been processed. Trying each unused j covers every next assignment, and discarding a more expensive history with the same mask cannot worsen any common future completion.

```java
static long minimumXorSum(int[] a, int[] b) {
    Objects.requireNonNull(a, "a");
    Objects.requireNonNull(b, "b");
    int n = a.length;
    if (n != b.length || n > 14) throw new IllegalArgumentException("invalid lengths");
    for (int x : a) if (x < 0 || x > 10_000_000) throw new IllegalArgumentException("value out of range");
    for (int x : b) if (x < 0 || x > 10_000_000) throw new IllegalArgumentException("value out of range");
    int size = 1 << n;
    long[] dp = new long[size];
    Arrays.fill(dp, Long.MAX_VALUE);
    dp[0] = 0;
    for (int mask = 0; mask < size - 1; mask++) {
        int k = Integer.bitCount(mask);
        if (dp[mask] == Long.MAX_VALUE) continue;
        for (int j = 0; j < n; j++) {
            int bit = 1 << j;
            if ((mask & bit) == 0) {
                int next = mask | bit;
                dp[next] = Math.min(dp[next], dp[mask] + (long) (a[k] ^ b[j]));
            }
        }
    }
    return dp[size - 1];
}
```

```kotlin
fun minimumXorSum(a: IntArray, b: IntArray): Long {
    val n = a.size
    require(n == b.size && n <= 14) { "invalid lengths" }
    require(a.all { it in 0..10_000_000 } && b.all { it in 0..10_000_000 }) { "value out of range" }
    val size = 1 shl n
    val dp = LongArray(size) { Long.MAX_VALUE }
    dp[0] = 0L
    for (mask in 0 until size - 1) {
        val k = Integer.bitCount(mask)
        if (dp[mask] == Long.MAX_VALUE) continue
        for (j in 0 until n) {
            val bit = 1 shl j
            if ((mask and bit) == 0) {
                val next = mask or bit
                dp[next] = minOf(dp[next], dp[mask] + (a[k] xor b[j]).toLong())
            }
        }
    }
    return dp[size - 1]
}
```

O(n*2^n) time and O(2^n) auxiliary space, O(1) output space. Bit counting is constant time for the fixed 32-bit state. Long sums prevent overflow under the stated bounds.

- a=[], b=[] => 0
- a=[0], b=[7] => 7
- a=[2,2], b=[2,2] => 0
- a=[1,2], b=[2,3] => 2

Twist: For a general n-by-n cost matrix, replace XOR cost with cost[k][j]; the state proof is unchanged. For much larger n, investigate polynomial assignment algorithms rather than allocating 2^n states.

Practice source: https://leetcode.com/problems/minimum-xor-sum-of-two-arrays/

## Visual model

<figure><div class="diagram-scroll"><svg viewBox="0 0 950 180" role="img" aria-label="A subset records membership; a DP transition adds one unused item and remembers the least cost."><defs><marker id="arrow" markerWidth="8" markerHeight="8" refX="7" refY="3" orient="auto"><path d="M0,0 L0,6 L8,3 z" fill="#3a677e"/></marker></defs><path d="M175,72 Q195,72 215,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="195" y="46" text-anchor="middle" font-size="12">test bit 1 = 0</text><path d="M365,72 Q385,72 405,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">set bit 1; add XOR cost</text><path d="M175,72 Q385,72 595,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="385" y="46" text-anchor="middle" font-size="12">test bit 2 = 0</text><path d="M745,72 Q765,72 785,72" stroke="#3a677e" fill="none" marker-end="url(#arrow)"/><text x="765" y="46" text-anchor="middle" font-size="12">set bit 2; add XOR cost</text><rect x="25" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="30" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">mask 001: item 0 used; next position 1</div></foreignObject><rect x="215" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="220" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">choose unused item 1</div></foreignObject><rect x="405" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="410" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">mask 011: items 0 and 1 used</div></foreignObject><rect x="595" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="600" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">choose unused item 2</div></foreignObject><rect x="785" y="52" width="150" height="66" rx="8" fill="#e9f1f6" stroke="#52748b"/><foreignObject x="790" y="57" width="140" height="60"><div xmlns="http://www.w3.org/1999/xhtml" style="font:14px system-ui;text-align:center;line-height:1.3;padding:5px;color:#203447">mask 101: items 0 and 2 used</div></foreignObject></svg></div><figcaption>A subset records membership; a DP transition adds one unused item and remembers the least cost.</figcaption></figure>

## Additional practice

### [Number of 1 Bits](https://leetcode.com/problems/number-of-1-bits/) (Easy)

Hint: Remove the lowest remaining 1 each iteration.

Approach: Use x &= x-1 until zero. O(w) worst-case time, O(1) space for w-bit integers. Twist: count the full two's-complement representation of negative inputs; unsigned shifts avoid nontermination in a scanning version.

### [Missing Number](https://leetcode.com/problems/missing-number/) (Easy)

Hint: Both index range and input are almost the same multiset.

Approach: XOR all integers 0..n and all input values: common elements cancel. O(n) time, O(1) space. Requires distinct input values drawn from 0..n; duplicates break the promise.

### [Power of Two](https://leetcode.com/problems/power-of-two/) (Easy)

Hint: A positive power of two has exactly one set bit.

Approach: Require n>0 and (n & (n-1))==0. O(1) time/space for fixed-width inputs. Zero must be rejected, and a negative sign-bit-only Int is not a positive power of two.

### [Single Number II](https://leetcode.com/problems/single-number-ii/) (Medium)

Hint: Parity is insufficient for triplicates; retain count modulo three.

Approach: For each of 32 bit positions, count set bits across inputs modulo 3; reconstruct the remaining bit pattern. O(32n) time, O(1) space. Use unsigned extraction and preserve bit 31 to support negative unique values.

### [Subsets](https://leetcode.com/problems/subsets/) (Medium)

Hint: Each subset is a membership vector.

Approach: Enumerate masks 0 through 2^n-1 and include element i when bit i is set. O(n*2^n) time and output space, O(n) transient storage. This enumerates indices; duplicate values need deduplication logic if the requested subsets must be unique.

### [Maximum XOR of Two Numbers in an Array](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) (Medium)

Hint: Maximize the highest differing bit first.

Approach: For nonnegative 31-bit values, insert binary representations in a trie, then greedily choose the opposite bit when present. O(31n) time and O(31n) space; positive high bits dominate all lower bits combined. Signed maximum requires a changed sign-bit preference.

### [Shortest Path Visiting All Nodes](https://leetcode.com/problems/shortest-path-visiting-all-nodes/) (Hard)

Hint: Location alone does not describe progress; combine it with a visited set.

Approach: Multi-source BFS on (node,mask), starting at every node with its own bit. First state with full mask is optimal in an unweighted connected graph. O((n+m)*2^n) time, O(n*2^n) state space; revisits to a node with a different mask are meaningful.

### [Partition to K Equal Sum Subsets](https://leetcode.com/problems/partition-to-k-equal-sum-subsets/) (Medium)

Hint: A used-item mask can describe completed groups and a current remainder.

Approach: With nonnegative values, positive target, and small n, dp[mask] stores a feasible partial-bucket remainder, advancing only if adding an unused value does not exceed target. O(n*2^n) time, O(2^n) space. Handle total=0 separately; signed values invalidate the simple no-overflow-bucket pruning.

## Check your reasoning

When does XORing all array values recover the sole value occurring once?

1. Every other value occurs an even number of times.
2. Every other value occurs exactly three times.
3. Every value is nonnegative, regardless of frequency.

Answer: 1. XOR retains parity, so even multiplicities cancel. Signedness is irrelevant to cancellation.

Why can an assignment DP discard the order used to reach a mask?

1. Every ordering always has the same accumulated cost.
2. Only the best accumulated cost and used set affect the remaining choices.
3. Masks automatically sort the input.

Answer: 2. Histories can have different costs, but their available future assignments are identical once the used set and next position are fixed.

## Transfer
A workshop has n <= 15 jobs and n workers. cost[i][j] is the fee for worker j doing job i. Every worker can do exactly one job. Find the minimum total fee without enumerating all n! assignments. Fees are nonnegative and totals fit Long.

Process jobs in a fixed order; what information determines which workers remain available?

Represent that chosen set with a mask and derive the next job index from its number of set bits.

Let dp[mask] be the minimum fee for assigning the first popcount(mask) jobs to exactly the workers in mask. Start dp[0]=0. For every unused worker j, relax dp[mask | (1<<j)] with dp[mask]+cost[popcount(mask)][j]. Future choices depend only on this set, so discard more expensive histories. Return dp[(1<<n)-1]. O(n*2^n) time, O(2^n) space, with no input mutation. This is the final XOR assignment problem with a general cost matrix.

## Recall
- Derive x & (x - 1) using the binary representation of 12.
- Explain why Single Number allows negative values and fails for triplicates.
- Show why two unique values have at least one differing bit and why partitioning by that bit separates them.
- Reconstruct the assignment mask invariant without looking at the code.
- Contrast arithmetic shift with unsigned shift for a negative Int.

## Sources
- [Java Language Specification: expressions](https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html): Primary reference for shifts and integer operators; not retrieved during this pass. No company frequency claim.
- [Kotlin numbers](https://kotlinlang.org/docs/numbers.html): Primary reference for Kotlin numeric/bitwise operations; not retrieved during this pass.


# Practice room: make the pattern survive a twist

Use the experiments below to inspect actual state changes. Then solve a transfer prompt before opening the related lesson. Your aim is to explain why the technique applies when the story and assumptions change.

## A repeatable session

1. Read the contract and create an example with a boundary case.
2. Write a correct baseline and count its operations.
3. Identify the repeated work or redundant choices.
4. Write the smallest state that removes that work.
5. State the invariant before writing code.
6. Implement in your primary language, then test against the contract.
7. Change an assumption and decide whether your proof survives.

Use the lesson hints as graduated help: recognition first, state next, transition next, implementation last. After reading a solution, close it and reconstruct it the following day.

## Change one condition at a time

| Original | Twist | What you must reconsider |
|---|---|---|
| Positive subarray sum | Negative numbers allowed | Window monotonicity fails; prefix relations can still hold |
| Return existence | Count every solution | Keep frequencies, not only a set |
| Static range totals | Values change between queries | Prefix recomputation versus Fenwick/segment tree updates |
| Shortest path with equal costs | Unequal nonnegative costs | FIFO order no longer settles cheapest paths |
| Any solution | Lexicographically smallest solution | Traversal ordering, feasibility and tie-breaking |
| Tree input | Graph with shared nodes and cycles | Visited state, cycle handling, parent assumptions |
| Contiguous segment | Subsequence | Endpoints no longer describe the selected values |
| Reusable item | Use each item once | DP loop direction changes what states can be reused |
| Score only | Reconstruct the chosen items | Store predecessors or recompute a valid path |

## Mixed mock rounds

The links use neutral labels so the pattern title is not visible until you open the original problem. Attempt one pair per session. Difficulty labels are practice-platform classifications, not company hiring thresholds.

| Round | First problem | Second problem | Follow-up to say aloud |
|---|---|---|---|
| A | [Prompt A1](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | [Prompt A2](https://leetcode.com/problems/subarray-sum-equals-k/) | Which assumption makes shrinking safe or unsafe? |
| B | [Prompt B1](https://leetcode.com/problems/number-of-islands/) | [Prompt B2](https://leetcode.com/problems/course-schedule/) | What does visited mean in each problem? |
| C | [Prompt C1](https://leetcode.com/problems/kth-largest-element-in-an-array/) | [Prompt C2](https://leetcode.com/problems/search-in-rotated-sorted-array/) | What information can you discard permanently? |
| D | [Prompt D1](https://leetcode.com/problems/house-robber/) | [Prompt D2](https://leetcode.com/problems/coin-change/) | What future decisions does the state have to remember? |
| E | [Prompt E1](https://leetcode.com/problems/merge-intervals/) | [Prompt E2](https://leetcode.com/problems/non-overlapping-intervals/) | Does the sorting criterion need a correctness proof? |
| F | [Prompt F1](https://leetcode.com/problems/word-search-ii/) | [Prompt F2](https://leetcode.com/problems/sliding-window-maximum/) | Which two techniques cooperate here? |

## Score your reasoning, not only the result

After a mock, assign 0 (missing), 1 (with help), or 2 (independent) for each: contract, baseline, state choice, invariant, implementation, edge tests, complexity, and follow-up. A correct output with an unexplained invariant is an unfinished learning task. This score is a personal diagnostic, not a prediction of hiring decisions.

Keep a small error log:

```text
Problem and date:
My first model:
Smallest input that breaks it:
Which assumption failed:
Repaired state and invariant:
What I can now derive without looking:
Next retrieval: +1, +3, +7, +21 days
```

## Advanced breadth and the limits of a pattern library

After these lessons, explore string matching (KMP and Z-function), rolling hashes and collision verification, quickselect and randomized selection, divide-and-conquer counting, meet-in-the-middle, sweep lines, interval DP, tree DP, and bitmask DP. Several appear as extensions here; they are not all taught with the same depth as the core 20 patterns. Scheduling and graph questions may also require a combination of techniques.

Keep room for unfamiliar problems. You may need to invent a representation instead of selecting a known pattern. Practice explaining a partial correct solution, reasoning about trade-offs, and incorporating a hint. Interview outcomes also depend on communication, role expectations and the particular round; no finite question list guarantees success.
