fun main() {
    class Graph(paths: List<Pair<String, String>>) {

        private val edges = mutableMapOf<String, MutableList<String>>()
        private val smallCaves = mutableSetOf<String>()

        init {
            for (path in paths) {
                val (a, b) = path
                if (a[0].isLowerCase()) smallCaves.add(a)
                if (b[0].isLowerCase()) smallCaves.add(b)
                edges.getOrPut(a) { mutableListOf() }.add(b)
                edges.getOrPut(b) { mutableListOf() }.add(a)
            }
        }

        private fun dfs(
            curr: String,
            target: String,
            visited: MutableSet<String>,
            banned: MutableSet<String>
        ): Int {
            if (curr.contentEquals(target)) return 1
            if (curr in smallCaves) {
                if (banned.isNotEmpty() && curr in visited) banned.add(curr)
                else visited.add(curr)
            }
            var sum = 0
            for (next in edges[curr]!!) {
                if (next in smallCaves) {
                    if (next in banned || (next in visited && banned.size != 1)) continue
                }
                sum += dfs(next, target, visited, banned)
            }
            if (curr in smallCaves) {
                if (curr in banned) banned.remove(curr)
                else visited.remove(curr)
            }
            return sum
        }

        fun countPaths(part: Int): Int {
            val banned = mutableSetOf<String>()
            if (part == 2) banned.add("start")
            return dfs("start", "end", mutableSetOf(), banned)
        }
    }

    fun parseInput(input: List<String>): Graph {
        val paths = input.map {
            val (first, second) = it.split('-')
            Pair(first, second)
        }
        return Graph(paths)
    }

    fun part1(input: List<String>): Int {
        val graph = parseInput(input)
        return graph.countPaths(1)
    }

    fun part2(input: List<String>): Int {
        val graph = parseInput(input)
        return graph.countPaths(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
