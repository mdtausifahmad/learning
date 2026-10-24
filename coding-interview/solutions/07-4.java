import java.util.*;

class Solution07P4 {
static class Node { int val; Node next; Node(int v) { val=v; } }
static Node reverseGroups(Node head, int k) {
 if (k<1) throw new IllegalArgumentException("k must be positive");
 Node dummy=new Node(0); dummy.next=head; Node before=dummy;
 while (true) {
  Node end=before;
  for (int i=0;i<k && end!=null;i++) end=end.next;
  if (end==null) return dummy.next;
  Node after=end.next, current=before.next, previous=after;
  while (current!=after) {
   Node saved=current.next; current.next=previous;
   previous=current; current=saved;
  }
  Node oldFirst=before.next; before.next=end; before=oldFirst;
 }
}
}
