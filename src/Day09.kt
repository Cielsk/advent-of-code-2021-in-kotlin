import java.util.*

fun main() {
    val dr = intArrayOf(-1, 0, 1, 0)
    val dc = intArrayOf(0, 1, 0, -1)

    fun parseInput(input: List<String>): Array<IntArray> {
        val m = input.size
        val n = input[0].length
        return Array(m) { r ->
            IntArray(n) { c ->
                input[r][c].digitToInt()
            }
        }
    }

    fun isLowPoint(matrix: Array<IntArray>, row: Int, col: Int): Boolean {
        for (i in 0 until 4) {
            val r = row + dr[i]
            val c = col + dc[i]
            if (r !in matrix.indices || c !in matrix[0].indices) continue
            if (matrix[r][c] <= matrix[row][col]) return false
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val matrix = parseInput(input)
        var sum = 0
        for (row in matrix.indices) {
            for (col in matrix[0].indices) {
                if (isLowPoint(matrix, row, col))
                    sum += matrix[row][col] + 1
            }
        }
        return sum
    }

    fun dfs(matrix: Array<IntArray>, row: Int, col: Int, visited: Array<BooleanArray>): Int {
        visited[row][col] = true
        var re = 1
        for (i in 0 until 4) {
            val r = row + dr[i]
            val c = col + dc[i]
            if (r !in matrix.indices || c !in matrix[0].indices || visited[r][c] || matrix[r][c] == 9) continue
            re += dfs(matrix, r, c, visited)
        }
        return re
    }

    fun part2(input: List<String>): Int {
        val matrix = parseInput(input)
        val visited = Array(matrix.size) { BooleanArray(matrix[0].size) }
        val heap = PriorityQueue<Int>()
        for (row in matrix.indices) {
            for (col in matrix[0].indices) {
                if (visited[row][col] || matrix[row][col] == 9) continue
                val area = dfs(matrix, row, col, visited)
                heap.add(area)
                if (heap.size > 3) heap.poll()
            }
        }
        return heap.fold(1) { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
