import java.util.*

fun maxJobReward(jobs: Array<IntArray>): Long {
    for (j in jobs) require(j[0] < j[1]) { "nonempty job required" }
    val a = jobs.sortedWith(compareBy<IntArray> { it[1] }.thenBy { it[0] })
    val dp = LongArray(a.size + 1)
    for (i in a.indices) {
        var lo = 0; var hi = i
        while (lo < hi) {
            val mid = lo + (hi - lo) / 2
            if (a[mid][1] <= a[i][0]) lo = mid + 1 else hi = mid
        }
        dp[i + 1] = maxOf(dp[i], a[i][2].toLong() + dp[lo])
    }
    return dp[a.size]
}
