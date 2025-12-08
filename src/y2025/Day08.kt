import Timer.time
import java.io.File
import java.util.PriorityQueue
import kotlin.math.sqrt

private const val inputPrefix = "src/y2025/Day08"

fun main() {

    class OrderedQueue<T, R : Comparable<R>>(private val maxSize: Int,
                                             private val propertySelector: (T) -> R) {
        private val queue = PriorityQueue<T>(compareByDescending(propertySelector))

        fun add(element: T) {
            if (queue.size < maxSize) {
                queue += element
            } else {
                val head = queue.peek() ?: return

                if (propertySelector(element) < propertySelector(head)) {
                    queue.poll()
                    queue += element
                }
            }
        }

        fun toSortedList(): List<T> = queue.toList().sortedBy(propertySelector)
    }

    fun calculateDistance3D(p1: List<Double>, p2: List<Double>): Double {
        val dx = p2[0] - p1[0]
        val dy = p2[1] - p1[1]
        val dz = p2[2] - p1[2]

        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun calculateConnections(input: List<String>, maxConnections: Int): List<Pair<String, String>> {
        val parsedCoords = input.map { it.split(",").map { coord -> coord.toDouble() } }

        val queue = OrderedQueue<Pair<Double, Pair<String, String>>, Double>(maxConnections) { it.first }
        for (i in parsedCoords.indices) {
            val p1 = parsedCoords[i]
            for (j in i + 1 until parsedCoords.size) {
                val p2 = parsedCoords[j]
                val distance = calculateDistance3D(p1, p2)
                queue.add(distance to (input[i] to input[j]))
            }
        }

        return queue.toSortedList().map { it.second }
    }

    fun calculateLinkedConnections(connections: List<Pair<String, String>>): MutableList<MutableSet<String>> {
        val nodeToSet = mutableMapOf<String, MutableSet<String>>()
        val linkedConnections = mutableListOf<MutableSet<String>>()

        for ((first, second) in connections) {
            val set1 = nodeToSet[first]
            val set2 = nodeToSet[second]

            when {
                set1 == null && set2 == null -> {
                    val newSet = mutableSetOf(first, second)
                    linkedConnections += newSet
                    nodeToSet[first] = newSet
                    nodeToSet[second] = newSet
                }

                set1 != null && set2 == null -> {
                    set1 += second
                    nodeToSet[second] = set1
                }

                set1 == null && set2 != null -> {
                    set2 += first
                    nodeToSet[first] = set2
                }

                set1 !== set2 -> {
                    val (small, big) = if (set1!!.size < set2!!.size) set1 to set2 else set2 to set1

                    for (node in small) {
                        big += node
                        nodeToSet[node] = big
                    }
                    linkedConnections.remove(small)
                }
            }
        }

        linkedConnections.sortByDescending { it.size }
        return linkedConnections
    }

    fun part1(input: List<String>, maxConnections: Int): Int {
        val connections = calculateConnections(input, maxConnections)
        val linkedConnections = calculateLinkedConnections(connections)

        return linkedConnections
            .take(3)
            .fold(1) { acc, set -> acc * set.size }
    }

    fun part2(input: List<String>): Long {
        val parsedCoords = input.map { it.split(",").map { coord -> coord.toDouble() } }

        val arr = mutableListOf<Pair<Double, Pair<Int, Int>>>()
        (0..<parsedCoords.size).forEach { i ->
            val p1 = parsedCoords[i]

            (i + 1..<parsedCoords.size).forEach { j ->
                val p2 = parsedCoords[j]

                val distance = calculateDistance3D(p1, p2)
                arr.add(distance to (i to j))
            }
        }
        val sortedConnections = arr.sortedBy { it.first }

        val allElementsSize = input.size
        val seen = mutableSetOf<Int>()

        var result = 0L
        for ((_, pair) in sortedConnections) {
            seen += pair.first
            seen += pair.second
            if (seen.size == allElementsSize) {
                val first = parsedCoords[pair.first][0].toLong()
                val second = parsedCoords[pair.second][0].toLong()
                result = first * second
                break
            }
        }

        return result
    }

    fun part2Reuse(input: List<String>): Long {
        val sortedConnections = calculateConnections(input, Int.MAX_VALUE)

        val allElementsSize = input.size
        val seen = mutableSetOf<String>()

//        var lastPair: Pair<String, String> = "" to ""
        var result = 0L
        for (pair in sortedConnections) {
            seen += pair.first
            seen += pair.second
            if (seen.size == allElementsSize) {
                val a = pair.first.substringBefore(",").toLong()
                val b = pair.second.substringBefore(",").toLong()
                result = a * b
                break
            }
        }

        return result
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    // 591.836ms | PriorityQueue 125.791333ms | opt 33.652959ms
    val testAnswerPart1 = part1(testInput, 10)
    check(testAnswerPart1 == 40, { "part 1 failed: $testAnswerPart1" })
    time { println(part1(input, 1000)) } // 175440

    // 207.655458ms | opt 138.681834ms | part2Reuse 200ms
    val testAnswerPart2 = part2(testInput)
    check(testAnswerPart2 == 25272L, { "part 2 failed: $testAnswerPart2" })
    time { println(part2(input)) } // 3200955921
}
