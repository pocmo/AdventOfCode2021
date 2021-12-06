import java.io.File

fun readFishMap(): MutableMap<Int, Long> {
    val fishes = File("day6.txt")
        .readLines()[0]
        .split(",")
        .map { it.toInt() }

    val map = mutableMapOf<Int, Long>()
    fishes.forEach { fish ->
        val count = map.getOrDefault(fish, 0)
        map[fish] = count + 1
    }

    return map
}

fun MutableMap<Int, Long>.simulateLanternFishes() {
    val reproducingFishes = get(0) ?: 0

    for (i in 1 .. 8) {
        val count = get(i) ?: 0
        put(i - 1, count)
    }

    put(8, reproducingFishes)
    val sixFishesCount = get(6) ?: 0
    put(6, sixFishesCount + reproducingFishes)
}

fun part2() {
    val map = readFishMap()
    println(map)

    repeat(256) {
        map.simulateLanternFishes()
    }

    println(map)

    val fishCount = map.values.sum()
    println("Total fishes: $fishCount")
}

fun part1() {
    val map = readFishMap()
    println(map)

    repeat(80) {
        map.simulateLanternFishes()
    }

    println(map)

    val fishCount = map.values.sum()
    println("Total fishes: $fishCount")
}

fun main() {
    part2()
}