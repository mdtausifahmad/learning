import java.util.*

fun validPalindrome(s: String): Boolean {
    fun exact(start: Int, end: Int): Boolean {
        var l = start; var r = end
        while (l < r) if (s[l++] != s[r--]) return false
        return true
    }
    var l = 0; var r = s.lastIndex
    while (l < r) {
        if (s[l] != s[r]) return exact(l + 1, r) || exact(l, r - 1)
        l++; r--
    }
    return true
}
