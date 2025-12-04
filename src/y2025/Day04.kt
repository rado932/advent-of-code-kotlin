import Timer.time
import java.io.File

private const val inputPrefix = "src/y2025/Day04"

private val NEIGHBOUR_OFFSETS = arrayOf(
    -1 to -1, -1 to 0, -1 to 1,
    0 to -1, 0 to 1,
    1 to -1, 1 to 0, 1 to 1
)

fun main() {
    fun hasLessThanFourNeighbours(x: Int,
                                  y: Int,
                                  height: Int,
                                  width: Int,
                                  input: List<List<Char>>): Boolean {
        var neighbours = 0
        for ((dx, dy) in NEIGHBOUR_OFFSETS) {
            val nx = x + dx
            val ny = y + dy
            if (ny in 0 until height && nx in 0 until width && input[ny][nx] == '@') {
                neighbours++
                if (neighbours >= 4) return false
            }
        }
        return true
    }

    fun part1(input: List<List<Char>>): Int {
        val height = input.size
        val width = input[0].size
        return input.flatMapIndexed { y, row ->
            row.filterIndexed { x, char -> char == '@' && hasLessThanFourNeighbours(x, y, height, width, input) }
        }.size
    }

    fun hasLessThanFourNeighbours(x: Int,
                                  y: Int,
                                  height: Int,
                                  width: Int,
                                  grid: Array<BooleanArray>): Boolean {
        var neighbours = 0
        for ((dx, dy) in NEIGHBOUR_OFFSETS) {
            val nx = x + dx
            val ny = y + dy
            if (ny in 0 until height && nx in 0 until width && grid[ny][nx]) {
                neighbours++
                if (neighbours >= 4) return false
            }
        }
        return true
    }

    fun part2(input: List<List<Char>>): Int {
        val grid = Array(input.size) { BooleanArray(input[0].size) }
        val height = grid.size
        val width = grid[0].size

        val nextPointToCheck: MutableSet<Pair<Int, Int>> = input.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, char ->
                if (char == '@') {
                    grid[y][x] = true
                    x to y
                } else null
            }
        }.toMutableSet()

        return generateSequence {
            val pointsToRemove = nextPointToCheck.filterTo(HashSet()) { (x, y) ->
                hasLessThanFourNeighbours(x, y, height, width, grid)
            }

            nextPointToCheck -= pointsToRemove
            pointsToRemove.forEach { (x, y) -> grid[y][x] = false }
            pointsToRemove.size
        }.takeWhile { it != 0 }.sum()
    }

    val testInput = File("${inputPrefix}_test.txt").readLines().map { it.toMutableList() }.toMutableList()

    val input = File("$inputPrefix.txt").readLines().map { it.toMutableList() }.toMutableList()

    // initial 6.739958ms | optimised 3.180375ms
    val testAnswerPart1 = part1(testInput)
    check(testAnswerPart1 == 13, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input)) } // 1493

    // initial 47.960584ms | using grid 19.169541ms
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 43, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 9194
}
