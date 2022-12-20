package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day14"

fun main() {

    fun Point2D.bottomNeighbours(): List<Point2D> =
        listOf(copy(x = x - 1, y = y + 1), copy(y = y + 1), copy(x = x + 1, y = y + 1))

    class Cave(occupiedPoints: Set<Point2D>) {
        val occupiedPoints = occupiedPoints.toMutableSet()
        val rocks: Int
        val lowestY: Int
        val floorY: Int

        init {
            rocks = occupiedPoints.size
            lowestY = occupiedPoints.maxOf { it.y }
            floorY = lowestY + 2
        }

        fun findUnoccupiedDownSibling(point: Point2D): Point2D? {
            if (point.y + 1 == floorY) return null

            val (downLeft, down, downRight) = point.bottomNeighbours()

            if (!occupiedPoints.contains(down)) return down
            if (!occupiedPoints.contains(downLeft)) return downLeft
            if (!occupiedPoints.contains(downRight)) return downRight

            return null
        }

        fun dustFall(dust: Point2D): Int {
            var fallingDust = dust

            while (fallingDust.y < lowestY) {
                val downSibling = findUnoccupiedDownSibling(fallingDust)

                if (downSibling != null) {
                    fallingDust = downSibling
                    continue
                }

                occupiedPoints.add(fallingDust)
                fallingDust = dust
            }

            return occupiedPoints.size - rocks
        }


        fun dustFull(dust: Point2D): Int {
            var fallingDust = dust

            while (true) {
                val downSibling = findUnoccupiedDownSibling(fallingDust)

                if (downSibling != null) {
                    fallingDust = downSibling
                    continue
                }

                if (occupiedPoints.contains(fallingDust))
                    break

                occupiedPoints.add(fallingDust)
                fallingDust = dust
            }

            return occupiedPoints.size - rocks
        }
    }

    fun String.splitToPoint(delimiter: String): Point2D = split(delimiter).let { Point2D(it[0].toInt(), it[1].toInt()) }
    fun Set<Point2D>.toCave() = Cave(this)

    fun part1(cave: Cave): Int = cave.dustFall(Point2D(500, 0))

    fun part2(cave: Cave): Int = cave.dustFull(Point2D(500, 0))

    fun File.parseInput() = readLines().map {
        it.split(" -> ")
            .map { it.splitToPoint(",") }
            .windowed(2, 1) {
                it[0].lineTo(it[1])
            }.flatten().toSet()
    }.flatten().toSet().toCave()

    val testInput: Cave = File("${inputPrefix}_test.txt").parseInput()
    val input = File("$inputPrefix.txt").parseInput()

    check(part1(testInput) == 24)
    println(part1(input))

    check(part2(testInput) == 93)
    println(part2(input))
}
