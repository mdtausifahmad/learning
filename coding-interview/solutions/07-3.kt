import java.util.*

class Node(val value: Int, var next: Node? = null)
fun merge(first: Node?, second: Node?): Node? {
 val dummy = Node(0)
 var tail = dummy
 var a = first
 var b = second
 while (a != null && b != null) {
  if (a.value <= b.value) { tail.next = a; a = a.next }
  else { tail.next = b; b = b.next }
  tail = tail.next!!
 }
 tail.next = a ?: b
 return dummy.next
}
