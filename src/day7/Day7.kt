package day7

import java.io.File
import kotlin.math.absoluteValue

fun readCrabSubmarines(): List<Int> {
    return File("day7.txt")
        .readLines()[0]
        .split(",")
        .map { it.toInt() }
}

fun calculateFuelSimple(crabSubmarines: List<Int>, position: Int): Int {
    return crabSubmarines.sumOf { current ->
        (current - position).absoluteValue
    }
}

fun calculateFuelComplex(crabSubmarines: List<Int>, position: Int): Int {
    return crabSubmarines.sumOf { current ->
        val steps = (current - position).absoluteValue
        (steps * (steps + 1)) / 2
    }
}

fun List<Int>.findCheapestPosition(
    calculateFuel: (List<Int>, Int) -> Int
): Pair<Int, Int> {
    val min = minOrNull() ?: throw IllegalStateException("No min")
    val max = maxOrNull() ?: throw IllegalStateException("No max")

    println("Range: $min - $max")

    var cheapestPosition: Int? = null
    var fuelNeeded: Int? = null

    for (position in min..max) {
        val fuel = calculateFuel(this, position)
        if (fuelNeeded == null || fuel < fuelNeeded) {
            cheapestPosition = position
            fuelNeeded = fuel
        }
    }

    return Pair(cheapestPosition!!, fuelNeeded!!)
}

fun part2() {
    val crabSubmarines = readCrabSubmarines()
    val (position, fuel) = crabSubmarines.findCheapestPosition { submarines, position ->
        calculateFuelComplex(submarines, position)
    }

    println("Position: $position")
    println("Fuel: $fuel")
}

fun part1() {
    val crabSubmarines = readCrabSubmarines()
    val (position, fuel) = crabSubmarines.findCheapestPosition { submarines, position ->
        calculateFuelSimple(submarines, position)
    }

    println("Position: $position")
    println("Fuel: $fuel")
}

fun main() {
    part2()
}