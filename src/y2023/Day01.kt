package y2023

import java.io.File

fun main() {
    fun parseInput(input: String) = input.split("\n")

    fun getNumber(string: String): Int {
        val numbers = "\\d".toRegex().findAll(string)
        return "${numbers.first().value}${numbers.last().value}".toInt()
    }

    fun String.toNumber(): String = when (this) {
        "zero" -> "0"
        "one" -> "1"
        "two" -> "2"
        "three" -> "3"
        "four" -> "4"
        "five" -> "5"
        "six" -> "6"
        "seven" -> "7"
        "eight" -> "8"
        "nine" -> "9"
        else -> this
    }

    fun getNumber2(string:String): Int {
        val matches = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "zero")
        val findFirst = string.findAnyOf(matches)
        val findLast = string.findLastAnyOf(matches)
        return "${findFirst?.second?.toNumber()}${findLast?.second?.toNumber()}".toInt()
    }

    fun part1(input: List<String>): Int = input.sumOf { getNumber(it) }

    fun part2(input: List<String>): Int = input.sumOf {
        getNumber2(it)
//        getNumber(
//            it.replace("zero", "0o")
//                .replace("one", "o1e")
//                .replace("two", "t2o")
//                .replace("three", "t3e")
//                .replace("four", "4")
//                .replace("five", "5e")
//                .replace("six", "6")
//                .replace("seven", "7n")
//                .replace("eight", "e8t")
//                .replace("nine", "n9e")
//        )
    }

    val testInput1 = File("src/y2023/Day01_test.txt").reader().readText()
    check(part1(parseInput(testInput1)) == 142) // 12, 38, 15, and 77
    val testInput2 = File("src/y2023/Day01_test_2.txt").reader().readText()
    check(part2(parseInput(testInput2)) == 281) // 29, 83, 13, 24, 42, 14, and 76

    val input = File("src/y2023/Day01.txt").reader().readText()
    println(part1(parseInput(input)))
    println(part2(parseInput(input)))
}
