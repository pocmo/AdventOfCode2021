import java.io.File

fun List<Int>.countIncreases(): Int {
    var count = 0
    var previous: Int? = null

    forEach { current ->
        if (previous != null) {
            if (current > previous!!) {
                count++
            }
        }
        previous = current
    }

    return count
}

fun readValues(): List<Int> {
    val lines = File("day1.txt").readLines()
    return lines.map { it.toInt() }
}

fun day1_part1() {
    val values = readValues()
    val count = values.countIncreases()
    println("Increases: $count")
}

fun day1_part2() {
    val values = readValues()
    val newValues = mutableListOf<Int>()

    for (index in values.indices) {
        if (index < values.size - 2) {
            newValues.add(values[index] + values[index + 1] + values[index + 2])
        }
    }

    val count = newValues.countIncreases()
    println("Increases: $count")
}

fun main() {
    day1_part2()
}
