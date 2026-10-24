import java.util.*

class DSU(n: Int) {
    private val parent = IntArray(n) { it }
    val size = IntArray(n) { 1 }
    fun find(value: Int): Int {
        var x = value
        while (x != parent[x]) { parent[x] = parent[parent[x]]; x = parent[x] }
        return x
    }
    fun union(a: Int, b: Int): Boolean {
        var ra = find(a)
        var rb = find(b)
        if (ra == rb) return false
        if (size[ra] < size[rb]) { val t = ra; ra = rb; rb = t }
        parent[rb] = ra
        size[ra] += size[rb]
        return true
    }
}
fun largestIsland(grid: Array<IntArray>): Int {
    val n = grid.size
    require(n <= 500)
    if (n == 0) return 0
    val dsu = DSU(n * n)
    for (r in 0 until n) for (c in 0 until n) if (grid[r][c] == 1) {
        if (r + 1 < n && grid[r+1][c] == 1) dsu.union(r*n+c, (r+1)*n+c)
        if (c + 1 < n && grid[r][c+1] == 1) dsu.union(r*n+c, r*n+c+1)
    }
    var answer = 0
    val dr = intArrayOf(-1,1,0,0)
    val dc = intArrayOf(0,0,-1,1)
    for (r in 0 until n) for (c in 0 until n) {
        if (grid[r][c] == 1) { answer = maxOf(answer, dsu.size[dsu.find(r*n+c)]); continue }
        val roots = mutableSetOf<Int>()
        for (d in 0 until 4) {
            val nr = r + dr[d]
            val nc = c + dc[d]
            if (nr in 0 until n && nc in 0 until n && grid[nr][nc] == 1) roots.add(dsu.find(nr*n+nc))
        }
        var area = 1
        for (root in roots) area += dsu.size[root]
        answer = maxOf(answer, area)
    }
    return answer
}
