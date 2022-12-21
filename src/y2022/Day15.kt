package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day15"

enum class PointType { SENSOR, BEACON, NOTHING }

fun main() {
    class Cave(private val input: List<Pair<Point2D, Point2D>>) {
        fun Point2D.turningFrequency() = (x * 4_000_000L) + y

        fun Pair<Int, Int>.merge(range: IntRange): Pair<Int, Int> {
            val start = if (first >= range.first) first else range.first
            val end = if (second <= range.last) second else range.last
            return start to end
        }

        private val points: Map<Point2D, PointType> = input.flatMap { (sensor, beacon) ->
            listOf(
                sensor to PointType.SENSOR,
                beacon to PointType.BEACON
            )
        }.toMap()

        fun occupiedPositionsInRow(
            y1: Int,
            range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE
        ): Set<Int> {
            input.map { (sensor, beacon) ->
                sensor.findCrossSection(beacon, y1)
            }.mapNotNull { it?.merge(range) }
                .sortedBy { it.first }

            return setOf(range.count())
        }

        fun fullRowOccupied(
            y1: Int,
            range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE
        ): Boolean {
            return try {
                input.map { (sensor, beacon) ->
                    sensor.findCrossSection(beacon, y1)
                }.mapNotNull { it?.merge(range) }
                    .sortedBy { it.first }.fold(0) { acc, (first, second) ->
                        if (acc < first) throw IllegalArgumentException("Cannot be folded")
                        else if (acc < second) second
                        else acc
                    }

                true
            } catch (ex: IllegalArgumentException) {
                false
            }
        }

        fun findNotFullRow(range: IntRange): Int = range.first { y1 ->
            !fullRowOccupied(y1, range)
        }

        fun findTurningFrequency(range: IntRange): Long {
            val y = findNotFullRow(range)

            val notFullSet: Set<Int> = findOccupuedPositionsOnRow(y)
            val x = range.first { x1 -> !notFullSet.contains(x1) }

            val point = Point2D(x, y)
            return point.turningFrequency()
        }


        fun findOccupuedPositionsOnRow(y1: Int) = input.map { (sensor, beacon) ->
            sensor.findCrossSection(beacon, y1)
        }.filterNotNull()
            .flatMap { it.first..it.second }
            .toSet()

        fun occupiedNotBeaconPositionOnRow(y1: Int): Int = findOccupuedPositionsOnRow(y1).count {
            !points.contains(Point2D(it, y1))
        }
    }

    fun part1(cave: Cave, row: Int): Int = cave.occupiedNotBeaconPositionOnRow(row)

    fun part2(cave: Cave, range: IntRange): Long = cave.findTurningFrequency(range)

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

    check(part2(testInput, 0..20) == 56000011L)
    println(part2(input, 0..4_000_000))
}
