import java.util.*

class Node(val value: Int, var left: Node? = null, var right: Node? = null)
data class LcaInfo(val mask: Int, val ancestor: Node?)
fun lca(root: Node?, p: Node, q: Node): Node? {
 fun visit(n: Node?): LcaInfo {
  if (n == null) return LcaInfo(0, null)
  val l = visit(n.left)
  val r = visit(n.right)
  val mask = l.mask or r.mask or (if (n === p) 1 else 0) or (if (n === q) 2 else 0)
  val a = when {
   l.mask == 3 -> l.ancestor
   r.mask == 3 -> r.ancestor
   mask == 3 -> n
   else -> null
  }
  return LcaInfo(mask, a)
 }
 val result = visit(root)
 return if (result.mask == 3) result.ancestor else null
}
