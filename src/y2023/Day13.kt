package y2023

import Timer.time
import java.io.File

private const val inputPrefix = "src/y2023/Day13"

private class MirrorMap(val mirrorMap: List<String>) {
    val rows by lazy {
        mirrorMap.map { it.toCharArray() }
    }

    val columns by lazy {
        mirrorMap[0].indices.map { col ->
            mirrorMap.map { it[col] }.toCharArray()
        }
    }

    fun findMirrorRows(): List<Int> = findMirrors(rows)
    fun findMirrorColumns(): List<Int> = findMirrors(columns)

    // Each list entry is the first column of a mirror pair
    private fun findMirrors(charArrs: List<CharArray>): List<Int> =
        charArrs.windowed(2).mapIndexedNotNull { index, (a, b) ->
            if (a.contentEquals(b)) index
            else null
        }

    fun rowIsMirrorPatternStart(row: Int): Boolean = isMirrorPatternStart(rows, row)
    fun columnIsMirrorPatternStart(column: Int): Boolean = isMirrorPatternStart(columns, column)

    // assuming row and row+1 are mirrors
    private fun isMirrorPatternStart(charArrs: List<CharArray>, index: Int): Boolean {
        var leftRow = index - 1
        var rightRow = index + 2

        while (leftRow >= 0 && rightRow < charArrs.size) {
            if (!charArrs[leftRow].contentEquals(charArrs[rightRow])) return false
            leftRow -= 1
            rightRow += 1
        }

        return true
    }
}

private fun List<String>.toMirrorMaps(): List<MirrorMap> =
    map { MirrorMap(it.split("\n")) }

fun main() {

    fun part1(input: List<MirrorMap>): Int = input.sumOf { mirrorMap ->
        mirrorMap.findMirrorRows()
            .find { mirrorMap.rowIsMirrorPatternStart(it) }?.let {
                return@sumOf (it + 1) * 100
            }

        mirrorMap.findMirrorColumns()
            .find { mirrorMap.columnIsMirrorPatternStart(it) }?.let {
                return@sumOf it + 1
            }

        throw Exception("Not Found for ${mirrorMap.mirrorMap}")
    }

    fun part2(input: List<String>): Int = 0

    val testInput: List<MirrorMap> = File("${inputPrefix}_test.txt").readText()
        .split("\n\n")
        .toMirrorMaps()

    val input = File("$inputPrefix.txt").readText()
        .split("\n\n")
        .toMirrorMaps()

    check(part1(testInput) == 405)
    time { println(part1(input)) }

//    check(part2(testInput) == 400)
//    time { println(part2(input)) }
}
