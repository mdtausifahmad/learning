import java.util.*;

class Solution19P1 {
static class RangeSum {
 private final long[] prefix;
 RangeSum(long[] values) {
  prefix=new long[values.length+1];
  for(int i=0;i<values.length;i++) prefix[i+1]=prefix[i]+values[i];
 }
 long sum(int left,int right) {
  int n=prefix.length-1;
  if(left<0||left>right||right>n) throw new IndexOutOfBoundsException();
  return prefix[right]-prefix[left];
 }
}
}
