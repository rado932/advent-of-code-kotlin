package y2022

import y2022.Day13.Packet
import java.io.File

private const val inputPrefix = "src/y2022/Day13"

// https://todd.ginsberg.com/post/advent-of-code/2022/day13/

object Day13 {
    sealed class Packet : Comparable<Packet> {
        companion object {
            fun of(input: String): Packet =
                of(
                    input.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex())
                        .filter { it.isNotBlank() }
                        .filter { it != "," }
                        .iterator()
                )

            private fun of(input: Iterator<String>): Packet {
                val packets = mutableListOf<Packet>()

                while (input.hasNext()) {
                    when (val symbol = input.next()) {
                        "]" -> return ListPacket(packets)
                        "[" -> packets.add(of(input))
                        else -> packets.add(IntPacket(symbol.toInt()))
                    }
                }

                return ListPacket(packets)
            }
        }
    }

    private class IntPacket(val amount: Int) : Packet() {
        fun asList(): Packet = ListPacket(listOf(this))

        override fun compareTo(other: Packet): Int =
            when (other) {
                is IntPacket -> amount.compareTo(other.amount)
                is ListPacket -> asList().compareTo(other)
            }
    }

    private class ListPacket(val subPackets: List<Packet>) : Packet() {
        override fun compareTo(other: Packet): Int =
            when (other) {
                is IntPacket -> compareTo(other.asList())
                is ListPacket -> subPackets.zip(other.subPackets)
                    .map { it.first.compareTo(it.second) }
                    .firstOrNull { it != 0 } ?: subPackets.size.compareTo(other.subPackets.size)
            }
    }
}

fun main() {

    fun part1(packetPairs: List<Packet>): Int = packetPairs.chunked(2)
        .foldIndexed(0) { i, acc, (left, right) ->
            if (left < right) acc + i + 1
            else acc
        }


    fun part2(packetPairs: List<Packet>): Int {
        val p1 = Packet.of("[[2]]")
        val p2 = Packet.of("[[6]]")

        val sortedPackets = packetPairs.toMutableList()
            .also { it.add(p1) }
            .also { it.add(p2) }
            .sorted()

        return (sortedPackets.indexOf(p1) + 1) * (sortedPackets.indexOf(p2) + 1)
    }

    val testInput = File("${inputPrefix}_test.txt").readLines()
        .filter { it.isNotEmpty() }
        .map { Packet.of(it) }

    val input = File("$inputPrefix.txt").readLines()
        .filter { it.isNotEmpty() }
        .map { Packet.of(it) }

    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 140)
    println(part2(input))
}
