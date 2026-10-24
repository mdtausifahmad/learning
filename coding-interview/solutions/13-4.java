import java.util.*;

class Solution13P4 {
static int ladderLength(String begin, String end, List<String> words) {
    Objects.requireNonNull(begin, "begin"); Objects.requireNonNull(end, "end");
    Objects.requireNonNull(words, "words");
    if (begin.equals(end)) return 1;
    Set<String> remaining = new HashSet<>(words);
    if (!remaining.contains(end)) return 0;
    remaining.remove(begin);
    ArrayDeque<String> queue = new ArrayDeque<>(); queue.add(begin);
    int length = 1;
    while (!queue.isEmpty()) {
        int layer = queue.size();
        for (int k = 0; k < layer; k++) {
            char[] chars = queue.remove().toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char saved = chars[i];
                for (char ch = 'a'; ch <= 'z'; ch++) {
                    if (ch == saved) continue;
                    chars[i] = ch;
                    String next = new String(chars);
                    if (remaining.remove(next)) {
                        if (next.equals(end)) return length + 1;
                        queue.add(next);
                    }
                }
                chars[i] = saved;
            }
        }
        length++;
    }
    return 0;
}
}
