import java.io.File
import Timer.time
import kotlin.math.abs

private const val inputPrefix = "src/y2025/Day01"
private const val startPos = 50
private const val maxPos = 100
private const val triggerPos = 0
private fun String.toDelta() = replace("R", "+").replace("L", "-").toInt()

fun main() {
    fun part1(input: List<String>): Int {
        var pos = startPos
        var passOnZero = 0

        input.forEach {
            val cur = pos + it.toDelta()
            pos = cur.mod(maxPos)
            if (pos == triggerPos) passOnZero++
        }

        return passOnZero
    }

    fun part1Map(input: List<String>): Int {
        var pos = startPos
        return input.map {
            val cur = pos + it.toDelta()
            pos = cur.mod(maxPos)
            if (pos == triggerPos) 1 else 0
        }.sum()
    }

    fun part2(input: List<String>): Int {
        var pos = startPos
        var passOnZero = 0

        input.forEach {
            val cur = pos + it.toDelta()

            if (pos != triggerPos && cur <= 0) passOnZero++
            passOnZero += abs(cur) / maxPos

            pos = cur.mod(maxPos)
        }

        return passOnZero
    }

    fun part2Map(input: List<String>): Int {
        var pos = startPos
        return input.sumOf {
            val cur = pos + it.toDelta()
            ((abs(cur) / maxPos) + (if (pos != triggerPos && cur <= 0) 1 else 0)).also {
                pos = cur.mod(maxPos)
            }
        }
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    val testAnswerPart1 = part1Map(testInput)
    check(testAnswerPart1 == 3, { "part 1 failed: $testAnswerPart1" })
    time { println(part1Map(input)) } // 1172

    val testAnswerPart2 = part2Map(testInput)
    check(testAnswerPart2 == 6, { "part 2 failed: $testAnswerPart2" })
    time { println(part2Map(input)) } // 6932
}
