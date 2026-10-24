import java.util.*

fun editDistance(a: String,b: String): Int {
    val dp=IntArray(b.length+1){it}
    for(i in 1..a.length) {
        var diagonal=dp[0]; dp[0]=i
        for(j in 1..b.length) {
            val old=dp[j]
            val cost=if(a[i-1]==b[j-1]) 0 else 1
            dp[j]=minOf(diagonal+cost,old+1,dp[j-1]+1)
            diagonal=old
        }
    }
    return dp[b.length]
}
