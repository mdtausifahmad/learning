import java.util.*

fun canJump(a: IntArray): Boolean {
    if(a.isEmpty()) return false
    var far=0L
    for(i in a.indices) {
        if(i.toLong()>far) return false
        far=maxOf(far,i.toLong()+a[i])
        if(far>=a.lastIndex.toLong()) return true
    }
    return false
}
