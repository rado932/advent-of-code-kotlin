package y2021

fun main() {
    fun simulateDays(days: Int, fishiesPerDay: LongArray): Long {
        repeat(days) {
            fishiesPerDay.rotateLeftInPlace()
            fishiesPerDay[6] += fishiesPerDay[8]
        }
        return fishiesPerDay.sum()
    }

    fun part1(fishiesPerDay: LongArray) = simulateDays(80, fishiesPerDay)

    fun part2(fishiesPerDay: LongArray) = simulateDays(256, fishiesPerDay)

    val testInput = parseInput(readInput("Day06_test").first())
    checkPart(5934L, part1(testInput.copyOf()), "Part1 Example")
    checkPart(26984457539L, part2(testInput.copyOf()), "Part2 Example")

    val input = parseInput(readInput("Day06").first())
    println(part1(input.copyOf()))
    println(part2(input.copyOf()))
}

// solution from https://todd.ginsberg.com/post/advent-of-code/2021/day6/
private fun parseInput(input: String): LongArray =
    LongArray(9).apply {
        input.split(",")
            .map { it.toInt() }
            .forEach { this[it] += 1L }
    }

private fun LongArray.rotateLeftInPlace() {
    val leftMost = first()
    this.copyInto(this, startIndex = 1)
    this[this.lastIndex] = leftMost
}

// fish 1 TTB(day) - 4(0), 6(4), 6(11), 6(18)
// fish 1.1 TTB(day) - 8(4), 6(13), 6(20)
private fun bruteForce(currentDay: Int, timeToBirth: Int, totalDays: Int): Long {
    var output = 1L

    var timeOfNextBirth = currentDay + timeToBirth
    while (timeOfNextBirth <= totalDays) {
        output += bruteForce(timeOfNextBirth, 9, totalDays)
        timeOfNextBirth += 7
    }

    return output
}