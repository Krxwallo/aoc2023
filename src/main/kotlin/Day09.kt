fun extrapolate(ints: List<Int>): Int {
    if (ints.all { it == 0 }) return 0
    return ints.last() + extrapolate(ints.diffs())
}


fun main() = day(9) {
    val sequences = inputLines.map { it.parseSignedInts().let { ints ->
        if (currentPart == 2) ints.reversed() else ints
    } }

    part1 {
        sequences.sumOf {
            extrapolate(it)
        }
    }

    part2 {
        sequences.sumOf {
            extrapolate(it)
        }
    }


    expectPart1 = 114
    expectPart2 = 2
}