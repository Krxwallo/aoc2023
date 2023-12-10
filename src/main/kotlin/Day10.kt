
fun main() = day(10) {
    val pipes = inputLines.readGrid()
    val start = pipes.firstNotNullOf { if (it.value == 'S') it.key else null }

    fun connectionsFor(pipe: Pos): List<Pos> {
        pipes[pipe] ?: return emptyList()
        val x = pipe.x
        val y = pipe.y
        return when (pipes[pipe]) {
            '|' -> listOf(Pos(x, y + 1), Pos(x, y - 1))
            '-' -> listOf(Pos(x + 1, y), Pos(x - 1, y))
            'J' -> listOf(Pos(x - 1, y), Pos(x, y - 1))
            '7' -> listOf(Pos(x - 1, y), Pos(x, y + 1))
            'F' -> listOf(Pos(x + 1, y), Pos(x, y + 1))
            'L' -> listOf(Pos(x, y - 1), Pos(x + 1, y))
            'S' -> pipe.neighbours
            else -> emptyList()
        }
    }


    part1 {
        var lastPipe: Pos? = null
        var pipe: Pos = start
        repeat(Int.MAX_VALUE) { counter ->
            val next = connectionsFor(pipe).firstOrNull {
                pipes[it] != null
                        && it != lastPipe
                        && it != start
                        && pipe in connectionsFor(it)
            } ?: return@part1 (counter + 1) / 2
            lastPipe = pipe
            pipe = next
        }
    }

    part2 {

    }

    expectPart2 = null
}