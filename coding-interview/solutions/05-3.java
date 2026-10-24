import java.util.*;

class Solution05P3 {
static long shipWithinDays(int[] weights,int days) {
    if(days<=0) throw new IllegalArgumentException("days");
    long lo=0,hi=0;
    for(int w:weights) {
        if(w<0) throw new IllegalArgumentException("negative weight");
        lo=Math.max(lo,w); hi+=w;
    }
    while(lo<hi) {
        long mid=lo+(hi-lo)/2,load=0; int used=1;
        for(int w:weights) {
            if(load+w>mid) { used++; load=0; }
            load+=w;
        }
        if(used<=days) hi=mid; else lo=mid+1;
    }
    return lo;
}
}
