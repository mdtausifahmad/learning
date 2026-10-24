import java.util.*;

class Solution02P1 {
static int[] sortedPair(int[] a,long target) {
    int l=0,r=a.length-1;
    while(l<r) {
        long sum=(long)a[l]+a[r];
        if(sum==target) return new int[]{l,r};
        if(sum<target) l++; else r--;
    }
    return new int[0];
}
}
