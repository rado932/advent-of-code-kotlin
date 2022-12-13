fun main() {
    fun part1(input: List<String>): Int {
        val (numbers, boards) = extractNumbersAndBoardsFromInput(input)

        var matchedBoard: List<List<Int>>? = null
        var lastNumber: Int = -100
        numbers@for (number in numbers) {
            board@for (board in boards) {
                for (line in board) {
                    val matchedIndex = line.replaceWithNegativeIfExists(number)
                    if (matchedIndex >= 0) {
                        if (line.isRowComplete() || board.isColumnComplete(matchedIndex)) {
                            matchedBoard = board
                            lastNumber = number
                            break@numbers
                        }
                        continue@board
                    }
                }
            }
        }

        if (matchedBoard == null) return 0

        val unmatchedSum = matchedBoard.fold(0) { acc, list -> acc + list.filter { it > 0 }.sum() }
        return unmatchedSum * lastNumber
    }

    fun part2(input: List<String>): Int {
        val (numbers, boards) = extractNumbersAndBoardsFromInput(input)

        val finishedBoards = mutableListOf<Int>()
        var lastMatchedBoard: List<List<Int>>? = null
        var lastMatchedNumberNumber: Int = -100
        numbers@for (number in numbers) {
            board@for ((i, board) in boards.withIndex()) {
                if (finishedBoards.contains(i)) continue

                for (line in board) {
                    val matchedIndex = line.replaceWithNegativeIfExists(number)
                    if (matchedIndex >= 0) {
                        if (line.isRowComplete() || board.isColumnComplete(matchedIndex)) {
                            lastMatchedBoard = board
                            lastMatchedNumberNumber = number
                            finishedBoards.add(i)
                        }
                        continue@board
                    }
                }
            }
        }

        if (lastMatchedBoard == null) return 0

        val unmatchedSum = lastMatchedBoard.fold(0) { acc, list -> acc + list.filter { it > 0 }.sum() }
        return unmatchedSum * lastMatchedNumberNumber
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

fun MutableList<Int>.replaceWithNegativeIfExists(item: Int): Int {
    val index = indexOf(item)
    if (index >= 0) {
        this[index] = -100 - item
    }

    return index
}

fun List<Int>.isRowComplete(): Boolean {
    forEach { if (it >= 0) return false }
    return true
}

fun List<List<Int>>.isColumnComplete(index: Int): Boolean {
    forEach { if (it[index] >= 0) return false }
    return true
}

fun extractNumbersAndBoardsFromInput(input: List<String>): Pair<List<Int>, List<List<MutableList<Int>>>> {
    val numbers = input[0].split(",").map { it.toInt() }

    val boards = input.subList(1, input.size)
        .chunked(6)
        .map { it.subList(1, it.size) }
        .map { list ->
            list.map { lineString ->
                lineString.trim()
                    .split("\\W+".toRegex())
                    .map { it.toInt() }
                    .toMutableList()
            }
        }

    return Pair(numbers, boards)
}