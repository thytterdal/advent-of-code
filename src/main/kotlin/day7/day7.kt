package day7

import readInput

fun main() {
    val lines = readInput("day7/input")

    firstStar(lines)
    secondStar(lines)
}


fun firstStar(lines: List<String>) {
    val hands = parseHands(lines)
    val handValues = hands.sorted().mapIndexed { index, hand ->
        (index + 1) * hand.bid
    }
    println(handValues.sum())
}

fun secondStar(lines: List<String>) {
    val hands = parseHands(lines, joker = true)
    val handValues = hands.sorted().mapIndexed { index, hand ->
        (index + 1) * hand.bid
    }
    println(handValues.sum())
}

fun parseHands(lines: List<String>, joker: Boolean = false): List<CamelPokerHand> =
    lines.map {
        val (cards, bid) = it.split(" ")
        CamelPokerHand(cards = cards, bid = bid.toInt(), joker = joker)
    }


data class CamelPokerHand(
    val cards: String,
    val bid: Int,
    val joker: Boolean = false
) : Comparable<CamelPokerHand> {
    private val handType: HandType

    init {
        handType = findHandType()
    }

    private fun findHandType(): HandType {
        val chars = cards.toCharArray().toTypedArray()
        val tmpOccurrences = chars.groupingBy { it }.eachCount()
        val occurrences = if (joker && tmpOccurrences.keys.contains('J')) {
            val jokers = tmpOccurrences['J'] ?: throw Exception("Jokers should not be null")
            val notJoker = tmpOccurrences.filter { it.key != 'J' }.toMutableMap()
            val keyWithMostOccurrences = if (notJoker.isNotEmpty()) notJoker.maxBy { it.value }.key else 'A'
            val mostOccurrences = tmpOccurrences[keyWithMostOccurrences] ?: 0
            notJoker[keyWithMostOccurrences] = mostOccurrences + jokers
            notJoker
        } else tmpOccurrences

        return when {
            occurrences.maxOf { it.value } == 5 -> HandType.FIVE_OF_A_KIND
            occurrences.maxOf { it.value } == 4 -> HandType.FOUR_OF_A_KIND
            occurrences.maxOf { it.value } == 3 && occurrences.values.contains(2) -> HandType.FULL_HOUSE
            occurrences.maxOf { it.value } == 3 -> HandType.THREE_OF_A_KIND
            occurrences.values.count { it == 2 } == 2 -> HandType.TWO_PAIR
            occurrences.values.contains(2) -> HandType.ONE_PAIR

            else -> HandType.HIGH_CARD
        }
    }


    override fun compareTo(other: CamelPokerHand): Int {
        if (handType.rank != other.handType.rank) {
            return if (handType.rank > other.handType.rank) 1 else -1
        }
        val cardValues = cards.toCardValueList(joker)
        val otherCardValues = other.cards.toCardValueList(joker)

        (cardValues zip otherCardValues).forEach {
            if (it.first != it.second) {
                return if (it.first > it.second) 1 else -1
            }
        }
        return 0
    }
}

private fun String.toCardValueList(joker: Boolean): List<Int> {
    return this.toCharArray().map {
        when (it) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if (joker) 0 else 11
            'T' -> 10
            else -> it.digitToInt()
        }
    }
}


enum class HandType(val rank: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}
