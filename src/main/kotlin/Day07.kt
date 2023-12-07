val order = listOf("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A")

data class Hand(val data: List<Int>, val bid: Int) {
    val handValue = data.joinToString { it.toChar().toString() }

    fun typeValue(allowJoker: Boolean): Int {
        var sortedData = data.sorted()

        var countsList = buildList {
            while (sortedData.isNotEmpty()) {
                val toTake = sortedData.takeWhile { it == sortedData.first() }
                sortedData = sortedData.drop(toTake.size)
                add(toTake)
            }
        }.sortedByDescending { it.size }.toMutableList()

        // Maybe add joker count to the first x of a kind
        if (allowJoker && countsList.size > 1) {
            countsList.indexOfFirst {
                0 in it
            }.takeIf { it != -1 }?.let { jokerIndex ->
                val jokers = countsList[jokerIndex]
                countsList = countsList.filterIndexed { i, b ->
                    i != jokerIndex
                }.toMutableList()
                countsList[0] += jokers
            }
        }

        val counts = countsList.map { it.size }

        return if (counts.first() == 5) 7
        else if (counts.first() == 4) 6
        else if (counts.first() == 3 && counts[1] == 2) 5
        else if (counts.first() == 3) 4
        else if (counts.first() == 2 && counts[1] == 2) 3
        else if (counts.first() == 2) 2
        else if (counts.size == 5) 1 else throw RuntimeException()
    }
}


fun main() = day(7) {

    fun getHands(joker: Boolean) = inputLines.map { line ->
        Hand(line.split(" ")[0].map { c ->
            order.let { list ->
                if (joker) listOf("J") + (list - "J") else list
            }.indexOf(c.toString())
        }, line.split(" ")[1].toInt())
    }

    part1 {
        // Sort hands
        val rankedHands = getHands(false).sortedWith(compareBy(
            { it.typeValue(false) },
            { it.handValue }
        ))

        // Sum up
        rankedHands.withIndex().sumOf {
            (it.index + 1) * it.value.bid
        }
    }

    part2 {
        // Sort hands
        val rankedHands = getHands(true).sortedWith(compareBy(
            { it.typeValue(true) },
            { it.handValue }
        ))

        // Sum up
        rankedHands.withIndex().sumOf {
            (it.index + 1) * it.value.bid
        }
    }

    expectPart2 = 5905
}