import java.util.*

class Node(val value: Int, var next: Node? = null)
fun reverseGroups(head: Node?, k: Int): Node? {
 require(k >= 1)
 val dummy = Node(0, head)
 var before = dummy
 while (true) {
  var end: Node? = before
  var count = 0
  while (count < k && end != null) { end = end?.next; count++ }
  val groupEnd = end ?: return dummy.next
  val after = groupEnd.next
  var current = before.next
  var previous = after
  while (current !== after) {
   val node = current!!
   val saved = node.next
   node.next = previous
   previous = node
   current = saved
  }
  val oldFirst = before.next!!
  before.next = groupEnd
  before = oldFirst
 }
}
