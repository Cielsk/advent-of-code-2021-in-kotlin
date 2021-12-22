fun main() {
    data class Command(
            val type: Int,
            val x1: Long,
            val x2: Long,
            val y1: Long,
            val y2: Long,
            val z1: Long,
            val z2: Long
    )

    fun solve(commands: List<Command>): Long {
        val cubes = mutableListOf<Command>()

        for (command in commands) {
            val diffCubes = mutableListOf<Command>()
            for (cube in cubes) {
                val x1 = maxOf(command.x1, cube.x1)
                val x2 = minOf(command.x2, cube.x2)
                val y1 = maxOf(command.y1, cube.y1)
                val y2 = minOf(command.y2, cube.y2)
                val z1 = maxOf(command.z1, cube.z1)
                val z2 = minOf(command.z2, cube.z2)
                if (x1 <= x2 && y1 <= y2 && z1 <= z2)
                    diffCubes.add(Command(-cube.type, x1, x2, y1, y2, z1, z2))
            }
            cubes.addAll(diffCubes)
            if (command.type > 0) cubes.add(command)
        }

        return cubes.sumOf { it.type * (it.x2 - it.x1 + 1) * (it.y2 - it.y1 + 1) * (it.z2 - it.z1 + 1) }
    }

    fun parseInput(input: List<String>): List<Command> = input.map { line ->
        val (head, ranges) = line.split(" ")
        val (xRange, yRange, zRange) = ranges.split(",").map { it.split("=").last() }
        val (x1, x2) = xRange.split("..").map { it.toLong() }.sorted()
        val (y1, y2) = yRange.split("..").map { it.toLong() }.sorted()
        val (z1, z2) = zRange.split("..").map { it.toLong() }.sorted()
        val type = if (head == "on") 1 else -1
        Command(type, x1, x2, y1, y2, z1, z2)
    }

    fun part1(input: List<String>): Int {
        val commands = parseInput(input)
        val filtered = commands.filter { cmd ->
            listOf(cmd.x1, cmd.x2, cmd.y1, cmd.y2, cmd.z1, cmd.z2).all { it in -50..50 }
        }
        return solve(filtered).toInt()
    }

    fun part2(input: List<String>): Long {
        val commands = parseInput(input)
        return solve(commands)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 474140)
    check(part2(testInput) == 2758514936282235)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
