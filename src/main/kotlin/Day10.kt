


fun main() = day(10) {
    val pipes = inputLines.readGrid()

    val start = pipes.firstNotNullOf { if (it.value == 'S') it.key else null }

    fun connectedTo(pipe: Pos): List<Pos> {
        val c = pipes[pipe] ?: return emptyList()
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

    fun connected(pos1: Pos, pos2: Pos): Boolean {
        val connected1 = connectedTo(pos1)
        val connected2 = connectedTo(pos2)
        val connected = connected1.any { it == pos2 } && connected2.any { it == pos1 }
        return connected
    }

    var tryedPositions = sequenceOf<Pos>()

    part1 {
        /*fun tryPos(pos: Pos): Int {
            tryedPositions += pos
            return pos.neighbours.maxOf { n ->
                val pipe = charGrid[n] ?: return@maxOf 0
                val connected = connected(n, pos)
                if (!connected) return@maxOf 0
                if (n in tryedPositions) {
                    return@maxOf 0
                }
                return@maxOf tryPos(n) + 1
            }
        }
        (tryPos(start) + 1) / 2*/
        var nextPositions = connectedTo(start)
        var previousPositions = emptyList<Pos>()
        var previousPositions1 = emptyList<Pos>()
        var steps = 0

        while (nextPositions.any { it !in previousPositions }) {
            println("Trying for $nextPositions | $previousPositions")
            previousPositions = previousPositions1.toList()
            previousPositions1 = nextPositions.toList()
            nextPositions = buildList {
                for (n in nextPositions.toList()) {
                    val successors = connectedTo(n)
                    for (s in successors) {
                        pipes[s] ?: continue
                        if (s in previousPositions) continue

                        val c = connected(n, s)
                        if (c) add(s)
                    }
                }
            }
            steps++
        }

        fun tryPos(pos: Pos): Int {
            tryedPositions += pos
            return pos.neighbours.maxOf { n ->
                val pipe = pipes[n] ?: return@maxOf 0
                val connected = connected(n, pos)
                if (!connected) return@maxOf 0
                if (n in tryedPositions) {
                    return@maxOf 0
                }
                return@maxOf tryPos(n) + 1
            }
        }
        steps
    }

    part2 {

    }


    expectPart1 = null
    expectPart2 = null
}