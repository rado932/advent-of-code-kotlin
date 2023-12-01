package y2023

import java.io.File

fun main() {
    fun parseInput(input: String) = input.split("\n")

    fun getNumber(string: String): Int {
        val numbers = "\\d".toRegex().findAll(string)
        return "${numbers.first().value}${numbers.last().value}".toInt()
    }

    fun part1(input: List<String>): Int = input.sumOf {
        val numbers = "\\d".toRegex().findAll(it)
        "${numbers.first().value}${numbers.last().value}".toInt()
    }

    fun part2(input: List<String>): Int = input.sumOf {
        getNumber(
            it.replace("zero", "zero0zero")
                .replace("one", "one1one")
                .replace("two", "two2two")
                .replace("three", "three3three")
                .replace("four", "four4four")
                .replace("five", "five5five")
                .replace("six", "six6six")
                .replace("seven", "seven7seven")
                .replace("eight", "eight8eight")
                .replace("nine", "nine9nine")
        )

    }

    val testInput1 = File("src/y2023/Day01_test.txt").reader().readText()
    check(part1(parseInput(testInput1)) == 142) // 12, 38, 15, and 77
    val testInput2 = File("src/y2023/Day01_test_2.txt").reader().readText()
    check(part2(parseInput(testInput2)) == 281) // 29, 83, 13, 24, 42, 14, and 76

    val input = File("src/y2023/Day01.txt").reader().readText()
    println(part1(parseInput(input)))
    println(part2(parseInput(input)))
}
