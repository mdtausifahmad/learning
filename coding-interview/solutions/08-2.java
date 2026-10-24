import java.util.*;

class Solution08P2 {
static int[] warmer(int[] t) {
 int[] answer=new int[t.length]; Deque<Integer> stack=new ArrayDeque<>();
 for(int i=0;i<t.length;i++) {
  while(!stack.isEmpty() && t[i]>t[stack.peek()]) {
   int j=stack.pop(); answer[j]=i-j;
  }
  stack.push(i);
 }
 return answer;
}
}
