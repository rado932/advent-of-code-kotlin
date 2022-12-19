package y2022

import java.io.File
import java.util.*

private const val inputPrefix = "src/y2022/Day11"

typealias ToMonkey = Int

fun main() {

    class Monkey(
        private val items: PriorityQueue<Int>,
        private val inspectAnItem: (Int) -> Int, // worry -> new worry
        private val testAnItem: (Int) -> ToMonkey // worry -> monkey
    ) {
        val inspectedItems
            get() = _inspectedItems
        private var _inspectedItems = 0

        fun add(worry: Int) = items.add(worry)

        fun hasItems() = items.isNotEmpty()

        fun processNextItem(): Pair<ToMonkey, Int> {
            _inspectedItems += 1

            val item = items.poll()

            var worryLevel = inspectAnItem(item)
            worryLevel = getBored(worryLevel)

            return testAnItem(worryLevel) to worryLevel
        }

        private fun getBored(worryLevel: Int) = worryLevel / 3
    }

    fun part1(monkeys: List<Monkey>): Int {
        repeat(20) {
            monkeys.forEach {
                while(it.hasItems()) {
                    val (toMonkey, worry) = it.processNextItem()
                    monkeys[toMonkey].add(worry)
                }
            }
        }

        return monkeys.map { it.inspectedItems }
            .sortedDescending()
            .take(2)
            .fold(1) { acc, inspectedItems -> acc * inspectedItems }
    }

    fun part2(input: List<Monkey>): Int {
        return 0
    }

    fun String.parseInput(): List<Monkey> = split("Monkey ").filter { it.isNotEmpty() }
        .map {
            val lines = it.lines()

            val items = lines[1].substringAfter("Starting items: ").split(", ").map { it.toInt() }

            val (operationStr, operationNumStr) = lines[2].substringAfter("Operation: new = old ").split(" ")
            val operationNum = operationNumStr.toIntOrNull()
            val operation: (Int) -> Int =
                if (operationNum == null) {
                    if (operationNumStr == "old") { { num -> num * num } } // todo I only have `old * old` in my inputs
                    else error("Unknown operation num $operationNum")
                }
                else when(operationStr) {
                    "+" -> { { num -> num + operationNum} }
                    "-" -> { { num -> num - operationNum} }
                    "*" -> { { num -> num * operationNum} }
                    "/" -> { { num -> num / operationNum} }
                    else -> error("Unknown operation $operationStr")
                }

            val testNumber = lines[3].substringAfter("Test: divisible by ").toInt()
            val testTrueOutcome = lines[4].substringAfter("If true: throw to monkey ").toInt()
            val testFalseOutcome = lines[5].substringAfter("If false: throw to monkey ").toInt()
            val test: (Int) -> Int = { num -> if (num % testNumber == 0) testTrueOutcome else testFalseOutcome }

            Monkey(
                PriorityQueue<Int>(items),
                operation,
                test
            )
        }

    val testInput = File("${inputPrefix}_test.txt").reader().readText().parseInput()

    val input = File("$inputPrefix.txt").reader().readText().parseInput()

    check(part1(testInput) == 10605)
    println(part1(input))

//    check(part2(testInput) == 0)
//    println(part2(input))
}
