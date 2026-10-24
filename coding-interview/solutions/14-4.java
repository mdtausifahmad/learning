import java.util.*;

class Solution14P4 {
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
static int largestIsland(int[][] grid) {
    Objects.requireNonNull(grid, "grid");
    int n = grid.length;
    if (n > 500) throw new IllegalArgumentException("n too large");
    if (n == 0) return 0;
    DSU dsu = new DSU(n * n);
    for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) if (grid[r][c] == 1) {
        if (r + 1 < n && grid[r+1][c] == 1) dsu.union(r*n+c, (r+1)*n+c);
        if (c + 1 < n && grid[r][c+1] == 1) dsu.union(r*n+c, r*n+c+1);
    }
    int answer = 0;
    int[] dr = {-1,1,0,0}, dc = {0,0,-1,1};
    for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
        if (grid[r][c] == 1) { answer = Math.max(answer, dsu.size[dsu.find(r*n+c)]); continue; }
        Set<Integer> roots = new HashSet<>();
        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d], nc = c + dc[d];
            if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 1) roots.add(dsu.find(nr*n+nc));
        }
        int area = 1;
        for (int root : roots) area += dsu.size[root];
        answer = Math.max(answer, area);
    }
    return answer;
}
}
