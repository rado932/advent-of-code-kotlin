package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day15"

enum class PointType { SENSOR, BEACON, NOTHING }

fun main() {
    class Cave(input: List<Pair<Point2D, Point2D>>) {
        val points = mutableMapOf<Point2D, PointType>()

        init {
            input.forEach { (sensor, beacon) ->
                points[sensor] = PointType.SENSOR
                points[beacon] = PointType.BEACON

                process(sensor, beacon)
            }
        }

        fun occupiedPositionsOnRow(y: Int) = points.count { (point, type) -> point.y == y && type != PointType.BEACON }

        fun process(sensor: Point2D, beacon: Point2D) {
            sensor.allPointsWithSameOrLessDistanceThan(beacon)
                .filter { !points.contains(it) }
                .forEach { points[it] = PointType.NOTHING }
        }
    }

    fun part1(cave: Cave, row: Int): Int = cave.occupiedPositionsOnRow(row)

    fun part2(cave: Cave): Int = 0

    fun createPoint(str: String): Point2D {
        val (x,y) = str.split(", ")
        return Point2D(x.substringAfter("x=").toInt(), y.substringAfter("y=").toInt())
    }

    fun File.parseInput() = readLines().map {
        val (sensorStr, beaconLoc) = it.split(": closest beacon is at ")
        val sensorLoc = sensorStr.substringAfter("Sensor at ")
        createPoint(sensorLoc) to createPoint(beaconLoc)
    }.let { Cave(it) }

    val testInput = File("${inputPrefix}_test.txt").parseInput()

    val input = File("$inputPrefix.txt").parseInput()

    check(part1(testInput, 10) == 26)
    println(part1(input, 2000000))

//    check(part2(testInput) == 0)
//    println(part2(input))
}
