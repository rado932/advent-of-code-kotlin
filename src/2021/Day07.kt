package `2021`

import checkPart
import readInput

fun main() {
    fun part1(input: List<Int>): Int {
        val positionsSum = input.sum()
        val maxPosition = input.maxOrNull()
        val medium = positionsSum / maxPosition!!
        return medium
    }

    fun part2(input: List<Int>): Int {
        return input.size
    }

    val testInput = readInput("Day07_test")
        .first()
        .split(",")
        .map { it.toInt() }
    checkPart(10, part1(testInput), "Part1 Example")
    checkPart(10, part2(testInput), "Part2 Example")

    val input = readInput("Day07")
        .first()
        .split(",")
        .map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
