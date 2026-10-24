import java.util.*

fun shipWithinDays(weights: IntArray, days: Int): Long {
    require(days > 0) { "days" }
    var lo = 0L; var hi = 0L
    for (w in weights) { require(w >= 0); lo = maxOf(lo, w.toLong()); hi += w }
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2; var load = 0L; var used = 1
        for (w in weights) {
            if (load + w > mid) { used++; load = 0L }
            load += w
        }
        if (used <= days) hi = mid else lo = mid + 1
    }
    return lo
}
