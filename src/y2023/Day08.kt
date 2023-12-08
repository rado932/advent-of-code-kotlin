package y2023

import java.io.File
import Timer.time
import java.lang.Exception
import kotlin.math.abs

private const val inputPrefix = "src/y2023/Day08"

private class Maps(val instructions: String,
                   val map: Map<String, Pair<String, String>>)

fun main() {

    fun String.asRepeatingSequence() =
        sequence { while (true) yieldAll(asSequence()) }

    fun countStepsFor(input: Maps, startingIndex: String, until: (String) -> Boolean): Long = with(input) {
        instructions.asRepeatingSequence()
            .fold(startingIndex to 0L) { (index, count), cmd ->
                val row = map[index]!!
                when {
                    until(index) -> return count
                    cmd == 'L' -> row.first
                    else -> row.second
                } to count + 1
            }.second
    }

    tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun calculateLCM(a: Long, b: Long): Long {
        require(a != 0L || b != 0L) { "Both numbers cannot be zero." }
        return if (a == 0L || b == 0L) 0 else abs(a * b) / gcd(a, b)
    }

    fun part1(input: Maps): Long = countStepsFor(input, "AAA") { it == "ZZZ" }

    fun part2(input: Maps): Long = with(input) {
        map.filterKeys { it.last() == 'A' }
            .map { (index, _) ->
                countStepsFor(input, index) { it.last() == 'Z' }
            }.reduce { a, b -> calculateLCM(a, b) }
    }

    fun File.parseInput(): Maps {
        val regex = """(\w+) = \((\w+), (\w+)\)""".toRegex()

        val text = readText()
        val (instructions, mapStr) = text.split("\n\n")
        val map = mapStr.split("\n").map {
            val (index, left, right) = regex.find(it)!!.destructured
            index to (left to right)
        }.toMap()
        return Maps(instructions, map)
    }

    val testInput1 = File("${inputPrefix}_test.txt").parseInput()
    val testInput2 = File("${inputPrefix}_test2.txt").parseInput()

    val input = File("$inputPrefix.txt").parseInput()

    check(part1(testInput1) == 6L)
    time { println(part1(input)) }

    check(part2(testInput2) == 6L)
    time { println(part2(input)) }
}
