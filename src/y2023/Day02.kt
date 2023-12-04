package y2023

import java.io.File

private const val inputPrefix = "src/y2023/Day02"

fun main() {

    val maxCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)

    fun parseInput(string: String): Pair<Int, List<String>> {
        val (name, game) = string.split(":")
        val gameNumber = name.split("Game ")[1].toInt()

        return gameNumber to game.split(";").flatMap { it.split(",") }
    }

    fun isValid(cube: String): Boolean {
        val (_, number, color) = cube.split(" ")
        val maxCubesPerColor = maxCubes[color]!!
        return maxCubesPerColor >= number.toInt()
    }

    fun getPowerMap() = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)

    fun part1(input: List<String>): Int = input.sumOf {
        val (gameNumber, cubes) = parseInput(it)

        cubes.find { !isValid(it) } ?: return@sumOf gameNumber
        return@sumOf 0
    }

    fun part2(input: List<String>): Int = input.sumOf {
        val (gameNumber, cubes) = parseInput(it)

        val powerMap = getPowerMap()

        cubes.forEach { cube ->
            val (_, number, color) = cube.split(" ")
            number.toInt().let {
                if (it > powerMap[color]!!) {
                    powerMap[color] = it
                }
            }
        }
        return@sumOf powerMap["red"]!! * powerMap["green"]!! * powerMap["blue"]!!
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 8)
    println(part1(input))

    check(part2(testInput) == 2286)
    println(part2(input))
}
