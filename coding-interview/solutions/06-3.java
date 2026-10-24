import java.util.*;

class Solution06P3 {
static int eraseOverlap(int[][] intervals) {
    int[][] a=intervals.clone();
    for(int[] x:a) if(x[0]>=x[1]) throw new IllegalArgumentException("nonempty interval required");
    Arrays.sort(a,Comparator.comparingInt((int[] x)->x[1]).thenComparingInt(x->x[0]));
    int kept=0; long end=Long.MIN_VALUE;
    for(int[] x:a) if(x[0]>=end) { kept++; end=x[1]; }
    return a.length-kept;
}
}
