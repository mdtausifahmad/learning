# Linked lists: preserve the path while changing links: retrieval sheet

## Recognition
- The input consists of node references and next links; changing connectivity can replace expensive copying.
- An operation treats the first node differently: a dummy predecessor can make the first position ordinary.
- A cycle or unknown midpoint asks about relative progress along one successor chain.
- Sorted chains must be combined while preserving their internal order: compare their current heads.

## Invariant
Maintain explicit reachability: every original node belongs to either the completed result or an unfinished region reachable through a saved reference. No node may silently disappear, and no unintended cycle may appear. For each operation, state which predecessor and successor remain fixed. Termination follows because the unfinished region shrinks or a cursor progresses toward an end; cycle algorithms instead use finite modular positions.

## Failure boundaries
- Overwrite next and then advance through next.: On A→B→C, setting A.next=null before saving B makes A.next lead nowhere; B and C are lost to the algorithm. Repair: Save next=A.next before every destructive redirect, then advance using the saved reference.
- Detect a cycle by comparing node values.: A(7)→B(7)→null has repeated values but no cycle. Value equality would incorrectly return true. Repair: Compare reference identity: Java slow==fast; Kotlin slow===fast.

## Recall
- Draw reversal with named node identities and identify the reference that would be lost.
- Prove the cycle-entry reset using distances a,b,c.
- Implement group reversal without looking at code; state both boundaries first.
