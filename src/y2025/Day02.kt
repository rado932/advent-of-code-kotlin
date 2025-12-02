import java.io.File
import Timer.time
import kotlin.math.pow

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

    fun generateRepeatingPatternNumbers(first: Long, last: Long): List<Long> {
        val result = mutableSetOf<Long>()
        val firstStr = first.toString()
        val lastStr = last.toString()

        for (totalLength in firstStr.length..lastStr.length) {
            // Iterate through all possible pattern lengths
            for (patternLen in 1..totalLength / 2) {
                if (totalLength % patternLen != 0) continue
                val repetitions = totalLength / patternLen

                // Calculate the range of patterns to generate
                // For the first length, start from a pattern that would generate >= first
                // For other lengths, start from smallest pattern
                val minPattern: Long = if (totalLength == firstStr.length) {
                    val patt = firstStr.take(patternLen)
                    (patt.toLong() + if (patt.repeat(repetitions).toLong() < first) 1 else 0)
                } else {
                    10.0.pow(patternLen - 1).toLong()
                }

                // For the last length, end at a pattern that would generate <= last
                // For other lengths, end at largest pattern
                val maxPattern: Long = if (totalLength == lastStr.length) {
                    lastStr.take(patternLen).toLong()
                } else {
                    10.0.pow(patternLen).toLong() - 1
                }

                // Generate all numbers with this pattern length
                for (pattern in minPattern..maxPattern) {
                    val patternStr = pattern.toString()
                    if (patternStr.length != patternLen) continue

                    val number = patternStr.repeat(repetitions).toLong()

                    if (number in first..last) {
                        result.add(number)
                    }
                }
            }
        }

        return result.toList()
    }

    fun part2Optimised(input: List<String>): Long = input.first().split(",").sumOf { strRange ->
        val (first, last) = strRange.split("-").map { it.toLong() }
        generateRepeatingPatternNumbers(first, last).sum()
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
