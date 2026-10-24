import java.util.*

fun firstMissingPositive(a: IntArray): Int {
    val n = a.size
    for (i in a.indices) {
        while (a[i] >= 1 && a[i] <= n && a[a[i] - 1] != a[i]) {
            val j = a[i] - 1; val tmp = a[j]; a[j] = a[i]; a[i] = tmp
        }
    }
    for (i in a.indices) if (a[i] != i + 1) return i + 1
    return n + 1
}
