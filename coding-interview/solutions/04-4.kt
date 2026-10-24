import java.util.*

fun countDivisible(a: IntArray, k: Int): Long {
    require(k > 0) { "k must be positive" }
    val freq = HashMap<Int, Long>(); freq[0] = 1L
    var residue = 0; var answer = 0L
    for (x in a) {
        residue = Math.floorMod(residue.toLong() + x, k.toLong()).toInt()
        answer += freq[residue] ?: 0L
        freq[residue] = (freq[residue] ?: 0L) + 1L
    }
    return answer
}
