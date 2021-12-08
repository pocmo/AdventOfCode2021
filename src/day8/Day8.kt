package day8

import java.io.File

fun find_one_chars(input: List<List<Char>>): List<Char> {
    return input.find { it.size == 2 } ?: throw IllegalStateException("One not found")
}

fun find_four_chars(input: List<List<Char>>): List<Char> {
    return input.find { it.size == 4 } ?: throw IllegalStateException("Four not found")
}

fun find_seven_chars(input: List<List<Char>>): List<Char> {
    return input.find { it.size == 3 } ?: throw IllegalStateException("Seven not found")
}

fun find_eight_chars(input: List<List<Char>>): List<Char> {
    return input.find { it.size == 7 } ?: throw IllegalStateException("Seven not found")
}

fun find_nine_chars(input: List<List<Char>>, four: List<Char>): List<Char> {
    return input.filter { it.size == 6 }
        .find { it.containsAll(four) }
        ?: throw IllegalStateException("Nine not found")
}

fun find_zero_chars(input: List<List<Char>>, one: List<Char>, nine: List<Char>): List<Char> {
    return input
        .filter { it.size == 6 }
        .filter { it.containsAll(one) }
        .firstOrNull() { it != nine } ?: throw IllegalStateException("Zero not found")
}

fun find_six_chars(input: List<List<Char>>, nine: List<Char>, zero: List<Char>): List<Char> {
    return input
        .filter { it.size == 6 }
        .filter { it != nine }
        .firstOrNull() { it != zero } ?: throw IllegalStateException("Six not found")
}

fun find_five_chars(input: List<List<Char>>, six: List<Char>): List<Char> {
    return input
        .filter { it.size == 5 }
        .firstOrNull { six.containsAll(it) }?: throw IllegalStateException("Five not found")
}

fun find_three_chars(input: List<List<Char>>, seven: List<Char>): List<Char> {
    return input
        .filter { it.size == 5 }
        .firstOrNull { it.containsAll(seven) } ?: throw IllegalStateException("Three not found")
}

fun find_two_chars(input: List<List<Char>>, threeChars: List<Char>, fiveChars: List<Char>): List<Char> {
    return input
        .filter { it.size == 5 }
        .firstOrNull { it != threeChars && it != fiveChars }  ?: throw IllegalStateException("Three not found")
}

fun findMappings(input: List<List<Char>>): Map<List<Char>, Char> {
    val oneChars = find_one_chars(input).sorted()
    val fourChars = find_four_chars(input).sorted()
    val sevenChars = find_seven_chars(input).sorted()
    val eightChars = find_eight_chars(input).sorted()
    val nineChars = find_nine_chars(input, fourChars).sorted()
    val zeroChars = find_zero_chars(input, oneChars, nineChars).sorted()
    val sixChars = find_six_chars(input, nineChars, zeroChars).sorted()
    val threeChars = find_three_chars(input, sevenChars).sorted()
    val fiveChars = find_five_chars(input, sixChars).sorted()
    val twoChars = find_two_chars(input, threeChars, fiveChars).sorted()

    val map = mapOf(
        zeroChars.sorted() to '0',
        oneChars.sorted() to '1',
        twoChars.sorted() to '2',
        threeChars.sorted() to '3',
        fourChars.sorted() to '4',
        fiveChars.sorted() to '5',
        sixChars.sorted() to '6',
        sevenChars.sorted() to '7',
        eightChars.sorted() to '8',
        nineChars.sorted() to '9'
    )

    if (map.keys.size != 10) {
        map.values.sorted().forEach {
            println(it)
        }
        println("3 = " + map[threeChars])
        throw IllegalStateException("Map size is not 10")
    }

    return map
}

fun translate(input: List<List<Char>>, value: List<List<Char>>): List<Char> {
    val mappings = findMappings(input)

    return value.map { segments ->
        mappings[segments.sorted()] ?: '.'
    }
}

/*

 aa
b  c
b  c
 dd
e  f
e  f
 gg

[x]  0: 6  abcefg
[x]  1: 2  cf
[x]  2: 5  acdeg
[x]  3: 5  acdfg
[x]  4: 4  bcdf
[x]  5: 5  abdfg
[x]  6: 6  abdefg
[x]  7: 3  acf
[x]  8: 7  abcdefg
[x]  9: 6  abcdfg

[x]  1: 2  cf
[x]  7: 3  acf
[x]  4: 4  bcdf
[x]  2: 5  acdeg
[x]  3: 5  acdfg
[x]  5: 5  abdfg
[x]  6: 6  abdefg
[x]  9: 6  abcdfg
[x]  0: 6  abcefg
[x]  8: 7  abcdefg


  0:      1:      2:      3:      4:
 aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
 ....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
 gggg    ....    gggg    gggg    ....

  5:      6:      7:      8:      9:
 aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
 dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
 gggg    gggg    ....    gggg    gggg
 */

fun readInput(filename: String = "day8.txt"): List<Pair<List<List<Char>>, List<List<Char>>>> {
    return File(filename)
        .readLines()
        .map { line ->
            val (rawInput, rawValue) = line.split("|")
            Pair(
                rawInput.trim().split(" ").map { it.toCharArray().toList().sorted() },
                rawValue.trim().split(" ").map { it.toCharArray().toList().sorted() },
            )
        }
}

fun part1() {
    val inputs = readInput()

    val sum = inputs.map { (input, value) ->
        translate(input, value)
    }.sumOf {
        it.count { character -> character in listOf('1', '4', '7', '8') }
    }

    println("Digits: $sum")
}

fun part2() {
    val inputs = readInput()

    val sum = inputs.map { (input, value) ->
        translate(input, value)
    }.map {
        it.joinToString("").toInt()
    }.sum()

    print(sum)
}

fun part2_debug() {
    val inputs = readInput("day8_debug.txt")

    inputs.map { (input, value) ->
        translate(input, value)
    }.forEach { translation ->
        println(translation.joinToString(""))
    }
}

fun main() {
    part2()
}