import java.util.*;

class Solution09P4 {
static class Node { int val; Node left,right; Node(int v) { val=v; } }
static Long maximumPath(Node root) {
 if(root==null) return null;
 long[] best={Long.MIN_VALUE}; gain(root,best); return best[0];
}
static long gain(Node n,long[] best) {
 if(n==null) return 0;
 long left=Math.max(0,gain(n.left,best));
 long right=Math.max(0,gain(n.right,best));
 best[0]=Math.max(best[0],(long)n.val+left+right);
 return (long)n.val+Math.max(left,right);
}
}
