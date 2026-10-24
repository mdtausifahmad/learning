import java.util.*;

class Solution06P4 {
static List<List<Integer>> skyline(int[][] buildings) {
    List<int[]> events=new ArrayList<>();
    for(int[] b:buildings) {
        if(b[0]>=b[1] || b[2]<=0) throw new IllegalArgumentException("building");
        events.add(new int[]{b[0],b[2],1}); events.add(new int[]{b[1],b[2],-1});
    }
    events.sort(Comparator.comparingInt(e->e[0]));
    TreeMap<Integer,Integer> active=new TreeMap<>(); active.put(0,1);
    List<List<Integer>> ans=new ArrayList<>(); int i=0,previous=0;
    while(i<events.size()) {
        int x=events.get(i)[0];
        while(i<events.size() && events.get(i)[0]==x) {
            int[] e=events.get(i++); int count=active.getOrDefault(e[1],0)+e[2];
            if(count==0) active.remove(e[1]); else active.put(e[1],count);
        }
        int height=active.lastKey();
        if(height!=previous) { ans.add(List.of(x,height)); previous=height; }
    }
    return ans;
}
}
