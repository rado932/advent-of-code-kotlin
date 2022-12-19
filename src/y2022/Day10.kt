package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day10"

fun main() {

    fun part1(input: List<String>): Int {
        fun getBreakPoint(cycle: Int): Int =
            if (cycle == 20 || (cycle - 20) % 40 == 0) cycle
            else 0

        var x = 1
        var cycle = 1

        return input.sumOf {
            when {
                it.startsWith("addx ") -> {
                    cycle += 1
                    val breakPoint2 = getBreakPoint(cycle)

                    x += it.substringAfter("addx ").toInt()
                    breakPoint2 * x
                }

                else -> {
                    cycle += 1
                    val breakPoint = getBreakPoint(cycle)
                    breakPoint * x
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    fun parseInput(input: List<String>): List<String> = input.flatMap {
        if (it == "noop") listOf(it)
        else listOf("noop", it)
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(parseInput(testInput)) == 13140)
    println(part1(parseInput(input)))

//    check(part2(testInput) == 0)
//    println(part2(input))
}
