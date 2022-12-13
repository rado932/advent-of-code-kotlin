import y2021.checkPart
import y2021.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("DayXX_test")
    checkPart(0, part1(testInput), "Part1 Example")
    checkPart(0, part2(testInput), "Part2 Example")

    val input = readInput("DayXX")
    println(part1(input))
    println(part2(input))
}
