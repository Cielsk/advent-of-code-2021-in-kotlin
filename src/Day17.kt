import kotlin.math.abs

fun main() {

    fun parseInput(input: List<String>): Pair<IntRange, IntRange> {
        val list = input.first().removePrefix("target area: ").split(", ").map {
            val (start, end) = it.drop(2).split("..")
            start.toInt()..end.toInt()
        }
        return Pair(list.first(), list.last())
    }

    fun part1(input: List<String>): Int {
        val (xRange, yRange) = parseInput(input)
        var re = Int.MIN_VALUE
        for (vx0 in 0..xRange.last) {
            val finalX = (vx0 + 1) * vx0 / 2
            if (finalX < xRange.first) continue
            // Because of gravity, probe will fall to y=0 after some time,
            // then vy is -vy0 in next step.
            // That is after some time probe's y position is -vy0.
            // Make sure it will not fall below the bottom line of the rectangle.
            for (vy0 in 0..abs(yRange.first)) {
                var validStart = false
                var x = 0
                var y = 0
                var vx = vx0
                var vy = vy0
                var maxY = Int.MIN_VALUE
                while (x <= xRange.last && y >= yRange.first) {
                    x += vx
                    y += vy
                    maxY = maxOf(maxY, y)
                    if (x in xRange && y in yRange) {
                        validStart = true
                        break
                    }
                    if (vx > 0) vx--
                    vy--
                }
                if (validStart) re = maxOf(re, maxY)
            }
        }
        return re
    }

    fun part2(input: List<String>): Int {
        val (xRange, yRange) = parseInput(input)
        var re = 0
        for (vx0 in 0..xRange.last) {
            val finalX = (vx0 + 1) * vx0 / 2
            if (finalX < xRange.first) continue
            for (vy0 in yRange.first..abs(yRange.first)) {
                var validStart = false
                var x = 0
                var y = 0
                var vx = vx0
                var vy = vy0
                while (x <= xRange.last && y >= yRange.first) {
                    x += vx
                    y += vy
                    if (x in xRange && y in yRange) {
                        validStart = true
                        break
                    }
                    if (vx > 0) vx--
                    vy--
                }
                if (validStart) re++
            }
        }
        return re
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
