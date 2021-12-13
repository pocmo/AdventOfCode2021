package day11

import java.io.File

data class Position(
    val x: Int,
    val y: Int
)

data class Octopus(
    var energy: Int,
    var flashed: Boolean = false
) {
    fun canFlash(): Boolean = energy > 9 && !flashed

    fun flash() {
        energy = 0
        flashed = true
    }

    fun reset() {
        if (flashed) {
            flashed = false
            energy = 0
        }
    }
}

fun List<List<Octopus>>.dump(): String {
    val octopuses = this
    return buildString {
        octopuses.forEach { row ->
            row.forEach { octopus ->
                append(octopus.energy)
            }
            appendLine()
        }
    }.trim()
}

fun List<List<Octopus>>.increase() {
    forEach { row ->
        row.forEach { octopus ->
            octopus.energy++
        }
    }
}

fun List<List<Octopus>>.increase(neighbors: List<Position>) {
    neighbors.forEach { neighbor ->
        get(neighbor.y)[neighbor.x].energy++
    }
}

fun List<List<Octopus>>.neighborsOf(x: Int, y: Int): List<Position> {
    val candidates = listOf(
        Position(x - 1, y - 1),
        Position(x, y - 1),
        Position(x + 1, y - 1),

        Position(x - 1, y),
        Position(x + 1, y),

        Position(x - 1, y + 1),
        Position(x, y + 1),
        Position(x + 1, y + 1),
    )

    return candidates.filter { candidate ->
        candidate.y in indices &&
        candidate.x in get(0).indices
    }
}

fun List<List<Octopus>>.flash(): Pair<Int, List<Position>> {
    val neighbors = mutableListOf<Position>()
    var flashes = 0

    indices.forEach { y ->
        get(y).indices.forEach { x ->
            val octopus = get(y)[x]
            if (octopus.canFlash()) {
                octopus.flash()
                flashes++
                neighbors.addAll(neighborsOf(x, y))
            }
        }
    }

    return Pair(flashes, neighbors)
}

fun List<List<Octopus>>.simulate(): Int {
    increase()

    var (flashes, neighbors) = flash()
    while (neighbors.isNotEmpty()) {
        increase(neighbors)
        val (additionalFlashes, newNeighbors) = flash()
        neighbors = newNeighbors
        flashes += additionalFlashes
    }

    reset()

    return flashes
}

fun List<List<Octopus>>.simulate(times: Int): Int {
    var flashes = 0

    repeat(times) {
        flashes += simulate()
    }

    return flashes
}

fun List<List<Octopus>>.reset() {
    forEach { row ->
        row.forEach { octopus ->
            octopus.reset()
        }
    }
}

fun List<List<Octopus>>.allFlashed(): Boolean {
    return all { row ->
        row.all { octopus -> octopus.energy == 0 }
    }
}

fun List<List<Octopus>>.simulateUntilAllFlashed(): Int {
    var step = 0
    while (!allFlashed()) {
        simulate()
        step++
    }
    return step
}

fun readOctopuses(fileName: String): List<List<Octopus>> {
    return readOctopusesFromString(File(fileName)
        .readText())
}

fun readOctopusesFromString(input: String): List<List<Octopus>> {
    return input
        .lines()
        .map { line ->
            line.toCharArray().map { value -> Octopus(value.digitToInt()) }
        }
}

fun part1() {
    val octopuses = readOctopuses("day11.txt")

    val flashes = octopuses.simulate(100)

    println("Flashes = $flashes")
}

fun part2() {
    val octopuses = readOctopuses("day11.txt")

    val steps = octopuses.simulateUntilAllFlashed()

    println("Steps = $steps")
}

fun main() {
    part2()
}
