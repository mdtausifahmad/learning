import java.util.*

fun queens(n: Int): List<List<String>> {
 require(n in 0..12)
 val chosen = IntArray(n)
 val columns = BooleanArray(n)
 val down = BooleanArray(2 * n + 1)
 val up = BooleanArray(2 * n + 1)
 val out = mutableListOf<List<String>>()
 fun dfs(row: Int) {
  if (row == n) {
   out.add((0 until n).map { r ->
    val line = CharArray(n) { '.' }; line[chosen[r]] = 'Q'; String(line)
   })
   return
  }
  for (c in 0 until n) {
   val d = row - c + n
   val u = row + c
   if (columns[c] || down[d] || up[u]) continue
   columns[c] = true; down[d] = true; up[u] = true; chosen[row] = c
   dfs(row + 1)
   columns[c] = false; down[d] = false; up[u] = false
  }
 }
 dfs(0)
 return out
}
