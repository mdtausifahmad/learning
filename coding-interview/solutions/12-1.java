import java.util.*;

class Solution12P1 {
static class Trie {
 private static class TNode { TNode[] next=new TNode[26]; boolean terminal; }
 private final TNode root=new TNode();
 private void validate(String s) {
  Objects.requireNonNull(s);
  for(int i=0;i<s.length();i++) if(s.charAt(i)<'a'||s.charAt(i)>'z') throw new IllegalArgumentException("lowercase a-z only");
 }
 void insert(String word) {
  validate(word); TNode n=root;
  for(int i=0;i<word.length();i++) {
   int c=word.charAt(i)-'a';
   if(n.next[c]==null) n.next[c]=new TNode();
   n=n.next[c];
  }
  n.terminal=true;
 }
 private TNode follow(String s) {
  validate(s); TNode n=root;
  for(int i=0;i<s.length();i++) { n=n.next[s.charAt(i)-'a']; if(n==null) return null; }
  return n;
 }
 boolean search(String word) { TNode n=follow(word); return n!=null&&n.terminal; }
 boolean startsWith(String prefix) { return follow(prefix)!=null; }
}
}
