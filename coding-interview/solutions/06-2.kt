import java.util.*

fun insertInterval(a: Array<IntArray>, added: IntArray): Array<IntArray> {
    require(added[0] <= added[1])
    for (j in a.indices) require(a[j][0] <= a[j][1] && (j == 0 || a[j - 1][1] < a[j][0]))
    val ans = mutableListOf<IntArray>(); var i = 0; var start = added[0]; var end = added[1]
    while (i < a.size && a[i][1] < start) ans.add(a[i++].copyOf())
    while (i < a.size && a[i][0] <= end) {
        start = minOf(start, a[i][0]); end = maxOf(end, a[i][1]); i++
    }
    ans.add(intArrayOf(start, end))
    while (i < a.size) ans.add(a[i++].copyOf())
    return ans.toTypedArray()
}
