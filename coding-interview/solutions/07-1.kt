import java.util.*

class Node(val value: Int, var next: Node? = null)
fun reverse(head: Node?): Node? {
 var previous: Node? = null
 var current = head
 while (current != null) {
  val saved = current.next
  current.next = previous
  previous = current
  current = saved
 }
 return previous
}
