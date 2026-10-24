import java.util.*;

class Solution04P4 {
static long countDivisible(int[] a,int k) {
    if(k<=0) throw new IllegalArgumentException("k must be positive");
    Map<Integer,Long> freq=new HashMap<>(); freq.put(0,1L);
    int residue=0; long answer=0;
    for(int x:a) {
        residue=(int)Math.floorMod((long)residue+x,(long)k);
        answer+=freq.getOrDefault(residue,0L);
        freq.merge(residue,1L,Long::sum);
    }
    return answer;
}
}
