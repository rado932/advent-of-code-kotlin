package y2022

import java.io.File
import java.util.PriorityQueue

fun main() {
    fun parseInput(input: String) = input.split("\n\n").map { elf ->
        elf.lines().sumOf { it.toInt() }
    }

    fun List<Int>.sumFirstMaxN(n: Int): Int {
        val queue = PriorityQueue<Int>()
        for (cal in this) {
            queue.add(cal)
            if (queue.size > n) {
                queue.poll()
            }
        }
        return queue.sum()
    }

    fun part1(input: String): Int = parseInput(input).sumFirstMaxN(1)

    fun part2(input: String): Int = parseInput(input).sumFirstMaxN(3)

    val testInput = File("src/y2022/Day01_test.txt").reader().readText()
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = File("src/y2022/Day01.txt").reader().readText()
    println(part1(input))
    println(part2(input))
}
