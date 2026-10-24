import java.util.*;

class Solution02P4 {
static long trap(int[] h) {
    int l=0,r=h.length-1,leftMax=0,rightMax=0;
    long water=0;
    while(l<=r) {
        if(leftMax<=rightMax) {
            leftMax=Math.max(leftMax,h[l]);
            water+=(long)leftMax-h[l++];
        } else {
            rightMax=Math.max(rightMax,h[r]);
            water+=(long)rightMax-h[r--];
        }
    }
    return water;
}
}
