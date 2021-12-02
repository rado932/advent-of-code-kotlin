fun main() {
    fun part1(input: List<String>): Int {
        var horizontal = 0
        var depth = 0

        input.map { it.split(" ") }
            .forEach {
                when(it[0]) {
                    "forward" -> horizontal += it[1].toInt()
                    "down" -> depth += it[1].toInt()
                    "up" -> depth -= it[1].toInt()
                }
            }

        return horizontal * depth
    }

    fun part2(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0

        input.map { it.split(" ") }
            .forEach {
            when(it[0]) {
                "forward" -> {
                    horizontal += it[1].toInt()
                    depth += it[1].toInt() * aim
                }
                "down" -> aim += it[1].toInt()
                "up" -> aim -= it[1].toInt()
            }
        }

        return horizontal * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
