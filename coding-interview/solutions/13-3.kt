import java.util.*

fun orangesRotting(grid: Array<IntArray>): Int {
    val rows = grid.size
    if (rows == 0) return 0
    val cols = grid[0].size
    val distance = Array(rows) { IntArray(cols) { -1 } }
    val queue = ArrayDeque<Pair<Int, Int>>()
    var fresh = 0
    var minutes = 0
    for (r in 0 until rows) for (c in 0 until cols) {
        if (grid[r][c] == 1) fresh++
        if (grid[r][c] == 2) { distance[r][c] = 0; queue.add(r to c) }
    }
    val dr = intArrayOf(-1,1,0,0)
    val dc = intArrayOf(0,0,-1,1)
    while (queue.isNotEmpty()) {
        val (cr, cc) = queue.remove()
        for (d in 0 until 4) {
            val r = cr + dr[d]
            val c = cc + dc[d]
            if (r in 0 until rows && c in 0 until cols && grid[r][c] == 1 && distance[r][c] == -1) {
                distance[r][c] = distance[cr][cc] + 1
                minutes = maxOf(minutes, distance[r][c])
                fresh--
                queue.add(r to c)
            }
        }
    }
    return if (fresh == 0) minutes else -1
}
