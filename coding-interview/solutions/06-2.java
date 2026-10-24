import java.util.*;

class Solution06P2 {
static int[][] insertInterval(int[][] a,int[] added) {
    if(added[0]>added[1]) throw new IllegalArgumentException("new interval");
    for(int j=0;j<a.length;j++) {
        if(a[j][0]>a[j][1] || (j>0 && a[j-1][1]>=a[j][0]))
            throw new IllegalArgumentException("sorted disjoint intervals required");
    }
    List<int[]> ans=new ArrayList<>(); int i=0,start=added[0],end=added[1];
    while(i<a.length && a[i][1]<start) ans.add(a[i++].clone());
    while(i<a.length && a[i][0]<=end) {
        start=Math.min(start,a[i][0]); end=Math.max(end,a[i][1]); i++;
    }
    ans.add(new int[]{start,end});
    while(i<a.length) ans.add(a[i++].clone());
    return ans.toArray(new int[0][]);
}
}
