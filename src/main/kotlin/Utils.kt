import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/kotlin/$name.txt").readLines()

fun binarySearch(
    lowerBound: Long,
    upperBound: Long,
    inverted: Boolean = false,
    evaluation: (Long) -> Boolean
): Long? {
    var begin = lowerBound
    var end = upperBound
    var result: Long? = null
    while (begin <= end) {
        val mid = (begin + end) / 2L
        if (evaluation(mid)) {
            result = mid
            if (inverted) {
                end = mid - 1
            } else {
                begin = mid + 1
            }
        } else {
            if (inverted) {
                begin = mid + 1
            } else {
                end = mid - 1
            }
        }
    }
    return result
}