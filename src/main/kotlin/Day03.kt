private data class PartNumber(val pos: Pos, var value: String) {
    val number get() = value.toInt()
    val digitLength get() = value.length
    val adjacentPositions: List<Pos>
        get() = buildList {
            for (x in pos.x - 1..pos.x + digitLength) {
                for (y in pos.y - 1..pos.y + 1) add(Pos(x, y))
            }
        }
    infix fun isAdjacentTo(symbol: Symbol) = symbol.pos in adjacentPositions
}

private data class Symbol(val char: Char, val pos: Pos) {
    infix fun isAdjacentTo(partNumber: PartNumber) = partNumber isAdjacentTo this
}

fun main() = day(3) {
    val symbols = mutableListOf<Symbol>()
    val partNumbers = mutableListOf<PartNumber>()
    inputLines.forEachIndexed { y, line ->
        var currentPartNumber: PartNumber? = null
        line.forEachIndexed { x, c ->
            val pos = Pos(x, y)
            if (c.isDigit()) {
                currentPartNumber?.let {
                    it.value += c
                } ?: run {
                    currentPartNumber = PartNumber(pos, c.toString())
                }
            } else {
                currentPartNumber?.let { partNumbers.add(it) }
                currentPartNumber = null
            }
            if (!c.isDigit() && c != '.') symbols.add(Symbol(c, pos))
        }
        currentPartNumber?.let { partNumbers.add(it) }
    }

    part1 {
        partNumbers.sumOf { part ->
            if (symbols.any { it isAdjacentTo part }) part.number else 0
        }
    }

    part2 {
        symbols.sumOf { symbol ->
            if (symbol.char != '*') return@sumOf 0
            val adjacentParts = partNumbers.filter { it isAdjacentTo symbol }
            if (adjacentParts.size == 2) adjacentParts.first().number * adjacentParts.last().number else 0
        }
    }

    expectPart1 = 4361
    expectPart2 = 467835
}