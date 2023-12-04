package y2023

import java.io.File

private const val inputPrefix = "src/y2023/Day03"

fun main() {

    fun isEngineNumber(
        input: Map<Int, String>,
        x: Int,
        y: IntRange,
    ): Boolean {
        val line1 = input[x - 1]?.substring(y) ?: ""
        val line21 = input[x]?.get(y.first) ?: ""
        val line22 = input[x]?.get(y.last) ?: ""
        val line3 = input[x + 1]?.substring(y) ?: ""
        val neighbours = line1 + line21 + line22 + line3

        val notADigitOrADotRegex = "[^\\d.]".toRegex()
        return notADigitOrADotRegex.containsMatchIn(neighbours)
    }

    fun part1(input: Map<Int, String>): Int {
        val digitRegex = "\\d+".toRegex()

        var sum = 0
        for ((x, line) in input) {
            val matches = digitRegex.findAll(line)
            for (match in matches) {
                val startRange = with(match.range.first) {
                    if (this > 0) this - 1
                    else this
                }
                val endRange = with(match.range.last) {
                    if (line.length - 1 > this) this + 1
                    else this
                }
                val extendedRange = startRange..endRange
                if (isEngineNumber(input, x, extendedRange)) {
                    sum += match.value.toInt()
                }
            }
        }
        return sum
    }
    fun doRangesMeet(range1: IntRange, range2: IntRange): Boolean {
        return range1.first <= range2.last && range1.last >= range2.first
    }

    check(doRangesMeet(1..2, 1..2)) { "1..2 <=> 1..2" }
    check(doRangesMeet(1..2, 2..3)) { "1..2 <=> 2..3" }
    check(!doRangesMeet(1..2, 3..4)) { "!1..2 <=> 3..4" }
    check(doRangesMeet(2..3, 1..2)) { "2..3 <=> 1..2" }
    check(!doRangesMeet(3..4, 1..2)) { "!3..4 <=> 1..2" }

    fun getEnginesOnLine(line: String?): Sequence<MatchResult> {
        if (line.isNullOrEmpty()) return sequence { }

        val digitRegex = "\\d+".toRegex()
        return digitRegex.findAll(line)
    }

    fun getEngineNumbers(
        input: Map<Int, String>,
        x: Int,
        y: IntRange,
    ): Pair<Long, Long>? {
        val numbers = mutableListOf<Long>()

        val line1Numbers = getEnginesOnLine(input[x - 1])
        val line2Numbers = getEnginesOnLine(input[x])
        val line3Numbers = getEnginesOnLine(input[x + 1])

        for (match in line1Numbers) {
            if (doRangesMeet(match.range, y)) {
                numbers.add(match.value.toLong())
            }
        }

        for (match in line2Numbers) {
            if (doRangesMeet(match.range, y)) {
                numbers.add(match.value.toLong())
            }
        }

        for (match in line3Numbers) {
            if (doRangesMeet(match.range, y)) {
                numbers.add(match.value.toLong())
            }
        }

        return if (numbers.size >= 2) numbers[0] to numbers[1]
        else null
    }

    fun part2(input: Map<Int, String>): Long {
        val starRegex = "\\*".toRegex()

        var sum = 0L
        for ((x, line) in input) {
            val matches = starRegex.findAll(line)
            for (match in matches) {
                val position = match.range.first
                val extendedRange = position - 1..position + 1
                val engineNumbers = getEngineNumbers(input, x, extendedRange)
                if (engineNumbers != null) {
//                    println("match " + match.value.toInt())
                    sum += engineNumbers.first * engineNumbers.second
                }
            }
        }

        return sum
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()
        .mapIndexed { index, str ->
            index to str
        }.toMap()


    val input = File("$inputPrefix.txt").readLines()
        .mapIndexed { index, str ->
            index to str
        }.toMap()

    check(part1(testInput) == 4361)
    println(part1(input))

    check(part2(testInput) == 467835L)
    println(part2(input))
}
