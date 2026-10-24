import java.util.*

class BoardNode { val next = arrayOfNulls<BoardNode>(26); var word: String? = null }
fun boardWords(board: Array<CharArray>, words: Array<String>): List<String> {
 val root = BoardNode()
 for (word in words) {
  require(word.isNotEmpty() && word.all { it in 'a'..'z' })
  var n = root
  for (c in word) {
   val i = c - 'a'
   if (n.next[i] == null) n.next[i] = BoardNode()
   n = n.next[i]!!
  }
  n.word = word
 }
 val rows = board.size
 val cols = if (rows == 0) 0 else board[0].size
 require(board.all { row -> row.size == cols && row.all { it in 'a'..'z' } })
 val used = Array(rows) { BooleanArray(cols) }
 val out = mutableListOf<String>()
 fun dfs(r: Int, c: Int, parent: BoardNode) {
  if (r !in 0 until rows || c !in 0 until cols || used[r][c]) return
  val n = parent.next[board[r][c] - 'a'] ?: return
  n.word?.let { out.add(it); n.word = null }
  used[r][c] = true
  dfs(r - 1, c, n); dfs(r + 1, c, n); dfs(r, c - 1, n); dfs(r, c + 1, n)
  used[r][c] = false
 }
 for (r in 0 until rows) for (c in 0 until cols) dfs(r, c, root)
 return out
}
