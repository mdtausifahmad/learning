import java.util.*;

class Solution03P3 {
static long exactlyK(int[] a,int k) {
    return k<=0 ? 0 : atMost(a,k)-atMost(a,k-1);
}
static long atMost(int[] a,int k) {
    if(k<0) return 0;
    Map<Integer,Integer> freq=new HashMap<>(); int left=0; long total=0;
    for(int r=0;r<a.length;r++) {
        freq.merge(a[r],1,Integer::sum);
        while(freq.size()>k) {
            int x=a[left++], c=freq.get(x)-1;
            if(c==0) freq.remove(x); else freq.put(x,c);
        }
        total+=r-left+1;
    }
    return total;
}
}
