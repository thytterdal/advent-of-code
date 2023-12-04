package day3

import readInput

fun main() {
    val lines = readInput("day3/input")

    first(lines)
    second(lines)
}

fun first(lines: List<String>) {
    val numbers = mutableListOf<Long>()

    for (i in 0..lines.lastIndex) {
        val (previousLine, currentLine, nextLine) = findNumbersAndSymbols(lines, i)

        currentLine.filterIsInstance<SchematicItem.Symbol>().forEach { symbol ->
            findNeighbours(
                symbol = symbol,
                previousLine = previousLine,
                currentLine = currentLine.filterIsInstance<SchematicItem.PartNumber>(),
                nextLine = nextLine,
                list = numbers
            )
        }
    }

    val total = numbers.sum()
    println("Sum of part numbers: $total")

}

fun second(lines: List<String>) {
    val numbers = mutableListOf<Long>()

    for (i in 0..lines.lastIndex) {
        val (previousLine, currentLine, nextLine) = findNumbersAndSymbols(lines, i)

        currentLine.filterIsInstance<SchematicItem.Symbol>().filter { it.symbol == "*" }.forEach { symbol ->
            val tempNumbers = mutableListOf<Long>()

            findNeighbours(
                symbol = symbol,
                previousLine = previousLine,
                currentLine = currentLine.filterIsInstance<SchematicItem.PartNumber>(),
                nextLine = nextLine,
                list = tempNumbers
            )

            if (tempNumbers.size == 2) {
                numbers.add(tempNumbers[0] * tempNumbers[1])
            }
        }
    }

    val total = numbers.sum()
    println("Gear ratio: $total")
}

private fun findNeighbours(
    symbol: SchematicItem.Symbol,
    previousLine: List<SchematicItem.PartNumber>,
    currentLine: List<SchematicItem.PartNumber>,
    nextLine: List<SchematicItem.PartNumber>,
    list: MutableList<Long>
) {
    previousLine.forEach { previous ->
        if (previous.range.contains(symbol.index)) {
            list.add(previous.value)
        }
    }

    currentLine.forEach { currenLineNumber ->
        if (currenLineNumber.range.contains(symbol.index)) {
            list.add(currenLineNumber.value)
        }
    }

    nextLine.forEach { next ->
        if (next.range.contains(symbol.index)) {
            list.add(next.value)
        }
    }
}

private fun findNumbersAndSymbols(
    lines: List<String>, i: Int
): Triple<List<SchematicItem.PartNumber>, List<SchematicItem>, List<SchematicItem.PartNumber>> {
    val previousLine = lines.getOrElse(i - 1) { "" }.findNumbersOnLine()
    val currentLine = lines[i].findNumbersAndSymbolsOnLine()
    val nextLine = lines.getOrElse(i + 1) { "" }.findNumbersOnLine()
    return Triple(previousLine, currentLine, nextLine)
}

fun String.findNumbersOnLine(): List<SchematicItem.PartNumber> {
    val regex = Regex("""\d+""")
    val numbers = regex.findAll(this).map {
        val rangeStart = (it.range.first -1).coerceAtLeast(0)
        val rangeEnd = if(it.range.last == this.lastIndex) it.range.last else it.range.last + 1
        val range = rangeStart..rangeEnd
        SchematicItem.PartNumber(
            value = it.value.toLong(),
            range = range
        )
    }.toList()

    return numbers
}

fun String.findNumbersAndSymbolsOnLine(): List<SchematicItem> {
    val regex = Regex("""\d+|[^.]""")
    val symbols = regex.findAll(this).map {
        val isNumber = it.value.toIntOrNull() != null

        if (isNumber) {
            val rangeStart = (it.range.first -1).coerceAtLeast(0)
            val rangeEnd = if(it.range.last == this.lastIndex) it.range.last else it.range.last + 1
            SchematicItem.PartNumber(
                value = it.value.toLong(),
                range = rangeStart..rangeEnd
            )
        } else {
            SchematicItem.Symbol(
                symbol = it.value,
                index = it.range.first
            )
        }
    }.toList()

    return symbols
}

sealed interface SchematicItem {
    data class PartNumber(
        val value: Long,
        val range: IntRange
    ) : SchematicItem

    data class Symbol(
        val symbol: String,
        val index: Int
    ) : SchematicItem
}
