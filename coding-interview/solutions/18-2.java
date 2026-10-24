import java.util.*;

class Solution18P2 {
static int lcs(String a,String b) {
    int[] dp=new int[b.length()+1];
    for(int i=1;i<=a.length();i++) {
        int diagonal=0;
        for(int j=1;j<=b.length();j++) {
            int old=dp[j];
            dp[j]=a.charAt(i-1)==b.charAt(j-1)?diagonal+1:Math.max(dp[j],dp[j-1]);
            diagonal=old;
        }
    }
    return dp[b.length()];
}
}
