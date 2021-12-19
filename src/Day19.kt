import kotlin.math.abs

data class Point3D(val x: Int, val y: Int, val z: Int) {

    val orientations: List<Point3D> by lazy {
        val cache = mutableListOf<Point3D>()
        var rotated = this
        for (i in 1..2) {
            for (j in 1..3) {
                rotated = roll(rotated)
                cache.add(rotated)
                for (k in 1..3) {
                    rotated = turn(rotated)
                    cache.add(rotated)
                }
            }
            rotated = roll(turn(roll(rotated)))
        }
        cache
    }

    companion object {
        fun roll(point: Point3D): Point3D {
            return Point3D(point.x, point.z, -point.y)
        }

        fun turn(point: Point3D): Point3D {
            return Point3D(-point.y, point.x, point.z)
        }
    }

    operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Point3D) = Point3D(x - other.x, y - other.y, z - other.z)
    infix fun dist(other: Point3D) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    override fun toString() = "($x,$y,$z)"
}

data class Scanner(val beacons: List<Point3D>) {
    val orientations: List<Scanner> by lazy {
        val cache = Array(24) { mutableListOf<Point3D>() }
        for (beacon in beacons) {
            repeat(24) { cache[it].add(beacon.orientations[it]) }
        }
        cache.map { Scanner(it) }
    }
}

fun main() {

    fun parseInput(input: List<String>): List<Scanner> {
        val scanners = mutableListOf<Scanner>()
        var idx = 0
        while (idx < input.size) {
            idx++   // skip header
            val beacons = mutableListOf<Point3D>()
            while (idx < input.size && input[idx].isNotEmpty()) {
                val (x, y, z) = input[idx].split(',').map { it.toInt() }
                beacons.add(Point3D(x, y, z))
                idx++
            }
            scanners.add(Scanner(beacons))
            idx++   // skip blank line
        }
        return scanners
    }

    fun solve(input: List<String>): Pair<Int, Int> {
        val scanners = parseInput(input)
        val queue = ArrayDeque<Int>()
        val visited = BooleanArray(scanners.size)
        val standardBeaconSets = Array(scanners.size) { setOf<Point3D>() }
        val positions = Array(scanners.size) { Point3D(0, 0, 0) }
        standardBeaconSets[0] = scanners[0].beacons.toSet()
        queue.addLast(0)
        visited[0] = true
        while (queue.isNotEmpty()) {
            val idx = queue.removeFirst()
            val standardBeacons = standardBeaconSets[idx]
            for (i in scanners.indices) {
                if (visited[i]) continue
                ort@ for (scOrt in scanners[i].orientations) {
                    for (standard in standardBeacons) {
                        for (target in scOrt.beacons) {
                            val delta = standard - target
                            val candidates = scOrt.beacons.map { it + delta }
                            if (candidates.count { it in standardBeacons } >= 12) {
                                queue.addLast(i)
                                positions[i] = delta
                                standardBeaconSets[i] = candidates.toSet()
                                visited[i] = true
                                break@ort
                            }
                        }
                    }
                }
            }
        }

        var maxDist = Int.MIN_VALUE
        for (i in 0 until positions.lastIndex) {
            for (j in i + 1 until positions.size) {
                maxDist = maxOf(maxDist, positions[i] dist positions[j])
            }
        }
        val allBeacons = standardBeaconSets.flatMap { it }.toSet()
        return Pair(allBeacons.size, maxDist)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    val (test1, test2) = solve(testInput)
    check(test1 == 79)
    check(test2 == 3621)

    val input = readInput("Day19")
    val (part1, part2) = solve(input)
    println(part1)
    println(part2)
}