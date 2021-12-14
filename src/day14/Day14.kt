package day14

import java.io.File

fun readFile(fileName: String): Pair<String, Map<String, String>> {
    return readFromString(File(fileName)
        .readText()
        .trim())
}

fun Map<String, String>.toCharacterMap(): Map<String, Char> {
    return mapValues { (_, character) -> character[0] }
}

fun performInsertion(template: String, map: Map<String, String>): String {
    val insertions = mutableListOf<String>()

    val groups = template.windowed(size = 2, step = 1, partialWindows = false)
    groups.forEach { group ->
        val insertion = map[group] ?: throw IllegalStateException("No mapping for $group")
        insertions.add(insertion)
    }

    return buildString {
        insertions.forEachIndexed { index, insertion ->
            append(template[index])
            append(insertion)
        }
        append(template.last())
    }
}

fun createGroupCount(template: String): Map<String, Long> {
    val map = mutableMapOf<String, Long>()
    template.windowed(size = 2, step = 1, partialWindows = false).forEach { group ->
        map.count(group, 1)
    }
    return map
}

fun MutableMap<Char, Long>.count(character: Char, count: Long) {
    val value = getOrDefault(character, 0L)
    val updated = value + count
    if (updated < value) {
        throw IllegalStateException("Oops, we failed")
    }
    put(character, updated)
}

fun MutableMap<String, Long>.count(group: String, count: Long = 1L) {
    val value = getOrDefault(group, 0L)
    val updated = value + count
    if (updated < value) {
        throw IllegalStateException("Oops, we failed")
    }
    put(group, updated)
}

fun Map<String, Char>.toInsertionMap(): Map<String, Pair<String, String>> {
    return mapValues { (group, character) ->
        Pair(
            "${group[0]}$character",
            "$character${group[1]}"
        )
    }
}

fun countAllTheThings(
    template: String,
    times: Int,
    characterMap: Map<String, Char>,
    insertionMap: Map<String, Pair<String, String>>
): Map<Char, Long> {
    val countCharacters = mutableMapOf<Char, Long>() // B -> 0
    var groupCount = createGroupCount(template)      // AB -> 0

    repeat(times) {
        val updatedGroupCount = mutableMapOf<String, Long>()

        groupCount.forEach { (group, count) ->
            val insertion = characterMap[group]
                ?: throw IllegalStateException("No character mapping for group: $group")
            countCharacters.count(insertion, count)

            val (first, second) = insertionMap[group]
                ?: throw IllegalStateException("No group mapping for group: $group")

            updatedGroupCount.count(first, count)
            updatedGroupCount.count(second, count)
        }

        groupCount = updatedGroupCount
    }

    template.toCharArray().forEach { character ->
        countCharacters.count(character, 1)
    }

    return countCharacters
}

fun countQuantities(input: String): Map<Char, Int> {
    return input.toCharArray().groupBy { it }
        .mapValues { (_, list) -> list.size }
}

fun Map<Char, Int>.max(): Pair<Char, Int> {
    return maxByOrNull { (_, count) -> count}!!.toPair()
}

fun Map<Char, Int>.min(): Pair<Char, Int> {
    return minByOrNull { (_, count) -> count}!!.toPair()
}

fun Map<Char, Long>.maxLong(): Pair<Char, Long> {
    return maxByOrNull { (_, count) -> count}!!.toPair()
}

fun Map<Char, Long>.minLong(): Pair<Char, Long> {
    return minByOrNull { (_, count) -> count}!!.toPair()
}

fun readFromString(input: String): Pair<String, Map<String, String>> {
    val lines = input.lines()
    val template = lines[0]
    val map = mutableMapOf<String, String>()

    for (line in lines.drop(2)) {
        val (from, to) = line.split(" -> ")
        map[from] = to
    }

    return Pair(template, map)
}

fun part1() {
    var (template, map) = readFile("day14.txt")

    repeat(10) {
       template = performInsertion(template, map)
    }

    val quantities = countQuantities(template)

    val (minCharacter, minValue) = quantities.min()
    val (maxCharacter, maxValue) = quantities.max()

    println("Min: $minCharacter => $minValue")
    println("Max: $maxCharacter => $maxValue")

    val result = maxValue - minValue

    println("Result: $result")
}

fun part2() {
    val (template, map) = readFile("day14.txt")

    val characterMap = map.toCharacterMap()
    val insertionMap = characterMap.toInsertionMap()

    val countMap = countAllTheThings(
        template,
        40,
        characterMap,
        insertionMap
    )

    val (maxChar, maxCount) = countMap.maxLong()
    val (minChar, minCount) = countMap.minLong()

    println("Max $maxChar = $maxCount")
    println("Min $minChar = $minCount")
    println("Result = ${maxCount - minCount}")
}

fun main() {
    println(">> PART 1")
    part1()
    println(">> PART 2")
    part2()
}
