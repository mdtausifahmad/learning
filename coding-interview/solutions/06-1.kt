import java.util.*

fun mergeIntervals(intervals: Array<IntArray>): Array<IntArray> {
    val a = intervals.map { require(it[0] <= it[1]); it.copyOf() }
        .sortedWith(compareBy<IntArray> { it[0] }.thenBy { it[1] })
    val ans = mutableListOf<IntArray>()
    for (x in a) {
        if (ans.isEmpty() || x[0] > ans.last()[1]) ans.add(x.copyOf())
        else ans.last()[1] = maxOf(ans.last()[1], x[1])
    }
    return ans.toTypedArray()
}
