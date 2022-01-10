package day18

import org.junit.Assert.*
import org.junit.Test

class Day18KtTest {
    @Test
    fun testParser() {
        val parser = Parser()

        val case1 = parser.parse("[1,2]")

        assertEquals(
            Node.PairNode(Node.ValueNode(1), Node.ValueNode(2)),
            case1
        )

        val case2 = parser.parse("[[1,9],[8,5]]")

        assertEquals(
            Node.PairNode(
                Node.PairNode(Node.ValueNode(1), Node.ValueNode(9)),
                Node.PairNode(Node.ValueNode(8), Node.ValueNode(5))
            ),
            case2
        )
    }

    @Test
    fun testParseAndDump() {
        val parser = Parser()

        assertEquals(
            "[[1,9],[8,5]]",
            parser.parse("[[1,9],[8,5]]").toString()
        )

        assertEquals(
            "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
            parser.parse("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]").toString()
        )

        assertEquals(
            "[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
            parser.parse("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]").toString()
        )

        assertEquals(
            "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]",
            parser.parse("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]").toString()
        )
    }

    @Test
    fun testPlus() {
        val parser = Parser()

        assertEquals(
            "[[1,2],[[3,4],5]]",
            (parser.parse("[1,2]") + parser.parse("[[3,4],5]")).toString()
        )
    }

    @Test
    fun testExplode() {
        val parser = Parser()

        assertEquals(
            "[[[[0,9],2],3],4]",
            parser.parse("[[[[[9,8],1],2],3],4]").apply { explode() }.toString()
        )

        assertEquals(
            "[7,[6,[5,[7,0]]]]",
            parser.parse("[7,[6,[5,[4,[3,2]]]]]").apply { explode() }.toString()
        )

        assertEquals(
            "[[6,[5,[7,0]]],3]",
            parser.parse("[[6,[5,[4,[3,2]]]],1]").apply { explode() }.toString()
        )

        assertEquals(
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
            parser.parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").apply { explode() }.toString()
        )

        assertEquals(
            "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
            parser.parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").apply { explode() }.toString()
        )
    }

    @Test
    fun testDebugExplodeError() {
        val parser = Parser()

        assertEquals(
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
            parser.parse("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]").apply { reduce() }.toString()
        )
    }

    @Test
    fun testSimpleExample() {
        val parser = Parser()
        val nodes = parser.parseLines(listOf(
            "[[[[4,3],4],4],[7,[[8,4],9]]]",
            "[1,1]"
        ))

        assertEquals(
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
            nodes.sum().toString()
        )
    }

    @Test
    fun testSmallExamples() {
        val parser = Parser()

        assertEquals(
            "[[[[1,1],[2,2]],[3,3]],[4,4]]",
            parser.parseLines(listOf(
                "[1,1]",
                "[2,2]",
                "[3,3]",
                "[4,4]"
            )).sum().toString()
        )

        assertEquals(
            "[[[[3,0],[5,3]],[4,4]],[5,5]]",
            parser.parseLines(listOf(
                "[1,1]",
                "[2,2]",
                "[3,3]",
                "[4,4]",
                "[5,5]"
            )).sum().toString()
        )

        assertEquals(
            "[[[[5,0],[7,4]],[5,5]],[6,6]]",
            parser.parseLines(listOf(
                "[1,1]",
                "[2,2]",
                "[3,3]",
                "[4,4]",
                "[5,5]",
                "[6,6]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExamplePart1() {
        val parser = Parser()

        assertEquals(
            "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]",
            parser.parseLines(listOf(
                "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExamplePart2() {
        val parser = Parser()

        assertEquals(
            "[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]",
            parser.parseLines(listOf(
                "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]",
                "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExamplePart3() {
        val parser = Parser()

        assertEquals(
            "[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]",
            parser.parseLines(listOf(
                "[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]",
                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]"
            )).sum().toString()
        )
    }
    @Test
    fun testLargeExamplePart4() {
        val parser = Parser()

        assertEquals(
            "[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]",
            parser.parseLines(listOf(
                "[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]",
                "[7,[5,[[3,8],[1,4]]]]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExamplePart5() {
        val parser = Parser()

        assertEquals(
            "[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]",
            parser.parseLines(listOf(
                "[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]",
                "[[2,[2,2]],[8,[8,1]]]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExamplePart6() {
        val parser = Parser()

        assertEquals(
            "[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]",
            parser.parseLines(listOf(
                "[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]",
                "[2,9]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExamplePart7() {
        val parser = Parser()

        assertEquals(
            "[[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]",
            parser.parseLines(listOf(
                "[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]",
                "[1,[[[9,3],9],[[9,0],[0,7]]]]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExamplePart8() {
        val parser = Parser()

        assertEquals(
            "[[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]",
            parser.parseLines(listOf(
                "[[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]",
                "[[[5,[7,4]],7],1]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExamplePart9() {
        val parser = Parser()

        assertEquals(
            "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]",
            parser.parseLines(listOf(
                "[[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]",
                "[[[[4,2],2],6],[8,7]]"
            )).sum().toString()
        )
    }

    @Test
    fun testLargeExample() {
        val parser = Parser()

        assertEquals(
            "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]",
            parser.parseLines(listOf(
                "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
                "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
                "[7,[5,[[3,8],[1,4]]]]",
                "[[2,[2,2]],[8,[8,1]]]",
                "[2,9]",
                "[1,[[[9,3],9],[[9,0],[0,7]]]]",
                "[[[5,[7,4]],7],1]",
                "[[[[4,2],2],6],[8,7]]"
            )).sum().toString()
        )
    }

    @Test
    fun testSimpleMagnitude() {
        val parser = Parser()

        assertEquals(
            29,
            parser.parse("[9,1]").magnitude()
        )

        assertEquals(
            21,
            parser.parse("[1,9]").magnitude()
        )

        assertEquals(
            129,
            parser.parse("[[9,1],[1,9]]").magnitude()
        )
    }

    @Test
    fun testMagnitudeExamples() {
        val parser = Parser()

        assertEquals(
            143,
            parser.parse("[[1,2],[[3,4],5]]").magnitude()
        )

        assertEquals(
            1384,
            parser.parse("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").magnitude()
        )

        assertEquals(
            445,
            parser.parse("[[[[1,1],[2,2]],[3,3]],[4,4]]").magnitude()
        )

        assertEquals(
            791,
            parser.parse("[[[[3,0],[5,3]],[4,4]],[5,5]]").magnitude()
        )

        assertEquals(
            1137,
            parser.parse("[[[[5,0],[7,4]],[5,5]],[6,6]]").magnitude()
        )

        assertEquals(
            3488,
            parser.parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude()
        )
    }

    @Test
    fun testExampleHomework() {
        val parser = Parser()

        val nodes = parser.parseLines(listOf(
            "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
            "[[[5,[2,8]],4],[5,[[9,9],0]]]",
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
            "[[[[5,4],[7,7]],8],[[8,3],8]]",
            "[[9,3],[[9,9],[6,[4,9]]]]",
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"
        ))

        val sum = nodes.sum()

        assertEquals(
            "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]",
            sum.toString()
        )

        assertEquals(4140, sum.magnitude())
    }

    @Test
    fun testLargestMagnitudeOfHomework() {
        val parser = Parser()

        val nodes = parser.parseLines(listOf(
            "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
            "[[[5,[2,8]],4],[5,[[9,9],0]]]",
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
            "[[[[5,4],[7,7]],8],[[8,3],8]]",
            "[[9,3],[[9,9],[6,[4,9]]]]",
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"
        ))

        val maxMagnitude = nodes.largestPairMagnitude()

        assertEquals(
            3993,
            maxMagnitude
        )
    }
}