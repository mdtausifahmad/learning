import java.util.*;

class Solution11P1 {
static List<List<Integer>> subsets(int[] a) {
 List<List<Integer>> out=new ArrayList<>();
 subsetDfs(a,0,new ArrayList<>(),out); return out;
}
static void subsetDfs(int[] a,int start,List<Integer> path,List<List<Integer>> out) {
 out.add(new ArrayList<>(path));
 for(int i=start;i<a.length;i++) {
  path.add(a[i]); subsetDfs(a,i+1,path,out); path.remove(path.size()-1);
 }
}
}
