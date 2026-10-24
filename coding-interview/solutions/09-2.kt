import java.util.*

class Node(val value: Int, var left: Node? = null, var right: Node? = null)
fun isBST(root: Node?): Boolean {
 fun within(n: Node?, low: Long, high: Long): Boolean {
  if (n == null) return true
  val v = n.value.toLong()
  if (v <= low || v >= high) return false
  return within(n.left, low, v) && within(n.right, v, high)
 }
 return within(root, Long.MIN_VALUE, Long.MAX_VALUE)
}
