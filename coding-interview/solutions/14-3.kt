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
fun findRedundantConnection(edges: Array<IntArray>): IntArray {
    val dsu = DSU(edges.size + 1)
    for (edge in edges) if (!dsu.union(edge[0], edge[1])) return intArrayOf(edge[0], edge[1])
    throw IllegalArgumentException("tree plus one edge required")
}
