package utils

inline fun <reified T> List<List<T>>.transpose(): List<List<T>> {
    return List(this[0].size) { i -> List(this.size) { j -> this[j][i] } }
}

fun List<String>.splitOnEmpty(): List<List<String>> {
    return this
        .joinToString("\n")
        .split("\n\n")
        .map { it.split("\n") }
}

fun List<String>.toIntMatrix(): List<List<Int>> =
    this.map { line -> line.toCharArray().map { char -> char.digitToInt() } }

fun <T> List<List<T>>.positionIsValid(point: Point): Boolean {
    return point.x in this.first().indices && point.y in this.indices
}