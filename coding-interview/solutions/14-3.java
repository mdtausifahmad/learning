import java.util.*;

class Solution14P3 {
static final class DSU {
    final int[] parent, size;
    DSU(int n) {
        parent = new int[n]; size = new int[n];
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    }
    int find(int x) {
        while (x != parent[x]) { parent[x] = parent[parent[x]]; x = parent[x]; }
        return x;
    }
    boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra; size[ra] += size[rb]; return true;
    }
}
static int[] findRedundantConnection(int[][] edges) {
    Objects.requireNonNull(edges, "edges");
    DSU dsu = new DSU(edges.length + 1);
    for (int[] edge : edges) if (!dsu.union(edge[0], edge[1])) return new int[]{edge[0], edge[1]};
    throw new IllegalArgumentException("tree plus one edge required");
}
}
