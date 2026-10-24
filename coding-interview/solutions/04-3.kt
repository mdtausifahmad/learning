import java.util.*

fun countSum(a: IntArray, k: Long): Long {
    val freq = HashMap<Long, Long>(); freq[0L] = 1L
    var prefix = 0L; var answer = 0L
    for (x in a) {
        prefix += x
        val overflow = (k > 0 && prefix < Long.MIN_VALUE + k) ||
            (k < 0 && prefix > Long.MAX_VALUE + k)
        if (!overflow) answer += freq[prefix - k] ?: 0L
        freq[prefix] = (freq[prefix] ?: 0L) + 1L
    }
    return answer
}
