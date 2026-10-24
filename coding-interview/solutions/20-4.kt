import java.util.*

fun minimumXorSum(a: IntArray, b: IntArray): Long {
    val n = a.size
    require(n == b.size && n <= 14) { "invalid lengths" }
    require(a.all { it in 0..10_000_000 } && b.all { it in 0..10_000_000 }) { "value out of range" }
    val size = 1 shl n
    val dp = LongArray(size) { Long.MAX_VALUE }
    dp[0] = 0L
    for (mask in 0 until size - 1) {
        val k = Integer.bitCount(mask)
        if (dp[mask] == Long.MAX_VALUE) continue
        for (j in 0 until n) {
            val bit = 1 shl j
            if ((mask and bit) == 0) {
                val next = mask or bit
                dp[next] = minOf(dp[next], dp[mask] + (a[k] xor b[j]).toLong())
            }
        }
    }
    return dp[size - 1]
}
