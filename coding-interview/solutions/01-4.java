import java.util.*;

class Solution01P4 {
static int firstMissingPositive(int[] a) {
    int n=a.length;
    for (int i=0;i<n;i++) {
        while (a[i]>=1 && a[i]<=n && a[a[i]-1]!=a[i]) {
            int j=a[i]-1, tmp=a[j]; a[j]=a[i]; a[i]=tmp;
        }
    }
    for (int i=0;i<n;i++) if (a[i]!=i+1) return i+1;
    return n+1;
}
}
