fun main() {
    fun part1(input: List<String>): Int {
        var prev = Int.MAX_VALUE
        var cnt = 0
        for (line in input) {
            val curr = line.toInt()
            if (curr > prev) cnt++
            prev = curr
        }
        return cnt
    }

    fun part2(input: List<String>): Int {
        if (input.size < 3) return 0
        val cache = input.map { it.toInt() }
        var cnt = 0
        for (i in 3 until cache.size) {
            val curr = cache[i]
            val prev = cache[i - 3]
            if (curr > prev) cnt++
        }
        return cnt
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
