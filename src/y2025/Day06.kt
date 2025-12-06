import Timer.time
import java.io.File

private const val inputPrefix = "src/y2025/Day06"

fun main() {

    fun sanitiseInput(input: List<String>) = input.map { it.trim().split("\\s+".toRegex()) }

    fun part1(input: List<String>): Long {
        val betterInput = sanitiseInput(input)
        val height = betterInput.size
        val numberRows = (0..height - 2)

        return betterInput.last().foldIndexed(0L) { index, acc, char ->
            when (char) {
                "+" -> numberRows.fold(0L) { acc, it -> acc + betterInput[it][index].toInt() }
                "-" -> numberRows.fold(0L) { acc, it -> acc - betterInput[it][index].toInt() }
                "*" -> numberRows.fold(1L) { acc, it -> acc * betterInput[it][index].toInt() }
                else -> 0
            } + acc
        }
    }

    fun toNumberList(numberEntries: List<String>): List<Int> {
        val numbers = (0..numberEntries.first().length - 1).mapNotNull { x ->
            (0..numberEntries.size - 1).joinToString("") { y ->
                numberEntries[y][x].toString()
            }.trim().toIntOrNull()
        }
        return numbers
    }

    fun calculateResult(numbers: List<Int>, operation: Char): Long {
        return when (operation) {
            '+' -> numbers.fold(0L) { acc, it -> acc + it }
            '-' -> numbers.fold(0L) { acc, it -> acc - it }
            '*' -> numbers.fold(1L) { acc, it -> if (it == 0) acc else acc * it }
            else -> 0
        }
    }

    fun solveBlock(numberRows: IntRange,
                   input: List<String>,
                   lastOperationIndex: Int,
                   index: Int,
                   lastOperation: Char): Long {
        if (lastOperation.code == 0) return 0L

        val numberEntries = numberRows.map { input[it].substring(lastOperationIndex, index) }
        val numbers = toNumberList(numberEntries)
        val result = calculateResult(numbers, lastOperation)
        return result
    }

    fun part2(input: List<String>): Long {
        val numberRows = 0 .. input.size - 2

        var lastOperation: Char = Char(0)
        var lastOperationIndex: Int = 0
        return input.last().foldIndexed(0L) { index, acc, char ->
            if (char != ' ') {
                val value = solveBlock(numberRows, input, lastOperationIndex, index, lastOperation)

                lastOperation = char
                lastOperationIndex = index

                acc + value
            } else acc
        } + solveBlock(numberRows, input, lastOperationIndex, input.first().length, lastOperation)
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    // 2.711584ms
    val testAnswerPart1 = part1(testInput)
    check(testAnswerPart1 == 4277556L, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input)) } // 5877594983578

    // 5.292250ms
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 3263827L, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 11159825706149
}
