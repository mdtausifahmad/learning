import java.util.*

class WordDictionary {
 private class TNode { val next = arrayOfNulls<TNode>(26); var terminal = false }
 private val root = TNode()
 fun addWord(word: String) {
  require(word.all { it in 'a'..'z' })
  var n = root
  for (c in word) {
   val i = c - 'a'
   if (n.next[i] == null) n.next[i] = TNode()
   n = n.next[i]!!
  }
  n.terminal = true
 }
 fun search(pattern: String): Boolean {
  require(pattern.all { it == '.' || it in 'a'..'z' })
  fun match(n: TNode, index: Int): Boolean {
   if (index == pattern.length) return n.terminal
   val c = pattern[index]
   if (c != '.') return n.next[c - 'a']?.let { match(it, index + 1) } ?: false
   for (child in n.next) if (child != null && match(child, index + 1)) return true
   return false
  }
  return match(root, 0)
 }
}
