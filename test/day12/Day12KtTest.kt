package day12

import org.junit.Assert.*
import org.junit.Test

class Day12KtTest {
    @Test
    fun testReadCavesFromString() {
        val caveMap = readCavesFromString("""
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent())

        assertEquals(6, caveMap.size)
        assertEquals(2, caveMap[Cave("start")]?.size)
        assertEquals(4, caveMap[Cave("A")]?.size)
        assertEquals(4, caveMap[Cave("b")]?.size)
        assertEquals(1, caveMap[Cave("c")]?.size)
        assertEquals(1, caveMap[Cave("d")]?.size)
        assertEquals(2, caveMap[Cave("end")]?.size)
    }

    @Test
    fun findPathsSimple() {
        val caveMap = readCavesFromString("""
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent())

        val paths = caveMap.findPaths()

        assertEquals(10, paths.size)
    }

    @Test
    fun findPathsMediumSize() {
        val caveMap = readCavesFromString("""
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
        """.trimIndent())

        val paths = caveMap.findPaths()

        assertEquals(19, paths.size)
    }

    @Test
    fun testFindPathsLarge() {
        val caveMap = readCavesFromString("""
            fs-end
            he-DX
            fs-he
            start-DX
            pj-DX
            end-zg
            zg-sl
            zg-pj
            pj-he
            RW-he
            fs-DX
            pj-RW
            zg-RW
            start-pj
            he-WI
            zg-he
            pj-fs
            start-RW
        """.trimIndent())

        val paths = caveMap.findPaths()

        assertEquals(226, paths.size)
    }

    @Test
    fun testFindPathsSpecialSimple() {
        val caveMap = readCavesFromString("""
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent())

        val paths = caveMap.findPathsSpecial()

        assertEquals(36, paths.size)
    }

    @Test
    fun testFindPathsSpecialMedium() {
        val caveMap = readCavesFromString("""
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
        """.trimIndent())

        val paths = caveMap.findPathsSpecial()

        assertEquals(103, paths.size)
    }

    @Test
    fun testFindPathsSpecialLarge() {
        val caveMap = readCavesFromString("""
            fs-end
            he-DX
            fs-he
            start-DX
            pj-DX
            end-zg
            zg-sl
            zg-pj
            pj-he
            RW-he
            fs-DX
            pj-RW
            zg-RW
            start-pj
            he-WI
            zg-he
            pj-fs
            start-RW
        """.trimIndent())

        val paths = caveMap.findPathsSpecial()

        assertEquals(3509, paths.size)
    }
}