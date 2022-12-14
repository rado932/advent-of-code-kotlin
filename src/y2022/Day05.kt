package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day05"

fun main() {
    class Command private constructor(val quantity: Int, val fromStack: Int, val toStack: Int) {
        constructor(strs: List<String>) : this(strs[1].toInt(), (strs[3].toInt() - 1), (strs[5].toInt() - 1))
    }

    fun parseInput(input: String) =
        input
            .split("\n\n")
            .take(2)
            .let { (first, second) ->
                first.lines().dropLast(1) to
                    second.lines().map { command -> Command(command.split(' ')) }
            }

    // todo optimize
    fun initialStateToState(initialState: List<String>) = (0..initialState[0].length / 4).map { index ->
        initialState
            .map {
                it[(index * 4) + 1]
            }
            .filterNot { it == ' ' }
            .toMutableList()
    }.toMutableList()

    fun List<List<Char>>.firstElementsAsString(): String {
        var output = ""
        forEach {
            output += it.firstOrNull() ?: ""
        }
        return output
    }

    fun part1(input: String): String {
        val (initialState, commands) = parseInput(input)
        val state = initialStateToState(initialState)

        commands.forEach { command ->
            repeat((1..command.quantity).count()) {
                state[command.toStack].add(0, state[command.fromStack].removeFirst())
            }
        }

        return state.firstElementsAsString()
    }

    fun part2(input: String): String {
        val (initialState, commands) = parseInput(input)
        val state = initialStateToState(initialState)

        commands.forEach { command ->
            repeat((1..command.quantity).count()) { index ->
                state[command.toStack].add(index, state[command.fromStack].removeFirst())
            }
        }

        return state.firstElementsAsString()
    }

    val testInput = File("${inputPrefix}_test.txt").reader().readText()

    val input = File("$inputPrefix.txt").reader().readText()

    check(part1(testInput) == "CMZ")
    println(part1(input))

    check(part2(testInput) == "MCD")
    println(part2(input))
}

