fun main() {
    class BingoBoard(val data: Array<IntArray>) {
        private val map = mutableMapOf<Int, Pair<Int, Int>>()
        private val rowCounter = IntArray(5)
        private val colCounter = IntArray(5)
        val isWinner: Boolean
            get() = rowCounter.any { it == 5 } or colCounter.any { it == 5 }
        private var lastMark = -1

        init {
            for (r in data.indices) {
                for (c in data[r].indices) {
                    map[data[r][c]] = Pair(r, c)
                }
            }
        }

        fun addMark(num: Int) {
            val (row, col) = map[num] ?: return
            rowCounter[row]++
            colCounter[col]++
            lastMark = num
        }

        fun getScore(marks: Set<Int>): Int {
            if (lastMark == -1 || !isWinner) return -1
            val sum = data.sumOf { arr -> arr.sumOf { if (it in marks) 0 else it } }
            return sum * lastMark
        }
    }

    fun parseInput(input: List<String>): Pair<List<Int>, List<BingoBoard>> {
        val markList = input[0].split(',').map { it.toInt() }
        val boards = mutableListOf<BingoBoard>()
        var i = 1
        val spacesRegex = Regex("\\s+")
        while (i < input.size) {
            if (input[i].isNotEmpty()) {
                val data = Array(5) { input[i++].trim().split(spacesRegex).map { str -> str.toInt() }.toIntArray() }
                val board = BingoBoard(data)
                boards.add(board)
            } else
                i++
        }
        return Pair(markList, boards.toList())
    }

    fun part1(input: List<String>): Int {
        val (markList, boards) = parseInput(input)
        val markSet = mutableSetOf<Int>()
        for (mark in markList) {
            markSet.add(mark)
            for (board in boards) {
                board.addMark(mark)
                if (board.isWinner) {
                    return board.getScore(markSet)
                }
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val (markList, boards) = parseInput(input)
        val markSet = mutableSetOf<Int>()
        val scoreList = mutableListOf<Int>()
        val won = BooleanArray(boards.size)
        for (mark in markList) {
            markSet.add(mark)
            for (j in boards.indices) {
                if (won[j]) continue
                boards[j].addMark(mark)
                if (boards[j].isWinner) {
                    scoreList.add(boards[j].getScore(markSet))
                    won[j] = true
                }
            }
        }
        return scoreList.last()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
