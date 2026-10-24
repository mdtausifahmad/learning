import java.util.*;

class Solution20P1 {
static int singleNumber(int[] nums) {
    Objects.requireNonNull(nums, "nums");
    if (nums.length == 0) throw new IllegalArgumentException("nonempty required");
    int answer = 0;
    for (int x : nums) answer ^= x;
    return answer;
}
}
