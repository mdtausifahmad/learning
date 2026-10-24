import java.util.*

fun longestConsecutive(nums: IntArray): Int {
    val values = nums.map { it.toLong() }.toHashSet()
    var best = 0
    for (x in values) {
        if (x - 1 in values) continue
        var cur = x; var length = 0
        while (cur in values) { length++; cur++ }
        best = maxOf(best, length)
    }
    return best
}
