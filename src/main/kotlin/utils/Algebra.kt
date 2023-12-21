package utils

import kotlin.math.abs

/**
 * Computes the greatest common divisor of two integers.
 */
tailrec fun greatestCommonDivisor(a: Long, b: Long): Long {
    if (b == 0L) {
        return a
    }
    return greatestCommonDivisor(b, a % b)
}

/**
 * Computes the least common multiple of two integers.
 */
fun leastCommonMultiple(a: Long, b: Long): Long {
    return if (a == 0L || b == 0L) 0L else {
        val gcd = greatestCommonDivisor(a, b)
        abs(a * b) / gcd
    }
}

/**
 * Takes a list of base/modulo combinations and returns the lowest number for which the states coincide such that:
 *
 * for all i: state(i) == base_state(i).
 *
 * E.g. chineseRemainder((3,4), (5,6), (2,5)) == 47
 */
fun chineseRemainder(values: List<Pair<Long, Long>>): Long {
    if (values.isEmpty()) {
        return 0L
    }
    var result = values[0].first
    var lcm = values[0].second
    for (i in 1..<values.size) {
        val (base, modulo) = values[i]
        val target = base % modulo
        while (result % modulo != target) {
            result += lcm
        }
        lcm = leastCommonMultiple(lcm, modulo)
    }
    return result
}