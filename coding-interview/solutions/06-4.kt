import java.util.*

fun skyline(buildings: Array<IntArray>): List<List<Int>> {
    val events = mutableListOf<IntArray>()
    for (b in buildings) {
        require(b[0] < b[1] && b[2] > 0)
        events.add(intArrayOf(b[0], b[2], 1)); events.add(intArrayOf(b[1], b[2], -1))
    }
    events.sortBy { it[0] }
    val active = TreeMap<Int, Int>(); active[0] = 1
    val ans = mutableListOf<List<Int>>(); var i = 0; var previous = 0
    while (i < events.size) {
        val x = events[i][0]
        while (i < events.size && events[i][0] == x) {
            val e = events[i++]; val count = (active[e[1]] ?: 0) + e[2]
            if (count == 0) active.remove(e[1]) else active[e[1]] = count
        }
        val height = active.lastKey()
        if (height != previous) { ans.add(listOf(x, height)); previous = height }
    }
    return ans
}
