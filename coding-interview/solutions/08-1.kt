import java.util.*

fun validBrackets(s: String): Boolean {
 val stack = ArrayDeque<Char>()
 for (c in s) {
  when (c) {
   '(' -> stack.push(')')
   '[' -> stack.push(']')
   '{' -> stack.push('}')
   ')', ']', '}' -> if (stack.isEmpty() || stack.pop() != c) return false
   else -> return false
  }
 }
 return stack.isEmpty()
}
