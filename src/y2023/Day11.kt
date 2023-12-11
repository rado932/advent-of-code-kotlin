package y2023

import Timer.time
import java.io.File

private const val inputPrefix = "src/y2023/Day11"

private data class Point(val x: Int = 0, val y: Int = 0)

private class Space(val points: List<Point>) {
    val yStars: Set<Int> by lazy { points.map { it.y }.toSet() }

    val xStars: Set<Int> by lazy { points.map { it.x }.toSet() }

    val starPairs: List<Pair<Point, Point>> by lazy {
        mutableListOf<Pair<Point, Point>>().apply {
            for (i in 0 until points.size - 1) {
                for (j in i + 1 until points.size) {
                    add(points[i] to points[j])
                }
            }
        }
    }

    fun spaceBetween(a: Point, b: Point, galaxyExpansionRate: Int): Long {
        val xRange = if (a.x > b.x) b.x..a.x else a.x..b.x
        val yRange = if (a.y > b.y) b.y..a.y else a.y..b.y

        val xExpanded = with (xRange.filterNot { xStars.contains(it) }.size) {
            if (this == 0) 0
            else this * (galaxyExpansionRate - 1)
        }.toLong()

        val yExpanded = with (yRange.filterNot { yStars.contains(it) }.size) {
            if (this == 0) 0
            else this * (galaxyExpansionRate - 1)
        }.toLong()

        return (xRange.last - xRange.first) + (yRange.last - yRange.first) + xExpanded + yExpanded
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
