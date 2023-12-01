fun main() = day(1) {

    val input = inputString.split("\n\n").asSequence()
        .map { g -> g.lines().sumOf { it.toInt() } }

    part1 {
        input.max()
    }

    part2 {
        input.sortedDescending().take(3).sum()
    }

    expectPart1 = 24000
    expectPart2 = 45000
}