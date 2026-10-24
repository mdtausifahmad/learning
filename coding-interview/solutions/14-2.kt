import java.util.*

fun findOrder(n: Int, prerequisites: Array<IntArray>): IntArray {
    require(n >= 0)
    val out = Array(n) { mutableListOf<Int>() }
    val indegree = IntArray(n)
    for (pair in prerequisites) { out[pair[1]].add(pair[0]); indegree[pair[0]]++ }
    val queue = ArrayDeque<Int>()
    for (i in 0 until n) if (indegree[i] == 0) queue.add(i)
    val order = IntArray(n)
    var count = 0
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        order[count++] = current
        for (next in out[current]) { indegree[next]--; if (indegree[next] == 0) queue.add(next) }
    }
    return if (count == n) order else IntArray(0)
}
