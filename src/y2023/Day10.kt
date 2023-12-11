package y2023

import Timer.time
import java.io.File
import java.lang.Exception

private const val inputPrefix = "src/y2023/Day10"

fun main() {

    fun List<String>.getStartingPosition(): Pair<Int, Int> {
        forEachIndexed { x, s ->
            val y = s.indexOfFirst { it == 'S' }
            if (y >= 0) return x to y
        }
        throw Exception("Bad Input")
    }

    fun Pair<Int, Int>.getLeft() = copy(second = second - 1)
    fun Pair<Int, Int>.getBottom() = copy(first = first + 1)
    fun Pair<Int, Int>.getRight() = copy(second = second + 1)
    fun Pair<Int, Int>.getTop() = copy(first = first - 1)

    fun List<String>.connectedPipe(cur: Pair<Int, Int>,
                                   prev: Pair<Int, Int>): Pair<Int, Int>? {
        val (y, x) = cur
        return when (this[y][x]) {
            '|' -> listOf(cur.getBottom(), cur.getTop()) - prev
            '-' -> listOf(cur.getLeft(), cur.getRight()) - prev
            'L' -> listOf(cur.getTop(), cur.getRight()) - prev
            'J' -> listOf(cur.getLeft(), cur.getTop()) - prev
            '7' -> listOf(cur.getLeft(), cur.getBottom()) - prev
            'F' -> listOf(cur.getBottom(), cur.getRight()) - prev
            'S' -> { println("Starting point"); return null }
            else -> { println("Pipe not connected to anything"); return null }
        }.also {
            if (it.size > 1) {
                println("foo newConnectedPipes is bigger than one")
            }
        }.first()
    }

    fun List<String>.getAllConnectedPipes(cur: Pair<Int, Int>,
                                          prev: Pair<Int, Int>): List<Pair<Int, Int>> {
        var current: Pair<Int, Int>? = cur
        var previous: Pair<Int, Int>? = prev

        if (current!!.first < 0 || current.second < 0) return listOf()

        return mutableListOf(previous!!).apply {
            do {
                add(current!!)
                connectedPipe(current!!, previous!!).let {
                    previous = current
                    current = it
                }
            } while (current != null)
        }
    }

    fun part1(input: List<String>): Int {
        val (y, x) = input.getStartingPosition()
        val paths = listOf(
            input.getAllConnectedPipes(y to x - 1, y to x),
            input.getAllConnectedPipes(y + 1 to x, y to x),
            input.getAllConnectedPipes(y to x + 1, y to x),
            input.getAllConnectedPipes(y - 1 to x, y to x)
        ).filter { it.size > 3 }
        return paths[0].size / 2
    }

    fun part2(input: List<String>): Int = 0

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 8)
    time { println(part1(input)) }

//    check(part2(testInput) == 0)
//    time { println(part2(input)) }
}
