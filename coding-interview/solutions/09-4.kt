import java.util.*

class Node(val value: Int, var left: Node? = null, var right: Node? = null)
fun maximumPath(root: Node?): Long? {
 if (root == null) return null
 var best = Long.MIN_VALUE
 fun gain(n: Node?): Long {
  if (n == null) return 0L
  val left = maxOf(0L, gain(n.left))
  val right = maxOf(0L, gain(n.right))
  best = maxOf(best, n.value.toLong() + left + right)
  return n.value.toLong() + maxOf(left, right)
 }
 gain(root)
 return best
}
