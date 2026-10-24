import java.util.*;

class Solution17P4 {
static long maxJobReward(int[][] jobs) {
    int[][] a=jobs.clone();
    for(int[] j:a) if(j[0]>=j[1]) throw new IllegalArgumentException("nonempty job required");
    Arrays.sort(a,Comparator.comparingInt((int[] j)->j[1]).thenComparingInt(j->j[0]));
    long[] dp=new long[a.length+1];
    for(int i=0;i<a.length;i++) {
        int lo=0,hi=i;
        while(lo<hi) {
            int mid=lo+(hi-lo)/2;
            if(a[mid][1]<=a[i][0]) lo=mid+1; else hi=mid;
        }
        dp[i+1]=Math.max(dp[i],(long)a[i][2]+dp[lo]);
    }
    return dp[a.length];
}
}
