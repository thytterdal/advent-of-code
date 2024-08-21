package utils

fun String.numbers(): List<Int> {
    val regex = """\d+""".toRegex()
    return regex.findAll(this).map { it.value.toInt() }.toList()
}

fun String.isNumeric() = this.all { it in '0'..'9' }