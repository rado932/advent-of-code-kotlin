package y2022

import java.io.File
import java.util.*

private const val inputPrefix = "src/y2022/Day11"

typealias ToMonkey = Int

fun main() {

    class Monkey(
        private val items: PriorityQueue<Long>,
        private val inspectAnItem: (Long) -> Long,
        val testNumber: Long,
        private val testAnItem: (Long, Long) -> ToMonkey // worry -> monkey
    ) {
        val inspectedItems
            get() = _inspectedItems
        private var _inspectedItems = 0L

        fun add(worry: Long) = items.add(worry)

        fun hasItems() = items.isNotEmpty()

        fun processNextItem(getBored: (Long) -> Long): Pair<ToMonkey, Long> {
            _inspectedItems += 1

            val item = items.poll()

            var worryLevel = inspectAnItem(item)
            worryLevel = getBored(worryLevel)

            return testAnItem(worryLevel, testNumber) to worryLevel
        }
    }

    fun part1(monkeys: List<Monkey>): Long {
        val getBored: (Long) -> Long = { worry -> worry / 3 }

        repeat(20) {
            monkeys.forEach {
                while(it.hasItems()) {
                    val (toMonkey, worry) = it.processNextItem(getBored)
                    monkeys[toMonkey].add(worry)
                }
            }
        }

        return monkeys.map { it.inspectedItems }
            .sortedDescending()
            .take(2)
            .fold(1L) { acc, inspectedItems -> acc * inspectedItems }
    }

    fun part2(monkeys: List<Monkey>): Long {
        val divisor = monkeys.fold(1L) { acc, monkey -> acc * monkey.testNumber }
        val getBored: (Long) -> Long = { worry -> worry % divisor }

        repeat(10_000) {
            monkeys.forEach {
                while(it.hasItems()) {
                    val (toMonkey, worry) = it.processNextItem(getBored)
                    monkeys[toMonkey].add(worry)
                }
            }
        }

        return monkeys.map { it.inspectedItems }
            .sortedDescending()
            .take(2)
            .fold(1L) { acc, inspectedItems -> acc * inspectedItems }
    }

    fun String.parseInput(): List<Monkey> = split("Monkey ").filter { it.isNotEmpty() }
        .map {
            val lines = it.lines()

            val items = lines[1].substringAfter("Starting items: ").split(", ").map { it.toLong() }

            val (operationStr, operationNumStr) = lines[2].substringAfter("Operation: new = old ").split(" ")
            val operationNum = operationNumStr.toIntOrNull()
            val operation: (Long) -> Long =
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

            val testNumber = lines[3].substringAfter("Test: divisible by ").toLong()
            val testTrueOutcome = lines[4].substringAfter("If true: throw to monkey ").toInt()
            val testFalseOutcome = lines[5].substringAfter("If false: throw to monkey ").toInt()
            val test: (Long, Long) -> ToMonkey = { num, testNumber -> if (num % testNumber == 0L) testTrueOutcome else testFalseOutcome }

            Monkey(
                PriorityQueue(items),
                operation,
                testNumber,
                test
            )
        }

    fun testInput() = File("${inputPrefix}_test.txt").reader().readText().parseInput()
    fun input() = File("$inputPrefix.txt").reader().readText().parseInput()

    check(part1(testInput()) == 10605L)
    println(part1(input()))

    check(part2(testInput()) == 2713310158L)
    println(part2(input()))
}
