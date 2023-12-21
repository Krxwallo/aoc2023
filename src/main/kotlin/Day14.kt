fun main() = day(14) {
    fun List<List<Char>>.tilted() = map { column ->
        column.splitAt('#')
            .map { it.sortedBy { c -> if (c == 'O') 0 else 1 } }
            .joinToString("#") { it.toCharArray().concatToString() }
            .toList()
    }

    /** Rotate the platform by 90° */
    fun List<List<Char>>.rotated() = transpose().reversed()

    /** Each cycle tilts the platform four times so that the rounded rocks roll north, then west, then south, then east. */
    fun List<List<Char>>.cycled(): List<List<Char>> =
        tilted().rotated().tilted().rotated().tilted().rotated().tilted().rotated()

    fun List<List<Char>>.calculateLoad() = sumOf {
        it.withIndex()
            .filter { (_, c) -> c == 'O' }
            .sumOf { (i, _) -> it.size - i }
    }

    val columns = inputLines.transpose()

    part1 {
        columns.tilted().calculateLoad()
    }

    part2 {
        val history = mutableSetOf<List<List<Char>>>()
        var c = columns

        val totalCount = 1000000000
        var cycleLength = 0
        repeat(Int.MAX_VALUE) {
            c = c.cycled()
            if (c in history) {
                val ind = history.indexOf(c)
                if (cycleLength == 0) cycleLength = it - ind
                if ((totalCount - it - 1) % cycleLength == 0) return@part2 c.calculateLoad()
            } else history += c
        }
    }

    expectPart2 = 64
}