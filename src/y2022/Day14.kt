package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day14"

fun main() {

    fun Point2D.bottomNeighbours(): List<Point2D> =
        listOf(copy(x = x - 1, y = y + 1), copy(y = y + 1), copy(x = x + 1, y = y + 1))

    class Cave(occupiedPoints: Set<Point2D>) {
        val occupiedPoints = occupiedPoints.toMutableSet()
        val lowestY: Int
        val floorY: Int

        init {
            lowestY = occupiedPoints.maxOf { it.y }
            floorY = lowestY + 2
        }

        fun dustFall(dust: Point2D): Int {
            var fallingDust = dust
            var positionedDust = 0

            while (fallingDust.y < lowestY) {
                val (downLeft, down, downRight) = fallingDust.bottomNeighbours()

                if (!occupiedPoints.contains(down)) {
                    fallingDust = down
                    continue
                }
                if (!occupiedPoints.contains(downLeft)) {
                    fallingDust = downLeft
                    continue
                }
                if (!occupiedPoints.contains(downRight)) {
                    fallingDust = downRight
                    continue
                }

                occupiedPoints.add(fallingDust)
                positionedDust += 1
                fallingDust = dust
            }

            return positionedDust
        }
    }

    fun String.splitToPoint(delimiter: String): Point2D = split(delimiter).let { Point2D(it[0].toInt(), it[1].toInt()) }
    fun Set<Point2D>.toCave() = Cave(this)

    fun part1(cave: Cave): Int = cave.dustFall(Point2D(500, 0))

    fun part2(input: List<String>): Int {
        return 0
    }

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

//    check(part2(testInput) == 93)
//    println(part2(input))
}
