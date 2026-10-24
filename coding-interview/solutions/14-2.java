import java.util.*;

class Solution14P2 {
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
}
