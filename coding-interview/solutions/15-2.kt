import java.util.*

fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Long {
    require(n in 1..100_000)
    val out = Array(n + 1) { mutableListOf<IntArray>() }
    for (edge in times) out[edge[0]].add(intArrayOf(edge[1], edge[2]))
    val dist = LongArray(n + 1) { Long.MAX_VALUE }
    dist[k] = 0L
    val heap = PriorityQueue<LongArray> { a, b -> a[0].compareTo(b[0]) }
    heap.add(longArrayOf(0L, k.toLong()))
    while (heap.isNotEmpty()) {
        val entry = heap.remove()
        val cost = entry[0]
        val current = entry[1].toInt()
        if (cost != dist[current]) continue
        for (edge in out[current]) {
            val nextCost = cost + edge[1].toLong()
            if (nextCost < dist[edge[0]]) { dist[edge[0]] = nextCost; heap.add(longArrayOf(nextCost, edge[0].toLong())) }
        }
    }
    var answer = 0L
    for (i in 1..n) { if (dist[i] == Long.MAX_VALUE) return -1L; answer = maxOf(answer, dist[i]) }
    return answer
}
