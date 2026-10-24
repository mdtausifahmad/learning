import java.util.*;

class Solution16P1 {
static int cookies(int[] greed, int[] sizes) {
    int[] g=greed.clone(), s=sizes.clone();
    Arrays.sort(g); Arrays.sort(s);
    int i=0;
    for(int size:s) if(i<g.length && size>=g[i]) i++;
    return i;
}
}
