import java.util.*;

class Solution10P3 {
static class Entry {
 int value,row,col;
 Entry(int v,int r,int c) { value=v; row=r; col=c; }
}
static int[] mergeStreams(int[][] streams) {
 int total=0;
 PriorityQueue<Entry> heap=new PriorityQueue<>(Comparator.comparingInt(e->e.value));
 for(int r=0;r<streams.length;r++) {
  total=Math.addExact(total,streams[r].length);
  if(streams[r].length>0) heap.add(new Entry(streams[r][0],r,0));
 }
 int[] out=new int[total]; int position=0;
 while(!heap.isEmpty()) {
  Entry e=heap.remove(); out[position++]=e.value;
  int next=e.col+1;
  if(next<streams[e.row].length) heap.add(new Entry(streams[e.row][next],e.row,next));
 }
 return out;
}
}
