import java.util.*;

class Solution03P4 {
static String minWindow(String s,String t) {
    if(t.isEmpty()) return "";
    Map<Character,Integer> need=new HashMap<>();
    for(char c:t.toCharArray()) need.merge(c,1,Integer::sum);
    int missing=t.length(),left=0,bestStart=0,bestLen=Integer.MAX_VALUE;
    for(int r=0;r<s.length();r++) {
        char c=s.charAt(r);
        if(need.containsKey(c)) {
            int old=need.get(c); if(old>0) missing--; need.put(c,old-1);
        }
        while(missing==0) {
            if(r-left+1<bestLen) { bestLen=r-left+1; bestStart=left; }
            char gone=s.charAt(left++);
            if(need.containsKey(gone)) {
                int now=need.get(gone)+1; need.put(gone,now); if(now>0) missing++;
            }
        }
    }
    return bestLen==Integer.MAX_VALUE ? "" : s.substring(bestStart,bestStart+bestLen);
}
}
