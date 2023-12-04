package y2023

import java.io.File

private const val inputPrefix = "src/y2023/Day04"

private class ScratchCard(val number: Int,
                          val winningNumbers: Set<String>,
                          val selectedNumbers: Set<String>)

private fun ScratchCard.calculateMatches(): Int {
    val set = winningNumbers + selectedNumbers
    return (winningNumbers.size + selectedNumbers.size) - set.size
}

private fun ScratchCard.calculateScore(): Int = with(calculateMatches()) {
    if (this == 0) return 0
    return 1 shl (this - 1)
}

private fun ScratchCard.calculatedAwardedScratchCards(): IntRange = with(calculateMatches()) {
    if (this == 0) return 0..-1 // returning bad set instead of null
    return (number + 1)..(number + this)
}

fun main() {
    val digitRegex = "\\d+".toRegex()
    // ScratchCard.number to total number of scratch cards awarded
    var awardedScratchCards = mutableMapOf<Int, Int>()

    fun getNumbers(str: String): Set<String> = digitRegex.findAll(str)
        .map { it.value }
        .toSet()

    fun addAwardedCards(amountOfWiningScratchCards: Int,
                        newAwardedScratchCards: IntRange) =
        newAwardedScratchCards.forEach {
            val currentCardsAmount = awardedScratchCards.getOrDefault(it, 0)
            awardedScratchCards[it] = currentCardsAmount + amountOfWiningScratchCards
        }

    fun part1(input: List<String>): Int = input.sumOf {
        val (game, allNumbers) = it.split(":")
        val (winningNumbers, selectedNumbers) = allNumbers.split("|")

        ScratchCard(
            digitRegex.find(game, 4)!!.value.toInt(),
            getNumbers(winningNumbers),
            getNumbers(selectedNumbers)
        ).calculateScore()
    }

    fun part2(input: List<String>): Int {
        awardedScratchCards = mutableMapOf()

        return input.sumOf {
            val (game, allNumbers) = it.split(":")
            val (winningNumbers, selectedNumbers) = allNumbers.split("|")

            val scratchCard = ScratchCard(
                digitRegex.find(game, 4)!!.value.toInt(),
                getNumbers(winningNumbers),
                getNumbers(selectedNumbers)
            )

            val newAwardedScratchCards = scratchCard.calculatedAwardedScratchCards()
            val totalCopiesOfThisScratchCard = awardedScratchCards.getOrDefault(scratchCard.number, 0) + 1
            addAwardedCards(totalCopiesOfThisScratchCard, newAwardedScratchCards)

            totalCopiesOfThisScratchCard
        }
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 30)
    println(part2(input))
}
