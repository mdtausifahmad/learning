import java.util.*

fun threeSum(nums: IntArray): List<List<Int>> {
    val a = nums.copyOf(); a.sort()
    val ans = mutableListOf<List<Int>>()
    for (i in 0 until maxOf(0, a.size - 2)) {
        if (i > 0 && a[i] == a[i - 1]) continue
        var l = i + 1; var r = a.lastIndex
        while (l < r) {
            val sum = a[i].toLong() + a[l] + a[r]
            when {
                sum < 0 -> l++
                sum > 0 -> r--
                else -> {
                    ans.add(listOf(a[i], a[l], a[r]))
                    val lv = a[l]; val rv = a[r]
                    while (l < r && a[l] == lv) l++
                    while (l < r && a[r] == rv) r--
                }
            }
        }
    }
    return ans
}
