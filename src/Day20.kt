fun main() {
    fun parseInput(input: List<String>): Pair<IntArray, Array<IntArray>> {
        val enhanceTable = input.first().map { if (it == '#') 1 else 0 }.toIntArray()
        val matrix = input.drop(2).map { it.map { c -> if (c == '#') 1 else 0 }.toIntArray() }
        return Pair(enhanceTable, matrix.toTypedArray())
    }

    fun enhanceAndCountLightPixel(matrix: Array<IntArray>, table: IntArray, times: Int): Int {
        val flag = table[0] == 1
        var curr = matrix
        repeat(times) {
            val m = curr.size
            val n = curr[0].size
            val next = Array(m + 2) { IntArray(n + 2) }
            val default = if (flag) it % 2 else 0
            for (i in 0 until m + 2) for (j in 0 until n + 2) {
                var idx = 0
                for (dr in -1..1) for (dc in -1..1) {
                    idx = (idx shl 1) + (curr.getOrNull(i + dr - 1)?.getOrNull(j + dc - 1) ?: default)
                }
                next[i][j] = table[idx]
            }
            curr = next
        }
        return curr.sumOf { it.sum() }
    }

    fun part1(input: List<String>): Int {
        val (enhanceTable, matrix) = parseInput(input)
        return enhanceAndCountLightPixel(matrix, enhanceTable, 2)
    }

    fun part2(input: List<String>): Int {
        val (enhanceTable, matrix) = parseInput(input)
        return enhanceAndCountLightPixel(matrix, enhanceTable, 50)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
