import java.util.*;

class Solution05P4 {
static double medianSorted(int[] first,int[] second) {
    int[] a=first,b=second;
    if(a.length>b.length) { a=second; b=first; }
    int m=a.length,n=b.length;
    if(m==0 && n==0) throw new IllegalArgumentException("empty");
    int half=(int)(((long)m+n+1)/2),lo=0,hi=m;
    while(lo<=hi) {
        int i=lo+(hi-lo)/2,j=half-i;
        long al=i==0?Long.MIN_VALUE:a[i-1], ar=i==m?Long.MAX_VALUE:a[i];
        long bl=j==0?Long.MIN_VALUE:b[j-1], br=j==n?Long.MAX_VALUE:b[j];
        if(al<=br && bl<=ar) {
            long left=Math.max(al,bl);
            if(((long)m+n)%2==1) return (double)left;
            return (left+Math.min(ar,br))/2.0;
        }
        if(al>br) hi=i-1; else lo=i+1;
    }
    throw new IllegalArgumentException("arrays must be sorted");
}
}
