import java.util.*

fun rob(rewards: IntArray): Long {
    var two = 0L; var one = 0L
    for (x in rewards) {
        val current = maxOf(one, two + x); two = one; one = current
    }
    return one
}
