import java.io.File
import Timer.time

private const val inputPrefix = "src/y2025/Day02"

fun main() {

    fun Long.isRepeated(): Boolean = with(toString()) {
        length % 2 == 0 && substring(0, length / 2) == substring(length / 2)
    }

    fun Long.hasRepeatingElements(): Boolean = with(toString()) {
        (1..length / 2).any { patternLen ->
            length % patternLen == 0 && substring(0, patternLen).repeat(length / patternLen) == this
        }
    }

    fun part1(input: List<String>): Long = input.first().split(",").sumOf { strRange ->
        val (first, last) = strRange.split("-").map { it.toLong() }
        (first..last).filter { it.isRepeated() }.sum()
    }

    fun part2(input: List<String>): Long = input.first().split(",").sumOf { strRange ->
        val (first, last) = strRange.split("-").map { it.toLong() }
        (first..last).filter { it.hasRepeatingElements() }.sum()
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    val testAnswerPart1 = part1(testInput)
    check(testAnswerPart1 == 1227775554L, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input)) } // 23534117921

    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 4174379265L, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 31755323497
}
