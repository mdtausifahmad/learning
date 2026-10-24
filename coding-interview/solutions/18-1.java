import java.util.*;

class Solution18P1 {
static long paths(int m,int n) {
    if(m<1 || m>20 || n<1 || n>20) throw new IllegalArgumentException();
    long[] dp=new long[n]; Arrays.fill(dp,1L);
    for(int r=1;r<m;r++) for(int c=1;c<n;c++) dp[c]+=dp[c-1];
    return dp[n-1];
}
}
