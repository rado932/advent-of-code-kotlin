package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day10"

fun main() {

    fun getBreakPoint(cycle: Int): Int =
        if (cycle == 20 || (cycle - 20) % 40 == 0) cycle
        else 0

    fun part1(input: List<String>): Int = input.foldIndexed(1 to 0) { n, (x, sum), command ->
        when {
            command.startsWith("addx ") -> {
                val breakPoint = getBreakPoint(n + 1)
                val newX = x + command.substringAfter("addx ").toInt()
                newX to sum + (breakPoint * x)
            }

            else -> {
                val breakPoint = getBreakPoint(n + 1)
                x to sum + (breakPoint * x)
            }
        }
    }.second

    fun part2(input: List<String>): String = input.foldIndexed(1 to "") { n, (x, acc), command ->
        val cycle = n % 40

        val newChar = if ((cycle - x) in -1..1) "#"
        else "."

        val newX = if (command.startsWith("addx ")) {
            x + command.substringAfter("addx ").toInt()
        } else x

        newX to acc + newChar
    }.second

    fun List<String>.parseInput(): List<String> = flatMap {
        if (it == "noop") listOf(it)
        else listOf("noop", it)
    }

    fun String.formatOutput() = toCharArray().toList().chunked(40).forEach {
        println(it.joinToString(""))
    }

    val testInput = File("${inputPrefix}_test.txt").readLines().parseInput()
    val input = File("$inputPrefix.txt").readLines().parseInput()

    check(part1(testInput) == 13140)
    println("Part 1 Output")
    println(part1(input))

    val part2TestOutput = part2(testInput)
    println("Part 2 Test Output")
    part2TestOutput.formatOutput()

    val expectedTestOutput =
        "##..##..##..##..##..##..##..##..##..##..###...###...###...###...###...###...###.####....####....####....####....####....#####.....#####.....#####.....#####.....######......######......######......###########.......#######.......#######....."
    check(part2TestOutput == expectedTestOutput)

    println("Part 2 Output")
    part2(input).formatOutput()
}
