import java.util.*;

class Solution06P1 {
static int[][] mergeIntervals(int[][] intervals) {
    int[][] a=new int[intervals.length][2];
    for(int i=0;i<a.length;i++) {
        if(intervals[i][0]>intervals[i][1]) throw new IllegalArgumentException("reversed interval");
        a[i]=intervals[i].clone();
    }
    Arrays.sort(a,Comparator.comparingInt((int[] x)->x[0]).thenComparingInt(x->x[1]));
    List<int[]> ans=new ArrayList<>();
    for(int[] x:a) {
        if(ans.isEmpty() || x[0]>ans.get(ans.size()-1)[1]) ans.add(x.clone());
        else ans.get(ans.size()-1)[1]=Math.max(ans.get(ans.size()-1)[1],x[1]);
    }
    return ans.toArray(new int[0][]);
}
}
