import java.util.*

fun eraseOverlap(intervals: Array<IntArray>): Int {
    for (x in intervals) require(x[0] < x[1])
    val a = intervals.sortedWith(compareBy<IntArray> { it[1] }.thenBy { it[0] })
    var kept = 0; var end = Long.MIN_VALUE
    for (x in a) if (x[0].toLong() >= end) { kept++; end = x[1].toLong() }
    return a.size - kept
}
