fun main() {

    fun match(left: Char, right: Char) = when {
        left == '(' && right == ')' -> true
        left == '[' && right == ']' -> true
        left == '{' && right == '}' -> true
        left == '<' && right == '>' -> true
        else -> false
    }

    fun isLeftPart(c: Char) = when (c) {
        '(', '[', '{', '<' -> true
        else -> false
    }

    fun getIncorrectScore(c: Char) = when (c) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException("Can not parse the score of $c.")
    }

    fun part1(input: List<String>): Int {
        val stack = ArrayDeque<Char>()
        var re = 0
        for (line in input) {
            for (c in line) {
                when {
                    stack.isEmpty() || isLeftPart(c) -> stack.addLast(c)
                    match(stack.last(), c) -> stack.removeLast()
                    else -> {
                        re += getIncorrectScore(c)
                        break
                    }
                }
            }
            stack.clear()
        }
        return re
    }

    fun part2(input: List<String>): Long {
        val stack = ArrayDeque<Char>()
        val scores = mutableListOf<Long>()
        for (line in input) {
            for (c in line) {
                when {
                    stack.isEmpty() || isLeftPart(c) -> stack.addLast(c)
                    match(stack.last(), c) -> stack.removeLast()
                    else -> {
                        stack.clear()
                        break
                    }
                }
            }
            if (stack.isNotEmpty()) {
                var score = 0L
                while (stack.isNotEmpty()) {
                    score *= 5L
                    score += when (stack.removeLast()) {
                        '(' -> 1L
                        '[' -> 2L
                        '{' -> 3L
                        '<' -> 4L
                        else -> throw IllegalArgumentException("Can not parse line content: $line")
                    }
                }
                scores.add(score)
                stack.clear()
            }
        }
        scores.sort()
        return scores[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
