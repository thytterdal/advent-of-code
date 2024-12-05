package utils

inline fun <reified T> List<List<T>>.transpose(): List<List<T>> {
    return List(this[0].size) { i -> List(this.size) { j -> this[j][i] } }
}

fun List<String>.splitOnEmptyLine(): List<List<String>> {
    return this
        .joinToString("\n")
        .split("\n\n")
        .map { it.split("\n") }
}

fun List<String>.toGrid(): Map<Point, Char> = buildMap {
    this@toGrid.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            put(Point(x, y), char)
        }
    }
}

fun <T> List<String>.toGrid(transform: (Char) -> T): Map<Point, T> = buildMap {
    this@toGrid.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            put(Point(x, y), transform(char))
        }
    }
}

fun List<String>.toLongGrid(): Map<PointL, Char> = buildMap {
    this@toLongGrid.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            put(PointL(x.toLong(), y.toLong()), char)
        }
    }
}

fun <T> List<String>.toLongGrid(transform: (Char) -> T): Map<PointL, T> = buildMap {
    this@toLongGrid.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            put(PointL(x.toLong(), y.toLong()), transform(char))
        }
    }
}

fun List<String>.toIntMatrix(): List<List<Int>> =
    this.map { line -> line.toCharArray().map { char -> char.digitToInt() } }

fun <T> List<List<T>>.positionIsValid(point: Point): Boolean {
    return point.x in this.first().indices && point.y in this.indices
}

fun List<String>.positionInStringIsValid(point: Point): Boolean {
    return point.x in this.first().indices && point.y in this.indices
}

infix operator fun <T> List<List<T>>.get(point: Point): T {
    return this[point.y][point.x]
}

infix operator fun List<String>.get(point: Point): Char {
    return this[point.y][point.x]
}

fun <T> List<T>.permutations(): List<List<T>> {
    if (isEmpty()) return emptyList()
    if (size == 1) return listOf(this)

    return drop(1).permutations().fold(mutableListOf()) { acc, perm ->
        (0..perm.size).mapTo(acc) { i ->
            perm.subList(0, i) + this@permutations.first() + perm.subList(i, perm.size)
        }
    }
}