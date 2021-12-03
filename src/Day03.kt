private class TrieNode(var cnt: Int = 0) {
    val children = Array<TrieNode?>(2) { null }
}

fun main() {
    fun part1(input: List<String>): Int {
        if (input.isEmpty()) return 0
        var gamma = 0
        var epsilon = 0
        val total = input.size
        val bitCnt = IntArray(input[0].length)
        for (line in input) {
            for (i in line.indices) {
                if (line[i] == '1') bitCnt[i]++
            }
        }
        for (i in bitCnt.indices) {
            val digitInGamma = if (bitCnt[i] > total - bitCnt[i]) 1 else 0
            gamma = gamma * 2 + digitInGamma
            epsilon = epsilon * 2 + 1 - digitInGamma
        }
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        if (input.isEmpty()) return 0
        val root = TrieNode()
        for (line in input) {
            var curr = root
            for (c in line) {
                curr.cnt++
                val idx = c - '0'
                if (curr.children[idx] == null)
                    curr.children[idx] = TrieNode()
                curr = curr.children[idx]!!
            }
        }
        var oxygen = 0
        var co2 = 0
        var curr = root
        while (curr.cnt > 0) {
            var digit = 0
            when {
                curr.children[1] == null -> curr = curr.children[0]!!
                curr.children[0] == null || curr.children[1]!!.cnt >= curr.children[0]!!.cnt -> {
                    curr = curr.children[1]!!
                    digit = 1
                }
                else -> curr = curr.children[0]!!
            }
            oxygen = 2 * oxygen + digit
        }
        curr = root
        while (curr.cnt > 0) {
            var digit = 1
            when {
                curr.children[0] == null -> curr = curr.children[1]!!
                curr.children[1] == null || curr.children[0]!!.cnt <= curr.children[1]!!.cnt -> {
                    curr = curr.children[0]!!
                    digit = 0
                }
                else -> curr = curr.children[1]!!
            }
            co2 = 2 * co2 + digit
        }
        return oxygen * co2
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
