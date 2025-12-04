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

    fun lessThanFourNeighboursWithSideEffect(x: Int,
                                             y: Int,
                                             input: MutableList<MutableList<Char>>,
                                             validChars: List<Char>): Boolean {
        val coordinatesToCheck = getCoordinatesToCheck(x, y)

        val neighbourCount = coordinatesToCheck.count { (x, y) ->
            (input.getOrNull(y)?.getOrNull(x) in validChars)
        }
        return (neighbourCount < 4).also {
            if (it) input[y][x] = validChars[0]
        }
    }

    // todo store ranges of all '@'s instead and go over those only
    fun solutionPart1(input: MutableList<MutableList<Char>>, validChars: List<Char>): Int = input.mapIndexed { y, row ->
        row.mapIndexed { x, char ->
            if (char !in validChars) 0
            else if (lessThanFourNeighboursWithSideEffect(x, y, input, validChars)) 1
            else 0
        }.sum()
    }.sum()

    fun part2(input: MutableList<MutableList<Char>>): Int {
        var specialChar = '@'
        var validChars: List<Char>

        return generateSequence {
            specialChar = Char(specialChar.code + 1)
            validChars = listOf(specialChar, '@')

            solutionPart1(input, validChars)
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
