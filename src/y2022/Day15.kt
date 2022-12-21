package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day15"

enum class PointType { SENSOR, BEACON, NOTHING }

fun main() {
    class Cave(private val input: List<Pair<Point2D, Point2D>>) {
        private val points: Map<Point2D, PointType>

        init {
            points = input.flatMap { (sensor, beacon) ->
                listOf(
                    sensor to PointType.SENSOR,
                    beacon to PointType.BEACON
                )
            }.toMap()
        }

        fun occupiedPositionsOnRow(y1: Int): Int = input.flatMap { (sensor, beacon) ->
            sensor.findCrossSection(beacon, y1)
        }.toSet().count { !points.contains(it) }

//        fun process(sensor: Point2D, beacon: Point2D) {
//            sensor.allPointsWithSameOrLessDistanceThan(beacon)
//                .filter { !points.contains(it) }
//                .forEach { points[it] = PointType.NOTHING }
//        }
    }

    fun part1(cave: Cave, row: Int): Int = cave.occupiedPositionsOnRow(row)

    fun part2(cave: Cave): Int = 0

    fun createPoint(str: String): Point2D {
        val (x, y) = str.split(", ")
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
