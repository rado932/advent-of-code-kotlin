package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day04"

fun main() {

    fun String.toRange() = this.split('-')
        .take(2)
        .let { (first, second) ->
            first.toInt()..second.toInt()
        }

    fun tasksToRangeList(tasks: String): List<IntRange> = tasks.split(',')
        .take(2)
        .map { it.toRange() }

    fun part1(input: List<String>): Int {
        fun IntRange.contains(anotherRange: IntRange) = first >= anotherRange.first && last <= anotherRange.last

        return input.count { tasks ->
            tasksToRangeList(tasks).let { (first, second) ->
                first.contains(second) || second.contains(first)
            }
        }
    }

    fun part2(input: List<String>): Int {
        fun IntRange.overlaps(anotherRange: IntRange) = first <= anotherRange.last && last >= anotherRange.first

        return input.count { tasks ->
            tasksToRangeList(tasks).let { (first, second) ->
                first.overlaps(second) || second.overlaps(first)
            }
        }
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 2)
    println(part1(input))

    check(part2(testInput) == 4)
    println(part2(input))
}
