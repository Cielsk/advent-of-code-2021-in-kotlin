fun main() {
    fun parseInput(input: List<String>) = input.map { it.toCharArray() }.toTypedArray()

    fun moveAndCount(grid: Array<CharArray>): Int {
        var cnt = 0
        val m = grid.size
        val n = grid[0].size
        // move east
        for (row in grid) {
            var c = 0
            val tmp = row[0]
            while (c < n) {
                val nc = (c + 1) % n
                val next = if (nc == 0) tmp else row[nc]
                if (row[c] == '>' && next == '.') {
                    row[nc] = '>'
                    row[c] = '.'
                    cnt++
                    c += 2
                } else c++
            }
        }
        // move south
        for (c in 0 until n) {
            var r = 0
            val tmp = grid[0][c]
            while (r < m) {
                val nr = (r + 1) % m
                val next = if (nr == 0) tmp else grid[nr][c]
                if (grid[r][c] == 'v' && next == '.') {
                    grid[nr][c] = 'v'
                    grid[r][c] = '.'
                    cnt++
                    r += 2
                } else r++
            }
        }
        return cnt
    }

    fun part1(input: List<String>): Int {
        val grid = parseInput(input)
        var steps = 0
        var moved = 1
        while (moved > 0) {
            moved = moveAndCount(grid)
            steps++
        }
        return steps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
}
