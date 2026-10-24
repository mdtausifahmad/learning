import java.util.*

fun twoSum(nums: IntArray, target: Long): IntArray {
    val seen = HashMap<Int, Int>()
    for (i in nums.indices) {
        val need = target - nums[i].toLong()
        if (need in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()) {
            val j = seen[need.toInt()]
            if (j != null) return intArrayOf(j, i)
        }
        seen[nums[i]] = i
    }
    return intArrayOf()
}
