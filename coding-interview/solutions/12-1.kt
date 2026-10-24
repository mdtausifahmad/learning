import java.util.*

class Trie {
 private class TNode { val next = arrayOfNulls<TNode>(26); var terminal = false }
 private val root = TNode()
 private fun validate(s: String) { require(s.all { it in 'a'..'z' }) }
 fun insert(word: String) {
  validate(word)
  var n = root
  for (c in word) {
   val index = c - 'a'
   if (n.next[index] == null) n.next[index] = TNode()
   n = n.next[index]!!
  }
  n.terminal = true
 }
 private fun follow(s: String): TNode? {
  validate(s)
  var n = root
  for (c in s) n = n.next[c - 'a'] ?: return null
  return n
 }
 fun search(word: String): Boolean = follow(word)?.terminal == true
 fun startsWith(prefix: String): Boolean = follow(prefix) != null
}
