import java.util.*;

class Solution09P2 {
static class Node { int val; Node left,right; Node(int v) { val=v; } }
static boolean isBST(Node root) {
 return within(root,Long.MIN_VALUE,Long.MAX_VALUE);
}
static boolean within(Node n,long low,long high) {
 if(n==null) return true;
 if(n.val<=low || n.val>=high) return false;
 return within(n.left,low,n.val) && within(n.right,n.val,high);
}
}
