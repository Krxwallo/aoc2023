import java.io.File


/*
NEEDS HEAVY CLEAN UP
 */
fun main() = day(10) {
    val grid = inputLines.readGrid()
    val startPos = grid.filterValues { it == 'S' }.keys.first()

    fun connectionsFor(pipePos: Pos): List<Pos> {
        val (x, y) = pipePos
        return when (grid[pipePos] ?: return emptyList()) {
            '|' -> listOf(Pos(x, y + 1), Pos(x, y - 1))
            '-' -> listOf(Pos(x + 1, y), Pos(x - 1, y))
            'J' -> listOf(Pos(x - 1, y), Pos(x, y - 1))
            '7' -> listOf(Pos(x - 1, y), Pos(x, y + 1))
            'F' -> listOf(Pos(x + 1, y), Pos(x, y + 1))
            'L' -> listOf(Pos(x, y - 1), Pos(x + 1, y))
            'S' -> pipePos.neighbours.filter {
                // Check neighbours
                pipePos in connectionsFor(it)
            }
            else -> emptyList()
        }
    }

    // list containing all pipe positions belonging to the big/main loop
    val mainLoop = buildList {
        var lastPipe: Pos? = null
        var pipe: Pos = startPos
        while (add(pipe)) {
            val next = connectionsFor(pipe).firstOrNull {
                grid[it] != null
                        && it != lastPipe
                        && it != startPos
                        && pipe in connectionsFor(it)
            } ?: break
            lastPipe = pipe
            pipe = next
        }
    }

    part1 {
        mainLoop.size / 2
    }

    part2 {
        fun Pos.to3x3() = Pos(x * 3, y * 3)
        fun Pos.to1x1() = Pos(x / 3, y / 3)

        fun Map<Pos, Char>.printOut() {
            File("Day10_vis.txt").writeText(buildString {
                (0..<bigGridHeight * 3).forEach { y ->
                    (0..<bigGridWidth * 3).forEach { x ->
                        append(this@printOut[Pos(x, y)].let { if (it == 'o') ' ' else it } ?: '.')
                    }
                    append("\n")
                }
            })
        }

        fun Map<Pos, Char>.floodFill(): Int {
            val outerTiles = filterKeys { it.x == 0 || it.y == 0 || it.x == bigGridWidth * 3 - 1 || it.y == bigGridHeight * 3 - 1}
                .filterValues { it == 'o' }

            printOut()

            val allTiles = toMutableMap()
            val currentTiles = outerTiles.toMutableMap()
            val floodedTiles = currentTiles.toMutableMap()
            while (currentTiles.any { it.value == 'o' }) {

                currentTiles.filterValues { it == 'o' }.toMap().also {
                    currentTiles.clear()
                }.forEach { (pos, _) ->
                    allTiles[pos] = '0'
                    floodedTiles[pos] = '0'


                    pos.neighbours.forEach { n ->
                        val neighbourChar = this[n]
                        if ((neighbourChar == 'o' || neighbourChar == 'S') && n !in floodedTiles) currentTiles[n] = 'o'
                    }
                }
            }

            allTiles.printOut()

            return grid.keys.count { pos1x1 ->
                // Checking middle tiles is enough
                allTiles[pos1x1.to3x3() + Pos(1, 1)] == 'o'
            }
        }

        /* Map each tile to 3x3 */
        val mappedTiles = buildMap {
            gridPositions.associateWith { grid[it].takeIf { _ -> it in mainLoop } }.forEach { (pos1x1, tile) ->
                when (tile) {
                    '|' -> "oxo\noxo\noxo"
                    '-' -> "ooo\nxxx\nooo"
                    'J' -> "oxo\nxxo\nooo"
                    '7' -> "ooo\nxxo\noxo"
                    'F' -> "ooo\noxx\noxo"
                    'L' -> "oxo\noxx\nooo"
                    'S' -> "oxo\noxo\noxo" // TODO don't hardcode start
                    null -> "ooo\nooo\nooo"
                    else -> panic("Unknown tile $tile")
                }.lines().readGrid().forEach { (innerPos, innerChar) ->
                    put(pos1x1.to3x3() + innerPos, innerChar)
                }
            }
        }

        mappedTiles.floodFill()
    }

    expectPart2 = 10
}