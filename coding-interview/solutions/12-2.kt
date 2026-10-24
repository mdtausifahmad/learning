import java.util.*

class RootNode { val next = arrayOfNulls<RootNode>(26); var terminal = false }
fun replaceRoots(roots: List<String>, sentence: String): String {
 require(sentence.isEmpty() || sentence.matches(Regex("[a-z]+( [a-z]+)*")))
 val root = RootNode()
 for (word in roots) {
  require(word.matches(Regex("[a-z]+")))
  var n = root
  for (c in word) {
   val i = c - 'a'
   if (n.next[i] == null) n.next[i] = RootNode()
   n = n.next[i]!!
  }
  n.terminal = true
 }
 if (sentence.isEmpty()) return ""
 return sentence.split(" ").joinToString(" ") { word ->
  var n = root
  var replacement = word
  for (i in word.indices) {
   n = n.next[word[i] - 'a'] ?: break
   if (n.terminal) { replacement = word.substring(0, i + 1); break }
  }
  replacement
 }
}
