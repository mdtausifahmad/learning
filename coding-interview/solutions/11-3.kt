import java.util.*

fun uniquePermutations(input: IntArray): List<List<Int>> {
 val a = input.sortedArray()
 val used = BooleanArray(a.size)
 val path = mutableListOf<Int>()
 val out = mutableListOf<List<Int>>()
 fun dfs() {
  if (path.size == a.size) { out.add(path.toList()); return }
  for (i in a.indices) {
   if (used[i] || (i > 0 && a[i] == a[i - 1] && !used[i - 1])) continue
   used[i] = true; path.add(a[i]); dfs()
   path.removeAt(path.lastIndex); used[i] = false
  }
 }
 dfs()
 return out
}
