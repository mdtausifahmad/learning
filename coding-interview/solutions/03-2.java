import java.util.*;

class Solution03P2 {
static int longestUnique(String s) {
    Map<Character,Integer> last=new HashMap<>();
    int left=0,best=0;
    for(int r=0;r<s.length();r++) {
        char c=s.charAt(r); Integer p=last.put(c,r);
        if(p!=null) left=Math.max(left,p+1);
        best=Math.max(best,r-left+1);
    }
    return best;
}
}
