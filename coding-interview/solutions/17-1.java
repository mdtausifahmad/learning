import java.util.*;

class Solution17P1 {
static long climbStairs(int n) {
    if(n<0 || n>91) throw new IllegalArgumentException("n must be in 0..91");
    if(n<=1) return 1;
    long previous=1,current=1;
    for(int i=2;i<=n;i++) {
        long next=previous+current; previous=current; current=next;
    }
    return current;
}
}
