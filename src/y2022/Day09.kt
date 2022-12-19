package y2022

import java.awt.Point
import java.io.File
import kotlin.math.sign

private const val inputPrefix = "src/y2022/Day09"

enum class Direction(val str: String) {
    U("Up"), L("Left"), D("Down"), R("Right")
}

fun main() {

    open class MyPoint {

        val x: Int
            get() = point.x
        val y: Int
            get() = point.y

        var point = Point(0, 0)
            set(value) {
                visited.add(value)
                field = value
            }

        val visited = mutableSetOf(point)

        override fun toString(): String = "($x, $y)"
    }

    class Field(size: Int) {
        val points = List(size) { MyPoint() }
        val head: MyPoint
            get() = points[0]

        fun moveHead(direction: Direction, times: Int) {
            for (n in 0 until times) moveHeadByOne(direction)
        }

        private fun moveHeadByOne(direction: Direction) {
            head.point = when (direction) {
                Direction.D -> Point(head.x, head.y - 1)
                Direction.L -> Point(head.x - 1, head.y)
                Direction.U -> Point(head.x, head.y + 1)
                Direction.R -> Point(head.x + 1, head.y)
            }

            for (i in 1..points.lastIndex) {
                val p1 = points[i - 1].point
                val p2 = points[i].point

                if (p1.distance(p2) >= 2) {
                    points[i].point = Point(
                        (p1.x - p2.x).sign + p2.x,
                        (p1.y - p2.y).sign + p2.y,
                    )
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val field = Field(2)
        input.forEach {
            val (directionStr, times) = it.split(" ")
            val direction = Direction.valueOf(directionStr)
            field.moveHead(direction, times.toInt())
        }

        return field.points[1].visited.size
    }

    fun part2(input: List<String>): Int {
        val field = Field(10)
        input.forEach {
            val (directionStr, times) = it.split(" ")
            val direction = Direction.valueOf(directionStr)
            field.moveHead(direction, times.toInt())
        }

        return field.points[9].visited.size
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()
    val testInput2 = File("${inputPrefix}_test2.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 1)
    check(part2(testInput2) == 36)
    println(part2(input))
}
