import java.util.*

fun singleNumber(nums: IntArray): Int {
    require(nums.isNotEmpty()) { "nonempty required" }
    var answer = 0
    for (x in nums) answer = answer xor x
    return answer
}
