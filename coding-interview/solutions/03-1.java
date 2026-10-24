import java.util.*;

class Solution03P1 {
static long maxWindowSum(int[] a,int k) {
    if(k<1 || k>a.length) throw new IllegalArgumentException("invalid k");
    long sum=0; for(int i=0;i<k;i++) sum+=a[i];
    long best=sum;
    for(int r=k;r<a.length;r++) {
        sum+=(long)a[r]-a[r-k]; best=Math.max(best,sum);
    }
    return best;
}
}
