import java.util.*;

class Solution12P4 {
static class BoardNode { BoardNode[] next=new BoardNode[26]; String word; }
static List<String> boardWords(char[][] board,String[] words) {
 BoardNode root=new BoardNode();
 for(String word:words) {
  if(word==null||!word.matches("[a-z]+")) throw new IllegalArgumentException("invalid word");
  BoardNode n=root;
  for(char c:word.toCharArray()) { int i=c-'a'; if(n.next[i]==null)n.next[i]=new BoardNode(); n=n.next[i]; }
  n.word=word;
 }
 int rows=board.length, cols=rows==0?0:board[0].length;
 for(char[] row:board) {
  if(row.length!=cols) throw new IllegalArgumentException("ragged board");
  for(char c:row) if(c<'a'||c>'z') throw new IllegalArgumentException("invalid board");
 }
 List<String> out=new ArrayList<>(); boolean[][] used=new boolean[rows][cols];
 for(int r=0;r<rows;r++) for(int c=0;c<cols;c++) boardDfs(board,r,c,root,used,out);
 return out;
}
static void boardDfs(char[][] b,int r,int c,BoardNode parent,boolean[][] used,List<String> out) {
 if(r<0||r>=b.length||c<0||c>=b[0].length||used[r][c]) return;
 BoardNode n=parent.next[b[r][c]-'a']; if(n==null) return;
 if(n.word!=null) { out.add(n.word); n.word=null; }
 used[r][c]=true;
 boardDfs(b,r-1,c,n,used,out); boardDfs(b,r+1,c,n,used,out);
 boardDfs(b,r,c-1,n,used,out); boardDfs(b,r,c+1,n,used,out);
 used[r][c]=false;
}
}
