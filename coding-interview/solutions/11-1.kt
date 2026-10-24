import java.util.*

fun subsets(a: IntArray): List<List<Int>> {
 val out = mutableListOf<List<Int>>()
 val path = mutableListOf<Int>()
 fun dfs(start: Int) {
  out.add(path.toList())
  for (i in start until a.size) {
   path.add(a[i]); dfs(i + 1); path.removeAt(path.lastIndex)
  }
 }
 dfs(0)
 return out
}
