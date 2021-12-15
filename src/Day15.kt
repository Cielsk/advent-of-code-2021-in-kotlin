import java.util.PriorityQueue

fun main() {
    val dr = intArrayOf(0, -1, 0, 1)
    val dc = intArrayOf(-1, 0, 1, 0)
    val inf = (1e9).toInt()

    fun parseInput(input: List<String>): Array<IntArray> {
        val m = input.size
        val n = input[0].length
        return Array(m) { r -> IntArray(n) { c -> input[r][c].digitToInt() } }
    }

    fun dijkstra(matrix: Array<IntArray>): Int {
        val risk = Array(matrix.size) { IntArray(matrix[0].size) { inf } }
        risk[0][0] = 0
        val heap = PriorityQueue<Pair<Int, Int>>(compareBy { risk[it.first][it.second] })
        heap.add(Pair(0, 0))
        val settled = Array(matrix.size) { BooleanArray(matrix[0].size) }
        while (heap.isNotEmpty()) {
            val (row, col) = heap.poll()
            if (settled[row][col]) continue
            settled[row][col] = true
            for (i in 0 until 4) {
                val r = row + dr[i]
                val c = col + dc[i]
                if (r !in matrix.indices || c !in matrix[0].indices || settled[r][c]) continue
                if (risk[r][c] > risk[row][col] + matrix[r][c])
                    risk[r][c] = risk[row][col] + matrix[r][c]
                heap.add(Pair(r, c))
            }
        }
        return risk.last().last()
    }

    fun part1(input: List<String>): Int {
        val matrix = parseInput(input)
        return dijkstra(matrix)
    }

    fun enlargeMatrix(matrix: Array<IntArray>): Array<IntArray> {
        val m = matrix.size
        val n = matrix[0].size
        val re = Array(5 * n) { IntArray(5 * n) }
        for (row in re.indices) {
            for (col in re[0].indices) {
                val inc = row / m + col / n
                re[row][col] = (inc % 10 + matrix[row % m][col % n]) % 9
                if (re[row][col] == 0) re[row][col] = 9
            }
        }
        return re
    }

    fun part2(input: List<String>): Int {
        val matrix = parseInput(input)
        val enlargedMatrix = enlargeMatrix(matrix)
        return dijkstra(enlargedMatrix)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
