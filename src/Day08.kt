fun main() {
    val map = mapOf(
        "abcefg" to 0,
        "cf" to 1,
        "acdeg" to 2,
        "acdfg" to 3,
        "bcdf" to 4,
        "abdfg" to 5,
        "abdefg" to 6,
        "acf" to 7,
        "abcdefg" to 8,
        "abcdfg" to 9
    )

    fun parseInput(input: List<String>): List<Pair<List<String>, List<String>>> {
        return input.map {
            val (t1, t2) = it.split(" | ")
            val list1 = t1.split(' ')
            val list2 = t2.split(' ')
            Pair(list1, list2)
        }
    }

    fun decode(code: Pair<List<String>, List<String>>): Int {
        val (input, output) = code
        val decodeMap = CharArray(8) { '#' }
        val charSet1 = mutableSetOf<Char>()
        val charSet7 = mutableListOf<Char>()
        val charSet4 = mutableListOf<Char>()
        val countChars = IntArray(8)
        for (str in input) {
            for (c in str) {
                countChars[c - 'a']++
            }
            when (str.length) {
                2 -> charSet1.addAll(str.toList())
                3 -> charSet7.addAll(str.toList())
                4 -> charSet4.addAll(str.toList())
            }
        }

        // 2 and 7 is unique, determine what overlaps 'a'
        val aCode = (charSet7 - charSet1).first()
        decodeMap[aCode - 'a'] = 'a'
        // 'c' 'e' 'b' 'f'
        for (i in countChars.indices) {
            when (countChars[i]) {
                8 -> if (decodeMap[i] == '#') decodeMap[i] = 'c'
                4 -> decodeMap[i] = 'e'
                6 -> decodeMap[i] = 'b'
                9 -> decodeMap[i] = 'f'
            }
        }
        val dCode = charSet4.first { decodeMap[it - 'a'] == '#' }
        decodeMap[dCode - 'a'] = 'd'
        val (gCode, _) = decodeMap.withIndex().first { it.value == '#' }
        decodeMap[gCode] = 'g'
        var re = 0
        for (str in output) {
            val arr = CharArray(str.length) { decodeMap[str[it] - 'a'] }
            arr.sort()
            val digit = map[String(arr)] ?: throw NoSuchElementException("$str not decoded.")
            re = re * 10 + digit
        }
        return re
    }

    fun part1(input: List<String>): Int {
        val data = parseInput(input)
        var cnt = 0
        val set = setOf(2, 4, 3, 7)
        for ((_, output) in data) {
            cnt += output.count { it.length in set }
        }
        return cnt
    }

    fun part2(input: List<String>): Int {
        return parseInput(input).sumOf { decode(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
