import java.util.*;

class Solution01P3 {
static int longestConsecutive(int[] nums) {
    Set<Long> values=new HashSet<>();
    for (int x:nums) values.add((long)x);
    int best=0;
    for (long x:values) {
        if (values.contains(x-1)) continue;
        long cur=x; int length=0;
        while (values.contains(cur)) { length++; cur++; }
        best=Math.max(best,length);
    }
    return best;
}
}
