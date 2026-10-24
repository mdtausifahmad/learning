import java.util.*

class Node(val value: Int, var left: Node? = null, var right: Node? = null)
fun maxDepth(root: Node?): Int {
 if (root == null) return 0
 val q = ArrayDeque<Node>()
 q.add(root)
 var depth = 0
 while (q.isNotEmpty()) {
  val size = q.size
  depth++
  repeat(size) {
   val n = q.remove()
   n.left?.let { q.add(it) }
   n.right?.let { q.add(it) }
  }
 }
 return depth
}
