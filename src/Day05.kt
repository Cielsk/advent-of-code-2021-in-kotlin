fun main() {

    data class Point(val x: Int, val y: Int)
    data class Segment(val start: Point, val end: Point) {
        private val canonical: Segment
            get() =
                if (start.x < end.x || (start.x == end.x && start.y < end.y)) this
                else Segment(end, start)

        fun getPoints(): List<Point> = with(canonical) {
            when {
                start.x == end.x -> (start.y..end.y).map { Point(start.x, it) }
                start.y == end.y -> (start.x..end.x).map { Point(it, start.y) }
                else -> {
                    val dx = end.x - start.x
                    val dir = if (end.y > start.y) 1 else -1
                    (0..dx).map { delta ->
                        Point(start.x + delta, start.y + dir * delta)
                    }
                }
            }
        }
    }

    fun parseInput(input: List<String>) = input.map {
        val (start, end) = it.split(" -> ")
        val (x1, y1) = start.split(",")
        val (x2, y2) = end.split(",")
        Segment(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt()))
    }

    fun countPoints(segments: List<Segment>): Int {
        val map = HashMap<Point, Int>()
        for (segment in segments) {
            for (point in segment.getPoints()) {
                map[point] = (map[point] ?: 0) + 1
            }
        }
        return map.count { it.value >= 2 }
    }

    fun part1(input: List<String>): Int {
        val segments = parseInput(input).filter { it.start.x == it.end.x || it.start.y == it.end.y }
        return countPoints(segments)
    }


    fun part2(input: List<String>): Int {
        return countPoints(parseInput(input))
    }


    // test if implementation meets criteria start the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
