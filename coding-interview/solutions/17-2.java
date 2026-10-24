import java.util.*;

class Solution17P2 {
static long rob(int[] rewards) {
    long two=0,one=0;
    for(int x:rewards) {
        long current=Math.max(one,two+x); two=one; one=current;
    }
    return one;
}
}
