fun main() = day(6) {
    fun compute(time: Long, distance: Long) = (0..time).count {
        (time - it) * it > distance
    }

    part1 {
        fun String.values() = substringAfter(":").split(' ').mapNotNull { it.toLongOrNull() }
        val times = inputLines.first().values()
        val distances = inputLines.last().values()

        var result = 1L
        times.withIndex().forEach {
            result *= compute(it.value, distances[it.index])
        }
        result
    }

    part2 {
        fun String.value() = substringAfter(":").replace(" ", "").toLong()
        val time = inputLines.first().value()
        val distance = inputLines.last().value()

        compute(time, distance)
    }

    expectPart1 = 288L
    expectPart2 = 71503
}