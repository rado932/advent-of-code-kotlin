package `2021`

import readInput

fun main() {
    fun partX(input: List<String>, isPart2: Boolean): Int {
        val pointPairs = input.map { line ->
            line.split(" -> ").let {
                Pair(Point.createFromCSV(it[0]), Point.createFromCSV(it[1]))
            }
        }

        val matchedPoints = mutableMapOf<Point, Int>()
        pointPairs.forEach { (start, end) -> matchedPoints.addRange(start, end, isPart2) }

        return matchedPoints.values.count { it > 1 }
    }

    fun part1(input: List<String>): Int = partX(input, false)
    fun part2(input: List<String>): Int = partX(input, true)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Point(val x: Int, val y: Int) {
    companion object Factory {
        fun createFromCSV(csv: String): Point {
            val split = csv.split(",")
            return Point(split[0].toInt(), split[1].toInt())
        }
    }
}

fun MutableMap<Point, Int>.addRange(
    start: Point,
    end: Point,
    isPart2: Boolean = false
) {
    if (start.x == end.x) {
        val stepY = if (start.y < end.y) 1 else -1
        var currentY = start.y
        while (currentY != end.y + stepY) {
            increase(Point(start.x, currentY), 1)
            currentY += stepY
        }
    } else if (start.y == end.y) {
        val stepX = if (start.x < end.x) 1 else -1
        var currentX = start.x
        while (currentX != end.x + stepX) {
            increase(Point(currentX, start.y), 1)
            currentX += stepX
        }
    } else if (isPart2) {
        val stepX = if (start.x < end.x) 1 else -1
        val stepY = if (start.y < end.y) 1 else -1
        var (currentX, currentY) = start
        while (currentX != end.x + stepX || currentY != end.y + stepY) {
            increase(Point(currentX, currentY), 1)
            currentX += stepX
            currentY += stepY
        }
    }
}

fun MutableMap<Point, Int>.increase(point: Point, value: Int) {
    val existingValue = getOrDefault(point, 0)
    put(point, existingValue + value)
}
