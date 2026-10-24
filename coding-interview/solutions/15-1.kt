import java.util.*

fun shortestEdges(n: Int, edges: Array<IntArray>, source: Int, target: Int): Int {
    require(n >= 1)
    val out = Array(n) { mutableListOf<Int>() }
    for (edge in edges) { out[edge[0]].add(edge[1]); out[edge[1]].add(edge[0]) }
    val distance = IntArray(n) { -1 }
    distance[source] = 0
    val queue = ArrayDeque<Int>()
    queue.add(source)
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        if (current == target) return distance[current]
        for (next in out[current]) if (distance[next] == -1) {
            distance[next] = distance[current] + 1
            queue.add(next)
        }
    }
    return -1
}
