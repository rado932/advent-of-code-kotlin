package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day07"

// https://todd.ginsberg.com/post/advent-of-code/2022/day7/

class Directory(val name: String) {
    private val subDirs: MutableMap<String, Directory> = mutableMapOf()
    private var sizeOfFiles: Int = 0

    val size: Int
        get() = sizeOfFiles + subDirs.values.sumOf { it.size }

    fun addFile(size: Int) {
        sizeOfFiles += size
    }

    fun traverse(dir: String): Directory =
        subDirs.getOrPut(dir) { Directory(dir) }

    fun find(predicate: (Directory) -> Boolean): List<Directory> =
        subDirs.values.filter(predicate) +
            subDirs.values.flatMap { it.find(predicate) }
}

fun main() {
    fun parseInput(input: List<String>): Directory {
        val callStack = ArrayDeque<Directory>().apply { add(Directory("/")) }
        input.forEach { item ->
            when {
                item == "$ ls" -> {}// Noop
                item.startsWith("dir") -> {} // Noop
                item == "$ cd /" -> callStack.removeIf { it.name != "/" }
                item == "$ cd .." -> callStack.removeFirst()
                item.startsWith("$ cd") -> {
                    val name = item.substringAfterLast(" ")
                    callStack.addFirst(callStack.first().traverse(name))
                }
                else -> {
                    val size = item.substringBefore(" ").toInt()
                    callStack.first().addFile(size)
                }
            }
        }
        return callStack.last()
    }

    fun part1(input: List<String>): Int {
        val rootDirectory: Directory = parseInput(input)

        return rootDirectory.find { it.size <= 100_000 }.sumOf { it.size }
    }

    fun part2(input: List<String>): Int {
        val rootDirectory: Directory = parseInput(input)

        val unusedSpace = 70_000_000 - rootDirectory.size
        val deficit = 30_000_000 - unusedSpace
        return rootDirectory.find { it.size >= deficit }.minOf { it.size }
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    check(part1(testInput) == 95437)
    println(part1(input))

    check(part2(testInput) == 24933642)
    println(part2(input))
}
