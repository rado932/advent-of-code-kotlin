package y2023

import Timer.time
import java.io.File

private const val inputPrefix = "src/y2023/Day06"

private class Track(val time: Long, val distance: Long) {
    override fun toString(): String {
        return "Track(time=$time, distance=$distance)"
    }
}

private fun Track.waysToWin(): Long =
    (1 until time).map { raceTime ->
        raceTime * (time - raceTime)
    }.count { it > distance }.toLong()

private fun Track.waysToWinOptimised(): Long {
    val possibleWaysToWin = time
    var possibleWaysToLose = 0

    for (raceTime in 1 until time) {
        val raceDistance = raceTime * (time - raceTime)
        if (raceDistance > distance){
            break
        } else {
            possibleWaysToLose += 1
        }
    }

    for (raceTime in time downTo 1) {
        val raceDistance = raceTime * (time - raceTime)
        if (raceDistance > distance){
            break
        } else {
            possibleWaysToLose += 1
        }
    }

    return possibleWaysToWin - possibleWaysToLose
}

fun main() {
    fun part1(input: List<Track>): Long = input.fold(1) { acc, track ->
        acc * track.waysToWinOptimised()
    }

    fun part2(input: Track): Long = input.waysToWinOptimised()

    val testInput = File("${inputPrefix}_test.txt").readLines()

    val input = File("$inputPrefix.txt").readLines()

    fun parseInput(input: List<String>): List<Track> {
        val digitRegex = "\\d+".toRegex()

        val timesStr = input[0].substringAfter(":")
        val distanceStr = input[1].substringAfter(":")

        val times = digitRegex.findAll(timesStr)
        val distaces = digitRegex.findAll(distanceStr).iterator()

        return times.mapIndexed { index, time ->
            Track(time.value.toLong(), distaces.next().value.toLong())
        }.toList()
    }

    fun parseInputPart2(input: List<String>): Track {
        val digitRegex = "\\d+".toRegex()
        val (time, distance) = input.map {
            val noSpace = it.replace(" ", "")
            digitRegex.find(noSpace)!!.value.toLong()
        }
        return Track(time, distance)
    }

    check(part1(parseInput(testInput)) == 288L)
    time { println(part1(parseInput(input))) }

    check(part2(parseInputPart2(testInput)) == 71503L)
    time { println(part2(parseInputPart2(input))) }
}
