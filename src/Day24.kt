fun main() {

    data class Section(val check: Int, val offset: Int)

    // thanks explanation from https://github.com/dphilipson/advent-of-code-2021/blob/master/src/days/day24.rs
    fun List<Section>.parseDigitRelations(): Array<Pair<Int, Int>> {
        val relations = Array(size) { Pair(-1, 0) }
        val stack = ArrayDeque<Pair<Int, Int>>()
        for (i in indices) {
            val (check, offset) = this[i]
            if (check > 0) stack.addLast(Pair(i, offset))
            else {
                val tmp = stack.removeLast()
                val parent = tmp.first
                val value = tmp.second + check
                relations[parent] = Pair(i, value)
            }
        }
        return relations
    }

    fun parseInput(input: List<String>): Array<Pair<Int, Int>> {
        return input.chunked(input.size / 14) { section ->
            val list = section.filter { line -> line.startsWith("add") }
            val (check, offset) = listOf(list[1], list[5]).map { line -> line.split(' ').last().toInt() }
            Section(check, offset)
        }.parseDigitRelations()
    }


    fun solve(getMin: Boolean, relations: Array<Pair<Int, Int>>): Long {
        val digits = IntArray(relations.size)
        for (i in relations.indices) {
            val (next, value) = relations[i]
            if (next < 0) continue
            // digits[next] = digits[i] + value
            // digits[i] in 1..9 and digits[next] in 1..9
            val min = maxOf(1, 1 - value)
            val max = minOf(9, 9 - value)
            digits[i] = if (getMin) min else max
            digits[next] = digits[i] + value
        }

        return digits.fold(0L) { acc, i -> acc * 10L + i }
    }

    fun part1(input: List<String>): Long {
        val relations = parseInput(input)
        return solve(false, relations)
    }

    fun part2(input: List<String>): Long {
        val relations = parseInput(input)
        return solve(true, relations)
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
