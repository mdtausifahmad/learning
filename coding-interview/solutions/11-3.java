import java.util.*;

class Solution11P3 {
static List<List<Integer>> uniquePermutations(int[] input) {
 int[] a=input.clone(); Arrays.sort(a);
 List<List<Integer>> out=new ArrayList<>();
 permutationDfs(a,new boolean[a.length],new ArrayList<>(),out); return out;
}
static void permutationDfs(int[] a,boolean[] used,List<Integer> path,List<List<Integer>> out) {
 if(path.size()==a.length) { out.add(new ArrayList<>(path)); return; }
 for(int i=0;i<a.length;i++) {
  if(used[i] || (i>0 && a[i]==a[i-1] && !used[i-1])) continue;
  used[i]=true; path.add(a[i]); permutationDfs(a,used,path,out);
  path.remove(path.size()-1); used[i]=false;
 }
}
}
