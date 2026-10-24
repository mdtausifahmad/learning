import java.util.*

class Node(val value: Int, var next: Node? = null)
fun cycleEntry(head: Node?): Node? {
 var slow = head
 var fast = head
 while (fast != null && fast.next != null) {
  slow = slow?.next
  fast = fast.next?.next
  if (slow === fast) {
   var p = head
   while (p !== slow) { p = p?.next; slow = slow?.next }
   return p
  }
 }
 return null
}
