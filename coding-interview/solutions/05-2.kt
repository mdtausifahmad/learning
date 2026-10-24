import java.util.*

fun searchRotated(a: IntArray, target: Int): Int {
    var lo = 0; var hi = a.lastIndex
    while (lo <= hi) {
        val mid = lo + (hi - lo) / 2
        if (a[mid] == target) return mid
        if (a[lo] <= a[mid]) {
            if (a[lo] <= target && target < a[mid]) hi = mid - 1 else lo = mid + 1
        } else {
            if (a[mid] < target && target <= a[hi]) lo = mid + 1 else hi = mid - 1
        }
    }
    return -1
}
