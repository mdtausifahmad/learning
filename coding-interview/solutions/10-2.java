import java.util.*;

class Solution10P2 {
static int[] topFrequent(int[] a,int k) {
 Map<Integer,Integer> counts=new HashMap<>();
 for(int v:a) counts.merge(v,1,Integer::sum);
 if(k<0||k>counts.size()) throw new IllegalArgumentException("invalid k");
 PriorityQueue<int[]> heap=new PriorityQueue<>((x,y)-> {
  int c=Integer.compare(x[1],y[1]);
  return c!=0?c:Integer.compare(y[0],x[0]);
 });
 for(Map.Entry<Integer,Integer> e:counts.entrySet()) {
  heap.add(new int[]{e.getKey(),e.getValue()});
  if(heap.size()>k) heap.remove();
 }
 int[] out=new int[k];
 for(int i=0;i<k;i++) out[i]=heap.remove()[0];
 return out;
}
}
