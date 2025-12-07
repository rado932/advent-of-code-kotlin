import Timer.time
import java.io.File

private const val inputPrefix = "src/y2025/Day07"

fun main() {

    fun part1(input: List<String>): Int {
        var beams: List<Int> = listOf(input.first().indexOf("S"))
        var split = 0

        input.drop(1).forEach { line ->
            beams = beams.flatMap { beamIndex ->
                if (line[beamIndex] == '^') {
                    split += 1
                    listOf(beamIndex - 1, beamIndex + 1)
                } else {
                    listOf(beamIndex)
                }
            }.distinct()
        }

        return split
    }

    @Deprecated("Bad solution - creates too much entries in the List...")
    fun part2_Bad(input: List<String>): Int {
        var beams: List<Int> = listOf(input.first().indexOf("S"))

        input.drop(1).forEach { line ->
            beams = beams.flatMap { beamIndex ->
                if (line[beamIndex] == '^') {
                    listOf(beamIndex - 1, beamIndex + 1)
                } else {
                    listOf(beamIndex)
                }
            }
        }
        return beams.size
    }

    fun<T> MutableMap<T, Long>.addToValueSafe(key: T, n: Long) {
        this[key] = getOrDefault(key, 0) + n
    }

    fun part2(input: List<String>): Long {
        var beamCounts = mutableMapOf(input.first().indexOf("S") to 1L)

        input.drop(1).forEach { line ->
            val newBeamCounts = mutableMapOf<Int, Long>()
            beamCounts.forEach { (beamIndex, count) ->
                if (line[beamIndex] == '^') {
                    newBeamCounts.addToValueSafe(beamIndex - 1, count)
                    newBeamCounts.addToValueSafe(beamIndex + 1, count)
                } else {
                    newBeamCounts.addToValueSafe(beamIndex, count)
                }
            }
            beamCounts = newBeamCounts
        }
        return beamCounts.values.sum()
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    // 3.158125ms
    val testAnswerPart1 = part1(testInput)
    check(testAnswerPart1 == 21, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input)) } // 1587

    // 1.892541ms
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 40L, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 5748679033029
}
