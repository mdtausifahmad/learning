import java.util.*

fun lcs(a: String,b: String): Int {
    val dp=IntArray(b.length+1)
    for(i in 1..a.length) {
        var diagonal=0
        for(j in 1..b.length) {
            val old=dp[j]
            dp[j]=if(a[i-1]==b[j-1]) diagonal+1 else maxOf(dp[j],dp[j-1])
            diagonal=old
        }
    }
    return dp[b.length]
}
