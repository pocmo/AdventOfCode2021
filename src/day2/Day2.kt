package day2

import java.io.File

sealed interface Command {
    data class Forward(
        val steps: Int
    ): Command

    data class Down(
        val steps: Int
    ) : Command

    data class Up(
        val steps: Int
    ) : Command
}

fun readCourse(): List<Command> {
    val lines = File("day2.txt").readLines()
    return lines.map { line ->
        val (command, steps) = line.split(" ").let { raw ->
            Pair(raw[0], raw[1].toInt())
        }
        when (command) {
            "forward" -> Command.Forward(steps)
            "down" -> Command.Down(steps)
            "up" -> Command.Up(steps)
            else -> throw IllegalStateException("Unknown command: $command")
        }
    }
}

class Submarine {
    var horizontalPosition = 0
        private set
    var depth = 0
        private set
    var aim = 0
        private set

    fun process(course: List<Command>) {
        course.forEach { command ->
            when (command) {
                is Command.Forward -> horizontalPosition += command.steps
                is Command.Up -> depth -= command.steps
                is Command.Down -> depth += command.steps
            }
        }
    }

    fun processNew(course: List<Command>) {
        course.forEach { command ->
            when (command) {
                is Command.Forward -> {
                    horizontalPosition += command.steps
                    depth += aim * command.steps
                }
                is Command.Up -> {
                    aim -= command.steps
                }
                is Command.Down -> {
                    aim += command.steps
                }
            }
        }
    }

    fun getSolution(): Int {
        return horizontalPosition * depth
    }
}

fun part1() {
    val course = readCourse()

    println("Size: " + course.size)

    val submarine = Submarine()
    submarine.process(course)

    println("Position: " + submarine.horizontalPosition)
    println("Depth: " + submarine.depth)

    println(submarine.getSolution())
}

fun part2() {
    val course = readCourse()

    println("Size: " + course.size)

    val submarine = Submarine()
    submarine.processNew(course)

    println("Position: " + submarine.horizontalPosition)
    println("Depth: " + submarine.depth)
    println("Aim: " + submarine.aim)

    println("Solution: " + submarine.getSolution())
}

fun main() {
    part2()
}