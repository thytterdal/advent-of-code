package day4

import readInput

fun main() {
    val lines = readInput("day4/input")

    firstStar(lines)
    secondStar(lines)
}

fun firstStar(lines: List<String>) {

    val sum = lines.sumOf { line ->
        val intersection = findIntersection(line)
        var value = 0
        repeat(intersection.size) {
            value = if (value == 0) {
                1
            } else {
                value * 2
            }
        }
        value
    }

    println("Silver star: $sum")
}

fun secondStar(lines: List<String>) {
    val cards = mutableMapOf<Int, Int>()


    lines.forEachIndexed { index, line ->
        val intersection = findIntersection(line)
        cards[index] = intersection.size
    }

    var sum = cards.size

    cards.keys.forEach {key ->
        sum += sumUpWinningCards(cards, key)
    }

    println("Gold star: $sum")

}

fun sumUpWinningCards(cards: MutableMap<Int, Int>, index: Int): Int {
    val winningNumbers = cards[index] ?: 0
    var sum = winningNumbers
    repeat(winningNumbers){
        sum += sumUpWinningCards(cards, index + it + 1)
    }

    return sum
}

private fun findIntersection(line: String): Set<String> {
    val (_, gameInput) = line.split(":")
    val (winningNumbers, numbers) = gameInput.split("|")
    val winningNumbersSplit = winningNumbers.trim().splitToSequence(" ").filter { it.isNotEmpty() }.toSet()
    val numbersSplit = numbers.trim().splitToSequence(" ").filter { it.isNotEmpty() }.toSet()

    return winningNumbersSplit.intersect(numbersSplit)
}