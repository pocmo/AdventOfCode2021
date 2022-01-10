package day18

import java.io.File
import java.lang.Long.max
import kotlin.math.ceil
import kotlin.math.floor

class Reader(
    private val value: String
) {
    var index = 0

    fun peek(): Char {
        return value[index]
    }

    fun consumeDigit(): Int {
        val digits = mutableListOf<Char>()

        while (peek().isDigit()) {
            digits.add(peek())
            index++
        }

        return digits.joinToString("").toInt()
    }

    fun consume(char: Char) {
        if (peek() == char) {
            index++
        } else {
            throw IllegalStateException("Expected $char but found ${peek()}")
        }
    }
}

class Parser {
    fun parse(input: String): Node {
        return parseNode(Reader(input))
    }

    fun parse(file: File): List<Node> {
        return parseLines(file.readLines())
    }

    fun parseLines(lines: List<String>): List<Node> {
        return lines.map { line -> parse(line) }
    }

    private fun parsePair(reader: Reader): Node.PairNode {
        reader.consume('[')

        val left = parseNode(reader)

        reader.consume(',')

        val right = parseNode(reader)

        reader.consume(']')

        return Node.PairNode(left, right).also {
            left.parent = it
            right.parent = it
        }
    }

    private fun parseNode(reader: Reader): Node {
        return when (reader.peek()) {
            '[' -> parsePair(reader)
            else -> parseValue(reader)
        }
    }

    private fun parseValue(reader: Reader): Node.ValueNode {
        return Node.ValueNode(reader.consumeDigit())
    }
}

sealed class Node {
    var parent: PairNode? = null

    data class PairNode(
        var left: Node,
        var right: Node
    ) : Node() {
        override fun toString(): String {
            return "[$left,$right]"
        }
    }

    data class ValueNode(
        var value: Int
    ) : Node() {
        override fun toString(): String {
            return value.toString()
        }
    }
}

fun Node.PairNode.leftAsValue(): Int {
    return (left as Node.ValueNode).value
}

fun Node.PairNode.rightAsValue(): Int {
    return (right as Node.ValueNode).value
}

operator fun Node.plus(node: Node): Node {
    return Node.PairNode(
        this, node
    ).also {
        it.left.parent = it
        it.right.parent = it
    }
}

fun Node.reduce() {
    var continueReduce = true

    while (continueReduce) {
        if (explode()) {
            continue
        } else if (split()) {
            continue
        } else {
            continueReduce = false
        }
    }
}

fun Node.PairNode.replace(node: Node, new: Node) {
    new.parent = this
    if (left === node) {
        left = new
    }
    if (right === node) {
        right = new
    }
}

fun Node.explode(
    level: Int = 0
): Boolean {
    if (this is Node.ValueNode) {
        return false
    }

    if (this !is Node.PairNode) {
        throw IllegalStateException("Expected PairNode")
    }

    if (level == 4) {
        parent!!.addLeft(leftAsValue(), this)
        parent!!.addRight(rightAsValue(), this)

        val value = Node.ValueNode(0)
        parent!!.replace(this, value)

        return true
    } else {
        if (left.explode(level + 1)) {
            return true
        }
        if (right.explode(level + 1)) {
            return true
        }
        return false
    }
}

fun Node.PairNode.addLeft(value: Int, left: Node) {
    if (left !== this.left) {
        // Go down
        this.left.addToChild(value, goLeft = false)
    } else {
        parent?.addLeft(value, this)
    }
}

fun Node.PairNode.addRight(value: Int, right: Node) {
    if (right !== this.right) {
        this.right.addToChild(value, goLeft = true)
    } else {
        parent?.addRight(value, this)
    }
}

fun Node.addToChild(value: Int, goLeft: Boolean) {
    val queue = mutableListOf<Node>()
    queue.add(this)

    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        if (node is Node.ValueNode) {
            node.value += value
            return
        }
        if (node is Node.PairNode) {
            if (goLeft) {
                queue.add(node.left)
            } else {
                queue.add(node.right)
            }
        }
    }
}

fun Node.split(): Boolean {
    if (this is Node.PairNode) {
        if (left.split()) {
            return true
        }
        if (right.split()) {
            return true
        }
        return false
    }

    if (this !is Node.ValueNode) {
        throw IllegalStateException("Split on non-ValueNode?!")
    }

    if (value >= 10) {
        val left = Node.ValueNode(floor(value.toDouble() / 2).toInt())
        val right = Node.ValueNode(ceil(value.toDouble() / 2).toInt())
        val pair = Node.PairNode(left, right).also {
            left.parent = it
            right.parent = it
        }
        parent!!.replace(this, pair)
        return true
    } else {
        return false
    }
}

fun List<Node>.sum(): Node {
    var sum = first()

    drop(1).forEach { current ->
        sum += current
        sum.reduce()
    }

    return sum
}

fun Node.magnitude(): Long {
    return when (this) {
        is Node.ValueNode -> value.toLong()
        is Node.PairNode -> (3 * left.magnitude()) + (2 * right.magnitude())
    }
}

fun Node.deepCopy(): Node {
    return when (this) {
        is Node.ValueNode -> Node.ValueNode(value)

        is Node.PairNode -> {
            val left = left.deepCopy()
            val right = right.deepCopy()

            Node.PairNode(left, right).also {
                left.parent = it
                right.parent = it
            }
        }
    }
}

fun List<Node>.largestPairMagnitude(): Long {
    var max: Long = 0

    for (a in this) {
        for (b in this) {
            if (a !== b) {
                val leftMagnitude = (a.deepCopy() + b.deepCopy()).also { it.reduce() }.magnitude()
                val rightMagnitude = (b.deepCopy() + a.deepCopy()).also { it.reduce() }.magnitude()
                max = max(max, leftMagnitude)
                max = max(max, rightMagnitude)
            }
        }
    }

    return max
}

fun part1() {
    val parser = Parser()
    val nodes = parser.parse(File("day18.txt"))

    val sum = nodes.sum()
    println("Magnitude: ${sum.magnitude()}")
}

fun part2() {
    val parser = Parser()
    val nodes = parser.parse(File("day18.txt"))

    val result = nodes.largestPairMagnitude()
    println("Largest magnitude of any sum: $result")
}

fun main() {
    part2()
}