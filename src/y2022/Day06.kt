package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day06"

fun main() {

    fun String.indexOfFirstUniqueN(n: Int): Int {
        for (index in n - 1 until length) {
            val foundElements = mutableSetOf<Char>()
            for (j in index - n + 1..index) {
                val current = get(j)
                if (foundElements.contains(current)) break
                foundElements.add(get(j))
            }

            if (foundElements.size == n) return index + 1
        }

        return 0
    }

    fun part1(input: String): Int = input.indexOfFirstUniqueN(4)

    fun part2(input: String): Int = input.indexOfFirstUniqueN(14)

    val testInput = File("${inputPrefix}_test.txt").readLines().first()

    val input = File("$inputPrefix.txt").readLines().first()

    check(part1(testInput) == 7)
    check(part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(part1("nppdvjthqldpwncqszvftbrmjlhg") == 6)
    check(part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10)
    check(part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11)
    println(part1(input))

    check(part2(testInput) == 19)
    check(part2("bvwbjplbgvbhsrlpgdmjqwftvncz") == 23)
    check(part2("nppdvjthqldpwncqszvftbrmjlhg") == 23)
    check(part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 29)
    check(part2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 26)
    println(part2(input))
}
