fun main() {
    val dr = intArrayOf(-1, -1, -1, 0, 0, 1, 1, 1)
    val dc = intArrayOf(-1, 0, 1, -1, 1, -1, 0, 1)

    fun parseInput(input: List<String>): Array<IntArray> {
        return Array(10) { r -> IntArray(10) { c -> input[r][c].digitToInt() } }
    }

    fun stepForwardOnce(matrix: Array<IntArray>): Int {
        val queue = ArrayDeque<Pair<Int, Int>>()
        for (r in matrix.indices) {
            for (c in matrix[0].indices) {
                matrix[r][c]++
                if (matrix[r][c] > 9) queue.addLast(Pair(r, c))
            }
        }
        var cnt = 0
        val flashed = Array(10) { BooleanArray(10) }
        while (queue.isNotEmpty()) {
            val (row, col) = queue.removeFirst()
            matrix[row][col] = 0
            flashed[row][col] = true
            cnt++
            for (i in 0 until 8) {
                val r = row + dr[i]
                val c = col + dc[i]
                if (r !in 0 until 10 || c !in 0 until 10 || flashed[r][c] || matrix[r][c] > 9) continue
                matrix[r][c]++
                if (matrix[r][c] > 9) queue.addLast(Pair(r, c))
            }
        }
        return cnt
    }

    fun part1(input: List<String>): Int {
        val matrix = parseInput(input)
        var re = 0
        repeat(100) {
            re += stepForwardOnce(matrix)
        }
        return re
    }

    fun part2(input: List<String>): Int {
        val matrix = parseInput(input)
        var cnt = 0
        while (true) {
            val flashes = stepForwardOnce(matrix)
            cnt++
            if (flashes == 100) return cnt
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
