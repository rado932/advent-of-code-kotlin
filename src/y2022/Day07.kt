package y2022

import java.io.File

private const val inputPrefix = "src/y2022/Day07"

private class TreeNode private constructor(val name: String, var size: Long = 0, val isDir: Boolean = true) {
    companion object {
        fun file(name: String, size: Long) = TreeNode(name, size, false)
        fun dir(name: String) = TreeNode(name)
    }

    var parent: TreeNode? = null
    val children = mutableListOf<TreeNode>()

    fun addChild(child: TreeNode) {
        child.parent = this
        children.add(child)
    }

    fun getChild(name: String): TreeNode = children.first { it.name == name }

    fun calculateSize(): Long {
        if (!isDir) return size

        size = children.fold(0L) { acc, treeNode -> acc + treeNode.calculateSize() }
        return size
    }

    fun getDirs(predicate: (TreeNode) -> Boolean): List<TreeNode> = getAllDirs().filter(predicate)

    private fun getAllDirs(): List<TreeNode> {
        if (!isDir) return emptyList()

        return listOf(this) + children.flatMap { it.getAllDirs() }
    }

    override fun toString(): String =
        if (isDir) "$name (dir)"
        else "$name (file, size=$size)"

    fun toString(indentLevel: Int): String {
        var indent = ""
        (0 until indentLevel).forEach { _ ->
            indent += "  "
        }

        var output = "${toString()}\n"
        children.forEach {
            output += it.toString(indentLevel + 1)
        }

        return "$indent${output}"
    }
}

fun main() {
    fun parseCommands(input: String) =
        input
            .split("$ ")
            .filterNot { it.isEmpty() }
            .map { command ->
                command
                    .lines()
                    .filterNot { it.isEmpty() }
            }

    fun processCommands(commands: List<List<String>>): TreeNode {
        val root = TreeNode.dir("/")

        var currentDir = root
        commands.forEach { command ->
            when {
                command[0] == "cd /" -> currentDir = root
                command[0] == "cd .." -> currentDir = currentDir.parent!!
                command[0].startsWith("cd") -> currentDir = currentDir.getChild(command[0].substring(3))
                command[0].startsWith("ls") -> {
                    (1 until command.size).forEach {
                        var subDir = currentDir
                        val (first, second) = command[it].split(' ')
                        when {
                            first == "dir" -> {
                                val dir = TreeNode.dir(second)
                                subDir.addChild(dir)
                                subDir = dir
                            }

                            first.toLongOrNull() != null -> {
                                val file = TreeNode.file(second, first.toLong())
                                subDir.addChild(file)
                            }

                            else -> error("Unknown ls response ${command[it]}")
                        }
                    }
                }

                else -> error("Unknown command ${command[0]}")
            }
        }

        return root
    }

    fun part1(input: String): Long {
        val commands = parseCommands(input)

        val maxSize = 100000L
        val root = processCommands(commands)

        root.calculateSize()
        return root.getDirs { it.size <= maxSize }.sumOf { it.size }
    }

    fun part2(input: String): Long {
        val commands = parseCommands(input)

        val totalSize = 70000000L
        val neededSize = 30000000L
        val root = processCommands(commands)

        root.calculateSize()

        val takenSize = root.size
        val freeSize = totalSize - takenSize

        return root.getDirs { it.size >= (neededSize - freeSize) }.minOf { it.size }
    }

    val testInput = File("${inputPrefix}_test.txt").reader().readText()

    val input = File("$inputPrefix.txt").reader().readText()

    check(part1(testInput) == 95437L)
    println(part1(input))

    check(part2(testInput) == 24933642L)
    println(part2(input))
}
