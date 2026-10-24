import java.util.*

fun rangeSums(a: IntArray, queries: Array<IntArray>): LongArray {
    val prefix = LongArray(a.size + 1)
    for (i in a.indices) prefix[i + 1] = prefix[i] + a[i]
    return LongArray(queries.size) { i ->
        val l = queries[i][0]; val r = queries[i][1]
        require(l >= 0 && l <= r && r < a.size) { "bounds" }
        prefix[r + 1] - prefix[l]
    }
}
