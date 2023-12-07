import kotlin.math.pow

val order = listOf("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A")

// get a big number
val p = (order.size + 1.0).pow(6)

data class Hand(val data: List<Int>, val bid: Int) {
    val cardValue = data.reversed().withIndex().sumOf {
        it.value * (order.size + 1.0).pow(it.index.toDouble())
    }

    fun typeValue(allowJoker: Boolean): Double {
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

        fun p(i: Int) = p * i

        return if (counts.first() == 5) p(7)
        else if (counts.first() == 4) p(6)
        else if (counts.first() == 3 && counts[1] == 2) p(5)
        else if (counts.first() == 3) p(4)
        else if (counts.first() == 2 && counts[1] == 2) p(3)
        else if (counts.first() == 2) p(2)
        else if (counts.size == 5) p(1) else 0.0
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
        val rankedHands = getHands(false).sortedBy {
            it.typeValue(false) + it.cardValue
        }

        // Sum up
        rankedHands.withIndex().sumOf {
            (it.index + 1) * it.value.bid
        }
    }

    part2 {
        // Sort hands
        val rankedHands = getHands(true).sortedBy {
            it.typeValue(true) + it.cardValue
        }

        // Sum up
        rankedHands.withIndex().sumOf {
            (it.index + 1) * it.value.bid
        }
    }

    expectPart2 = 5905
}