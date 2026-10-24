import java.util.*;

class Solution07P1 {
static class Node { int val; Node next; Node(int v) { val=v; } }
static Node reverse(Node head) {
 Node previous=null, current=head;
 while (current!=null) {
  Node saved=current.next; current.next=previous;
  previous=current; current=saved;
 }
 return previous;
}
}
