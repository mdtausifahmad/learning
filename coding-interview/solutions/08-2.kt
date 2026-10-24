import java.util.*

fun warmer(t: IntArray): IntArray {
 val answer = IntArray(t.size)
 val stack = ArrayDeque<Int>()
 for (i in t.indices) {
  while (stack.isNotEmpty() && t[i] > t[stack.peek()]) {
   val j = stack.pop()
   answer[j] = i - j
  }
  stack.push(i)
 }
 return answer
}
