import java.util.*;

class Solution20P3 {
static int[] twoSingles(int[] nums) {
    Objects.requireNonNull(nums, "nums");
    int diff = 0;
    for (int x : nums) diff ^= x;
    if (diff == 0) throw new IllegalArgumentException("promise violated");
    int bit = diff & -diff;
    int a = 0, b = 0;
    for (int x : nums) {
        if ((x & bit) == 0) a ^= x;
        else b ^= x;
    }
    return new int[] {Math.min(a, b), Math.max(a, b)};
}
}
