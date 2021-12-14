package day13

import java.io.File

data class Point(
    val x: Int,
    val y: Int
)

data class Paper(
    val points: Map<Point, Boolean>
) {
    fun fold(instruction: Instruction): Paper {
        return when (instruction) {
            is Instruction.FoldUp -> foldUp(instruction.y)
            is Instruction.FoldLeft -> foldLeft(instruction.x)
        }
    }

    fun fold(instructions: List<Instruction>): Paper {
        var paper = this

        for (instruction in instructions) {
            paper = paper.fold(instruction)
        }

        return paper
    }

    fun foldLeft(x: Int): Paper {
        val newPoints = mutableMapOf<Point, Boolean>()

        points.keys.forEach { point ->
            if (point.x < x) {
                newPoints[point] = true
            } else if (point.x > x) {
                val steps = point.x - x
                val mirroredPoint = Point(
                    x - steps,
                    point.y
                )
                newPoints[mirroredPoint] = true
            } else {
                throw IllegalStateException("Point on fold line")
            }
        }

        return Paper(newPoints)
    }

    fun foldUp(y: Int): Paper {
        val newPoints = mutableMapOf<Point, Boolean>()

        points.keys.forEach { point ->
            if (point.y < y) {
                newPoints[point] = true
            } else if (point.y > y) {
                val steps = point.y - y
                val mirroredPoint = Point(
                    point.x,
                    y - steps
                )
                newPoints[mirroredPoint] = true
            } else {
                throw IllegalStateException("Point on fold line")
            }
        }

        return Paper(newPoints)
    }

    fun countPoints(): Int {
        return points.size
    }

    fun isMarked(x: Int, y: Int): Boolean {
        return isMarked(Point(x, y))
    }

    fun isMarked(point: Point): Boolean {
        return points[point] == true
    }

    fun dump(): String {
        val size = size()

        return buildString {
            for (y in 0..size.y) {
                for (x in 0..size.x) {
                    if (isMarked(x, y)) {
                        append('#')
                    } else {
                        append('.')
                    }
                }
                appendLine()
            }
        }.trim()
    }

    fun size(): Point {
        var x = 0
        var y = 0

        points.keys.forEach { point ->
            x = if (point.x > x) point.x else x
            y = if (point.y > y) point.y else y
        }

        return Point(x, y)
    }
}

sealed interface Instruction {
    data class FoldUp(
        val y: Int
    ) : Instruction

    data class FoldLeft(
        val x: Int
    ) : Instruction
}

fun readFromFile(fileName: String): Pair<Paper, List<Instruction>> {
    return readFromString(File(fileName)
        .readText())
}

fun parsePoint(line: String): Point {
    val (x, y) = line.split(",")
    return Point(x.toInt(), y.toInt())
}

fun parseInstruction(line: String): Instruction {
    return when {
        line.startsWith("fold along x=") -> Instruction.FoldLeft(line.split("=")[1].toInt())
        line.startsWith("fold along y=") -> Instruction.FoldUp(line.split("=")[1].toInt())
        else -> throw IllegalStateException("Unknown instruction: $line")
    }
}

fun readFromString(input: String): Pair<Paper, List<Instruction>> {
    var parsingPoints = true
    val points = mutableMapOf<Point, Boolean>()
    val instructions = mutableListOf<Instruction>()

    input.lines().forEach { line ->
        when {
            line.isEmpty() -> parsingPoints = false
            parsingPoints -> points[parsePoint(line)] = true
            else -> instructions.add(parseInstruction(line))
        }
    }

    return Pair(Paper(points), instructions)
}

fun part1() {
    val (paper, instructions) = readFromFile("day13.txt")
    val instruction = instructions[0]

    val foldedPaper = paper.fold(instruction)
    val points = foldedPaper.countPoints()

    println("Points before fold: ${paper.countPoints()}")
    println("Points after fold: $points")
}

fun part2() {
    val (paper, instructions) = readFromFile("day13.txt")

    val foldedPaper = paper.fold(instructions)

    println(foldedPaper.dump())
}

fun main() {
    part2()
}
