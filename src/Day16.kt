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
        private var _versionSum = 0

        val versionSum: Int by lazy {
            idx = 0
            _versionSum = 0
            sumVersion(maxCnt = 1)
            _versionSum
        }
        val value: Long by lazy {
            idx = 0
            eval(maxCnt = 1)
        }

        private fun nextInt(n: Int) = packet.substring(idx, idx + n).toInt(2).also { idx += n }

        private fun sumVersion(maxLen: Int = packet.length, maxCnt: Int = packet.length) {
            val start = idx
            var cnt = 0
            while (++cnt <= maxCnt && idx - start < maxLen) {
                val version = nextInt(3)
                _versionSum += version
                when (nextInt(3)) {
                    4    -> {
                        while (nextInt(1) == 1) nextInt(4)
                        nextInt(4)
                    }
                    else -> when (nextInt(1)) {
                        0 -> sumVersion(maxLen = nextInt(15))
                        1 -> sumVersion(maxCnt = nextInt(11))
                    }
                }
            }
        }

        fun eval(maxLen: Int = packet.length, maxCnt: Int = packet.length, type: Int = -1): Long {
            val start = idx
            var cnt = 0
            val re = mutableListOf<Long>()
            while (++cnt <= maxCnt && idx - start < maxLen) {
                nextInt(3) // discard version number
                when (val currType = nextInt(3)) { // packet type
                    4    -> {
                        var num = 0L
                        while (nextInt(1) == 1) num = (num shl 4) + nextInt(4)
                        num = (num shl 4) + nextInt(4)
                        re.add(num)
                    }
                    else -> when (nextInt(1)) {
                        0 -> re.add(eval(maxLen = nextInt(15), type = currType))
                        1 -> re.add(eval(maxCnt = nextInt(11), type = currType))
                    }
                }
            }
            return when (type) {
                -1   -> re.first()
                0    -> re.sum()
                1    -> re.reduce(Long::times)
                2    -> re.minOrNull()!!
                3    -> re.maxOrNull()!!
                5    -> if (re[0] > re[1]) 1L else 0
                6    -> if (re[0] < re[1]) 1L else 0
                7    -> if (re[0] == re[1]) 1L else 0
                else -> throw IllegalArgumentException()
            }
        }
    }

    fun part1(input: String): Int {
        val packet = parseInput(input)
        val parser = PacketParser(packet)
        return parser.versionSum
    }

    fun part2(input: String): Long {
        val packet = parseInput(input)
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
    check(testInputs1.map { part1(it) } == results1)
    val testInputs2 = listOf(
            "C200B40A82", "04005AC33890", "880086C3E88112", "CE00C43D881120", "D8005AC2A8F0", "F600BC2D8F",
            "9C005AC2F8F0", "9C0141080250320F1802104A08"
    )
    val results2 = listOf(3L, 54L, 7L, 9L, 1L, 0L, 0L, 1L)
    check(testInputs2.map { part2(it) } == results2)

    val input = readInput("Day16").first()
    println(part1(input))
    println(part2(input))
}
