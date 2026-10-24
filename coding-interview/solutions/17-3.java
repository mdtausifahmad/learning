import java.util.*;

class Solution17P3 {
static int coinChange(int[] coins,int amount) {
    if(amount<0 || amount>1_000_000) throw new IllegalArgumentException("amount");
    for(int c:coins) if(c<=0) throw new IllegalArgumentException("positive coins required");
    int unreachable=amount+1;
    int[] dp=new int[amount+1]; Arrays.fill(dp,unreachable); dp[0]=0;
    for(int x=1;x<=amount;x++) {
        for(int c:coins) if(c<=x && dp[x-c]!=unreachable)
            dp[x]=Math.min(dp[x],dp[x-c]+1);
    }
    return dp[amount]==unreachable ? -1 : dp[amount];
}
}
