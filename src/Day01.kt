fun main() {
    fun part1Internal(input: List<Int>): Int {
        return input.foldIndexed(0) { index, acc, value ->
            if (index == 0 || value <= input[index -1]) acc
            else acc + 1
        }
    }

    fun part1(input: List<Int>): Int {
        return part1Internal(input)
    }

    fun part2(input: List<Int>): Int {
        val newValues = input.mapIndexed { index: Int, value: Int ->
            if (index + 2 >= input.size) null
            else value + input[index + 1] + input[index + 2]
        }.filterNotNull()

        return part1Internal(newValues)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test").map { it.toInt() }
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
