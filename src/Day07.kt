import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>) = input[0].trim().split(',').map { it.toInt() }

    fun totalFuel(input: List<String>, cost: (Int, Int) -> Int): Int {
        val positions = parseInput(input)
        val min = positions.minOrNull() ?: 0
        val max = positions.maxOrNull() ?: 0
        var total = Int.MAX_VALUE
        for (target in min..max) {
            total = minOf(total, positions.sumOf { cost(it, target) })
        }
        return total
    }

    fun part1(input: List<String>) = totalFuel(input) { a, b -> abs(a - b) }

    fun part2(input: List<String>) = totalFuel(input) { a, b ->
        val t = abs(a - b)
        t * (t + 1) / 2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
