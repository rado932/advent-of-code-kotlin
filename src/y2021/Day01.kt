package y2021

fun main() {

    fun part1(input: List<Int>): Int {
        return input.windowed(2)
            .count { (a, b) -> a < b }
    }

    fun part2(input: List<Int>): Int {
        return input.windowed(4)
            .count { it[0] < it[3] }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test").map { it.toInt() }
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
