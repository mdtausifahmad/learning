import java.util.*;

class Solution09P1 {
static class Node { int val; Node left,right; Node(int v) { val=v; } }
static int maxDepth(Node root) {
 if(root==null) return 0;
 Deque<Node> q=new ArrayDeque<>(); q.add(root); int depth=0;
 while(!q.isEmpty()) {
  int size=q.size(); depth++;
  for(int i=0;i<size;i++) {
   Node n=q.remove();
   if(n.left!=null) q.add(n.left);
   if(n.right!=null) q.add(n.right);
  }
 }
 return depth;
}
}
