import java.util.*;

class Solution11P4 {
static List<List<String>> queens(int n) {
 if(n<0||n>12) throw new IllegalArgumentException("n outside 0..12");
 List<List<String>> out=new ArrayList<>();
 queenDfs(0,n,new int[n],new boolean[n],new boolean[2*n+1],new boolean[2*n+1],out);
 return out;
}
static void queenDfs(int row,int n,int[] chosen,boolean[] columns,boolean[] down,boolean[] up,List<List<String>> out) {
 if(row==n) {
  List<String> board=new ArrayList<>();
  for(int r=0;r<n;r++) { char[] line=new char[n]; Arrays.fill(line,'.'); line[chosen[r]]='Q'; board.add(new String(line)); }
  out.add(board); return;
 }
 for(int c=0;c<n;c++) {
  int d=row-c+n,u=row+c;
  if(columns[c]||down[d]||up[u]) continue;
  columns[c]=down[d]=up[u]=true; chosen[row]=c;
  queenDfs(row+1,n,chosen,columns,down,up,out);
  columns[c]=down[d]=up[u]=false;
 }
}
}
