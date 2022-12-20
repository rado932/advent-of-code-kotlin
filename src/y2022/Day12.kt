package y2022

import y2022.Day12.parseInput
import y2022.Day12.HeightMap
import java.io.File
import java.util.*

private const val inputPrefix = "src/y2022/Day12"

// https://todd.ginsberg.com/post/advent-of-code/2022/day12/

object Day12 {
    class HeightMap(val elevations: Map<Point2D, Int>, val start: Point2D, val end: Point2D) {
        fun shortestPath(
            begin: Point2D,
            isGoal: (Point2D) -> Boolean,
            canMove: (Int, Int) -> Boolean
        ): Int {
            val seen = mutableSetOf<Point2D>()
            val queue = LinkedList<Pair<Point2D, Int>>().apply { add(begin to 0) }

            while (queue.isNotEmpty()) {
                val (currentPoint, cost) = queue.poll()

                if (currentPoint !in seen) {
                    seen += currentPoint

                    val neighbors = currentPoint.cardinalNeighbors()
                        .filter { neighbourPoint ->
                            neighbourPoint in elevations &&
                            canMove(elevations[currentPoint]!!, elevations[neighbourPoint]!!)
                        }

                    if (neighbors.any { isGoal(it) }) return cost + 1
                    queue.addAll(neighbors.map { neighbourPoint -> neighbourPoint to cost + 1 })
                }
            }

            throw IllegalStateException("No valid path from $start to $end")
        }
    }

    fun List<String>.parseInput(): HeightMap {
        var start: Point2D? = null
        var end: Point2D? = null
        val elevations = flatMapIndexed { y, row ->
            row.mapIndexed { x, char ->
                val here = Point2D(x, y)
                here to when (char) {
                    'S' -> 0.also { start = here }
                    'E' -> 25.also { end = here }
                    else -> char - 'a'
                }
            }
        }.toMap()
        return HeightMap(elevations, start!!, end!!)
    }
}

fun main() {

    fun part1(input: HeightMap): Int = input.shortestPath(
        begin = input.start,
        isGoal = { it == input.end },
        canMove = { from, to -> to - from <= 1 }
    )

    fun part2(input: HeightMap): Int  = input.shortestPath(
        begin = input.end,
        isGoal = { input.elevations[it] == 0 },
        canMove = { from, to -> from - to <= 1 }
    )

    val testInput: HeightMap = File("${inputPrefix}_test.txt").readLines().parseInput()

    val input = File("$inputPrefix.txt").readLines().parseInput()

    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}
