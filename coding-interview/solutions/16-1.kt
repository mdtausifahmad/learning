import java.util.*

fun cookies(greed: IntArray, sizes: IntArray): Int {
    val g=greed.sortedArray(); val s=sizes.sortedArray()
    var i=0
    for(size in s) if(i<g.size && size>=g[i]) i++
    return i
}
