import java.util.*;

class Solution07P3 {
static class Node { int val; Node next; Node(int v) { val=v; } }
static Node merge(Node a, Node b) {
 Node dummy=new Node(0), tail=dummy;
 while (a!=null && b!=null) {
  if (a.val<=b.val) { tail.next=a; a=a.next; }
  else { tail.next=b; b=b.next; }
  tail=tail.next;
 }
 tail.next=(a!=null?a:b);
 return dummy.next;
}
}
