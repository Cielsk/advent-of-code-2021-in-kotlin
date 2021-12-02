fun main() {
    fun parseCommand(cmd: String, forward: (Int) -> Unit, up: (Int) -> Unit, down: (Int) -> Unit) {
        val strings = cmd.split(' ')
        val step = strings[1].toInt()
        when (strings[0]) {
            "forward" -> forward(step)
            "up" -> up(step)
            "down" -> down(step)
        }
    }

    fun part1(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        for (cmd in input) {
            parseCommand(cmd,
                forward = { horizontal += it },
                up = { depth -= it },
                down = { depth += it })
        }
        return horizontal * depth
    }

    fun part2(input: List<String>): Int {
        var aim = 0
        var depth = 0
        var horizontal = 0
        for (cmd in input) {
            parseCommand(cmd,
                forward = {
                    horizontal += it
                    depth += aim * it
                },
                down = { aim += it },
                up = { aim -= it }
            )
        }
        return horizontal * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}