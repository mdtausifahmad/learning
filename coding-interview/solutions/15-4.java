import java.util.*;

class Solution15P4 {
static int minimumObstacles(int[][] grid) {
    Objects.requireNonNull(grid, "grid");
    int rows = grid.length, cols = grid[0].length;
    int[][] dist = new int[rows][cols];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    dist[0][0] = 0;
    ArrayDeque<int[]> deque = new ArrayDeque<>(); deque.addFirst(new int[]{0,0,0});
    int[] dr = {-1,1,0,0}, dc = {0,0,-1,1};
    while (!deque.isEmpty()) {
        int[] entry = deque.removeFirst(); int r = entry[0], c = entry[1], cost = entry[2];
        if (cost != dist[r][c]) continue;
        if (r == rows - 1 && c == cols - 1) return cost;
        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d], nc = c + dc[d];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
            int nextCost = cost + grid[nr][nc];
            if (nextCost < dist[nr][nc]) {
                dist[nr][nc] = nextCost;
                int[] next = {nr,nc,nextCost};
                if (grid[nr][nc] == 0) deque.addFirst(next); else deque.addLast(next);
            }
        }
    }
    return -1;
}
}
