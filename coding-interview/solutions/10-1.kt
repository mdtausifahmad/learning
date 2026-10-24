import java.util.*

fun kthLargest(a: IntArray, k: Int): Int {
 require(k >= 1 && k <= a.size)
 val heap = PriorityQueue<Int>()
 for (value in a) {
  heap.add(value)
  if (heap.size > k) heap.remove()
 }
 return heap.element()
}
