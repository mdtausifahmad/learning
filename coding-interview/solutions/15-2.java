import java.util.*;

class Solution15P2 {
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
}
