import java.util.*

fun longestUnique(s: String): Int {
    val last = HashMap<Char, Int>(); var left = 0; var best = 0
    for (r in s.indices) {
        val p = last.put(s[r], r)
        if (p != null) left = maxOf(left, p + 1)
        best = maxOf(best, r - left + 1)
    }
    return best
}
