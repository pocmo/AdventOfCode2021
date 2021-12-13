package day10

import java.io.File
import java.lang.Exception

abstract class ParserException(
    message: String,
    val character: Char,
    val index: Int
) : Exception(message)

class ChunkParseException(
    message: String,
    character: Char,
    index: Int
) : ParserException(message, character, index)

class ChunkEndParseException(
    message: String,
    character: Char,
    val endCharacter: Char,
    index: Int
) : ParserException(message, character, index)

class IncompleteChunkException(
    message: String,
    character: Char,
    index: Int
) : ParserException(message, character, index)

class InputEOF: Exception()

data class Input(
    val value: String,
    var index: Int = 0
) {
    val isDone: Boolean
        get() = index == value.length

    fun proceed() {
        index++
    }

    fun currentAndProceed(): Pair<Char, Int> {
        val current = Pair(current(), index)
        proceed()
        return current
    }

    fun current(): Char {
        if (index >= value.length) {
            throw InputEOF()
        }
        return value[index]
    }

    fun withAppended(character: Char): Input {
        return Input("$value$character")
    }
}

fun parseInnerChunk(input: Input, endCharacter: Char) {
    var current = try {
        input.current()
    } catch (e: InputEOF) {
        throw IncompleteChunkException("Incomplete chunk, was looking for $endCharacter", endCharacter, input.index)
    }

    while (current != endCharacter) {
        try {
            parseChunk(input)
        } catch (e: ChunkParseException) {
            throw ChunkEndParseException(
                "Unexpected chunk end: ${e.character}, was looking for $endCharacter",
                e.character,
                endCharacter,
                e.index
            )
        } catch (e: InputEOF) {
            throw IncompleteChunkException("Incomplete chunk, was looking for $endCharacter", endCharacter, input.index)
        }

        try {
            current = input.current()
        } catch (e: InputEOF) {
            throw IncompleteChunkException("Incomplete chunk, was looking for $endCharacter", endCharacter, input.index)
        }
    }

    input.proceed()
}

fun parseChunk(input: Input) {
    val (current, index) = input.currentAndProceed()
    when (current) {
        '(' -> parseInnerChunk(input, ')')
        '[' -> parseInnerChunk(input, ']')
        '{' -> parseInnerChunk(input, '}')
        '<' -> parseInnerChunk(input, '>')
        else -> throw ChunkParseException("Invalid chunk start: $current", current, index)
    }
}

fun parseLine(input: Input) {
    while (!input.isDone) {
        parseChunk(input)
    }
}

fun readInputs(): List<Input> {
    return File("day10.txt")
        .readLines()
        .map { Input(it) }
}

fun part1() {
    val inputs = readInputs()

    var result = 0

    inputs.forEachIndexed { index, input ->
        try {
            parseLine(input)
            println("Line $index parsed successfully")
        } catch (e: ChunkEndParseException) {
            val points = getPoints(e.character)
            println("Line $index: Was looking for ${e.endCharacter}, but found ${e.character}")
            result += points
        } catch (e: ParserException) {
            println("Line $index parse error: ${e.message} (${e.index})")
        }
    }

    println("Result: $result")
}

fun calculateAutocompletePoints(input: Input): Long? {
    var points: Long = 0
    var current = input

    while (true) {
        try {
            parseLine(current)
            return points
        } catch (e: IncompleteChunkException) {
            println(e.message)
            println("Line: ${current.value}")
            println("Appending: ${e.character}")

            points *= 5
            points += getAutocompletePoints(e.character)

            current = current.withAppended(e.character)
            println("New Line: ${current.value}")
        } catch (e: ParserException) {
            if (points > 0) {
                throw IllegalStateException("Parser exception during autocomplete", e)
            }
            return null
        }
    }
}

fun getAutocompletePoints(character: Char): Int {
    return when (character) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw IllegalStateException("Not an ending character: $character")
    }
}

fun part2() {
    val inputs = readInputs()

    val points = inputs
        .mapNotNull { input -> calculateAutocompletePoints(input) }
        .sorted()

    val middle = points[points.size / 2]

    println("Autocomplete points: $middle")
}

fun main() {
    part2()
}

fun debug2() {
    val inputs = """
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent()
        .lines()
        .map { Input(it) }

    val points = inputs
        .mapNotNull { input ->
            println("#########################################")
            println("INPUT: $input")
            println("#########################################")
            val points = calculateAutocompletePoints(input)
            println("Points: $points")
            points
        }
        .sorted()

    println("Points: $points")

    val middle = points[points.size / 2]

    println("Autocomplete points: $middle")
}

fun debug() {
    val inputs = """
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent()
        .lines()
        .map { line -> Input(line) }

    var result = 0

    inputs.forEachIndexed { index, input ->
        try {
            parseLine(input)
            println("Line $index parsed successfully")
        } catch (e: ChunkEndParseException) {
            val points = getPoints(e.character)
            println("Line $index: Was looking for ${e.endCharacter}, but found ${e.character}")
            result += points
        } catch (e: ParserException) {
            println("Line $index parse error: ${e.message} (${e.index})")
        }
    }

    println("Result: $result")
}

fun getPoints(character: Char): Int {
    return when (character) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalStateException("No points for character: $character")
    }
}