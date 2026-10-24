import java.util.*

fun coinChange(coins: IntArray, amount: Int): Int {
    require(amount in 0..1_000_000) { "amount" }
    for (c in coins) require(c > 0) { "positive coins required" }
    val unreachable = amount + 1
    val dp = IntArray(amount + 1) { unreachable }; dp[0] = 0
    for (x in 1..amount) {
        for (c in coins) if (c <= x && dp[x - c] != unreachable)
            dp[x] = minOf(dp[x], dp[x - c] + 1)
    }
    return if (dp[amount] == unreachable) -1 else dp[amount]
}
