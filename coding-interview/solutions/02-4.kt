import java.util.*

fun trap(h: IntArray): Long {
    var l = 0; var r = h.lastIndex
    var leftMax = 0; var rightMax = 0; var water = 0L
    while (l <= r) {
        if (leftMax <= rightMax) {
            leftMax = maxOf(leftMax, h[l]); water += leftMax.toLong() - h[l]; l++
        } else {
            rightMax = maxOf(rightMax, h[r]); water += rightMax.toLong() - h[r]; r--
        }
    }
    return water
}
