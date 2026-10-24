import java.util.*

fun ladderLength(begin: String, end: String, words: List<String>): Int {
    if (begin == end) return 1
    val remaining = words.toHashSet()
    if (end !in remaining) return 0
    remaining.remove(begin)
    val queue = ArrayDeque<String>()
    queue.add(begin)
    var length = 1
    while (queue.isNotEmpty()) {
        val layer = queue.size
        repeat(layer) {
            val chars = queue.remove().toCharArray()
            for (i in chars.indices) {
                val saved = chars[i]
                for (ch in 'a'..'z') {
                    if (ch == saved) continue
                    chars[i] = ch
                    val next = String(chars)
                    if (remaining.remove(next)) {
                        if (next == end) return length + 1
                        queue.add(next)
                    }
                }
                chars[i] = saved
            }
        }
        length++
    }
    return 0
}
