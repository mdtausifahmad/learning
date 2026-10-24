import java.util.*

fun exactlyK(a: IntArray, k: Int): Long {
    fun atMost(limit: Int): Long {
        if (limit < 0) return 0L
        val freq = HashMap<Int, Int>(); var left = 0; var total = 0L
        for (r in a.indices) {
            freq[a[r]] = (freq[a[r]] ?: 0) + 1
            while (freq.size > limit) {
                val x = a[left++]; val c = freq.getValue(x) - 1
                if (c == 0) freq.remove(x) else freq[x] = c
            }
            total += (r - left + 1).toLong()
        }
        return total
    }
    return if (k <= 0) 0L else atMost(k) - atMost(k - 1)
}
