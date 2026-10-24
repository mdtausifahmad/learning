import java.util.*

fun sortedPair(a: IntArray, target: Long): IntArray {
    var l = 0; var r = a.lastIndex
    while (l < r) {
        val sum = a[l].toLong() + a[r]
        if (sum == target) return intArrayOf(l, r)
        if (sum < target) l++ else r--
    }
    return intArrayOf()
}
