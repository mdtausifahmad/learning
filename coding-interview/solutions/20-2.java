import java.util.*;

class Solution20P2 {
static int[] countBits(int n) {
    if (n < 0 || n > 1_000_000) throw new IllegalArgumentException("n out of range");
    int[] count = new int[n + 1];
    for (int x = 1; x <= n; x++) count[x] = count[x & (x - 1)] + 1;
    return count;
}
}
