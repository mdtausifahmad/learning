import java.util.*;

class Solution04P3 {
static long countSum(int[] a,long k) {
    Map<Long,Long> freq=new HashMap<>(); freq.put(0L,1L);
    long prefix=0,answer=0;
    for(int x:a) {
        prefix+=x;
        // Prefix sums fit long for any JVM int array. Guard subtraction overflow.
        if(!((k>0 && prefix<Long.MIN_VALUE+k) || (k<0 && prefix>Long.MAX_VALUE+k)))
            answer+=freq.getOrDefault(prefix-k,0L);
        freq.merge(prefix,1L,Long::sum);
    }
    return answer;
}
}
