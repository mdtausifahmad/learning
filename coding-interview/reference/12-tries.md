# Tries: share prefixes and prune impossible continuations: retrieval sheet

## Recognition
- Queries repeatedly ask whether a string is a word or a prefix of any stored word.
- A dictionary is searched one character at a time, with early rejection when no prefix remains.
- Several target words are explored together in another search space, such as board paths.
- A wildcard represents several next-character alternatives, so search may branch at a prefix node.

## Invariant
At a trie node reached after consuming k symbols, the path from root spells exactly those k symbols. A terminal marker means that exact prefix is an inserted word. In product searches, the board path, trie path, and consumed character count describe the same prefix, while visited cells match only that active route. Missing edges prove no stored extension exists.

## Failure boundaries
- Treat every existing prefix path as a complete stored word.: After inserting apple, the path app exists, but exact search(app) must be false. Prefix search(app) is true. Repair: Store an independent terminal marker and distinguish exact search from prefix search.
- Keep board cells visited after returning from one search branch.: Board [a,b] with dictionary [ab,ba]: a first search can visit both cells; permanently marking them prevents the second starting position from finding ba. Repair: Mark only on the active recursion route and unmark before returning; global terminal clearing is a separate deduplication decision.

## Recall
- Explain terminal versus prefix using app and apple.
- Derive wildcard state and its actual visited-node runtime.
- Explain why found-word clearing is global but board visited flags are path-local.
