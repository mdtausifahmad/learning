import java.util.*

fun paths(m: Int,n: Int): Long {
    require(m in 1..20 && n in 1..20)
    val dp=LongArray(n){1L}
    for(r in 1 until m) for(c in 1 until n) dp[c]+=dp[c-1]
    return dp[n-1]
}
