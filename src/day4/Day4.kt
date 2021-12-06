package day4

import java.io.File

data class Number(
    val value: Int,
    var marked: Boolean = false
)

data class Board(
    val data: Array<Array<Number>> = Array(5) { Array(5) { Number(0) } }
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Board) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String {
        return data.joinToString("\n") { row ->
            row.joinToString(" ") { number ->
                if (number.marked) "X" else number.value.toString()
            }
        }
    }

    fun mark(value: Int) {
        data.forEach { row ->
            row.forEach { number ->
                if (number.value == value) number.marked = true
            }
        }
    }

    fun sumUnmarkedNumbers(): Int {
        return data.flatten().filter { !it.marked }.sumBy { it.value }
    }

    fun isWinner(): Boolean {
        return isRowWinner() || isColumnWinner()
    }

    private fun isRowWinner(): Boolean {
        for (y in 0 until 5) {
            var winner = true
            for (x in 0 until 5) {
                if (!data[y][x].marked) {
                    winner = false
                }
            }
            if (winner) return true
         }

        return false
    }

    private fun isColumnWinner(): Boolean {
        for (x in 0 until 5) {
            var winner = true
            for (y in 0 until 5) {
                if (!data[y][x].marked) {
                    winner = false
                }
            }
            if (winner) return true
        }

        return false
    }
}

fun readBoard(data: List<String>, position: Int): Board {
    val boardData = data.subList(position, position + 5)
    val board = Board()

    boardData.forEachIndexed { row, line ->
        val rowData = line.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        rowData.forEachIndexed { column, value ->
            board.data[row][column] = Number(value)
        }
    }

    return board
}

fun readData(): Pair<List<Int>, List<Board>> {
    val values = File("day4.txt").readLines()

    val numbers = values[0].split(",").map { it.toInt() }
    val boards = mutableListOf<Board>()

    for (i in 2 until values.size step 6) {
        val board = readBoard(values, i)
        boards.add(board)
    }

    return Pair(numbers, boards)
}

fun findWinningBoard(numbers: List<Int>, boards: List<Board>): Pair<Int, Board> {
    for (number in numbers) {
        for (board in boards) {
            board.mark(number)
            if (board.isWinner()) {
                return Pair(number, board)
            }
        }
    }

    throw IllegalStateException("No winning board found")
}

fun part1() {
    val (numbers, boards) = readData()

    val (number, board) = findWinningBoard(numbers, boards)

    println("Winning number: $number")
    println("Winning board:")
    println(board)

    val sum = board.sumUnmarkedNumbers()
    println("Unmarked numbers sum: $sum")

    println()
    println("Result: ${sum * number}")
}

fun part2() {
    val (numbers, boards) = readData()

    val boardCandidates = boards.toMutableList()

    while (boardCandidates.size > 1) {
        // Find the winning board and remove it from the list
        val (number, winningBoard) = findWinningBoard(numbers, boardCandidates)
        boardCandidates.remove(winningBoard)
    }

    val board = boardCandidates[0]

    val (number, winningBoard) = findWinningBoard(numbers, listOf(board))

    println("Last number: $number")

    println("Winning board found:")
    println(winningBoard)

    val sum = winningBoard.sumUnmarkedNumbers()
    println("Unmarked numbers sum: $sum")

    println()
    println("Result: ${sum * number}")
}

fun main() {
    part2()
}
