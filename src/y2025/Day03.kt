import java.io.File
import Timer.time
import java.time.Instant
import kotlin.math.pow

private const val inputPrefix = "src/y2025/Day03"

fun main() {

    @Deprecated("Use part1")
    fun part1Initial(input: List<String>): Int = input.map { bank ->
        val indexed = bank.withIndex()

        val bankWithoutLast = indexed.take(bank.length - 1)
        val maxElement = bankWithoutLast.maxByOrNull { it.value.code }!! // todo stop if you find 9!

        val nextBiggest = indexed.drop(maxElement.index + 1).maxByOrNull { it.value.code }!!

        "${maxElement.value}${nextBiggest.value}"
    }.sumOf { it.toInt() }

    @Deprecated("Use findMaxInStr")
    fun findMaxIn(indexed: Iterable<IndexedValue<Char>>, range: IntRange): IndexedValue<Char> =
        indexed.maxByOrNull { (i, value) ->
            if (i in range) value.code
            else -1
        }!!

    @Deprecated("Use findMaxInStrRecursive")
    fun findMaxInRecursive(indexed: Iterable<IndexedValue<Char>>, start: Int, n: Int): String {
        if (n <= 0) return ""

        val maxElement = findMaxIn(indexed, start..indexed.count() - n)
        return "${maxElement.value}${findMaxInRecursive(indexed, maxElement.index + 1, n - 1)}"
    }

    @Deprecated("Use part2")
    fun part2Initial(input: List<String>): Long = input.sumOf { bank ->
        val indexed = bank.withIndex()
        findMaxInRecursive(indexed, 0, 12).toLong()
    }

    fun findMaxInStr(bank: String, range: IntRange): Pair<Int, Char> =
        bank.substring(range).foldIndexed(-1 to '0') { index, acc, c ->
//            if (c == '9') return@foldIndexed index + range.first to c // nineCheck - results in wrong outputs
            if (c.code > acc.second.code) index + range.first to c else acc
        }

    fun findMaxInStrRecursive(bank: String, length: Int, start: Int, n: Int): String {
        if (n <= 0) return ""

        val end = length - n
        if (start == end) return bank.substring(start) // shortCircuit

        val (maxIndex, maxElement) = findMaxInStr(bank, start..end)
        return "${maxElement}${findMaxInStrRecursive(bank, length, maxIndex + 1, n - 1)}"
    }

    fun part1(input: List<String>): Int = input.sumOf { bank ->
        findMaxInStrRecursive(bank, bank.length, 0, 2).toInt()
    }

    fun part2(input: List<String>): Long = input.sumOf { bank ->
        findMaxInStrRecursive(bank, bank.length, 0, 12).toLong()
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    //4.940584ms
    val testAnswerPart1 = part1(testInput)
    check(testAnswerPart1 == 357, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input)) } // 17155

    // Iterable 10.941833ms | Str 1.265ms | str + shortC 1.171ms
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 3121910778619L, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 169685670469164
}
