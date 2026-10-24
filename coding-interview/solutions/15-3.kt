import java.util.*

fun cheapestFlight(n: Int, flights: Array<IntArray>, src: Int, dst: Int, stops: Int): Long {
    require(n in 1..1000 && stops in 0..1000)
    var previous = LongArray(n) { Long.MAX_VALUE }
    previous[src] = 0L
    for (used in 1..stops + 1) {
        val next = previous.copyOf()
        for (flight in flights) {
            if (previous[flight[0]] == Long.MAX_VALUE) continue
            next[flight[1]] = minOf(next[flight[1]], previous[flight[0]] + flight[2].toLong())
        }
        previous = next
    }
    return if (previous[dst] == Long.MAX_VALUE) -1L else previous[dst]
}
