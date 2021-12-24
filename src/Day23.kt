import java.util.PriorityQueue
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.abs

fun main() {
    data class State(val grid: Array<CharArray>, val cost: Int) {
        val roomSize = grid.size - 1

        override fun equals(other: Any?): Boolean =
                other is State && (0..roomSize).all { grid[it].contentEquals(other.grid[it]) }

        override fun hashCode(): Int = grid.map { it.contentHashCode() }.toIntArray().contentHashCode()

        fun copy(d1: Int) = State(Array(grid.size) { grid[it].copyOf() }, d1)

        override fun toString(): String = buildList {
            add("#".repeat(13))
            addAll(grid.map { it.concatToString() })
            add("  #########")
            add("cost=$cost")
        }.joinToString("\n")
    }

    fun cost(type: Char): Int = when (type) {
        'A'  -> 1
        'B'  -> 10
        'C'  -> 100
        'D'  -> 1000
        else -> throw IllegalArgumentException("Wrong amphipod type: $type")
    }

    fun dijkstra(start: State): Int {
        val sz = start.roomSize
        val memo = HashMap<State, Int>()
        val heap = PriorityQueue(compareBy(State::cost))
        val seen = HashSet<State>()
        fun enqueue(c: State) {
            val d0 = memo[c] ?: Int.MAX_VALUE
            if (c.cost >= d0) return
            memo[c] = c.cost
            heap += c
        }
        enqueue(start)
        while (heap.isNotEmpty()) {
            val curr = heap.remove()!!
            if (curr in seen) continue
            seen += curr
            val currCost = memo[curr]!!
            var ok = true
            // check if it got the target
            check@ for (r in 1..sz) for (i in 0..3) if (curr.grid[r][2 * i + 3] != 'A' + i) {
                ok = false
                break@check
            }
            if (ok) return curr.cost

            // move in
            for (col in 1..11) {
                val member = curr.grid[0][col]
                if (member !in 'A'..'D') continue
                val room = (member - 'A')
                val roomCol = 2 * room + 3
                // check if the path to the door of destination room is clear
                if (!(minOf(col, roomCol) + 1 until maxOf(col, roomCol)).all { curr.grid[0][it] == '.' }) continue
                // check if destination room is clear
                if (!(1..sz).all { curr.grid[it][roomCol] == '.' || curr.grid[it][roomCol] == member }) continue
                val destRow = (sz downTo 1).first { curr.grid[it][roomCol] == '.' }
                val next = curr.copy(currCost + cost(member) * (abs(roomCol - col) + destRow))
                next.grid[0][col] = '.'
                next.grid[destRow][roomCol] = member
                enqueue(next)
            }
            // move out
            for (room in 0..3) for (row in 1..sz) {
                val roomCol = 2 * room + 3
                val member = curr.grid[row][roomCol]
                if (member !in 'A'..'D') continue
                // check if the path to the door is clear
                if (!(1 until row).all { curr.grid[it][roomCol] == '.' }) continue
                // skip amphipod already in right position
                if (member == 'A' + room && (row..sz).all { curr.grid[it][roomCol] == member }) continue
                for (col in 1..11) {
                    // not to the door
                    if ((col - 3) % 2 == 0 && (col - 3) / 2 in 0..3) continue
                    // check if the path from the door to the destination cell is clear
                    if (!(minOf(col, roomCol)..maxOf(col, roomCol)).all { j -> curr.grid[0][j] == '.' }) continue
                    val next = curr.copy(currCost + cost(member) * (abs(col - roomCol) + row))
                    next.grid[row][roomCol] = '.'
                    next.grid[0][col] = member
                    enqueue(next)
                }
            }
        }
        return Int.MAX_VALUE
    }

    fun parseInput(input: List<String>, unfold: Boolean = false): State {
        val original = input.subList(1, 4)
        val list = if (unfold) buildList {
            add(original[0])
            add(original[1])
            add("  #D#C#B#A#  ")
            add("  #D#B#A#C#  ")
            add(original[2])
        } else original
        val arr = list.map { it.toCharArray() }.toTypedArray()
        return State(arr, 0)
    }

    fun part1(input: List<String>): Int {
        val start = parseInput(input)
        return dijkstra(start)
    }

    fun part2(input: List<String>): Int {
        val start = parseInput(input, true)
        return dijkstra(start)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 12521)
    check(part2(testInput) == 44169)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
