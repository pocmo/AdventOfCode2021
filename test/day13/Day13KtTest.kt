package day13

import org.junit.Assert.*
import org.junit.Test

class Day13KtTest {
    @Test
    fun testParsingPoints() {
        assertEquals(Point(460, 157), parsePoint("460,157"))
        assertEquals(Point(1081, 648), parsePoint("1081,648"))
        assertEquals(Point(0, 0), parsePoint("0,0"))
        assertEquals(Point(11, 0), parsePoint("11,0"))
    }

    @Test
    fun testParsingInstructions() {
        assertEquals(Instruction.FoldLeft(655), parseInstruction("fold along x=655"))
        assertEquals(Instruction.FoldUp(447), parseInstruction("fold along y=447"))
        assertEquals(Instruction.FoldLeft(327), parseInstruction("fold along x=327"))
        assertEquals(Instruction.FoldUp(223), parseInstruction("fold along y=223"))
    }

    @Test
    fun testParsingFromString() {
        val (paper, instructions) = readFromString("""
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent())

        assertEquals(18, paper.points.size)
        assertEquals(2, instructions.size)
        assertTrue(paper.points[Point(4,1)] ?: false)
        assertFalse(paper.points[Point(13,13)] ?: false)
    }

    @Test
    fun testDump() {
        val (paper, _) = readFromString("""
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent())

        assertEquals(
            """
                ...#..#..#.
                ....#......
                ...........
                #..........
                ...#....#.#
                ...........
                ...........
                ...........
                ...........
                ...........
                .#....#.##.
                ....#......
                ......#...#
                #..........
                #.#........
            """.trimIndent(),
            paper.dump()
        )
    }

    @Test
    fun testFoldingUp() {
        val (paper, _) = readFromString("""
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent())

        val instruction = Instruction.FoldUp(7)
        val foldedPaper = paper.fold(instruction)

        assertEquals(
            """
                #.##..#..#.
                #...#......
                ......#...#
                #...#......
                .#.#..#.###
            """.trimIndent(),
            foldedPaper.dump()
        )

        assertEquals(
            17,
            foldedPaper.countPoints()
        )
    }

    @Test
    fun testFoldingExample() {
        val (paper, instructions) = readFromString("""
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent())

        assertEquals(Instruction.FoldUp(7), instructions[0])
        assertEquals(Instruction.FoldLeft(5), instructions[1])

        val foldedPaper = paper.fold(instructions)

        assertEquals(
            """
                #####
                #...#
                #...#
                #...#
                #####
            """.trimIndent(),
            foldedPaper.dump()
        )
    }
}