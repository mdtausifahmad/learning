import java.util.*;

class Solution12P3 {
static class WordDictionary {
 private static class TNode { TNode[] next=new TNode[26]; boolean terminal; }
 private final TNode root=new TNode();
 void addWord(String word) {
  Objects.requireNonNull(word);
  for(char c:word.toCharArray()) if(c<'a'||c>'z') throw new IllegalArgumentException("invalid word");
  TNode n=root;
  for(char c:word.toCharArray()) { int i=c-'a'; if(n.next[i]==null)n.next[i]=new TNode(); n=n.next[i]; }
  n.terminal=true;
 }
 boolean search(String pattern) {
  Objects.requireNonNull(pattern);
  for(char c:pattern.toCharArray()) if(c!='.'&&(c<'a'||c>'z')) throw new IllegalArgumentException("invalid pattern");
  return match(root,pattern,0);
 }
 private boolean match(TNode n,String p,int index) {
  if(index==p.length()) return n.terminal;
  char c=p.charAt(index);
  if(c!='.') { TNode child=n.next[c-'a']; return child!=null&&match(child,p,index+1); }
  for(TNode child:n.next) if(child!=null&&match(child,p,index+1)) return true;
  return false;
 }
}
}
