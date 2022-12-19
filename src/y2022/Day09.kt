package y2022

import java.awt.Point
import java.io.File

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

    class Field {
        val head = MyPoint()
        val tail = MyPoint()

        fun moveHead(direction: Direction, times: Int) {
            for (n in 0 until times) moveHeadByOne(direction)
        }

        private fun moveHeadByOne(direction: Direction) {
            val newHead = when (direction) {
                Direction.D -> Point(head.x, head.y - 1)
                Direction.L -> Point(head.x - 1, head.y)
                Direction.U -> Point(head.x, head.y + 1)
                Direction.R -> Point(head.x + 1, head.y)
            }

            if (newHead.distance(tail.point) >= 2.0) {
                tail.point = head.point
            }

            head.point = newHead
        }
    }

    fun part1(input: List<String>): Int {
        val field = Field()
        input.forEach {
            val (directionStr, times) = it.split(" ")
            val direction = Direction.valueOf(directionStr)
            field.moveHead(direction, times.toInt())
        }

        return field.tail.visited.size
    }

    fun part2(input: List<String>): Int = 0

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 13)
    println(part1(input)) // not 6180

//    check(part2(testInput) == 0)
//    println(part2(input))
}
