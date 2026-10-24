import java.util.*

fun windowMax(a: IntArray, k: Int): IntArray {
 require(k >= 1 && k <= a.size)
 val out = IntArray(a.size - k + 1)
 val q = ArrayDeque<Int>()
 for (i in a.indices) {
  while (q.isNotEmpty() && q.peekFirst() <= i - k) q.removeFirst()
  while (q.isNotEmpty() && a[q.peekLast()] <= a[i]) q.removeLast()
  q.addLast(i)
  if (i >= k - 1) out[i - k + 1] = a[q.peekFirst()]
 }
 return out
}
