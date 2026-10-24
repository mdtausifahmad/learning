import java.util.*

fun minWindow(s: String, t: String): String {
    if (t.isEmpty()) return ""
    val need = HashMap<Char, Int>()
    for (c in t) need[c] = (need[c] ?: 0) + 1
    var missing = t.length; var left = 0; var bestStart = 0; var bestLen = Int.MAX_VALUE
    for (r in s.indices) {
        val c = s[r]; val old = need[c]
        if (old != null) { if (old > 0) missing--; need[c] = old - 1 }
        while (missing == 0) {
            if (r - left + 1 < bestLen) { bestLen = r - left + 1; bestStart = left }
            val gone = s[left++]; val count = need[gone]
            if (count != null) { val now = count + 1; need[gone] = now; if (now > 0) missing++ }
        }
    }
    return if (bestLen == Int.MAX_VALUE) "" else s.substring(bestStart, bestStart + bestLen)
}
