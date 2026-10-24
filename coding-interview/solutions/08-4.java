import java.util.*;

class Solution08P4 {
static int[] windowMax(int[] a,int k) {
 if(k<1||k>a.length) throw new IllegalArgumentException("invalid window");
 int[] out=new int[a.length-k+1]; Deque<Integer> q=new ArrayDeque<>();
 for(int i=0;i<a.length;i++) {
  while(!q.isEmpty() && q.peekFirst()<=i-k) q.removeFirst();
  while(!q.isEmpty() && a[q.peekLast()]<=a[i]) q.removeLast();
  q.addLast(i);
  if(i>=k-1) out[i-k+1]=a[q.peekFirst()];
 }
 return out;
}
}
