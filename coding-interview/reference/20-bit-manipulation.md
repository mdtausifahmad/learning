# Bit manipulation and state compression: retrieval sheet

## Recognition
- Every value occurs an even number of times except a few: XOR cancels equal pairs without storing a frequency table.
- The question manipulates membership of a small fixed universe: one bit per element can encode the entire chosen set.
- You need to count, isolate, or remove set bits: inspect how the lowest 1 changes under subtraction.
- You see subset enumeration and n is roughly 20 or smaller: an exponential bitmask state may be feasible; inspect the actual memory/time bound first.

## Invariant
Always say what each bit or accumulator means. For parity accumulation, after consuming the first k values, bit b equals the parity of the number of consumed values having bit b set. Appending a value flips exactly its set columns, preserving that meaning. Equal paired values flip each of their columns twice and disappear. For a subset mask, bit j is 1 exactly when item j is used. A transition may set a previously zero bit; then the number of chosen items increases by exactly one. In dynamic programming, dp[mask] is the best cost among every partial assignment having precisely that used set. These invariants are different; using XOR cancellation does not justify a subset optimization or vice versa.

## Failure boundaries
- XOR all values to find the value that appears once even if the others occur three times.: [2, 2, 2, 3] XORs to 1, not the unique value 3. Three copies have odd parity and do not disappear. Repair: For triplicates, count each bit modulo 3, or derive a two-register finite-state machine. State the frequency assumption first.
- Use XOR as an unconditional “remove this element” operation.: mask = 0100, remove item 1 using XOR 0010 gives 0110, adding an absent item. Repair: Use mask & ~(1 << i), or prove membership before toggling.
- Assume a compact mask makes an arbitrary large subset problem cheap.: n = 40 needs 2^40 states, over one trillion, despite each state fitting in one Long. Repair: Compute state count before allocation. Find a different structure, meet-in-the-middle, or a valid pruning argument.

## Recall
- Derive x & (x - 1) using the binary representation of 12.
- Explain why Single Number allows negative values and fails for triplicates.
- Show why two unique values have at least one differing bit and why partitioning by that bit separates them.
- Reconstruct the assignment mask invariant without looking at the code.
- Contrast arithmetic shift with unsigned shift for a negative Int.
