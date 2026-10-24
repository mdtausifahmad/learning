import java.util.*

data class Entry(val value: Int, val row: Int, val col: Int)
fun mergeStreams(streams: Array<IntArray>): IntArray {
 var total = 0
 val heap = PriorityQueue<Entry>(compareBy { it.value })
 for (r in streams.indices) {
  total = Math.addExact(total, streams[r].size)
  if (streams[r].isNotEmpty()) heap.add(Entry(streams[r][0], r, 0))
 }
 val out = IntArray(total)
 var position = 0
 while (heap.isNotEmpty()) {
  val e = heap.remove()
  out[position++] = e.value
  val next = e.col + 1
  if (next < streams[e.row].size) heap.add(Entry(streams[e.row][next], e.row, next))
 }
 return out
}
