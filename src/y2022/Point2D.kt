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

    fun findCrossSection(other: Point2D, y1: Int): Set<Point2D> {
        val deltaX = abs(x - other.x)
        val deltaY = abs(y - other.y)
        val maxDeltaY = deltaX + deltaY
        val deltaY1 = abs(y - y1)

        return (0..maxDeltaY - deltaY1).flatMap { i ->
            listOf(
                Point2D(x + i, y1),
                Point2D(x - i, y1),
            )
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