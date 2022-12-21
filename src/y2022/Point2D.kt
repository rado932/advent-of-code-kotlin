package y2022

import kotlin.math.abs
import kotlin.math.sign

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

    fun findCrossSection(
        other: Point2D,
        y1: Int
    ): Pair<Int, Int>? {
        val deltaX = abs(x - other.x)
        val deltaY = abs(y - other.y)
        val maxDeltaY = deltaX + deltaY
        val deltaY1 = abs(y - y1)

        val x1 = maxDeltaY - deltaY1
        if (x1 < 0) return null

        return x - x1 to x + x1
    }
}