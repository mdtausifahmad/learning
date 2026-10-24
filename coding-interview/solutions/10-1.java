import java.util.*;

class Solution10P1 {
static int kthLargest(int[] a,int k) {
 if(k<1||k>a.length) throw new IllegalArgumentException("invalid k");
 PriorityQueue<Integer> heap=new PriorityQueue<>();
 for(int value:a) {
  heap.add(value);
  if(heap.size()>k) heap.remove();
 }
 return heap.element();
}
}
