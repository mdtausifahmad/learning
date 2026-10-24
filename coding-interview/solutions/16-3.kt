import java.util.*

fun partitions(s: String): List<Int> {
    val last=IntArray(26)
    for(i in s.indices) last[s[i]-'a']=i
    val out=mutableListOf<Int>(); var start=0; var end=0
    for(i in s.indices) {
        end=maxOf(end,last[s[i]-'a'])
        if(i==end){out.add(i-start+1);start=i+1}
    }
    return out
}
