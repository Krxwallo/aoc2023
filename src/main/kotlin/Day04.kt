import kotlin.math.pow

data class Card(
    val winningNumbers: List<Int>,
    val ownNumbers: List<Int>,
    var count: Int = 1,
) {
    val ownWinningNumbers get() = ownNumbers.intersect(winningNumbers.toSet())
}

fun Card(
    data: String
): Card {
    val numbers = data.substringAfter(":").split(" | ")
    fun String.parseNumberList() = split(' ').mapNotNull { it.toIntOrNull() }
    return Card(
        numbers.first().parseNumberList(),
        numbers.last().parseNumberList()
    )
}

fun main() = day(4) {
    val cards = inputLines.map { Card(it) }

    part1 {
        cards.sumOf {
            val wins = it.ownWinningNumbers.size
            if (wins == 0) 0 else 2.toDouble().pow(wins.toDouble() - 1.0).toInt()
        }
    }

    part2 {
        cards.withIndex().sumOf {
            val wins = it.value.ownWinningNumbers.size
            if (wins > 0) cards.subList(it.index + 1, it.index + 1 + wins).forEach { nextCard ->
                nextCard.count += it.value.count
            }
            it.value.count
        }
    }

    expectPart1 = 13
    expectPart2 = 30
}