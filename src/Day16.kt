fun main() {

    fun parseInput(input: String) = buildString {
        for (c in input) {
            val num = c.digitToInt(16)
            val str = num.toString(2)
            append(str.padStart(4, '0'))
        }
    }

    class PacketParser(val packet: String) {
        private var idx = 0

        val versionSum: Int
        val value: Long

        init {
            val (s, v) = parse(maxCnt = 1)
            versionSum = s
            value = v
        }

        private fun nextInt(n: Int) = packet.substring(idx, idx + n).toInt(2).also { idx += n }

        private fun parse(maxLen: Int = packet.length, maxCnt: Int = packet.length, type: Int = -1): Pair<Int, Long> {
            val start = idx
            var cnt = 0
            val cache = mutableListOf<Long>()
            var sum = 0
            while (++cnt <= maxCnt && idx - start < maxLen) {
                sum += nextInt(3) // discard version number
                when (val currType = nextInt(3)) { // packet type
                    4    -> {
                        var num = 0L
                        while (nextInt(1) == 1) num = (num shl 4) + nextInt(4)
                        num = (num shl 4) + nextInt(4)
                        cache.add(num)
                    }
                    else -> {
                        val (subSum, subValue) = if (nextInt(1) == 0)
                            parse(maxLen = nextInt(15), type = currType)
                        else
                            parse(maxCnt = nextInt(11), type = currType)
                        sum += subSum
                        cache.add(subValue)
                    }
                }
            }
            val value = when (type) {
                -1   -> cache.first()
                0    -> cache.sum()
                1    -> cache.reduce(Long::times)
                2    -> cache.minOrNull()!!
                3    -> cache.maxOrNull()!!
                5    -> if (cache[0] > cache[1]) 1L else 0
                6    -> if (cache[0] < cache[1]) 1L else 0
                7    -> if (cache[0] == cache[1]) 1L else 0
                else -> throw IllegalArgumentException()
            }
            return Pair(sum, value)
        }
    }

    fun part1(packet: String): Int {
        val parser = PacketParser(packet)
        return parser.versionSum
    }

    fun part2(packet: String): Long {
        val parser = PacketParser(packet)
        return parser.value
    }

// test if implementation meets criteria from the description, like:
    val testInputs1 = listOf(
            "8A004A801A8002F478",
            "620080001611562C8802118E34",
            "C0015000016115A2E0802F182340",
            "A0016C880162017C3686B18A3D4780"
    )
    val results1 = listOf(16, 12, 23, 31)
    check(testInputs1.map { part1(parseInput(it)) } == results1)
    val testInputs2 = listOf(
            "C200B40A82", "04005AC33890", "880086C3E88112", "CE00C43D881120", "D8005AC2A8F0", "F600BC2D8F",
            "9C005AC2F8F0", "9C0141080250320F1802104A08"
    )
    val results2 = listOf(3L, 54L, 7L, 9L, 1L, 0L, 0L, 1L)
    check(testInputs2.map { part2(parseInput(it)) } == results2)

    val input = readInput("Day16").first()
    val packet = parseInput(input)
    println(part1(packet))
    println(part2(packet))
}
