import java.util.*;

class Solution04P5 {
static long countRangeSum(int[] a,long lower,long upper) {
    if(lower>upper || lower< -1_000_000_000_000_000_000L || upper>1_000_000_000_000_000_000L)
        throw new IllegalArgumentException("bounds");
    long[] p=new long[a.length+1],tmp=new long[p.length];
    for(int i=0;i<a.length;i++) p[i+1]=p[i]+a[i];
    return countMerge(p,tmp,0,p.length,lower,upper);
}
static long countMerge(long[] p,long[] tmp,int lo,int hi,long lower,long upper) {
    if(hi-lo<=1) return 0;
    int mid=lo+(hi-lo)/2;
    long ans=countMerge(p,tmp,lo,mid,lower,upper)+countMerge(p,tmp,mid,hi,lower,upper);
    int first=mid,after=mid;
    for(int i=lo;i<mid;i++) {
        while(first<hi && p[first]-p[i]<lower) first++;
        while(after<hi && p[after]-p[i]<=upper) after++;
        ans+=after-first;
    }
    int i=lo,j=mid,w=lo;
    while(i<mid || j<hi) tmp[w++]=(j==hi || (i<mid && p[i]<=p[j])) ? p[i++] : p[j++];
    System.arraycopy(tmp,lo,p,lo,hi-lo);
    return ans;
}
}
