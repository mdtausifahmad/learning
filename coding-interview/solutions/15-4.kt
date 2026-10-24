import java.util.*

fun minimumObstacles(grid: Array<IntArray>): Int {
    val rows = grid.size
    val cols = grid[0].size
    val dist = Array(rows) { IntArray(cols) { Int.MAX_VALUE } }
    dist[0][0] = 0
    val deque = ArrayDeque<IntArray>()
    deque.addFirst(intArrayOf(0,0,0))
    val dr = intArrayOf(-1,1,0,0)
    val dc = intArrayOf(0,0,-1,1)
    while (deque.isNotEmpty()) {
        val entry = deque.removeFirst()
        val r = entry[0]
        val c = entry[1]
        val cost = entry[2]
        if (cost != dist[r][c]) continue
        if (r == rows - 1 && c == cols - 1) return cost
        for (d in 0 until 4) {
            val nr = r + dr[d]
            val nc = c + dc[d]
            if (nr !in 0 until rows || nc !in 0 until cols) continue
            val nextCost = cost + grid[nr][nc]
            if (nextCost < dist[nr][nc]) {
                dist[nr][nc] = nextCost
                val next = intArrayOf(nr,nc,nextCost)
                if (grid[nr][nc] == 0) deque.addFirst(next) else deque.addLast(next)
            }
        }
    }
    return -1
}
