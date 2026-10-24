import java.util.*

class SegmentMax(values: LongArray) {
 private val n = values.size
 private val tree = LongArray(Math.multiplyExact(2, n))
 init {
  values.copyInto(tree, n)
  for (i in n - 1 downTo 1) tree[i] = maxOf(tree[2 * i], tree[2 * i + 1])
 }
 fun set(index: Int, value: Long) {
  require(index in 0 until n)
  var p = index + n
  tree[p] = value
  while (p > 1) { p /= 2; tree[p] = maxOf(tree[2 * p], tree[2 * p + 1]) }
 }
 fun maximum(left: Int, right: Int): Long? {
  require(left >= 0 && left <= right && right <= n)
  if (left == right) return null
  var l = left + n
  var r = right + n
  var best = Long.MIN_VALUE
  while (l < r) {
   if ((l and 1) == 1) { best = maxOf(best, tree[l]); l++ }
   if ((r and 1) == 1) { r--; best = maxOf(best, tree[r]) }
   l /= 2; r /= 2
  }
  return best
 }
}
