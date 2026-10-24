import java.util.*;

class Solution05P1 {
static int lowerBound(int[] a,int target) {
    int lo=0,hi=a.length;
    while(lo<hi) {
        int mid=lo+(hi-lo)/2;
        if(a[mid]<target) lo=mid+1; else hi=mid;
    }
    return lo;
}
}
