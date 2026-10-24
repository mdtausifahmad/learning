import java.util.*

fun countSmaller(a: IntArray): IntArray {
 val sorted = a.sortedArray()
 var unique = 0
 for (value in sorted) {
  if (unique == 0 || sorted[unique - 1] != value) sorted[unique++] = value
 }
 val bit = IntArray(unique + 1)
 val out = IntArray(a.size)
 for (i in a.lastIndex downTo 0) {
  val rank = Arrays.binarySearch(sorted, 0, unique, a[i])
  var p = rank
  var count = 0
  while (p > 0) { count += bit[p]; p -= p and -p }
  out[i] = count
  var update = rank.toLong() + 1
  while (update <= unique.toLong()) { bit[update.toInt()]++; update += update and -update }
 }
 return out
}
