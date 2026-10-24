import java.util.*

fun candy(rating: IntArray): Long {
    val n=rating.size; if(n==0) return 0L
    val left=IntArray(n){1}
    for(i in 1 until n) if(rating[i]>rating[i-1]) left[i]=left[i-1]+1
    var right=1; var total=left[n-1].toLong()
    for(i in n-2 downTo 0) {
        right=if(rating[i]>rating[i+1]) right+1 else 1
        total+=maxOf(left[i],right).toLong()
    }
    return total
}
