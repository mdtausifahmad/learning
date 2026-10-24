import java.util.*;

class Solution19P4 {
static int[] countSmaller(int[] a) {
 int[] sorted=a.clone(); Arrays.sort(sorted); int unique=0;
 for(int value:sorted) if(unique==0||sorted[unique-1]!=value) sorted[unique++]=value;
 int[] bit=new int[unique+1], out=new int[a.length];
 for(int i=a.length-1;i>=0;i--) {
  int rank=Arrays.binarySearch(sorted,0,unique,a[i]); int count=0;
  for(int p=rank;p>0;p-=p&-p) count+=bit[p];
  out[i]=count;
  for(long p=(long)rank+1;p<=unique;p+=p&-p) bit[(int)p]++;
 }
 return out;
}
}
