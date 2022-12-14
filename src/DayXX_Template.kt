import java.io.File

private const val inputPrefix = "src/y2022/DayXX"

fun main() {

    fun part1(input: List<String>): Int = 0

    fun part2(input: List<String>): Int = 0

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 0)
    println(part1(input))

    check(part2(testInput) == 0)
    println(part2(input))
}
