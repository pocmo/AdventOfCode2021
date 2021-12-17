package day17

import kotlin.math.max

data class TargetArea(
    val xRange: IntRange,
    val yRange: IntRange
) {
    fun calculateResult(x: Int, y: Int): AreaResult {
        return when {
            y < yRange.minOrNull()!! -> AreaResult.MissBottom
            x > xRange.maxOrNull()!! -> AreaResult.MissRight
            xRange.contains(x) && yRange.contains(y) -> AreaResult.Hit
            else -> AreaResult.Ongoing
        }
    }
}

data class Velocity(
    val x: Int,
    val y: Int
) {
    fun simulate(): Velocity {
        val newX = if (x > 0) {
            x - 1
        } else if (x < 0) {
            x + 1
        } else {
            0
        }

        val newY = y - 1

        return Velocity(newX, newY)
    }
}

sealed interface AreaResult {
    object Hit : AreaResult
    object Ongoing : AreaResult
    object MissBottom : AreaResult
    object MissRight : AreaResult
}

sealed interface Result {
    data class Hit(
        val maxHeight: Int
    ) : Result

    object MissBottom : Result
    object MissRight : Result
}

fun AreaResult.toResult(
    maxHeight: Int
): Result {
    return when (this) {
        is AreaResult.Ongoing -> throw IllegalStateException("Translating ongoing area result")
        is AreaResult.Hit -> Result.Hit(maxHeight)
        is AreaResult.MissBottom -> Result.MissBottom
        is AreaResult.MissRight -> Result.MissRight
    }
}

fun launchProbe(initial: Velocity, target: TargetArea): Result {
    var x = initial.x
    var y = initial.y
    var velocity = initial
    var maxHeight = initial.y
    var areaResult: AreaResult = target.calculateResult(x, y)

    while (areaResult is AreaResult.Ongoing) {
        velocity = velocity.simulate()
        x += velocity.x
        y += velocity.y
        areaResult = target.calculateResult(x, y)
        maxHeight = max(maxHeight, y)
    }

    return areaResult.toResult(maxHeight)
}

data class VelocityResults(
    val maxHeight: Int,
    val count: Int
)

fun findAllProbeVelocities(
    target: TargetArea,
    maxPossibleHeight: Int = 20
): VelocityResults {
    val xRange = 1..target.xRange.last // Too large (on the left side)
    val yRange = target.yRange.first .. maxPossibleHeight
    var count = 0
    var maxHeight = Int.MIN_VALUE

    for (x in xRange) {
        for (y in yRange) {
            val result = launchProbe(
                Velocity(x, y),
                target
            )

            if (result is Result.Hit) {
                maxHeight = max(maxHeight, result.maxHeight)
                count++
            }
        }
    }

    if (maxHeight == Int.MIN_VALUE) {
        throw IllegalStateException("Could not find max height")
    }

    return VelocityResults(maxHeight, count)
}

fun part1() {
    val target = TargetArea(
        xRange = 153..199,
        yRange = -114..-75
    )

    val result = findAllProbeVelocities(
        target,
        maxPossibleHeight = 1500
    )

    println("Result: $result")
}

fun part2() {
    val target = TargetArea(
        xRange = 153..199,
        yRange = -114..-75
    )

    val result = findAllProbeVelocities(
        target,
        maxPossibleHeight = 1000
    )

    println("Result: $result")
}

fun main() {
    part2()
}
