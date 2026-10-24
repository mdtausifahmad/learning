import java.util.*;

class Solution01P2 {
static List<List<String>> groupAnagrams(String[] words) {
    Map<String,List<String>> groups=new HashMap<>();
    for (String word:words) {
        char[] key=word.toCharArray(); Arrays.sort(key);
        groups.computeIfAbsent(new String(key),k->new ArrayList<>()).add(word);
    }
    return new ArrayList<>(groups.values());
}
}
