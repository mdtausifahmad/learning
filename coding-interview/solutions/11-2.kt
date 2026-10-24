import java.util.*

fun combinationSum(candidates: IntArray, target: Int): List<List<Int>> {
 require(target >= 0 && candidates.all { it > 0 })
 val a = candidates.sortedArray()
 val out = mutableListOf<List<Int>>()
 val path = mutableListOf<Int>()
 fun dfs(start: Int, remaining: Int) {
  if (remaining == 0) { out.add(path.toList()); return }
  for (i in start until a.size) {
   if (i > start && a[i] == a[i - 1]) continue
   if (a[i] > remaining) break
   path.add(a[i]); dfs(i, remaining - a[i]); path.removeAt(path.lastIndex)
  }
 }
 dfs(0, target)
 return out
}
