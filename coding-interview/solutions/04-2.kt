import java.util.*

fun pivotIndex(a: IntArray): Int {
    var total = 0L; for (x in a) total += x
    var left = 0L
    for (i in a.indices) {
        if (left == total - left - a[i]) return i
        left += a[i]
    }
    return -1
}
