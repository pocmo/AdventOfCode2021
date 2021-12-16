package day16

import org.junit.Assert.*
import org.junit.Test

class Day16KtTest {
    @Test
    fun testBuildingSequence() {
        val sequence = bitSequence("D2FE28")
        assertEquals(
            "110100101111111000101000",
            sequence.toList().joinToString(""),
        )
    }

    @Test
    fun testSequenceReader() {
        val reader = SequenceReader(bitSequence("D2FE28"))

        assertEquals(
            "110",
            reader.take(3).joinToString("")
        )

        assertEquals(
            "100",
            reader.take(3).joinToString("")
        )
    }

    @Test
    fun testReaderMarking() {
        val reader = SequenceReader(bitSequence("D2FE28"))

        reader.take(3)
        reader.take(3)

        val firstMark = reader.mark()

        reader.take(3)

        assertEquals(3, firstMark.bitsRead())

        reader.take(5)

        val secondMark = reader.mark()

        assertEquals(8, firstMark.bitsRead())
        assertEquals(0, secondMark.bitsRead())

        reader.take(2)

        assertEquals(10, firstMark.bitsRead())
        assertEquals(2, secondMark.bitsRead())
    }

    @Test
    fun testScanningForNextInt() {
        val scanner = Scanner("D2FE28")

        assertEquals(
            6,
            scanner.nextInt(3)
        )

        assertEquals(
            4,
            scanner.nextInt(3)
        )
    }

    @Test
    fun testParsingLiteralValuePacket() {
        val parser = Parser(Scanner("D2FE28"))

        val packet = parser.parsePacket()

        assertTrue(packet is Packet.LiteralValuePacket)

        val literalValuePacket = packet as Packet.LiteralValuePacket
        assertEquals(2021, literalValuePacket.value)
    }

    @Test
    fun testParsingOperatorWithFixedLength() {
        val parser = Parser(Scanner("EE00D40C823060"))

        val packet = parser.parsePacket()

        assertTrue(packet is Packet.OperatorPacket)
        val operatorPacket = packet as Packet.OperatorPacket

        assertEquals(3, operatorPacket.type)
        assertEquals(3, operatorPacket.packets.size)

        assertTrue(operatorPacket.packets.all { it is Packet.LiteralValuePacket })

        val (one, two, three) = operatorPacket.packets
            .map { it as Packet.LiteralValuePacket }
            .map { it.value }

        assertEquals(1, one)
        assertEquals(2, two)
        assertEquals(3, three)
    }

    @Test
    fun testParsingOperatorWithDynamicLength() {
        val parser = Parser(Scanner("38006F45291200"))

        val packet = parser.parsePacket()

        assertTrue(packet is Packet.OperatorPacket)
        val operatorPacket = packet as Packet.OperatorPacket

        assertEquals(6, operatorPacket.type)
        assertEquals(2, operatorPacket.packets.size)

        assertTrue(operatorPacket.packets.all { it is Packet.LiteralValuePacket })

        val (one, two) = operatorPacket.packets
            .map { it as Packet.LiteralValuePacket }
            .map { it.value }

        assertEquals(10, one)
        assertEquals(20, two)
    }

    @Test
    fun `sum versions of parse(8A004A801A8002F478)`() {
        val parser = Parser(Scanner("8A004A801A8002F478"))
        val packet = parser.parsePacket()

        assertEquals(4, packet.version)
        val level1Packet = packet as Packet.OperatorPacket

        assertEquals(1, level1Packet.packets.size)
        assertEquals(16, packet.sumVersions())
    }

    @Test
    fun `sum versions of parse(620080001611562C8802118E34)`() {
        val parser = Parser(Scanner("620080001611562C8802118E34"))
        val packet = parser.parsePacket()
        assertEquals(12, packet.sumVersions())
    }

    @Test
    fun `sum versions of parse(C0015000016115A2E0802F182340)`() {
        val parser = Parser(Scanner("C0015000016115A2E0802F182340"))
        val packet = parser.parsePacket()
        assertEquals(23, packet.sumVersions())
    }

    @Test
    fun `sum versions of parse(A0016C880162017C3686B18A3D4780)`() {
        val parser = Parser(Scanner("A0016C880162017C3686B18A3D4780"))
        val packet = parser.parsePacket()
        assertEquals(31, packet.sumVersions())
    }

    @Test
    fun `evaluate(C200B40A82)`() {
        val parser = Parser(Scanner("C200B40A82"))
        val packet = parser.parsePacket()

        val interpreter = Interpreter()
        assertEquals(3, interpreter.evaluate(packet))
    }

    @Test
    fun `evaluate(04005AC33890)`() {
        val parser = Parser(Scanner("04005AC33890"))
        val packet = parser.parsePacket()

        val interpreter = Interpreter()
        assertEquals(54, interpreter.evaluate(packet))
    }

    @Test
    fun `evaluate(880086C3E88112)`() {
        val parser = Parser(Scanner("880086C3E88112"))
        val packet = parser.parsePacket()

        val interpreter = Interpreter()
        assertEquals(7, interpreter.evaluate(packet))
    }

    @Test
    fun `evaluate(CE00C43D881120)`() {
        val parser = Parser(Scanner("CE00C43D881120"))
        val packet = parser.parsePacket()

        val interpreter = Interpreter()
        assertEquals(9, interpreter.evaluate(packet))
    }

    @Test
    fun `evaluate(D8005AC2A8F0)`() {
        val parser = Parser(Scanner("D8005AC2A8F0"))
        val packet = parser.parsePacket()

        val interpreter = Interpreter()
        assertEquals(1, interpreter.evaluate(packet))
    }

    @Test
    fun `evaluate(F600BC2D8F)`() {
        val parser = Parser(Scanner("F600BC2D8F"))
        val packet = parser.parsePacket()

        val interpreter = Interpreter()
        assertEquals(0, interpreter.evaluate(packet))
    }

    @Test
    fun `evaluate(9C005AC2F8F0)`() {
        val parser = Parser(Scanner("9C005AC2F8F0"))
        val packet = parser.parsePacket()

        val interpreter = Interpreter()
        assertEquals(0, interpreter.evaluate(packet))
    }

    @Test
    fun `evaluate(9C0141080250320F1802104A08)`() {
        val parser = Parser(Scanner("9C0141080250320F1802104A08"))
        val packet = parser.parsePacket()

        val interpreter = Interpreter()
        assertEquals(1, interpreter.evaluate(packet))
    }
}

