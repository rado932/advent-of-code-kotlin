import Timer.time
import java.io.File

private const val inputPrefix = "src/y2025/Day04"

fun main() {

    fun getCoordinatesToCheck(x: Int, y: Int): List<Pair<Int, Int>> = listOf(
        x - 1 to y - 1,
        x - 1 to y,
        x - 1 to y + 1,
        x to y - 1,
        x to y + 1,
        x + 1 to y - 1,
        x + 1 to y,
        x + 1 to y + 1
    )

    fun lessThanFourNeighbours(x: Int, y: Int, input: List<List<Char>>): Boolean {
        val coordinatesToCheck = getCoordinatesToCheck(x, y)
        return coordinatesToCheck.count { (x, y) -> input.getOrNull(y)?.getOrNull(x) == '@' } < 4
    }

    fun part1(input: List<List<Char>>): Int = input.mapIndexed { y, row ->
        row.mapIndexed { x, char ->
            if (char != '@') 0
            else if (lessThanFourNeighbours(x, y, input)) 1
            else 0
        }.sum()
    }.sum()

    data class Point2D(val x: Int, val y: Int)

    fun getCoordinatesToCheck(point: Point2D): List<Point2D> {
        val (x, y) = point
        return listOf(
            Point2D(x - 1, y + 1),
            Point2D(x - 1, y - 1),
            Point2D(x - 1, y),
            Point2D(x, y - 1),
            Point2D(x, y + 1),
            Point2D(x + 1, y - 1),
            Point2D(x + 1, y),
            Point2D(x + 1, y + 1)
        )
    }

    fun solutionPart1(pointsToCheck: Set<Point2D>, nextPointToCheck: MutableSet<Point2D>): Int =
        pointsToCheck.count { point ->
            val possibleNeighbours = getCoordinatesToCheck(point)
            val neighbours = possibleNeighbours.count { it in pointsToCheck }
            (neighbours < 4).also { if (it) nextPointToCheck.remove(point) }
        }

    fun part2(input: MutableList<MutableList<Char>>): Int {
        val nextPointToCheck: MutableSet<Point2D> = input.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, char -> if (char == '@') Point2D(x, y) else null }
        }.toMutableSet()

        var pointsToCheck: Set<Point2D>

        return generateSequence {
            pointsToCheck = nextPointToCheck.toSet()
            solutionPart1(pointsToCheck, nextPointToCheck)
        }.takeWhile { it != 0 }.sum()
    }

    val testInput = File("${inputPrefix}_test.txt").readLines().map { it.toMutableList() }.toMutableList()

    val input = File("$inputPrefix.txt").readLines().map { it.toMutableList() }.toMutableList()

    // initial 6.739958ms
    val testAnswerPart1 = part1(testInput)
    check(testAnswerPart1 == 13, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input)) } // 1493

    // initial 47.960584ms
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 43, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 9194
}
