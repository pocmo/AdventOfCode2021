package day17

import org.junit.Assert.*
import org.junit.Test

class Day17KtTest {
    @Test
    fun `Velocity (7,2)`() {
        val target = TargetArea(
            xRange = 20..30,
            yRange = -10 .. -5
        )

        val result = launchProbe(
            Velocity(7,2),
            target
        )

        println("Result: $result")

        assertTrue(result is Result.Hit)

        val hit = result as Result.Hit
        assertEquals(3, hit.maxHeight)
    }

    @Test
    fun `Velocity(6,3)`() {
        val target = TargetArea(
            xRange = 20..30,
            yRange = -10 .. -5
        )

        val result = launchProbe(
            Velocity(6,3),
            target
        )

        println("Result: $result")

        assertTrue(result is Result.Hit)

        val hit = result as Result.Hit
        assertEquals(6, hit.maxHeight)
    }

    @Test
    fun `Velocity(9,0)`() {
        val target = TargetArea(
            xRange = 20..30,
            yRange = -10 .. -5
        )

        val result = launchProbe(
            Velocity(9,0),
            target
        )

        println("Result: $result")

        assertTrue(result is Result.Hit)

        val hit = result as Result.Hit
        assertEquals(0, hit.maxHeight)
    }

    @Test
    fun `Velocity(17,-4)`() {
        val target = TargetArea(
            xRange = 20..30,
            yRange = -10 .. -5
        )

        val result = launchProbe(
            Velocity(17,-4),
            target
        )

        println("Result: $result")

        assertTrue(result is Result.MissRight)
    }

    @Test
    fun `Velocity(6,9)`() {
        val target = TargetArea(
            xRange = 20..30,
            yRange = -10 .. -5
        )

        val result = launchProbe(
            Velocity(6,9),
            target
        )

        println("Result: $result")

        assertTrue(result is Result.Hit)

        val hit = result as Result.Hit
        assertEquals(45, hit.maxHeight)
    }

    @Test
    fun `Find max height of example`() {
        val target = TargetArea(
            xRange = 20..30,
            yRange = -10 .. -5
        )

        val result = findAllProbeVelocities(target)

        assertEquals(45, result.maxHeight)
    }

    @Test
    fun `Find number of hitting velocities`() {
        val target = TargetArea(
            xRange = 20..30,
            yRange = -10 .. -5
        )

        val result = findAllProbeVelocities(
            target,
            maxPossibleHeight = 200
        )

        assertEquals(112, result.count)
    }
}
