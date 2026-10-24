# Stacks, monotonic frontiers, and deques: retrieval sheet

## Recognition
- Nested opening and closing operations require resolving the most recently opened obligation first.
- For each position, find the first greater or smaller position on one side: unresolved indices can form an ordered stack.
- An element becomes irrelevant forever when a newer, better element arrives: maintain a monotonic frontier.
- A moving window needs an extreme value and expiration by index: a deque supports removing both dominated candidates and expired candidates.

## Invariant
The container stores exactly the unresolved obligations or nondominated candidates needed for future answers. Its order is stated explicitly: chronological indices plus either nonincreasing or nondecreasing values. A deletion must have a proof: resolved forever, expired forever, or dominated forever. Since indices enter once and leave at most once, total container work is O(n).

## Failure boundaries
- Match brackets using three independent counters.: ([)] has balanced counts for parentheses and square brackets but invalid nesting. The first closing parenthesis conflicts with the latest open square bracket. Repair: Keep opening kinds in a stack and compare each close with the top.
- Keep only values for a sliding maximum.: For [5,5,1] with window size 2, the first 5 expires while the second remains. A value alone cannot identify which 5 expired. Repair: Store indices; remove front indices outside the active window, then read their values.

## Recall
- Derive the comparison sign for next greater, previous smaller, and window minimum.
- Explain why both index age and value matter for dominance.
- Draw histogram left and right boundaries and derive the minus one.
