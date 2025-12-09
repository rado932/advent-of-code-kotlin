import Timer.time
import java.io.File
import kotlin.math.abs

private const val inputPrefix = "src/y2025/Day09"

fun main() {

    data class Point(val x: Int, val y: Int)

    data class Line(val x: IntRange, val y: IntRange) {
        val isHorizontal = y.first == y.last
        val isVertical = x.first == x.last
    }

    fun calculateVolume2D(p1: Point, p2: Point): Long {
        val x = abs(p1.x - p2.x) + 1L
        val y = abs(p1.y - p2.y) + 1L
        return x * y
    }

    fun getOutline(p1: Point, p2: Point): Pair<IntRange, IntRange> {
        val (x1, y1) = p1
        val (x2, y2) = p2
        return minOf(x1, x2)..maxOf(x1, x2) to minOf(y1, y2)..maxOf(y1, y2)
    }

    fun toLines(points: List<Point>): List<Line> = points.indices.map { i ->
        val a = points[i]
        val b = points[(i + 1) % points.size] // wrap last to first
        Line(minOf(a.x, b.x)..maxOf(a.x, b.x), minOf(a.y, b.y)..maxOf(a.y, b.y))
    }

    fun IntRange.overlaps(other: IntRange): Boolean {
        val maxFirst = maxOf(this.first, other.first)
        val minLast = minOf(this.last, other.last)
        return maxFirst < minLast
    }

    fun edgesAreValid(p1: Point, p2: Point, lines: List<Line>): Boolean {
        val (xRange, yRange) = getOutline(p1, p2)

        for (line in lines) {
            if (line.isVertical) {
                if (line.x.first !in (xRange.first + 1)..<xRange.last) continue
                if (line.y.overlaps(yRange)) return false
            } else if (line.isHorizontal) {
                if (line.y.first !in (yRange.first + 1)..<yRange.last) continue
                if (line.x.overlaps(xRange)) return false
            }
        }

        return true
    }

    fun part2(input: List<String>): Long {
        val points = input.map {
            val (x, y) = it.split(",")
            Point(x.toInt(), y.toInt())
        }

        val lines = toLines(points)

        var maxArea = 0L
        for (i in points.indices) {
            val p1 = points[i]
            for (j in i + 1 until points.size) {
                val p2 = points[j]
                val area = calculateVolume2D(p1, p2)
                if (area > maxArea && edgesAreValid(p1, p2, lines)) maxArea = area
            }
        }
        return maxArea
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()
    val input = File("$inputPrefix.txt").readLines()

    // 101.908542ms | 23.696166ms
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 24L) { "part 2 failed: $testAnswerPart2" }
    time { println(part2(input)) } // 1516172795
}
