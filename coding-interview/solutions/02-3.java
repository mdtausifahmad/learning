import java.util.*;

class Solution02P3 {
static List<List<Integer>> threeSum(int[] nums) {
    int[] a=nums.clone(); Arrays.sort(a);
    List<List<Integer>> ans=new ArrayList<>();
    for(int i=0;i<a.length-2;i++) {
        if(i>0 && a[i]==a[i-1]) continue;
        int l=i+1,r=a.length-1;
        while(l<r) {
            long sum=(long)a[i]+a[l]+a[r];
            if(sum<0) l++; else if(sum>0) r--;
            else {
                ans.add(List.of(a[i],a[l],a[r]));
                int lv=a[l],rv=a[r];
                while(l<r && a[l]==lv) l++;
                while(l<r && a[r]==rv) r--;
            }
        }
    }
    return ans;
}
}
