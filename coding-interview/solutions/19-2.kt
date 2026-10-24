import java.util.*

class Fenwick(values: LongArray) {
 private val n = values.size
 private val bit = LongArray(n + 1)
 init { for (i in values.indices) add(i, values[i]) }
 fun add(index: Int, delta: Long) {
  require(index in 0 until n)
  var i = index.toLong() + 1
  while (i <= n.toLong()) { bit[i.toInt()] += delta; i += i and -i }
 }
 private fun prefix(end: Int): Long {
  var i = end
  var sum = 0L
  while (i > 0) { sum += bit[i]; i -= i and -i }
  return sum
 }
 fun sum(left: Int, right: Int): Long {
  require(left >= 0 && left <= right && right <= n)
  return prefix(right) - prefix(left)
 }
}
