import java.util.*;

class Solution11P2 {
static List<List<Integer>> combinationSum(int[] candidates,int target) {
 if(target<0) throw new IllegalArgumentException("negative target");
 for(int v:candidates) if(v<=0) throw new IllegalArgumentException("nonpositive candidate");
 int[] a=candidates.clone(); Arrays.sort(a);
 List<List<Integer>> out=new ArrayList<>();
 combinationDfs(a,0,target,new ArrayList<>(),out); return out;
}
static void combinationDfs(int[] a,int start,int remaining,List<Integer> path,List<List<Integer>> out) {
 if(remaining==0) { out.add(new ArrayList<>(path)); return; }
 for(int i=start;i<a.length;i++) {
  if(i>start && a[i]==a[i-1]) continue;
  if(a[i]>remaining) break;
  path.add(a[i]); combinationDfs(a,i,remaining-a[i],path,out);
  path.remove(path.size()-1);
 }
}
}
