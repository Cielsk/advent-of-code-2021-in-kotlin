fun main() {
    fun parseInput(input: List<String>): LongArray {
        val bucket = LongArray(9)
        for (s in input[0].trim().split(',')) {
            bucket[s.toInt()]++
        }
        return bucket
    }

    fun count(input: List<String>, days: Int): Long {
        var bucket = parseInput(input)
        var tmp = LongArray(9)
        repeat(days) {
            tmp.fill(0)
            tmp[6] = bucket[0]
            tmp[8] = bucket[0]
            for (i in 1 until 9) {
                tmp[i - 1] += bucket[i]
            }
            bucket = tmp.also { tmp = bucket }
        }
        return bucket.sum()
    }

    fun part1(input: List<String>): Int {
        return count(input, 80).toInt()
    }

    fun part2(input: List<String>): Long {
        return count(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
