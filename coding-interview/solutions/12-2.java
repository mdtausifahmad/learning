import java.util.*;

class Solution12P2 {
static class RootNode { RootNode[] next=new RootNode[26]; boolean terminal; }
static String replaceRoots(List<String> roots,String sentence) {
 Objects.requireNonNull(sentence);
 if(!sentence.isEmpty() && !sentence.matches("[a-z]+( [a-z]+)*")) throw new IllegalArgumentException("invalid sentence");
 RootNode root=new RootNode();
 for(String word:roots) {
  if(word==null||!word.matches("[a-z]+")) throw new IllegalArgumentException("invalid root");
  RootNode n=root;
  for(char c:word.toCharArray()) { int i=c-'a'; if(n.next[i]==null)n.next[i]=new RootNode(); n=n.next[i]; }
  n.terminal=true;
 }
 if(sentence.isEmpty()) return "";
 String[] words=sentence.split(" ");
 for(int w=0;w<words.length;w++) {
  RootNode n=root;
  for(int i=0;i<words[w].length();i++) {
   n=n.next[words[w].charAt(i)-'a']; if(n==null) break;
   if(n.terminal) { words[w]=words[w].substring(0,i+1); break; }
  }
 }
 return String.join(" ",words);
}
}
