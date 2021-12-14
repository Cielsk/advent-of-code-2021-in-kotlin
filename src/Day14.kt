fun main() {
    class InsertionRules() {
        private val data = mutableMapOf<Pair<Char, Char>, Char>()

        fun addRule(key: String, insertion: Char) {
            val pair = Pair(key[0], key[1])
            data[pair] = insertion
        }

        fun getInsertion(pair: Pair<Char, Char>) = data[pair]

        fun getCharSet(): Set<Char> {
            val set = mutableSetOf<Char>()
            for ((key, value) in data) {
                set.add(key.first)
                set.add(key.second)
                set.add(value)
            }
            return set
        }
    }

    fun parseInput(input: List<String>): Pair<String, InsertionRules> {
        val template = input.takeWhile { it.isNotEmpty() }.first()
        val insertionRules = InsertionRules()
        input.takeLastWhile { it.isNotEmpty() }.forEach {
            val (key, insertion) = it.split(" -> ")
            insertionRules.addRule(key, insertion[0])
        }
        return Pair(template, insertionRules)
    }

    fun merge(a: LongArray, b: LongArray, result: LongArray) {
        for (i in result.indices) {
            result[i] = a[i] + b[i]
        }
    }

    fun insert(template: String, insertionRules: InsertionRules, times: Int): List<Long> {
        val charSet = template.toSet() + insertionRules.getCharSet()
        val pairList = mutableListOf<Pair<Char, Char>>()
        for (first in charSet) {
            for (second in charSet) {
                pairList.add(Pair(first, second))
            }
        }
        val pairIdxMap = pairList.mapIndexed { index, pair -> Pair(pair, index) }.toMap()
        val dp = Array(times + 1) { Array(pairList.size) { LongArray(26) } }
        for (i in pairList.indices) {
            val pair = pairList[i]
            dp[0][i][pair.first - 'A']++
            dp[0][i][pair.second - 'A']++
        }
        for (i in 1..times) {
            for (j in pairList.indices) {
                val pair = pairList[j]
                val insertion = insertionRules.getInsertion(pair)
                if (insertion == null) dp[0][j].copyInto(dp[i][j])
                else {
                    val left = Pair(pair.first, insertion)
                    val right = Pair(insertion, pair.second)
                    val leftIdx = pairIdxMap[left]!!
                    val rightIdx = pairIdxMap[right]!!
                    merge(dp[i - 1][leftIdx], dp[i - 1][rightIdx], dp[i][j])
                    dp[i][j][insertion - 'A']--
                }
            }
        }
        val counter = LongArray(26)
        counter[template[0] - 'A']++
        for (i in 1 until template.length) {
            val pair = Pair(template[i - 1], template[i])
            val idx = pairIdxMap[pair]!!
            val pairCounter = dp[times][idx]
            merge(counter, pairCounter, counter)
            counter[template[i - 1] - 'A']--
        }
        return counter.filter { it > 0 }
    }

    fun part1(input: List<String>): Int {
        val (template, insertionRules) = parseInput(input)
        val counter = insert(template, insertionRules, 10)
        val max = counter.maxOrNull() ?: 0L
        val min = counter.minOrNull() ?: 0L
        return (max - min).toInt()
    }

    fun part2(input: List<String>): Long {
        val (template, insertionRules) = parseInput(input)
        val counter = insert(template, insertionRules, 40)
        val max = counter.maxOrNull() ?: 0L
        val min = counter.minOrNull() ?: 0L
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
