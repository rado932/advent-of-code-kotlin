package y2023

import Timer.time
import java.io.File

private const val inputPrefix = "src/y2023/Day13"

private class MirrorMap(val mirrorStr: String) {
    val mirrorMap = mirrorStr.split("\n")

    val rows by lazy {
        mirrorMap.map { it.toCharArray() }
    }

    val columns by lazy {
        mirrorMap[0].indices.map { col ->
            mirrorMap.map { it[col] }.toCharArray()
        }
    }

    fun findMirrorForPermutations(): Int {
        val excludeMirror = findMirror()!!

        return mirrorStr.indices.firstNotNullOf { indexToChange ->
            getPermutation(indexToChange)?.findMirror(excludeMirror)?.let {
                if (it == excludeMirror) null
                else it
            }
        }
    }

    fun getPermutation(index: Int): MirrorMap? {
        if (mirrorStr[index] == '\n') return null

        val mirrorStrBuilder = StringBuilder(mirrorStr)
        if (mirrorStr[index] == '.') mirrorStrBuilder.setCharAt(index, '#')
        else mirrorStrBuilder.setCharAt(index, '.')
        return MirrorMap(mirrorStrBuilder.toString())
    }

    fun findMirror(exclude: Int? = null): Int? {
        var excludeIndex: Int? = if (exclude != null && exclude % 100 == 0) (exclude / 100) - 1 else null
        findMirrorRows(excludeIndex)
            .find { rowIsMirrorPatternStart(it) }?.let {
                return (it + 1) * 100
            }

        excludeIndex = if (exclude != null && exclude % 100 != 0) exclude - 1 else null
        findMirrorColumns(excludeIndex)
            .find { columnIsMirrorPatternStart(it) }?.let {
                return it + 1
            }

        return null
    }

    fun findMirrorRows(excludeIndex: Int? = null): List<Int> = findMirrors(rows, excludeIndex)
    fun findMirrorColumns(excludeIndex: Int? = null): List<Int> = findMirrors(columns, excludeIndex)

    // Each list entry is the first column of a mirror pair
    private fun findMirrors(charArrs: List<CharArray>, excludeIndex: Int?): List<Int> =
        charArrs.windowed(2).mapIndexedNotNull { index, (a, b) ->
            if (index == excludeIndex) null
            else if (a.contentEquals(b)) index
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

private fun List<String>.toMirrorMaps(): List<MirrorMap> = map { MirrorMap(it) }

fun main() {

    fun part1(input: List<MirrorMap>): Int = input.sumOf { mirrorMap ->
        mirrorMap.findMirror() ?: throw Exception("Not Found for ${mirrorMap.mirrorMap}")
    }

    fun part2(input: List<MirrorMap>): Int = input.sumOf {
        it.findMirrorForPermutations()
    }

    val testInput: List<MirrorMap> = File("${inputPrefix}_test.txt").readText()
        .split("\n\n")
        .toMirrorMaps()

    val input = File("$inputPrefix.txt").readText()
        .split("\n\n")
        .toMirrorMaps()

    check(part1(testInput) == 405)
    time { println(part1(input)) }

    check(part2(testInput) == 400)
    time { println(part2(input)) }
}
