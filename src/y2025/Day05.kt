import Timer.time
import java.io.File

private const val inputPrefix = "src/y2025/Day05"

private val NEIGHBOUR_OFFSETS = arrayOf(
    -1 to -1, -1 to 0, -1 to 1,
    0 to -1, 0 to 1,
    1 to -1, 1 to 0, 1 to 1
)

fun main() {

    fun splitInput(input: List<String>): Pair<List<LongRange>, List<Long>> {
        val emptySpace = input.indexOf("")
        // todo iterate over list instead
        val ranges = (0 until emptySpace).map {
            val (first, last) = input[it].split("-")
            LongRange(first.toLong(), last.toLong())
        }
        val items = (emptySpace + 1 until input.size).map { input[it].toLong() }
        return ranges to items
    }

    fun part1(input: List<String>): Int {
        val (ranges, items) = splitInput(input)

        return items.count { item -> ranges.any { range -> item in range }.also { if (it) println(item) } }
    }

    fun List<LongRange>.mergeOverlapping(): List<LongRange> {
        val sorted = sortedBy { it.first }

        val merged = mutableListOf<LongRange>()
        var current = sorted.first()
        for (next in sorted.drop(1)) {
            if (next.first <= current.last + 1) {
                current = current.first..maxOf(current.last, next.last)
            } else {
                merged.add(current)
                current = next
            }
        }

        merged.add(current)
        return merged
    }

    fun List<LongRange>.mergeOverlapping2(): List<LongRange> {
        val sorted = sortedBy { it.first }

        var current = sorted.first()
        return sorted.drop(1).mapNotNull { next ->
            if (next.first <= current.last + 1) {
                current = current.first..maxOf(current.last, next.last)
                null
            } else {
                val finalisedRange = current
                current = next
                finalisedRange
            }
        } + listOf(current)
    }

    fun List<LongRange>.mergeOverlapping3(): List<LongRange> = buildList {
        val sorted = this@mergeOverlapping3.sortedBy { it.first }

        var current = sorted.first()
        for (next in sorted.drop(1)) {
            if (next.first <= current.last + 1) {
                current = current.first..maxOf(current.last, next.last)
            } else {
                add(current)
                current = next
            }
        }
        add(current)
    }

    fun part2(input: List<String>): Long {
        val (ranges, _) = splitInput(input)

        return ranges.mergeOverlapping3().sumOf { it.last - it.first + 1 }
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    // 8.507458ms
    val testAnswerPart1 = part1(testInput)
    check(testAnswerPart1 == 3, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input)) } // 789

    // 731.375us
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 14L, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 343329651880509
}
