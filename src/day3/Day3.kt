package day3

import java.io.File

data class Count(
    var zeros: Int = 0,
    var ones: Int = 0
)

fun part1() {
    val lines = File("day3.txt").readLines()

    val counts: List<Count> = generateSequence {
        Count()
    }.take(12).toList()

    lines.forEach { line ->
        line.forEachIndexed { index, character ->
            when (character) {
                '0' -> counts[index].zeros++
                '1' -> counts[index].ones++
            }
        }
    }

    val gammaRateRaw = counts.joinToString("") { count ->
        if (count.ones > count.zeros) {
            "1"
        } else if (count.zeros > count.ones) {
            "0"
        } else {
            throw IllegalStateException("No most common bit")
        }
    }

    val epsilonRateRaw = gammaRateRaw.map { character ->
        when (character) {
            '0' -> '1'
            '1' -> '0'
            else -> throw IllegalStateException("Unknown character: $character")
        }
    }.joinToString("")

    val gammaRate = Integer.parseInt(gammaRateRaw, 2)
    val epsilonRate = Integer.parseInt(epsilonRateRaw, 2)

    println("gamma: $gammaRateRaw -> $gammaRate")
    println("gamma: $epsilonRateRaw -> $epsilonRate")
    println("part1 solution = ${gammaRate * epsilonRate}")
}

fun findCount(values: List<String>, position: Int): Count {
    val count = Count()

    values.forEach { line ->
        val character = line[position]
        when (character) {
            '0' -> count.zeros++
            '1' -> count.ones++
        }
    }

    return count
}

fun findRating(
    values: List<String>,
    position: Int,
    filter: (String, Int, Count) -> Boolean
): String {
    val count = findCount(values, position)
    val filteredValues = values.filter { value ->
        filter(value, position, count)
    }
    return if (filteredValues.size == 1) {
        filteredValues[0]
    } else if (filteredValues.size > 1) {
        findRating(filteredValues, position + 1, filter)
    } else {
        throw IllegalStateException("Could not find values")
    }
}

fun findOxygenBit(count: Count): Char {
    return if (count.ones > count.zeros) {
        '1'
    } else if (count.zeros > count.ones) {
        '0'
    } else {
        '1'
    }
}

fun findCo2Bit(count: Count): Char {
    return if (count.ones < count.zeros) {
        '1'
    } else if (count.zeros < count.ones) {
        '0'
    } else {
        '0'
    }
}

fun part2() {
    val values = File("day3.txt").readLines()

    val oxygenRatingRaw = findRating(values, 0) { value, position, count ->
        val valueAtPosition = value[position]
        valueAtPosition == findOxygenBit(count)
    }
    val oxygenRating = Integer.parseInt(oxygenRatingRaw, 2)
    println("oxygen $oxygenRatingRaw -> $oxygenRating")

    val co2RatingRaw = findRating(values, 0) { value, position, count ->
        val valueAtPosition = value[position]
        valueAtPosition == findCo2Bit(count)
    }
    val co2Rating = Integer.parseInt(co2RatingRaw, 2)
    println("co2 $co2RatingRaw -> $co2Rating")

    println("Solution ${oxygenRating * co2Rating}")
}

fun main() {
    part2()
}