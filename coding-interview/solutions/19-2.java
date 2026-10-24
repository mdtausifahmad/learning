import java.util.*;

class Solution19P2 {
static class Fenwick {
 private final int n; private final long[] bit;
 Fenwick(long[] values) {
  n=values.length; bit=new long[n+1];
  for(int i=0;i<n;i++) add(i,values[i]);
 }
 void add(int index,long delta) {
  if(index<0||index>=n) throw new IndexOutOfBoundsException();
  for(long i=(long)index+1;i<=n;i+=i&-i) bit[(int)i]+=delta;
 }
 private long prefix(int end) {
  long sum=0;
  for(int i=end;i>0;i-=i&-i) sum+=bit[i];
  return sum;
 }
 long sum(int left,int right) {
  if(left<0||left>right||right>n) throw new IndexOutOfBoundsException();
  return prefix(right)-prefix(left);
 }
}
}
