package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day08"

class Tree(
    private val forest: Forest,
    val height: Int,
    val i: Int,
    val j: Int
) {
    private var _isVisible: Boolean = false
    val isVisible: Boolean
        get() = _isVisible

    private var _scenicScore: Int = 0
    val scenicScore: Int
        get() = _scenicScore

    fun markAsVisible() {
        _isVisible = true
    }

    // part 2
    fun updateScenicScore() {
        _scenicScore = tallerTreesUp(height) * tallerTreesDown(height) *
            tallerTreesLeft(height) * tallerTreesRight(height)
    }

    fun tallerTreesUp(requesterHeight: Int): Int = calculateSmallerTrees(
        requesterHeight,
        { forest.getUpSibling(this) },
        { it.tallerTreesUp(requesterHeight) },
    )

    fun tallerTreesLeft(requesterHeight: Int): Int = calculateSmallerTrees(
        requesterHeight,
        { forest.getLeftSibling(this) },
        { it.tallerTreesLeft(requesterHeight) },
    )

    fun tallerTreesDown(requesterHeight: Int): Int = calculateSmallerTrees(
        requesterHeight,
        { forest.getDownSibling(this) },
        { it.tallerTreesDown(requesterHeight) },
    )

    fun tallerTreesRight(requesterHeight: Int): Int = calculateSmallerTrees(
        requesterHeight,
        { forest.getRightSibling(this) },
        { it.tallerTreesRight(requesterHeight) },
    )

    private fun calculateSmallerTrees(
        requesterHeight: Int,
        getSibling: () -> Tree?,
        getSiblingSmallerTrees: (sibling: Tree) -> Int,
    ): Int {
        val sibling = getSibling() ?: return 0
        if (requesterHeight <= sibling.height) return 1
        return 1 + getSiblingSmallerTrees(sibling)
    }
}

class Forest(input: List<List<Char>>) {
    private val forest: List<List<Tree>>

    val rowsLastIndex: Int
    val columnsLastIndex: Int

    val maxScenicScore: Int
        get() = forest.maxOf { line ->
            line.maxOf { it.scenicScore }
        }

    val visibleTrees: Int
        get() = forest.sumOf { it.count { it.isVisible } }

    init {
        columnsLastIndex = input.lastIndex
        rowsLastIndex = input[0].lastIndex

        forest = input.mapIndexed { i, list ->
            list.mapIndexed { j, char ->
                Tree(this, char.digitToInt(), i, j)
            }
        }

        updateVisibility()
        updateScenicScores()
    }

    private fun updateVisibility() {
        infix fun Int.downUntil(to: Int): IntProgression {
            if (to >= Int.MAX_VALUE) return IntRange.EMPTY
            return this downTo (to + 1)
        }

        fun updateVisibilityInternal(
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

        for (i in 0..columnsLastIndex) {
            forest[i][0].markAsVisible()
            forest[i][rowsLastIndex].markAsVisible()

            if (i != 0 && i != columnsLastIndex) {
                updateVisibilityInternal(0 until rowsLastIndex) { j ->
                    forest[i][j]
                }
                updateVisibilityInternal(rowsLastIndex downUntil 0) { j ->
                    forest[i][j]
                }
            }
        }

        for (i in 0..rowsLastIndex) {
            forest[0][i].markAsVisible()
            forest[columnsLastIndex][i].markAsVisible()

            if (i != 0 && i != rowsLastIndex) {
                updateVisibilityInternal(0 until columnsLastIndex) { j ->
                    forest[j][i]
                }
                updateVisibilityInternal(columnsLastIndex downUntil 0) { j ->
                    forest[j][i]
                }
            }
        }
    }

    private fun updateScenicScores() {
        for (i in 1 until columnsLastIndex) {
            for (j in 1 until rowsLastIndex) {
                forest[i][j].updateScenicScore()
            }
        }
    }

    fun getUpSibling(tree: Tree): Tree? = try {
        forest[tree.i - 1][tree.j]
    } catch (ex: Exception) {
        null
    }

    fun getLeftSibling(tree: Tree): Tree? = try {
        forest[tree.i][tree.j - 1]
    } catch (ex: Exception) {
        null
    }

    fun getDownSibling(tree: Tree): Tree? = try {
        forest[tree.i + 1][tree.j]
    } catch (ex: Exception) {
        null
    }

    fun getRightSibling(tree: Tree): Tree? = try {
        forest[tree.i][tree.j + 1]
    } catch (ex: Exception) {
        null
    }
}

fun main() {

    fun part1(forest: Forest): Int = forest.visibleTrees

    fun part2(forest: Forest): Int = forest.maxScenicScore


    val testInput = File("${inputPrefix}_test.txt").readLines().map { it.toList() }
    val forestTest = Forest(testInput)

    val input = File("$inputPrefix.txt").readLines().map { it.toList() }
    val forest = Forest(input)

    check(part1(forestTest) == 21)
    println(part1(forest))
//
    check(part2(forestTest) == 8)
    println(part2(forest))
}
