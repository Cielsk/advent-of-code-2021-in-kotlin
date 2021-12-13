fun main() {
    fun parseInput(input: List<String>): Pair<List<Pair<Int, Int>>, List<Pair<Char, Int>>> {
        val dots = input.takeWhile { it.isNotEmpty() }
            .map {
                val (x, y) = it.split(',')
                Pair(x.toInt(), y.toInt())
            }
        val foldLines = input.takeLastWhile { it.isNotEmpty() }
            .map {
                val (axis, value) = it.removePrefix("fold along ").split('=')
                Pair(axis[0], value.toInt())
            }
        return Pair(dots, foldLines)
    }

    fun fold(dots: List<Pair<Int, Int>>, foldLine: Pair<Char, Int>): List<Pair<Int, Int>> {
        return if (foldLine.first == 'x') {
            val x = foldLine.second
            dots.map { point ->
                if (point.first < x) point
                else Pair(point.first - 2 * (point.first - x), point.second)
            }.distinct()
        } else {
            val y = foldLine.second
            dots.map { point ->
                if (point.second < y) point
                else Pair(point.first, point.second - 2 * (point.second - y))
            }.distinct()
        }
    }

    fun part1(input: List<String>): Int {
        val (dots, foldLines) = parseInput(input)
        val re = fold(dots, foldLines.first())
        return re.count()
    }

    fun getMatrix(dots: List<Pair<Int, Int>>): String {
        val maxX = dots.maxOf { it.first }
        val maxY = dots.maxOf { it.second }
        val matrix = Array(maxY + 1) { CharArray(maxX + 1) { '.' } }
        for ((x, y) in dots) {
            matrix[y][x] = '#'
        }
        return matrix.joinToString("\n") { String(it) }
    }

    fun part2(input: List<String>): String {
        val tmp = parseInput(input)
        var dots = tmp.first
        val foldLines = tmp.second
        for (foldLine in foldLines) {
            dots = fold(dots, foldLine)
        }
        return getMatrix(dots)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    val testOutput2 = """
        #####
        #...#
        #...#
        #...#
        #####
    """.trimIndent()
    check(part2(testInput) == testOutput2)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
