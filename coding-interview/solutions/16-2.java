import java.util.*;

class Solution16P2 {
static boolean canJump(int[] a) {
    if(a.length==0) return false;
    long far=0;
    for(int i=0;i<a.length;i++) {
        if(i>far) return false;
        far=Math.max(far,(long)i+a[i]);
        if(far>=a.length-1) return true;
    }
    return false;
}
}
