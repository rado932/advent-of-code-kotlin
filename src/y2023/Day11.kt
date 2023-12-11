package y2023

import Timer.time
import java.io.File

private const val inputPrefix = "src/y2023/Day11"

private data class Point(val x: Int = 0, val y: Int = 0)

private class Space(private val stars: List<Point>) {
    val xOccupied: Set<Int> by lazy { stars.map { it.x }.toSet() }
    val yOccupied: Set<Int> by lazy { stars.map { it.y }.toSet() }

    val starPairs: List<Pair<Point, Point>> by lazy {
        mutableListOf<Pair<Point, Point>>().apply {
            for (i in 0 until stars.size - 1) {
                for (j in i + 1 until stars.size) {
                    add(stars[i] to stars[j])
                }
            }
        }
    }

    fun spaceBetween(a: Point, b: Point, galaxyExpansionRate: Int): Long {
        val xRange = if (a.x > b.x) b.x..a.x else a.x..b.x
        val yRange = if (a.y > b.y) b.y..a.y else a.y..b.y

        val xSpace = xRange.last - xRange.first
        val ySpace = yRange.last - yRange.first

        val emptyRowMultiplier = (galaxyExpansionRate - 1).toLong()
        val xExpandedEmptyRows = xRange.filterNot { xOccupied.contains(it) }.size * emptyRowMultiplier
        val yExpandedEmptyRows = yRange.filterNot { yOccupied.contains(it) }.size * emptyRowMultiplier

        return xSpace + ySpace + xExpandedEmptyRows + yExpandedEmptyRows
    }
}

private fun List<String>.toSpace(): Space = Space(
    this.flatMapIndexed { index, line ->
        line.indices.filter { line[it] == '#' }.map {
            Point(index, it)
        }
    }
)

fun main() {
    fun part1(space: Space): Long = space.starPairs
        .sumOf { (a, b) ->
            space.spaceBetween(a, b, 2)
        }

    fun part2(space: Space): Long = space.starPairs
        .sumOf { (a, b) ->
            space.spaceBetween(a, b, 1000000)
        }

    val testInput = File("${inputPrefix}_test.txt").readLines().toSpace()

    val input = File("$inputPrefix.txt").readLines().toSpace()

    check(part1(testInput) == 374L)
    time { println(part1(input)) }

    check(part2(testInput) == 82000210L)
    time { println(part2(input)) }
}
