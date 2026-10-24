import java.util.*

class MedianStream {
 private val lower = PriorityQueue<Int>(Comparator.reverseOrder())
 private val upper = PriorityQueue<Int>()
 fun add(value: Int) {
  if (lower.isEmpty() || value <= lower.element()) lower.add(value)
  else upper.add(value)
  if (lower.size > upper.size + 1) upper.add(lower.remove())
  else if (upper.size > lower.size) lower.add(upper.remove())
 }
 fun median(): Double {
  check(lower.isNotEmpty()) { "empty stream" }
  if (lower.size > upper.size) return lower.element().toDouble()
  return (lower.element().toLong() + upper.element()) / 2.0
 }
}
