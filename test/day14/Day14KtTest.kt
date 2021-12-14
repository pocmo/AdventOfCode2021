package day14

import org.junit.Assert.*
import org.junit.Test

class Day14KtTest {
    @Test
    fun testReadFromString() {
        val input = """
            NNCB

            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent()

        val (template, map) = readFromString(input)

        assertEquals("NNCB", template)
        assertEquals(16, map.size)
        assertEquals("H", map["BH"])
        assertEquals("C", map["CN"])
    }

    @Test
    fun testPerformInsertion() {
        val (template, map) = readFromString("""
            NNCB

            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent())

        val newTemplate = performInsertion(template, map)

        assertEquals(
            "NCNBCHB",
            newTemplate
        )

        val secondTemplate = performInsertion(newTemplate, map)

        assertEquals(
            "NBCCNBBBCBHCB",
            secondTemplate
        )

        val thirdTemplate = performInsertion(secondTemplate, map)

        assertEquals(
            "NBBBCNCCNBBNBNBBCHBHHBCHB",
            thirdTemplate
        )

        val fourthTempalte = performInsertion(thirdTemplate, map)

        assertEquals(
            "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB",
            fourthTempalte
        )
    }

    @Test
    fun testFindMinMax() {
        var (template, map) = readFromString("""
            NNCB

            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent())

        repeat(10) {
            template = performInsertion(template, map)
        }

        assertEquals(3073, template.length)

        val quantities = countQuantities(template)
        assertEquals(4, quantities.size)

        val (minCharacter, minValue) = quantities.min()
        val (maxCharacter, maxValue) = quantities.max()

        println("Min: $minCharacter => $minValue")
        println("Max: $maxCharacter => $maxValue")

        assertEquals('H', minCharacter)
        assertEquals(161, minValue)
        assertEquals('B', maxCharacter)
        assertEquals(1749, maxValue)
    }

    @Test
    fun testGroupCount() {
        val count = createGroupCount("NNCB")
        assertEquals(
            mapOf(
                "NN" to 1L,
                "NC" to 1L,
                "CB" to 1L
            ),
            count
        )
    }

    @Test
    fun testInsertionMapCreation() {
        val map = mapOf(
            "CH" to 'B',
            "HH" to 'N',
            "CB" to 'H'
        )

        val insertionMap = map.toInsertionMap()

        assertEquals(
            mapOf(
                "CH" to Pair("CB", "BH"),
                "HH" to Pair("HN", "NH"),
                "CB" to Pair("CH", "HB")
            ),
            insertionMap
        )
    }

    @Test
    fun testCountAllTheThingsExample10Times() {
        val (template, map) = readFromString("""
            NNCB

            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent())

        val characterMap = map
            .mapValues { (_, character) -> character[0] }
        val insertionMap = characterMap.toInsertionMap()

        // NCNBCHB

        val countMap = countAllTheThings(
            template,
            10,
            characterMap,
            insertionMap
        )

        assertEquals(
            mapOf(
                'B' to 1749L,
                'C' to 298L,
                'H' to 161L,
                'N' to 865L,
            ),
            countMap,
        )
    }

    @Test
    fun testCountAllTheThingsExample40Times() {
        val (template, map) = readFromString("""
            NNCB

            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent())

        val characterMap = map.toCharacterMap()
        val insertionMap = characterMap.toInsertionMap()

        // NCNBCHB

        val countMap = countAllTheThings(
            template,
            40,
            characterMap,
            insertionMap
        )

        assertEquals(2192039569602L, countMap['B'])
        assertEquals(3849876073L, countMap['H'])

        val (maxChar, maxCount) = countMap.maxLong()
        val (minChar, minCount) = countMap.minLong()

        assertEquals('B', maxChar)
        assertEquals('H', minChar)

        assertEquals(
            2188189693529,
            maxCount - minCount
        )
    }
}