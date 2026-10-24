import java.util.*;

class Solution16P3 {
static List<Integer> partitions(String s) {
    int[] last=new int[26];
    for(int i=0;i<s.length();i++) last[s.charAt(i)-'a']=i;
    List<Integer> out=new ArrayList<>(); int start=0,end=0;
    for(int i=0;i<s.length();i++) {
        end=Math.max(end,last[s.charAt(i)-'a']);
        if(i==end){out.add(i-start+1);start=i+1;}
    }
    return out;
}
}
