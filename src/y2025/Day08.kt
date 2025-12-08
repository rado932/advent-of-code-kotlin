import Timer.time
import java.io.File
import java.util.PriorityQueue
import kotlin.math.sqrt

private const val inputPrefix = "src/y2025/Day08"

fun main() {

    class MaxPropertyBoundedList<T, R : Comparable<R>>(
        private val maxSize: Int,
        private val propertySelector: (T) -> R
    ) {
        private val queue = PriorityQueue<T>(compareByDescending { propertySelector(it) })

        fun add(element: T) {
            queue.add(element)

            if (queue.size > maxSize) {
                // Find element with minimum property value and remove it
                val toRemove = queue.maxByOrNull { propertySelector(it) }
                queue.remove(toRemove)
            }
        }

        fun getAll(): List<T> = queue.toList()
    }

    fun calculateDistance3D(p1: List<Double>, p2: List<Double>): Double {
        val dx = p2[0] - p1[0]
        val dy = p2[1] - p1[1]
        val dz = p2[2] - p1[2]

        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun part1(input: List<String>, connections: Int): Int {
        val arr = MaxPropertyBoundedList<Pair<Double, Pair<String, String>>, Double>(connections) { it.first }

        (0..<input.size).forEach { i ->
            val p1 = input[i].split(",").map { it.toDouble() }
            (i + 1..<input.size).forEach { j ->
                val p2 = input[j].split(",").map { it.toDouble() }

                val distance = calculateDistance3D(p1, p2)
                arr.add(distance to (input[i] to input[j]))
            }
        }

        val connections = arr.getAll().map { it.second }
        val result = mutableListOf<MutableSet<String>>()
        val seen = mutableSetOf<String>()

        connections.forEach { (first, second) ->
            if (seen.contains(first) && seen.contains(second)) {
                val firstSet = result.find { it.contains(first) }!!
                val secondSet = result.find { it.contains(second) }!!

                if (firstSet != secondSet) {
                    result.remove(secondSet)
                    firstSet += secondSet
                }
            } else if (seen.contains(first)) {
                val set = result.find { it.contains(first) }!!
                set += second
            } else if (seen.contains(second)) {
                val set = result.find { it.contains(second) }!!
                set += first
            } else {
                result.add(mutableSetOf(first, second))
            }

            seen += first
            seen += second
        }

        result.sortByDescending { it.size }
        return result.take(3).fold(1) { acc, set -> acc * set.size }
    }

    fun part2(input: List<String>): Long {
        val arr = mutableListOf<Pair<Double, Pair<String, String>>>()

        (0..<input.size).forEach { i ->
            val p1 = input[i].split(",").map { it.toDouble() }

            (i + 1..<input.size).forEach { j ->
                val p2 = input[j].split(",").map { it.toDouble() }

                val distance = calculateDistance3D(p1, p2)
                arr.add(distance to (input[i] to input[j]))
            }
        }

        val allElementsSize = input.size
        val seen = mutableSetOf<String>()
        val sortedConnections = arr.sortedBy { it.first }

        var lastPair: Pair<String, String> = "" to ""
        for ((_, pair) in sortedConnections) {
            seen += pair.first
            seen += pair.second
            if (seen.size == allElementsSize) {
                lastPair = pair
                break
            }
        }
        return lastPair.first.split(",")[0].toLong() * lastPair.second.split(",")[0].toLong()
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    // 591.836ms
    val testAnswerPart1 = part1(testInput, 10)
    check(testAnswerPart1 == 40, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input, 1000)) } // 175440

    // 217.999625ms
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 25272L, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 3200955921
}
