package y2023

import Timer.time
import java.io.File
import java.lang.Exception

private const val inputPrefix = "src/y2023/Day07"

private class Hand(val cards: String, val score: Int) {
    val powerP1: Int by lazy { power(false) }
    val powerP2: Int by lazy { power(true) }
    override fun toString(): String = "Hand(cards='$cards', score=$score, power=$powerP1 || $powerP2)"
}

private fun Hand.isStronger(other: Hand, p2: Boolean = false): Int {
    val thisPower = if (p2) powerP2 else powerP1
    val otherPower =if (p2) other.powerP2 else other.powerP1
    if (thisPower > otherPower) return 1
    else if (thisPower < otherPower) return -1

    cards.forEachIndexed { i, c ->
        c.isStronger(other.cards[i], p2)?.let {
            return it
        }
    }

    return -1
}

private fun Char.isStronger(other: Char, p2: Boolean = false): Int? {
    if (this == other) return null
    return if (power(p2) > other.power(p2)) 1
    else -1
}

/*
6 Five of a kind    11111   5x1
5 Four of a kind    11112   4x1 1x1
4 Full house        11122   3x1 2x1
3 Three of a kind   11123   3x1 1x2
2 Two pair          11223   2x2 1x1
1 One pair          11234   2x1 1x3
0 High card         12345   1x5
 */
private fun Hand.power(p2: Boolean = false): Int {
    val mapOfCards = if (p2) {
        cards.groupBy { it }
            .toMutableMap()
            .apply {
                if (containsKey('J')) {
                    val js = this['J']!!
                    val copy = this.filter { (key, _) -> key != 'J' }
                    if (copy.isNotEmpty()) {
                        val biggestEntry = copy.maxByOrNull { it.value.size }!!
                        set(biggestEntry.key, biggestEntry.value + js)
                        remove('J')
                    }
                }
            }
    } else {
        cards.groupBy { it }
    }.map { it.value.size }
        .groupBy { it }
        .mapValues { it.value.size }
        .toSortedMap(comparator = Comparator.reverseOrder()) // todo this is not needed

    return when {
        mapOfCards.containsKey(5) -> 6      // Five of a kind
        mapOfCards.containsKey(4) -> 5      // Four of a kind
        mapOfCards.containsKey(3) &&
            mapOfCards.containsKey(2) -> 4  // Full house
        mapOfCards.containsKey(3) -> 3      // Three of a kind
        mapOfCards.containsKey(2) &&
            mapOfCards[2] == 2 -> 2         // Two pair
        mapOfCards.containsKey(2) -> 1      // One pair
        else -> 0
    }
}

private fun Char.power(p2: Boolean = false): Int = when {
    this == 'A' -> 14
    this == 'K' -> 13
    this == 'Q' -> 12
    this == 'J' -> if (p2) 1 else 11
    this == 'T' -> 10
    isDigit() -> digitToInt()
    else -> throw Exception("Unknown char")
}

fun main() {
    // 32T3K KTJJT KK677 T55J5 QQQJA
    fun part1(input: List<Hand>): Long =
        input.sortedWith { h1, h2 -> h1.isStronger(h2) }
            .foldIndexed(0L) { index, acc, hand ->
                acc + ((index + 1) * hand.score)
            }

    // 32T3K KK677 T55J5 QQQJA KTJJT
    fun part2(input: List<Hand>): Long {
        val a = input.sortedWith { h1, h2 -> h1.isStronger(h2, true) }
        return input.sortedWith { h1, h2 -> h1.isStronger(h2, true) }
            .foldIndexed(0L) { index, acc, hand ->
                acc + ((index + 1) * hand.score)
            }
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    fun parseInput(input: List<String>): List<Hand> = input.map {
        val (cards, score) = it.split(" ")
        Hand(cards, score.toInt())
    }

    check(part1(parseInput(testInput)) == 6440L)
    time { println(part1(parseInput(input))) } // 253933213

    check(part2(parseInput(testInput)) == 5905L)
    time { println(part2(parseInput(input))) } // 253473930
}
