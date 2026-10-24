import java.util.*;

class Solution19P3 {
static class SegmentMax {
 private final int n; private final long[] tree;
 SegmentMax(long[] values) {
  n=values.length; tree=new long[Math.multiplyExact(2,n)];
  System.arraycopy(values,0,tree,n,n);
  for(int i=n-1;i>0;i--) tree[i]=Math.max(tree[2*i],tree[2*i+1]);
 }
 void set(int index,long value) {
  if(index<0||index>=n) throw new IndexOutOfBoundsException();
  int p=index+n; tree[p]=value;
  while(p>1) { p/=2; tree[p]=Math.max(tree[2*p],tree[2*p+1]); }
 }
 Long maximum(int left,int right) {
  if(left<0||left>right||right>n) throw new IndexOutOfBoundsException();
  if(left==right) return null;
  int l=left+n,r=right+n; long best=Long.MIN_VALUE;
  while(l<r) {
   if((l&1)==1) best=Math.max(best,tree[l++]);
   if((r&1)==1) best=Math.max(best,tree[--r]);
   l/=2; r/=2;
  }
  return best;
 }
}
}
