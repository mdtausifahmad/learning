import java.util.*

fun countBits(n: Int): IntArray {
    require(n in 0..1_000_000) { "n out of range" }
    val count = IntArray(n + 1)
    for (x in 1..n) count[x] = count[x and (x - 1)] + 1
    return count
}
