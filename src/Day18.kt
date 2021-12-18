private sealed class SnailFishNumber {

    operator fun plus(other: SnailFishNumber): SnailFishNumber {
        var value: SnailFishNumber = PairNumber(this, other)
        while (true)
            value = value.explode()?.number ?: value.split() ?: return value
    }

    abstract val magnitude: Int
    protected abstract fun addToFirstRegular(n: Int): SnailFishNumber
    protected abstract fun addToLastRegular(n: Int): SnailFishNumber
    protected abstract fun split(): SnailFishNumber?
    protected abstract fun explode(depth: Int = 0): ExplosionResult?


    data class RegularNumber(val value: Int) : SnailFishNumber() {
        override fun toString() = value.toString()
        override fun addToFirstRegular(n: Int) = RegularNumber(value + n)
        override fun addToLastRegular(n: Int) = RegularNumber(value + n)
        override fun split() = if (value >= 10) PairNumber(value / 2, (value + 1) / 2) else null
        override fun explode(depth: Int): ExplosionResult? = null
        override val magnitude: Int
            get() = value
    }

    data class PairNumber(val left: SnailFishNumber, val right: SnailFishNumber) : SnailFishNumber() {

        constructor(left: Int, right: Int) : this(RegularNumber(left), RegularNumber(right))

        override fun toString() = "[$left,$right]"
        override fun split(): SnailFishNumber? {
            val l = left.split()
            if (l != null)
                return PairNumber(l, right)
            val r = right.split()
            if (r != null)
                return PairNumber(left, r)
            return null
        }

        override fun explode(depth: Int): ExplosionResult? {
            if (depth == 4)
                return ExplosionResult(
                        RegularNumber(0),
                        (left as RegularNumber).value,
                        (right as RegularNumber).value
                )

            val leftExplosion = left.explode(depth + 1)
            if (leftExplosion != null)
                return ExplosionResult(
                        PairNumber(leftExplosion.number, right.addToFirstRegular(leftExplosion.rightResVal)),
                        leftExplosion.leftResVal,
                        0
                )
            val rightExplosion = right.explode(depth + 1)
            if (rightExplosion != null)
                return ExplosionResult(
                        PairNumber(left.addToLastRegular(rightExplosion.leftResVal), rightExplosion.number),
                        0,
                        rightExplosion.rightResVal
                )

            return null
        }

        override fun addToFirstRegular(n: Int) = PairNumber(left.addToFirstRegular(n), right)
        override fun addToLastRegular(n: Int) = PairNumber(left, right.addToLastRegular(n))
        override val magnitude: Int
            get() = 3 * left.magnitude + 2 * right.magnitude
    }

    companion object {
        operator fun invoke(s: String) = SimpleParser(s).parse()

        protected class ExplosionResult(val number: SnailFishNumber, val leftResVal: Int, val rightResVal: Int)

        private class SimpleParser(private val s: String) {
            private var idx = 0

            fun parse(): SnailFishNumber =
                    if (s[idx] == '[') {
                        idx++
                        val left = parse()
                        idx++
                        val right = parse()
                        idx++
                        PairNumber(left, right)
                    } else {
                        var num = 0
                        while (s[idx].isDigit()) {
                            num = num * 10 + s[idx++].digitToInt()
                        }
                        RegularNumber(num)
                    }
        }
    }
}

fun main() {

    fun parseInput(input: List<String>) = input.map { SnailFishNumber(it) }

    fun part1(input: List<String>): Int {
        val numbers = parseInput(input)
        return numbers.reduce { acc, number -> acc + number }.magnitude
    }

    fun part2(input: List<String>): Int {
        var max = Int.MIN_VALUE
        val numbers = parseInput(input)
        for (i in 0 until numbers.lastIndex) {
            for (j in i + 1 until numbers.size) {
                max = maxOf(max, (numbers[i] + numbers[j]).magnitude, (numbers[j] + numbers[i]).magnitude)
            }
        }
        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
