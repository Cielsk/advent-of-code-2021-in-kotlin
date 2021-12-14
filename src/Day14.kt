fun main() {
    class InsertionRules(private val map: Map<String, List<String>>) {
        fun getInsertResult(key: String) = map[key] ?: emptyList()
    }

    fun parseInput(input: List<String>): Pair<String, InsertionRules> {
        val template = input.first()
        val map = input.drop(2).associate {
            val (key, insertion) = it.split(" -> ")
            key to listOf("${key[0]}$insertion", "$insertion${key[1]}")
        }
        val insertionRules = InsertionRules(map)
        return Pair(template, insertionRules)
    }

    fun insert(template: String, insertionRules: InsertionRules, times: Int): List<Long> {
        var map = template.zip(template.drop(1)) { ch1, ch2 -> "$ch1$ch2" }
                .groupingBy { it }
                .eachCount()
                .mapValues { it.value.toLong() }
        repeat(times) {
            map = buildMap {
                for ((str, cnt) in map) {
                    for (newStr in insertionRules.getInsertResult(str)) {
                        compute(newStr) { _, v -> (v ?: 0) + cnt }
                    }
                }
            }
        }
        val counter = LongArray(26)
        counter[template[0] - 'A']++
        counter[template.last() - 'A']++
        for ((str, cnt) in map) {
            for (c in str) {
                counter[c - 'A'] += cnt
            }
        }
        return counter.filter { it > 0 }.map { it / 2 }
    }

    fun part1(input: List<String>): Int {
        val (template, insertionRules) = parseInput(input)
        val counter = insert(template, insertionRules, 10)
        val max = counter.maxOrNull() ?: 0
        val min = counter.minOrNull() ?: 0
        return (max - min).toInt()
    }

    fun part2(input: List<String>): Long {
        val (template, insertionRules) = parseInput(input)
        val counter = insert(template, insertionRules, 40)
        val max = counter.maxOrNull() ?: 0
        val min = counter.minOrNull() ?: 0
        return max - min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
