import java.util.*;

class Solution13P3 {
static int orangesRotting(int[][] grid) {
    Objects.requireNonNull(grid, "grid");
    int rows = grid.length;
    if (rows == 0) return 0;
    int cols = grid[0].length, fresh = 0, minutes = 0;
    int[][] distance = new int[rows][cols];
    ArrayDeque<int[]> queue = new ArrayDeque<>();
    for (int r = 0; r < rows; r++) {
        Arrays.fill(distance[r], -1);
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 1) fresh++;
            if (grid[r][c] == 2) { distance[r][c] = 0; queue.add(new int[]{r,c}); }
        }
    }
    int[] dr = {-1,1,0,0}, dc = {0,0,-1,1};
    while (!queue.isEmpty()) {
        int[] cell = queue.remove();
        for (int d = 0; d < 4; d++) {
            int r = cell[0] + dr[d], c = cell[1] + dc[d];
            if (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] == 1 && distance[r][c] == -1) {
                distance[r][c] = distance[cell[0]][cell[1]] + 1;
                minutes = Math.max(minutes, distance[r][c]);
                fresh--;
                queue.add(new int[]{r,c});
            }
        }
    }
    return fresh == 0 ? minutes : -1;
}
}
