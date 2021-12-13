package day12

import java.io.File

data class Cave(
    val name: String
) {
    fun isEnd() = name == "end"
    fun isStart() = name == "start"

    fun isLarge(): Boolean = name[0].isUpperCase()

    fun isSmall() = !isStart() && !isEnd() && name[0].isLowerCase()
}

fun readCavesFromFile(fileName: String): Map<Cave, MutableList<Cave>> {
    return readCavesFromString(File(fileName)
        .readText())
}

fun readCavesFromString(input: String): Map<Cave, MutableList<Cave>> {
    val caveMap = mutableMapOf<Cave, MutableList<Cave>>()

    input.lines()
        .map { line ->
            val (a, b) = line.split("-")
            Pair(a, b)
        }
        .forEach { (a, b) ->
            val caveA = Cave(a)
            val caveB = Cave(b)

            val connectionsA = caveMap.getOrDefault(caveA, mutableListOf())
            connectionsA.add(caveB)
            caveMap[caveA] = connectionsA

            val connectionB = caveMap.getOrDefault(caveB, mutableListOf())
            connectionB.add(caveA)
            caveMap[caveB] = connectionB
        }

    return caveMap
}

fun List<Cave>.visitedOnlyOnce(cave: Cave): Boolean {
    return count { current -> current == cave } == 1
}

fun Map<Cave, MutableList<Cave>>.continuePath(
    cave: Cave,
    path: List<Cave>,
    visited: List<Cave>,
    smallCaveToVisitAgain: Cave? = null
): List<List<Cave>> {
    val newPath = path + cave

    if (cave.isEnd()) {
        return listOf(newPath)
    }

    val connections = (get(cave) ?: throw IllegalStateException("No connection from $cave"))
        .filter { current ->
            val onlyVisitedOnce = visited.visitedOnlyOnce(current)
            val isSpecialSmallCave = current == smallCaveToVisitAgain
            current.isLarge() || current !in visited || (isSpecialSmallCave && onlyVisitedOnce)
        }

    if (connections.isEmpty()) {
        return emptyList()
    }

    val paths = mutableListOf<List<Cave>>()

    for (current in connections) {
        val newPaths = continuePath(
            current,
            newPath,
            visited + cave,
            smallCaveToVisitAgain
        )
        paths.addAll(newPaths)
    }

    return paths
}

fun Map<Cave, MutableList<Cave>>.findPaths(): List<List<Cave>> {
    return continuePath(
        Cave("start"),
        listOf(),
        listOf()
    )
}

fun Map<Cave, MutableList<Cave>>.findPathsSpecial(): List<List<Cave>> {
    val paths = mutableListOf<List<Cave>>()

    keys.filter { cave -> cave.isSmall() }
        .forEach { cave ->
            val currentPaths = continuePath(
                Cave("start"),
                listOf(),
                listOf(),
                cave
            )
            paths.addAll(currentPaths)
        }

    return paths.distinct()
}


fun part1() {
    val caveMap = readCavesFromFile("day12.txt")
    val paths = caveMap.findPaths()
    println("Paths = ${paths.size}")
}

fun part2() {
    val caveMap = readCavesFromFile("day12.txt")
    val paths = caveMap.findPathsSpecial()
    println("Paths = ${paths.size}")
}

fun main() {
    part2()
}
