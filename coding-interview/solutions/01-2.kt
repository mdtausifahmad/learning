import java.util.*

fun groupAnagrams(words: Array<String>): List<List<String>> {
    val groups = HashMap<String, MutableList<String>>()
    for (word in words) {
        val chars = word.toCharArray(); chars.sort()
        val key = String(chars)
        groups.getOrPut(key) { mutableListOf() }.add(word)
    }
    return groups.values.map { it.toList() }
}
