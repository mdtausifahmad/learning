import java.util.*;

class Solution05P2 {
static int searchRotated(int[] a,int target) {
    int lo=0,hi=a.length-1;
    while(lo<=hi) {
        int mid=lo+(hi-lo)/2;
        if(a[mid]==target) return mid;
        if(a[lo]<=a[mid]) {
            if(a[lo]<=target && target<a[mid]) hi=mid-1; else lo=mid+1;
        } else {
            if(a[mid]<target && target<=a[hi]) lo=mid+1; else hi=mid-1;
        }
    }
    return -1;
}
}
