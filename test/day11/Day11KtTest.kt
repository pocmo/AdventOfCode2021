package day11

import org.junit.Assert.*
import org.junit.Test

class Day11KtTest {
    @Test
    fun testReadingOctopuses() {
        val octopuses = readOctopuses("day11_test.txt")

        assertEquals(
            5,
            octopuses[0][0].energy
        )

        assertEquals(
            6,
            octopuses[9][9].energy
        )

        assertEquals(
            1,
            octopuses[3][3].energy
        )

        assertEquals(
            3,
            octopuses[0][9].energy
        )

        assertEquals(
            5,
            octopuses[9][0].energy
        )
    }

    @Test
    fun testReadingOctopusesDump() {
        val octopuses = readOctopuses("day11_test.txt")

        assertEquals("""
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526""".trimIndent(),
            octopuses.dump()
        )
    }

    @Test
    fun readFromStringAndDump() {
        val input = """
            5432
            1111
            0882
            0144
        """.trimIndent()

        val octopuses = readOctopusesFromString(input)

        assertEquals(
            """
            5432
            1111
            0882
            0144
            """.trimIndent(),
            octopuses.dump()
        )
    }

    @Test
    fun increase() {
        val input = """
            5432
            1111
            0882
            0144
        """.trimIndent()

        val octopuses = readOctopusesFromString(input)
        octopuses.increase()

        assertEquals(
            """
            6543
            2222
            1993
            1255
            """.trimIndent(),
            octopuses.dump()
        )
    }

    @Test
    fun testNeighborsOf() {
        val input = """
            5432
            1111
            0882
            0144
        """.trimIndent()

        val octopuses = readOctopusesFromString(input)

        assertEquals(
            listOf(
                Position(1, 0),
                Position(0, 1),
                Position(1, 1)
            ),
            octopuses.neighborsOf(0, 0)
        )

        assertEquals(
            listOf(
                Position(0, 0),
                Position(1, 0),
                Position(2, 0),

                Position(0, 1),
                Position(2, 1),

                Position(0, 2),
                Position(1, 2),
                Position(2, 2),
            ),
            octopuses.neighborsOf(1, 1)
        )

        assertEquals(
            listOf(
                Position(2, 2),
                Position(3, 2),
                Position(2, 3)
            ),
            octopuses.neighborsOf(3, 3)
        )
    }

    @Test
    fun increaseNeighbors() {
        val input = """
            5432
            1111
            0882
            0144
        """.trimIndent()

        val octopuses = readOctopusesFromString(input)

        octopuses.increase(
            octopuses.neighborsOf(3, 3)
        )

        assertEquals(
            """
            5432
            1111
            0893
            0154
            """.trimIndent(),
            octopuses.dump()
        )
    }

    @Test
    fun testSimulateOnce() {
        val octopuses = readOctopusesFromString("""
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent())

        octopuses.simulate()

        assertEquals("""
            6594254334
            3856965822
            6375667284
            7252447257
            7468496589
            5278635756
            3287952832
            7993992245
            5957959665
            6394862637
        """.trimIndent(),
            octopuses.dump()
        )
    }

    @Test
    fun testMinimalExample() {
        val octopuses = readOctopusesFromString("""
            11111
            19991
            19191
            19991
            11111
        """.trimIndent())

        octopuses.simulate()

        assertEquals("""
            34543
            40004
            50005
            40004
            34543
            """.trimIndent(),
            octopuses.dump()
        )

        octopuses.simulate()

        assertEquals("""
            45654
            51115
            61116
            51115
            45654
            """.trimIndent(),
            octopuses.dump()
        )
    }

    @Test
    fun test10Simulations() {
        val octopuses = readOctopusesFromString("""
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent())

        // Step 1
        octopuses.simulate()
        assertEquals("""
            6594254334
            3856965822
            6375667284
            7252447257
            7468496589
            5278635756
            3287952832
            7993992245
            5957959665
            6394862637
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 2
        octopuses.simulate()
        assertEquals("""
            8807476555
            5089087054
            8597889608
            8485769600
            8700908800
            6600088989
            6800005943
            0000007456
            9000000876
            8700006848
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 3
        octopuses.simulate()
        assertEquals("""
            0050900866
            8500800575
            9900000039
            9700000041
            9935080063
            7712300000
            7911250009
            2211130000
            0421125000
            0021119000
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 4
        octopuses.simulate()
        assertEquals("""
            2263031977
            0923031697
            0032221150
            0041111163
            0076191174
            0053411122
            0042361120
            5532241122
            1532247211
            1132230211
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 5
        octopuses.simulate()
        assertEquals("""
            4484144000
            2044144000
            2253333493
            1152333274
            1187303285
            1164633233
            1153472231
            6643352233
            2643358322
            2243341322
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 6
        octopuses.simulate()
        assertEquals("""
            5595255111
            3155255222
            3364444605
            2263444496
            2298414396
            2275744344
            2264583342
            7754463344
            3754469433
            3354452433
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 7
        octopuses.simulate()
        assertEquals("""
            6707366222
            4377366333
            4475555827
            3496655709
            3500625609
            3509955566
            3486694453
            8865585555
            4865580644
            4465574644
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 8
        octopuses.simulate()
        assertEquals("""
            7818477333
            5488477444
            5697666949
            4608766830
            4734946730
            4740097688
            6900007564
            0000009666
            8000004755
            6800007755
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 9
        octopuses.simulate()
        assertEquals("""
            9060000644
            7800000976
            6900000080
            5840000082
            5858000093
            6962400000
            8021250009
            2221130009
            9111128097
            7911119976
            """.trimIndent(),
            octopuses.dump()
        )

        // Step 10
        octopuses.simulate()
        assertEquals("""
            0481112976
            0031112009
            0041112504
            0081111406
            0099111306
            0093511233
            0442361130
            5532252350
            0532250600
            0032240000
            """.trimIndent(),
            octopuses.dump()
        )
    }

    @Test
    fun testFlashCount() {
        val octopuses = readOctopusesFromString("""
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent())

        val count = octopuses.simulate(10)

        assertEquals(204, count)
    }

    @Test
    fun testFlashCountAfter100Times() {
        val octopuses = readOctopusesFromString("""
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent())

        val count = octopuses.simulate(100)

        assertEquals(1656, count)
    }

    @Test
    fun testAllFlashesExamples() {
        val octopuses = readOctopusesFromString("""
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent())

        octopuses.simulate(195)

        assertEquals("""
            0000000000
            0000000000
            0000000000
            0000000000
            0000000000
            0000000000
            0000000000
            0000000000
            0000000000
            0000000000
            """.trimIndent(),
            octopuses.dump()
        )

        assertTrue(octopuses.allFlashed())
    }

    @Test
    fun testSimulateUntilAllFlashed() {
        val octopuses = readOctopusesFromString("""
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent())

        val steps = octopuses.simulateUntilAllFlashed()

        assertEquals(195, steps)
    }
}