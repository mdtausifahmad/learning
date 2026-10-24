import java.util.*

fun countRangeSum(a: IntArray, lower: Long, upper: Long): Long {
    require(lower <= upper && lower >= -1_000_000_000_000_000_000L && upper <= 1_000_000_000_000_000_000L)
    val p = LongArray(a.size + 1); val tmp = LongArray(p.size)
    for (i in a.indices) p[i + 1] = p[i] + a[i]
    fun solve(lo: Int, hi: Int): Long {
        if (hi - lo <= 1) return 0L
        val mid = lo + (hi - lo) / 2
        var ans = solve(lo, mid) + solve(mid, hi)
        var first = mid; var after = mid
        for (i in lo until mid) {
            while (first < hi && p[first] - p[i] < lower) first++
            while (after < hi && p[after] - p[i] <= upper) after++
            ans += (after - first).toLong()
        }
        var i = lo; var j = mid; var w = lo
        while (i < mid || j < hi) {
            if (j == hi || (i < mid && p[i] <= p[j])) tmp[w++] = p[i++] else tmp[w++] = p[j++]
        }
        for (k in lo until hi) p[k] = tmp[k]
        return ans
    }
    return solve(0, p.size)
}
