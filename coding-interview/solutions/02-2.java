import java.util.*;

class Solution02P2 {
static boolean validPalindrome(String s) {
    int l=0,r=s.length()-1;
    while(l<r) {
        if(s.charAt(l)!=s.charAt(r)) return exact(s,l+1,r)||exact(s,l,r-1);
        l++; r--;
    }
    return true;
}
static boolean exact(String s,int l,int r) {
    while(l<r) if(s.charAt(l++)!=s.charAt(r--)) return false;
    return true;
}
}
