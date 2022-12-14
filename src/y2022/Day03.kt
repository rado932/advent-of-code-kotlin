package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day03"

fun main() {
    fun Char.toPriority(): Int = when (this) {
        in 'A'..'Z' -> this - 'A' + 27
        in 'a'..'z' -> this - 'a' + 1
        else -> error("Unsupported Char $this")
    }

    fun part1(input: List<String>): Int = input.sumOf { rucksack ->
        val halved = with(rucksack) {
            substring(0, length / 2) to substring(length / 2, length)
        }
        halved.first.first { halved.second.contains(it) }.toPriority()
    }

    fun part2(input: List<String>): Int = input.windowed(3, 3).sumOf { rucksacks ->
        rucksacks[0].first { rucksacks[1].contains(it) && rucksacks[2].contains(it) }.toPriority()
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 157)
    println(part1(input))

    check(part2(testInput) == 70)
    println(part2(input))
}
