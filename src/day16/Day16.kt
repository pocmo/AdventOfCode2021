package day16

import java.io.File

val hexToBits = mapOf(
    '0' to listOf('0', '0', '0', '0'),
    '1' to listOf('0', '0', '0', '1'),
    '2' to listOf('0', '0', '1', '0'),
    '3' to listOf('0', '0', '1', '1'),
    '4' to listOf('0', '1', '0', '0'),
    '5' to listOf('0', '1', '0', '1'),
    '6' to listOf('0', '1', '1', '0'),
    '7' to listOf('0', '1', '1', '1'),
    '8' to listOf('1', '0', '0', '0'),
    '9' to listOf('1', '0', '0', '1'),
    'A' to listOf('1', '0', '1', '0'),
    'B' to listOf('1', '0', '1', '1'),
    'C' to listOf('1', '1', '0', '0'),
    'D' to listOf('1', '1', '0', '1'),
    'E' to listOf('1', '1', '1', '0'),
    'F' to listOf('1', '1', '1', '1')
)

fun bitSequence(input: String): Sequence<Char> {
    return sequence {
        input.toCharArray().forEach { character ->
            val bits = hexToBits[character] ?: throw IllegalStateException("Can't convert $character")
            bits.forEach { bit -> yield(bit) }
        }
    }
}

class Marker(
    private var mark: Int = 0,
    private val read: () -> Int
) {
    fun bitsRead(): Int {
        return read() - mark
    }
}

class SequenceReader(
    sequence: Sequence<Char>
) {
    private val iterator = sequence.iterator()
    private var read = 0

    fun mark(): Marker {
        return Marker(read) { read }
    }

    fun take(n: Int): List<Char> {
        val characters = mutableListOf<Char>()
        repeat(n) {
            characters.add(take())
        }
        return characters
    }

    fun take(): Char {
        read++
        return iterator.next()
    }
}

sealed interface Packet {
    val version: Int

    data class LiteralValuePacket(
        override val version: Int,
        val value: Long
    ): Packet {
        companion object {
            const val TYPE = 4
        }
    }

    data class OperatorPacket(
        override val version: Int,
        val type: Int,
        val packets: List<Packet>
    ): Packet
}

class Scanner(
    input: String
) {
    private val reader = SequenceReader(bitSequence(input))

    fun mark(): Marker {
        return reader.mark()
    }

    fun nextInt(bits: Int): Int {
        return nextBitsAsString(bits).toInt(2)
    }

    fun nextBitsAsString(bits: Int): String {
        return reader.take(bits).joinToString("")
    }

    fun nextBoolean(): Boolean {
        return when (reader.take()) {
            '0' -> false
            '1' -> true
            else -> throw IllegalStateException("Bit is neither 0 nor 1")
        }
    }
}

class Parser(
    private val scanner: Scanner
) {
    fun parsePacket(): Packet {
        val packetVersion = scanner.nextInt(3)

        return when (val packetType = scanner.nextInt(3)) {
            Packet.LiteralValuePacket.TYPE -> parseLiteralValuePacket(packetVersion)
            else -> parseOperatorPacket(packetVersion, packetType)
        }
    }

    private fun parseLiteralValuePacket(
        version: Int
    ): Packet.LiteralValuePacket {
        var loopBit = scanner.nextBoolean()
        val chunks = mutableListOf(scanner.nextBitsAsString(4))

        while (loopBit) {
            loopBit = scanner.nextBoolean()
            chunks.add(scanner.nextBitsAsString(4))
        }

        val value = chunks.joinToString("").toLong(2)

        return Packet.LiteralValuePacket(version, value)
    }

    private fun parseOperatorPacket(
        version: Int,
        type: Int
    ): Packet.OperatorPacket {
        return when (scanner.nextBoolean()) {
            false -> parseBitSizedOperatorPacket(version, type)
            true -> parseFixedSizeOperatorPacket(version, type)
        }
    }

    private fun parseBitSizedOperatorPacket(
        version: Int,
        type: Int
    ): Packet.OperatorPacket {
        val numberOfBits = scanner.nextInt(15)
        val packets = mutableListOf<Packet>()
        val mark = scanner.mark()

        while (mark.bitsRead() < numberOfBits) {
            packets.add(parsePacket())
        }

        return Packet.OperatorPacket(
            version, type, packets
        )
    }

    private fun parseFixedSizeOperatorPacket(
        version: Int,
        type: Int
    ): Packet.OperatorPacket {
        val numberOfPackets = scanner.nextInt(11)
        val packets = mutableListOf<Packet>()

        repeat(numberOfPackets) {
            packets.add(parsePacket())
        }

        return Packet.OperatorPacket(
            version, type, packets
        )
    }
}

fun Packet.sumVersions(): Int {
    return when (this) {
        is Packet.LiteralValuePacket -> version
        is Packet.OperatorPacket -> version + packets.sumOf { subPacket -> subPacket.sumVersions() }
    }
}

class Interpreter {
    fun evaluate(packet: Packet): Long {
        return when (packet) {
            is Packet.LiteralValuePacket -> packet.value
            is Packet.OperatorPacket -> evaluateOperator(packet)
        }
    }

    private fun evaluateOperator(packet: Packet.OperatorPacket): Long {
        return when (packet.type) {
            0 -> packet.packets.sumOf { evaluate(it) }
            1 -> packet.packets.productOf { evaluate(it) }
            2 -> packet.packets.minOf { evaluate(it) }
            3 -> packet.packets.maxOf { evaluate(it) }
            5 -> {
                val (first, second) = packet.packets
                if (evaluate(first) > evaluate(second)) {
                    1L
                } else {
                    0L
                }
            }
            6 -> {
                val (first, second) = packet.packets
                if (evaluate(first) < evaluate(second)) {
                    1L
                } else {
                    0L
                }
            }
            7 -> {
                val (first, second) = packet.packets
                if (evaluate(first) == evaluate(second)) {
                    1L
                } else {
                    0L
                }
            }
            else -> throw IllegalStateException("Unknown operator: ${packet.type}")
        }
    }
}

fun <T> Iterable<T>.productOf(selector: (T) -> Long): Long {
    var product: Long = 1
    forEach { product *= selector(it) }
    return product
}

fun part1() {
    val scanner = Scanner(
        File("day16.txt").readText().trim()
    )

    val parser = Parser(scanner)
    val packet = parser.parsePacket()

    println("Sum of versions = ${packet.sumVersions()}")
}

fun part2() {
    val scanner = Scanner(
        File("day16.txt").readText().trim()
    )

    val parser = Parser(scanner)
    val packet = parser.parsePacket()

    val interpreter = Interpreter()
    val result = interpreter.evaluate(packet)

    println("Result = $result")
}

fun main() {
    part1()
    part2()
}