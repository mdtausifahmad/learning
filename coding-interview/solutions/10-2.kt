import java.util.*

fun topFrequent(a: IntArray, k: Int): IntArray {
 val counts = HashMap<Int, Int>()
 for (v in a) counts[v] = (counts[v] ?: 0) + 1
 require(k >= 0 && k <= counts.size)
 val heap = PriorityQueue<IntArray>(Comparator { x, y ->
  val c = x[1].compareTo(y[1])
  if (c != 0) c else y[0].compareTo(x[0])
 })
 for ((value, count) in counts) {
  heap.add(intArrayOf(value, count))
  if (heap.size > k) heap.remove()
 }
 return IntArray(k) { heap.remove()[0] }
}
