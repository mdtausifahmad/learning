import java.util.*;

class Solution09P3 {
static class Node { int val; Node left,right; Node(int v) { val=v; } }
static class LcaInfo {
 int mask; Node ancestor;
 LcaInfo(int m,Node a) { mask=m; ancestor=a; }
}
static Node lca(Node root,Node p,Node q) {
 Objects.requireNonNull(p); Objects.requireNonNull(q);
 LcaInfo result=lcaInfo(root,p,q);
 return result.mask==3?result.ancestor:null;
}
static LcaInfo lcaInfo(Node n,Node p,Node q) {
 if(n==null) return new LcaInfo(0,null);
 LcaInfo l=lcaInfo(n.left,p,q), r=lcaInfo(n.right,p,q);
 int mask=l.mask|r.mask|(n==p?1:0)|(n==q?2:0);
 Node a=l.mask==3?l.ancestor:r.mask==3?r.ancestor:mask==3?n:null;
 return new LcaInfo(mask,a);
}
}
