import java.util.*

fun lowerBound(a: IntArray, target: Int): Int {
    var lo = 0; var hi = a.size
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (a[mid] < target) lo = mid + 1 else hi = mid
    }
    return lo
}
