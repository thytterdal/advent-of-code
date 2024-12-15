package utils

fun <T> Map<Point, T>.print() {
    val maxy = this.keys.maxBy { it.y }.y

    (0..maxy).forEach { y ->
        println(this.filterKeys { it.y == y }.keys.sortedBy { it.x }.map {
            this[it]
        }.joinToString(""))
    }
}