import java.util.*

fun maxWindowSum(a: IntArray, k: Int): Long {
    require(k >= 1 && k <= a.size) { "invalid k" }
    var sum = 0L
    for (i in 0 until k) sum += a[i]
    var best = sum
    for (r in k until a.size) {
        sum += a[r].toLong() - a[r - k]; best = maxOf(best, sum)
    }
    return best
}
