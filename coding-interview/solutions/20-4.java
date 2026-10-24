import java.util.*;

class Solution20P4 {
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
}
