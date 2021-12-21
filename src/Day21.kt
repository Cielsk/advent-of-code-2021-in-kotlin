fun main() {
    fun parseInput(input: List<String>) = input.mapIndexed { idx, str ->
        str.removePrefix("Player ${idx + 1} starting position: ").toInt()
    }


    fun part1(input: List<String>): Int {
        val positions = parseInput(input).toTypedArray()
        val points = IntArray(2)
        var cnt = 0
        outer@ while (true) {
            for (i in 0..1) {
                var diceSum = 0
                repeat(3) {
                    cnt++
                    diceSum += (cnt - 1) % 100 + 1
                }
                positions[i] = (positions[i] + diceSum - 1) % 10 + 1
                points[i] += positions[i]
                if (points[i] >= 1000) break@outer
            }
        }
        return cnt * points.minOrNull()!!
    }

    fun part2(input: List<String>): Long {
        val (startA, startB) = parseInput(input)
        val memo = Array(11) { Array(11) { Array(22) { Array(22) { LongArray(2) { -1L } } } } }
        fun count(posA: Int, posB: Int, pointA: Int, pointB: Int): LongArray {
            if (memo[posA][posB][pointA][pointB].all { it == -1L }) {
                var cntA = 0L
                var cntB = 0L
                for (d1 in 1..3) for (d2 in 1..3) for (d3 in 1..3) {
                    val nextPosA = (posA + d1 + d2 + d3 - 1) % 10 + 1
                    val nextPointA = pointA + nextPosA
                    if (nextPointA < 21) {
                        val (nextCntA, nextCntB) = count(posB, nextPosA, pointB, nextPointA)
                        cntA += nextCntB
                        cntB += nextCntA
                    } else
                        cntA++
                }
                memo[posA][posB][pointA][pointB][0] = cntA
                memo[posA][posB][pointA][pointB][1] = cntB
            }
            return memo[posA][posB][pointA][pointB]
        }

        return count(startA, startB, 0, 0).maxOrNull()!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
