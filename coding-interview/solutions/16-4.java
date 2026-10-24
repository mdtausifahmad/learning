import java.util.*;

class Solution16P4 {
static long candy(int[] rating) {
    int n=rating.length; if(n==0) return 0L;
    int[] left=new int[n]; Arrays.fill(left,1);
    for(int i=1;i<n;i++) if(rating[i]>rating[i-1]) left[i]=left[i-1]+1;
    int right=1; long total=left[n-1];
    for(int i=n-2;i>=0;i--) {
        right=rating[i]>rating[i+1]?right+1:1;
        total+=Math.max(left[i],right);
    }
    return total;
}
}
