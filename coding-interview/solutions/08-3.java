import java.util.*;

class Solution08P3 {
static long largestRectangle(int[] h) {
 for(int value:h) if(value<0) throw new IllegalArgumentException("negative height");
 Deque<Integer> stack=new ArrayDeque<>(); long best=0;
 for(int i=0;i<=h.length;i++) {
  int current=i==h.length?0:h[i];
  while(!stack.isEmpty() && h[stack.peek()]>current) {
   int j=stack.pop(); int left=stack.isEmpty()?-1:stack.peek();
   best=Math.max(best,(long)h[j]*(i-left-1));
  }
  if(i<h.length) stack.push(i);
 }
 return best;
}
}
