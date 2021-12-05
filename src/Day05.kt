fun main() {

    fun part1(input: List<String>): Int {
        val data = Array(1001) { IntArray(1001) }
        for (line in input) {
            val tmp = line.trim().split(' ')
            val point1 = tmp[0].split(',').map { it.toInt() }
            val point2 = tmp[2].split(',').map { it.toInt() }
            if (point1[0] == point2[0]) { // vertical segment
                val x = point1[0]
                val start = minOf(point1[1], point2[1])
                val end = maxOf(point1[1], point2[1])
                for (r in start..end) {
                    data[r][x]++
                }
            } else if (point1[1] == point2[1]) { // horizontal segment
                val y = point1[1]
                val start = minOf(point1[0], point2[0])
                val end = maxOf(point1[0], point2[0])
                for (c in start..end) {
                    data[y][c]++
                }
            }
        }
        return data.sumOf { row -> row.count { it >= 2 } }
    }

    fun part2(input: List<String>): Int {
        val data = Array(1001) { IntArray(1001) }
        for (line in input) {
            val tmp = line.trim().split(' ')
            val point1 = tmp[0].split(',').map { it.toInt() }
            val point2 = tmp[2].split(',').map { it.toInt() }
            if (point1[0] == point2[0]) { // vertical segment
                val x = point1[0]
                val start = minOf(point1[1], point2[1])
                val end = maxOf(point1[1], point2[1])
                for (r in start..end) {
                    data[r][x]++
                }
            } else if (point1[1] == point2[1]) { // horizontal segment
                val y = point1[1]
                val start = minOf(point1[0], point2[0])
                val end = maxOf(point1[0], point2[0])
                for (c in start..end) {
                    data[y][c]++
                }
            } else {
                val x1: Int
                val y1: Int
                val x2: Int
                val y2: Int
                if (point1[0] <= point2[0]) {
                    x1 = point1[0]
                    y1 = point1[1]
                    x2 = point2[0]
                    y2 = point2[1]
                } else {
                    x1 = point2[0]
                    y1 = point2[1]
                    x2 = point1[0]
                    y2 = point1[1]
                }
                val deltaX = x2 - x1
                val deltaY = y2 - y1
                for (x in x1..x2) {
                    if ((x - x1) * deltaX % deltaY != 0) continue
                    val y = (x - x1) * deltaX / deltaY + y1
                    data[y][x]++
                }
            }
        }
        return data.sumOf { row -> row.count { it >= 2 } }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
