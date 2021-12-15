package day15

import java.io.File

data class Point(
    val x: Int,
    val y: Int
)

data class Position(
    val point: Point,
    val riskLevel: Int,
    var from: Point? = null,
    var totalRisk: Int = Int.MAX_VALUE,
    var visited: Boolean = false,
    var queued: Boolean = false
)

data class Path(
    val totalRisk: Int,
    val positions: List<Position>
)

data class Cavern(
    val map: List<List<Position>>
) {
    val size by lazy {
        Point(
            x = map[0].size,
            y = map.size
        )
    }

    val start by lazy {
        Point(0, 0)
    }

    val end by lazy {
        Point(size.x - 1, size.y - 1)
    }

    fun dump(
        path: Path? = null,
        debug: Boolean = false
    ): String {
        return buildString {
            map.forEach { line ->
                line.forEach { position ->
                    if (debug) {
                        if (path != null && position in path.positions) {
                            append("[")
                            append(position.riskLevel)
                            append("]")
                        } else if (position.visited) {
                            append("<")
                            append(position.riskLevel)
                            append(">")
                        } else {
                            append(" ")
                            append(position.riskLevel)
                            append(" ")
                        }
                    } else {
                        append(position.riskLevel)
                    }
                }
                appendLine()
            }
        }.trim()
    }

    fun isStart(point: Point): Boolean {
        return point.x == 0 && point.y == 0
    }

    fun isEnd(point: Point): Boolean {
        return point.x == size.x - 1 && point.y == size.y - 1
    }

    fun get(x: Int, y: Int): Position {
        return map[y][x]
    }

    fun getOrNull(x: Int, y: Int): Position? {
        return map.getOrNull(y)?.getOrNull(x)
    }

    fun get(position: Position): Position {
        return get(position.point.x, position.point.y)
    }

    fun get(point: Point): Position {
        return get(point.x, point.y)
    }

    fun neighbor(position: Position): List<Position> {
        val x = position.point.x
        val y = position.point.y

        return listOfNotNull(
            getOrNull(x, y - 1), // top
            getOrNull(x - 1, y), // left
            getOrNull(x + 1, y), // right
            getOrNull(x, y + 1), // bottom
        )
    }

    fun constructPath(end: Position, startPoint: Point): Path {
        val positions = mutableListOf<Position>()

        var current = end
        while (current.point != startPoint) {
            positions.add(current)
            current = current.from?.let { get(it) } ?: throw IllegalStateException("No path from ${current.point}")
        }

        positions.add(get(startPoint))

        return Path(
            end.totalRisk,
            positions
        )
    }

    fun findPathWithLowestRisk(
        startPoint: Point = start,
        endPoint: Point = end,
    ): Path {
        val start = get(startPoint)
        start.totalRisk = 0

        var visitQueue = mutableListOf<Position>()
        visitQueue.add(start)

        while (visitQueue.isNotEmpty()) {
            val current = visitQueue.removeFirst()
            current.visited = true

            if (current.point == endPoint) {
                return constructPath(current, startPoint)
            }

            neighbor(current)
                .filter { position -> !position.visited }
                .forEach { position ->
                    val totalRisk = current.totalRisk + position.riskLevel
                    if (totalRisk < position.totalRisk) {
                        position.totalRisk = totalRisk
                        position.from = current.point
                    }

                    if (!position.queued) {
                        visitQueue.add(position)
                        position.queued = true
                    }
                }

            visitQueue = visitQueue.sortedBy { position -> position.totalRisk }.toMutableList()

        }

        throw IllegalStateException("Empty queue. And we didn't find the end.")
    }

    fun expand(times: Int): Cavern {
        val lines = mutableListOf<MutableList<Position>>()

        for (y in 0 until size.y * times) {
            val line = mutableListOf<Position>()

            for (x in 0 until size.x * times) {
                val translateX = (x % size.x)
                val translateY = (y % size.y)

                val shiftX = x / size.x
                val shiftY = y / size.y

                val source = get(translateX, translateY)
                line.add(Position(
                    Point(x, y),
                    riskLevel = (source.riskLevel + shiftX + shiftY).clamp(9)
                ))
            }

            lines.add(line)
        }

        return Cavern(lines)
    }
}

fun Int.clamp(max: Int): Int {
    var current = this
    while (current > max) {
        current -= max
    }
    return current
}

fun readFromFile(fileName: String): Cavern {
    return readFromString(File(fileName)
        .readText()
        .trim())
}

fun readFromString(input: String): Cavern {
    return Cavern(input.lines()
        .mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, character ->
                Position(
                    Point(x, y),
                    character.digitToInt()
                )
            }
        })
}

fun part1() {
    val cavern = readFromFile("day15.txt")
    val path = cavern.findPathWithLowestRisk(
        cavern.start,
        cavern.end
    )

    println("Found path with risk level = ${path.totalRisk}")
}

fun part2() {
    val cavern = readFromFile("day15.txt")
    val expandedCavern = cavern.expand(5)

    val path = expandedCavern.findPathWithLowestRisk()

    println("Found path with risk level = ${path.totalRisk}")
}

fun main() {
    part1()
    part2()
}