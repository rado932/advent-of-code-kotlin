package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day08"

fun main() {

    class Tree(val height: Int) {
        private var _isVisible: Boolean = false
        val isVisible: Boolean
            get() = _isVisible

        private var _scenicScore: Int = 0
        val scenicScore: Int
            get() = _scenicScore

        fun markAsVisible() {
            _isVisible = true
        }
    }

    fun part1(input: List<MutableList<Tree>>): Int {
        fun checkVisibility(
            range: IntProgression,
            maxHeight: Int = 9,
            getElement: (Int) -> Tree,
        ) {
            var currentMaxHeight = 0
            for (j in range) {
                val currentTree = getElement(j)

                if (currentTree.height > currentMaxHeight) {
                    currentMaxHeight = currentTree.height
                    currentTree.markAsVisible()
                    if (currentMaxHeight == maxHeight) break
                }
            }
        }

        val rowsLastIndex = input[0].lastIndex
        val columnsLastIndex = input.lastIndex

        for (i in 0..columnsLastIndex) {
            input[i][0].markAsVisible()
            input[i][rowsLastIndex].markAsVisible()

            if (i != 0 && i != columnsLastIndex) {
                checkVisibility(0..rowsLastIndex) { j ->
                    input[i][j]
                }
                checkVisibility(rowsLastIndex downTo 0) { j ->
                    input[i][j]
                }
            }
        }

        for (i in 0..rowsLastIndex) {
            input[0][i].markAsVisible()
            input[columnsLastIndex][i].markAsVisible()

            if (i != 0 && i != rowsLastIndex) {
                checkVisibility(0..columnsLastIndex) { j ->
                    input[j][i]
                }
                checkVisibility(columnsLastIndex downTo 0) { j ->
                    input[j][i]
                }
            }
        }

        return input.sumOf { it.count { it.isVisible } }
    }

    fun part2(input: List<MutableList<Tree>>): Int {

        return input.maxOf { it.maxOf { it.scenicScore } }
    }

    val testInput = File("${inputPrefix}_test.txt").readLines().map {
        it.toList().map { Tree(it.digitToInt()) }.toMutableList()
    }

    val input = File("$inputPrefix.txt").readLines().map {
        it.toList().map { Tree(it.digitToInt()) }.toMutableList()
    }

//    check(part1(testInput) == 21)
    println(part1(input))

    check(part2(testInput) == 8)
//    println(part2(input))
}
