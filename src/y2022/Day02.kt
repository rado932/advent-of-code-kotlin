package y2022

import java.io.File

fun main() {
    fun part1(input: List<String>): Int {
        fun Char.shapeScore() = this - 'W'
        fun Char.opponentShapeScore() = this - '@'

        fun String.isWin(): Boolean = when(this) {
            "C X", "A Y", "B Z" -> true
            else -> false
        }

        fun String.isDraw(): Boolean = this[2].shapeScore() == this[0].opponentShapeScore()

        fun String.matchScore(): Int = when {
            isWin() -> 6
            isDraw() -> 3
            else -> 0
        }

        return input.sumOf {
            it[2].shapeScore() + it.matchScore()
        }
    }

    fun part2(input: List<String>): Int {
        fun Char.matchScore() = (this - 'X') * 3

        fun String.shapeScore(): Int = when(this) {
            "A Y", "B X", "C Z" -> 1
            "B Y", "C X", "A Z" -> 2
            "C Y", "A X", "B Z" -> 3
            else -> error("Bar input $this")
        }

        return input.sumOf {
            it.shapeScore() + it[2].matchScore()
        }
    }

    val testInput = File("src/y2022/Day02_test.txt").readLines()
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = File("src/y2022/Day02.txt").readLines()
    println(part1(input))
    println(part2(input))
}
