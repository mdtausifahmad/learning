import java.util.*

fun regex(s: String,p: String): Boolean {
    for(j in p.indices) {
        val c=p[j]
        if(c=='*') require(j>0 && p[j-1]!='*')
        else require(c=='.' || c in 'a'..'z')
    }
    require(s.all { it in 'a'..'z' })
    val dp=Array(s.length+1){BooleanArray(p.length+1)}; dp[0][0]=true
    for(i in 0..s.length) for(j in 1..p.length) {
        val atom=p[j-1]
        dp[i][j]=if(atom=='*') {
            val prev=p[j-2]
            dp[i][j-2] || (i>0 && (prev=='.'||prev==s[i-1]) && dp[i-1][j])
        } else i>0 && (atom=='.'||atom==s[i-1]) && dp[i-1][j-1]
    }
    return dp[s.length][p.length]
}
