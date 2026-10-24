import java.util.*;

class Solution15P3 {
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
}
