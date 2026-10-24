import java.util.*;

class Solution18P3 {
static int editDistance(String a,String b) {
    int[] dp=new int[b.length()+1];
    for(int j=0;j<dp.length;j++) dp[j]=j;
    for(int i=1;i<=a.length();i++) {
        int diagonal=dp[0]; dp[0]=i;
        for(int j=1;j<=b.length();j++) {
            int old=dp[j];
            int cost=a.charAt(i-1)==b.charAt(j-1)?0:1;
            dp[j]=Math.min(diagonal+cost,Math.min(old+1,dp[j-1]+1));
            diagonal=old;
        }
    }
    return dp[b.length()];
}
}
