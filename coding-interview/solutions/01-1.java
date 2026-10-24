import java.util.*;

class Solution01P1 {
static int[] twoSum(int[] nums, long target) {
    Map<Integer,Integer> seen = new HashMap<>();
    for (int i=0;i<nums.length;i++) {
        long need=target-(long)nums[i];
        if (need>=Integer.MIN_VALUE && need<=Integer.MAX_VALUE) {
            Integer j=seen.get((int)need);
            if (j!=null) return new int[]{j,i};
        }
        seen.put(nums[i],i);
    }
    return new int[0];
}
}
