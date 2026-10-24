import java.util.*;

class Solution07P2 {
static class Node { int val; Node next; Node(int v) { val=v; } }
static Node cycleEntry(Node head) {
 Node slow=head, fast=head;
 while (fast!=null && fast.next!=null) {
  slow=slow.next; fast=fast.next.next;
  if (slow==fast) {
   Node p=head;
   while (p!=slow) { p=p.next; slow=slow.next; }
   return p;
  }
 }
 return null;
}
}
