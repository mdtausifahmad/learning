import java.util.*

fun twoSingles(nums: IntArray): IntArray {
    var diff = 0
    for (x in nums) diff = diff xor x
    require(diff != 0) { "promise violated" }
    val bit = diff and -diff
    var a = 0
    var b = 0
    for (x in nums) {
        if ((x and bit) == 0) a = a xor x
        else b = b xor x
    }
    return intArrayOf(minOf(a, b), maxOf(a, b))
}
