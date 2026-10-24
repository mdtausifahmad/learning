import java.util.*;

class Solution10P4 {
static class MedianStream {
 private final PriorityQueue<Integer> lower=new PriorityQueue<>(Comparator.reverseOrder());
 private final PriorityQueue<Integer> upper=new PriorityQueue<>();
 void add(int value) {
  if(lower.isEmpty()||value<=lower.element()) lower.add(value);
  else upper.add(value);
  if(lower.size()>upper.size()+1) upper.add(lower.remove());
  else if(upper.size()>lower.size()) lower.add(upper.remove());
 }
 double median() {
  if(lower.isEmpty()) throw new IllegalStateException("empty stream");
  if(lower.size()>upper.size()) return lower.element();
  return ((long)lower.element()+upper.element())/2.0;
 }
}
}
