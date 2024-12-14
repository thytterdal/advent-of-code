package utils

fun String.numbers(): List<Int> {
    val regex = Regex("""-?\d+""")
    return regex.findAll(this).map { it.value.toInt() }.toList()
}

fun String.longs(): List<Long> {
    val regex = Regex("""-?\d+""")
    return regex.findAll(this).map { it.value.toLong() }.toList()
}

fun String.isNumeric() = this.all { it in '0'..'9' }

