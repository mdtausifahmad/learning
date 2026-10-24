import java.util.*;

class Solution15P1 {
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
}
