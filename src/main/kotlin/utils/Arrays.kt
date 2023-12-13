package utils

inline fun <reified T> List<List<T>>.transpose(): List<List<T>> {
    return List(this[0].size) { i -> List(this.size) { j -> this[j][i] } }
}

inline fun <reified T> Array<Array<T>>.transpose(): Array<Array<T>> {
    return Array(this[0].size) { i -> Array(this.size) { j -> this[j][i] } }
}

fun List<String>.splitOnEmpty(): List<List<String>> {
    return this.joinToString("\n").split("\n\n").map { it.split("\n") }
}