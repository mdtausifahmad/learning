import java.util.*

fun numIslands(grid: Array<IntArray>): Int {
    val rows = grid.size
    if (rows == 0) return 0
    val cols = grid[0].size
    val seen = Array(rows) { BooleanArray(cols) }
    val dr = intArrayOf(-1, 1, 0, 0)
    val dc = intArrayOf(0, 0, -1, 1)
    val queue = ArrayDeque<Pair<Int, Int>>()
    var answer = 0
    for (r in 0 until rows) for (c in 0 until cols) {
        if (grid[r][c] != 1 || seen[r][c]) continue
        answer++
        seen[r][c] = true
        queue.add(r to c)
        while (queue.isNotEmpty()) {
            val (cr, cc) = queue.remove()
            for (d in 0 until 4) {
                val nr = cr + dr[d]
                val nc = cc + dc[d]
                if (nr in 0 until rows && nc in 0 until cols && grid[nr][nc] == 1 && !seen[nr][nc]) {
                    seen[nr][nc] = true
                    queue.add(nr to nc)
                }
            }
        }
    }
    return answer
}
