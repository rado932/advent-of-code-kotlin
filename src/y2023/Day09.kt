package y2023

import java.io.File
import Timer.time

private const val inputPrefix = "src/y2023/Day09"

fun main() {

    fun part1(input: List<List<Int>>): Long = input.sumOf { line ->
        var _line = line
        var right = 0L
        while (!_line.all { it == 0 }) {
            right += _line.last()
            _line = _line.windowed(2) { it[1] - it[0] }
        }
        right
    }

    fun part2(input: List<List<Int>>): Long = input.sumOf { line ->
        var currentLine = line
        var currentNumber: Long
        var left = 0L
        var isPositive = true
        while (!currentLine.all { it == 0 }) {
            currentNumber = with(currentLine.first().toLong()) { if (isPositive) this else 0 - this }
            left += currentNumber
            isPositive = !isPositive
            currentLine = currentLine.windowed(2) { it[1] - it[0] }
        }
        left
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()
        .map { it.split(" ").map { it.toInt() } }

    val input = File("$inputPrefix.txt").readLines()
        .map { it.split(" ").map { it.toInt() } }

    check(part1(testInput) == 114L)
    time { println(part1(input)) }

    check(part2(testInput) == 2L)
    time { println(part2(input)) }
}
