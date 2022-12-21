package y2022

import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

data class Point2D(val x: Int = 0, val y: Int = 0) {
    fun cardinalNeighbors(): Set<Point2D> =
        setOf(
            copy(x = x - 1),
            copy(x = x + 1),
            copy(y = y - 1),
            copy(y = y + 1),
        )

    fun lineTo(other: Point2D): List<Point2D> {
        val xDelta = (other.x - x).sign
        val yDelta = (other.y - y).sign
        val steps = maxOf(abs(x - other.x), abs(y - other.y))
        return (1..steps).scan(this) { last, _ -> Point2D(last.x + xDelta, last.y + yDelta) }
    }

    fun distance(other: Point2D): Double {
        val px: Double = other.x - x.toDouble()
        val py: Double = other.y - y.toDouble()
        return sqrt(px * px + py * py)
    }

    // [0,0].allPointsWithSameOrLessDistanceThan([1,1))
    // [1,0],[-1,0],[0,1],[0,-1],[1,1],[-1,-1],[1,-1],[-1,1]

    //[9,16].allPointsWithSameOrLessDistanceThan[10,16]
    //[9,16],[10,16],[9,17],[9,15]
    fun allPointsWithSameOrLessDistanceThanOld(other: Point2D): Set<Point2D> {
        val deltaX = abs(x - other.x)
        val deltaY = abs(y - other.y)

        return (0..deltaX).flatMap { x1 ->
            (0..deltaY).flatMap { y1 ->
                listOf(
                    Point2D(x + x1, y + y1),
                    Point2D(x + x1, y - y1),
                    Point2D(x - x1, y + y1),
                    Point2D(x - x1, y - y1),
                )
            }
        }.toSet()
    }

    fun allPointsWithSameOrLessDistanceThan(other: Point2D): Set<Point2D> {
        val output = mutableSetOf<Point2D>()

        var currentPoints = listOf(this)
        do {
            currentPoints = currentPoints.flatMap { it.cardinalNeighbors() }
            output += currentPoints
        } while (!currentPoints.contains(other))

        return output
    }
}