fun main() = day(13) {
    val patterns = inputString.split("\n\n")

    fun Map<Pos, Char>.getMirrorScore(smudgeCount: Int = 0): Int {
        var result = 0

        // Check vertical mirrors
        (1 until width).forEach { x ->
            var smudges = 0
            for (i in 1 until Int.MAX_VALUE) {
                val leftColumn = getColumn(x - i)
                val rightColumn = getColumn(x + i - 1)
                if (leftColumn.isEmpty() || rightColumn.isEmpty()) {
                    if (i == 1) panic("i must be != 1")
                    if (smudges == smudgeCount) result += x
                    break
                }
                smudges += leftColumn.withIndex().count { (index, c) -> rightColumn[index] != c }
                if (smudges > smudgeCount) break
            }
        }

        // Check horizontal mirrors
        (1 until height).forEach { y ->
            var smudges = 0
            for (i in 1 until Int.MAX_VALUE) {
                val leftRow = getRow(y - i)
                val rightRow = getRow(y + i - 1)
                if (leftRow.isEmpty() || rightRow.isEmpty()) {
                    if (i == 1) panic("i must be != 1")
                    if (smudges == smudgeCount) result += y * 100
                    break
                }
                smudges += leftRow.withIndex().count { (index, c) -> rightRow[index] != c }
                if (smudges > smudgeCount) break
            }
        }

        return result
    }

    fun computeFor(smudgeCount: Int) = patterns.sumOf { it.lines().readGrid().getMirrorScore(smudgeCount) }

    part1 {
        computeFor(0)
    }

    part2 {
        computeFor(1)
    }

    expectPart1 = 405
    expectPart2 = 400
}