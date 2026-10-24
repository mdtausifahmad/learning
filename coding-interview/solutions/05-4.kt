import java.util.*

fun medianSorted(first: IntArray, second: IntArray): Double {
    val a = if (first.size <= second.size) first else second
    val b = if (first.size <= second.size) second else first
    val m = a.size; val n = b.size
    require(m != 0 || n != 0) { "empty" }
    val half = ((m.toLong() + n + 1) / 2).toInt()
    var lo = 0; var hi = m
    while (lo <= hi) {
        val i = lo + (hi - lo) / 2; val j = half - i
        val al = if (i == 0) Long.MIN_VALUE else a[i - 1].toLong()
        val ar = if (i == m) Long.MAX_VALUE else a[i].toLong()
        val bl = if (j == 0) Long.MIN_VALUE else b[j - 1].toLong()
        val br = if (j == n) Long.MAX_VALUE else b[j].toLong()
        if (al <= br && bl <= ar) {
            val left = maxOf(al, bl)
            return if ((m.toLong() + n) % 2 == 1L) left.toDouble()
                else (left + minOf(ar, br)) / 2.0
        }
        if (al > br) hi = i - 1 else lo = i + 1
    }
    throw IllegalArgumentException("arrays must be sorted")
}
