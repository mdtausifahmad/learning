import java.util.*;

class Solution08P1 {
static boolean validBrackets(String s) {
 Objects.requireNonNull(s);
 Deque<Character> stack=new ArrayDeque<>();
 for (int i=0;i<s.length();i++) {
  char c=s.charAt(i);
  if(c=='(') stack.push(')');
  else if(c=='[') stack.push(']');
  else if(c=='{') stack.push('}');
  else if(c==')'||c==']'||c=='}') {
   if(stack.isEmpty()||stack.pop()!=c) return false;
  } else return false;
 }
 return stack.isEmpty();
}
}
