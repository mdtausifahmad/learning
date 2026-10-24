import java.util.*

fun climbStairs(n: Int): Long {
    require(n in 0..91) { "n must be in 0..91" }
    if (n <= 1) return 1L
    var previous = 1L; var current = 1L
    for (i in 2..n) {
        val next = previous + current; previous = current; current = next
    }
    return current
}
