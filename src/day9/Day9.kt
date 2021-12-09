package day9

import java.io.File

fun readInput(filename: String = "day9.txt"): List<List<Int>>{
    return File(filename)
        .readLines()
        .map { line -> line.toCharArray().map { character -> character.digitToInt() } }
}

fun List<List<Int>>.neighbours(x: Int, y: Int): List<Int> {
    val top = get(x).getOrNull(y - 1)
    val bottom = get(x).getOrNull(y + 1)
    val left = getOrNull(x - 1)?.get(y)
    val right = getOrNull(x + 1)?.get(y)

    return listOfNotNull(top, left, right, bottom)
}

fun List<List<Int>>.isValid(x: Int, y: Int): Boolean {
    return x in indices && y in get(0).indices
}

fun List<List<Int>>.neighbourPositions(x: Int, y: Int): List<Pair<Int, Int>> {
    val top = Pair(x, y - 1)
    val bottom = Pair(x, y + 1)
    val left = Pair(x - 1, y)
    val right = Pair(x + 1, y)

    return listOf(top, bottom, left, right).filter { (x, y) -> isValid(x, y) }
}

fun part1() {
    val input = readInput()
    var riskLevel = 0

    for (x in input.indices) {
        for (y in input[0].indices) {
            val current = input[x][y]
            val neighbours = input.neighbours(x, y)
            if (neighbours.all { value -> value > current }) {
                riskLevel += current + 1
            }
        }
    }

    println("Risk level: $riskLevel")
}

fun List<List<Int>>.findLowPoints(): List<Pair<Int, Int>> {
    val lowPoints = mutableListOf<Pair<Int, Int>>()

    for (x in indices) {
        for (y in get(0).indices) {
            val current = get(x)[y]
            val neighbours = neighbours(x, y)

            if (neighbours.all { value -> value > current }) {
                lowPoints.add(Pair(x, y))
            }
        }
    }

    return lowPoints
}

fun List<List<Int>>.findBasin(x: Int, y: Int): Set<Pair<Int, Int>> {
    val visitedPoints = mutableSetOf<Pair<Int, Int>>()
    val queue = mutableListOf(Pair(x, y))

    while (queue.isNotEmpty()) {
        val point = queue.removeFirst()
        visitedPoints.add(point)

        val neighbours = neighbourPositions(point.first, point.second)
        neighbours.forEach { neighbour ->
            if (!visitedPoints.contains(neighbour)) {
                if (get(neighbour.first)[neighbour.second] != 9) {
                    queue.add(neighbour)
                }
            }
        }
    }

    return visitedPoints
}

fun part2() {
    val input = readInput("day9.txt")
    val lowPoints = input.findLowPoints()

    val basins = lowPoints.map { point ->
        input.findBasin(point.first, point.second)
    }.sortedByDescending { points ->
        points.size
    }

    println("Basins: ${basins.size}")

    var result = 1
    basins.take(3).forEach { points ->
        result *= points.size
    }

    println("Result: $result")
}

fun main() {
    part2()
}