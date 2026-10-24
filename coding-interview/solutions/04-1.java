import java.util.*;

class Solution04P1 {
static long[] rangeSums(int[] a,int[][] queries) {
    long[] prefix=new long[a.length+1];
    for(int i=0;i<a.length;i++) prefix[i+1]=prefix[i]+a[i];
    long[] ans=new long[queries.length];
    for(int i=0;i<queries.length;i++) {
        int l=queries[i][0],r=queries[i][1];
        if(l<0 || l>r || r>=a.length) throw new IllegalArgumentException("bounds");
        ans[i]=prefix[r+1]-prefix[l];
    }
    return ans;
}
}
