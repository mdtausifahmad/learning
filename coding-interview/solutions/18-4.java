import java.util.*;

class Solution18P4 {
static boolean regex(String s,String p) {
    for(int j=0;j<p.length();j++) {
        char c=p.charAt(j);
        if(c=='*') {if(j==0 || p.charAt(j-1)=='*') throw new IllegalArgumentException();}
        else if(c!='.' && (c<'a' || c>'z')) throw new IllegalArgumentException();
    }
    for(int i=0;i<s.length();i++) if(s.charAt(i)<'a'||s.charAt(i)>'z') throw new IllegalArgumentException();
    boolean[][] dp=new boolean[s.length()+1][p.length()+1]; dp[0][0]=true;
    for(int i=0;i<=s.length();i++) for(int j=1;j<=p.length();j++) {
        char atom=p.charAt(j-1);
        if(atom=='*') {
            char prev=p.charAt(j-2);
            dp[i][j]=dp[i][j-2] || (i>0 && (prev=='.'||prev==s.charAt(i-1)) && dp[i-1][j]);
        } else dp[i][j]=i>0 && (atom=='.'||atom==s.charAt(i-1)) && dp[i-1][j-1];
    }
    return dp[s.length()][p.length()];
}
}
