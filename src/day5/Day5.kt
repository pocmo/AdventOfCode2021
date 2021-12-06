package day5

import java.io.File

data class Line(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int
) {
    private fun isHorizontal(): Boolean = y1 == y2
    private fun isVertical(): Boolean = x1 == x2
    private fun isDiagonal(): Boolean = !isHorizontal() && !isVertical()
    fun isHorizontalOrVertical(): Boolean = isHorizontal() || isVertical()

    fun getPoints(): List<Pair<Int, Int>> {
        return if (isHorizontal()) {
            val points = mutableListOf<Pair<Int, Int>>()
            val startX = x1.coerceAtMost(x2)
            val endX = x1.coerceAtLeast(x2)
            for (x in startX..endX) {
                points.add(Pair(x, y1))
            }
            points
        } else if (isVertical()) {
            val points = mutableListOf<Pair<Int, Int>>()
            val startY = y1.coerceAtMost(y2)
            val endY = y1.coerceAtLeast(y2)
            for (y in startY..endY) {
                points.add(Pair(x1, y))
            }
            points
        } else if (isDiagonal()) {
            getDiagonalPoints(x1, y1, x2, y2)
        } else {
            throw IllegalStateException("Line is not horizontal, vertical or diagonal")
        }
    }
}

fun getDiagonalPoints(startX: Int, startY: Int, endX: Int, endY: Int): List<Pair<Int, Int>> {
    val points = mutableListOf<Pair<Int, Int>>()
    val start = Pair(startX, startY)
    val end = Pair(endX, endY)
    val xDiff = end.first - start.first
    val yDiff = end.second - start.second
    val xStep = if (xDiff > 0) 1 else -1
    val yStep = if (yDiff > 0) 1 else -1
    var x = start.first
    var y = start.second
    while (x != end.first || y != end.second) {
        points.add(Pair(x, y))
        x += xStep
        y += yStep
    }
    points.add(Pair(x, y))
    return points
}

fun readLines(): List<Line> {
    val values = File("day5.txt").readLines()

    return values.map { line ->
        val (point1, point2) = line.split(" -> ")
        val (x1, y1) = point1.split(",")
        val (x2, y2) = point2.split(",")
        Line(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
    }
}

fun List<Pair<Int, Int>>.getDuplicates(): Set<Pair<Int, Int>> {
    val counts = mutableMapOf<Pair<Int, Int>, Int>()
    for (point in this) {
        val count = counts.getOrDefault(point, 0)
        counts[point] = count + 1
    }
    return counts.filter { it.value > 1 }.keys
}

fun part1() {
    val points = readLines()
        .filter { it.isHorizontalOrVertical() }
        .map { it.getPoints() }
        .flatten()
        .getDuplicates()

    println(points.size)
}

fun part2() {
    val points = readLines()
        .map { it.getPoints() }
        .flatten()
        .getDuplicates()

    // Wong: 22982
    // Right: 21406

    println(points.size)
}

fun main() {
    part2()
}
