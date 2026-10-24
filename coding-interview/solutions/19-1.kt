import java.util.*

class RangeSum(values: LongArray) {
 private val prefix = LongArray(values.size + 1)
 init { for (i in values.indices) prefix[i + 1] = prefix[i] + values[i] }
 fun sum(left: Int, right: Int): Long {
  require(left >= 0 && left <= right && right < prefix.size)
  return prefix[right] - prefix[left]
 }
}
