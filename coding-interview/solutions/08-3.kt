import java.util.*

fun largestRectangle(h: IntArray): Long {
 require(h.all { it >= 0 })
 val stack = ArrayDeque<Int>()
 var best = 0L
 for (i in 0..h.size) {
  val current = if (i == h.size) 0 else h[i]
  while (stack.isNotEmpty() && h[stack.peek()] > current) {
   val j = stack.pop()
   val left = if (stack.isEmpty()) -1 else stack.peek()
   best = maxOf(best, h[j].toLong() * (i - left - 1))
  }
  if (i < h.size) stack.push(i)
 }
 return best
}
