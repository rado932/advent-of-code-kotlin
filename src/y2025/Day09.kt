import Timer.time
import java.io.File
import kotlin.math.abs

private const val inputPrefix = "src/y2025/Day09"

fun main() {


    data class Point(val x: Int, val y: Int)
    data class Segment(val from: Point, val to: Point)
    data class Line(val x: IntRange, val y: IntRange) {
        val length: Int get() = maxOf(x.last - x.first + 1, y.last - y.first + 1)
        override fun toString(): String {
            return "Line(x=$x, y=$y, length=$length)"
        }
    }

    fun calculateVolume2D(p1: Point, p2: Point): Long {
        val x = abs(p1.x - p2.x) + 1L
        val y = abs(p1.y - p2.y) + 1L
        return x * y
    }

    fun convertToPoints(input: List<String>): List<Point> = input.map {
        val (x, y) = it.split(",")
        Point(x.toInt(), y.toInt())
    }

    fun part1(input: List<String>): Long {
        val points = convertToPoints(input)

        var maxVolume = 0L
        (0..points.lastIndex).forEach { i ->
            (i..points.lastIndex).forEach { j ->
                val volume = calculateVolume2D(points[i], points[j])
                if (volume > maxVolume) maxVolume = volume
            }
        }
        return maxVolume
    }


    fun Line.toPointsNew(): List<Point> = if (x.last - x.first == 0) {
        y.map { Point(x.first, it) }
    } else if (y.last - y.first == 0) {
        x.map { Point(it, y.first) }
    } else {
        emptyList()
    }

    fun toSegments(vertices: List<Point>): List<Segment> {
        return vertices.indices.map { i ->
            val a = vertices[i]
            val b = vertices[(i + 1) % vertices.size]
            Segment(a, b)
        }
    }

    fun isPointOnSegment(p: Point, s: Segment): Boolean {
        val (a, b) = s
        return if (a.x == b.x) {
            p.x == a.x && p.y in minOf(a.y, b.y)..maxOf(a.y, b.y)
        } else if (a.y == b.y) {
            p.y == a.y && p.x in minOf(a.x, b.x)..maxOf(a.x, b.x)
        } else {
            false
        }
    }

    fun isInside(point: Point, segments: List<Segment>): Boolean {
        if (segments.any { isPointOnSegment(point, it) }) return true
        var crossings = 0

        for (seg in segments) {
            val (a, b) = seg
            if (a.x != b.x) continue

            val x = a.x
            val y1 = minOf(a.y, b.y)
            val y2 = maxOf(a.y, b.y)

            if (point.x < x && point.y >= y1 && point.y < y2) {
                crossings++
            }
        }

        return crossings % 2 == 1
    }

    fun part2(input: List<String>): Long {
        val points = convertToPoints(input)

        val segments = toSegments(points)

        var maxVolume = 0L
        (0..points.lastIndex).forEach { i ->
            (i..points.lastIndex).forEach { j ->
                val volume = calculateVolume2D(points[i], points[j])
                if (volume > maxVolume) {

                    val xRange =
                        if (points[i].x < points[j].x) points[i].x..points[j].x else points[j].x..points[i].x
                    val yRange =
                        if (points[i].y < points[j].y) points[i].y..points[j].y else points[j].y..points[i].y

                    val points = listOf(
                        Line(xRange, yRange.last..yRange.last), // lineXHigh
                        Line(xRange, yRange.first..yRange.first), // lineXLow
                        Line(xRange.last..xRange.last, yRange), // lineYHigh
                        Line(xRange.first..xRange.first, yRange) // lineYLow
                    ).flatMap { it.toPointsNew() }

                    if (points.all { isInside(it, segments) }) maxVolume = volume
                }
            }
        }

        return maxVolume
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    // 7.298291ms
    val testAnswerPart1 = part1(testInput)
    check(testAnswerPart1 == 50L, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input)) } // 4790063600

//    // 35m 38.793498750s
//    val testAnswerPart2 = part2(testInput)
//    check(testAnswerPart2 == 24L, { "part 2 failed: $testAnswerPart2" })
//    time { println(part2(input)) } // 1516172795
}
