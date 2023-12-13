import java.io.File


/*
NEEDS HEAVY CLEAN UP
 */
fun main() = day(10) {
    val grid = inputLines.readGrid()
    val start = grid.firstNotNullOf { if (it.value == 'S') it.key else null }

    fun connectionsFor(pipe: Pos): List<Pos> {
        grid[pipe] ?: return emptyList()
        val x = pipe.x
        val y = pipe.y
        return when (grid[pipe]) {
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

    val bigLoop = buildList {
        var lastPipe: Pos? = null
        var pipe: Pos = start
        while (add(pipe)) {
            val next = connectionsFor(pipe).firstOrNull {
                grid[it] != null
                        && it != lastPipe
                        && it != start
                        && pipe in connectionsFor(it)
            } ?: break
            lastPipe = pipe
            pipe = next
        }
    }

    part1 {
        bigLoop.size / 2
    }

    part2 {
        val maxX = inputLines.first().count()
        val maxY = inputLines.count()
        fun Pos.to3x3() = Pos(x * 3, y * 3)
        fun Pos.to1x1() = Pos(x / 3, y / 3)

        fun Map<Pos, Char>.printOut() {
            File("Day10_vis.txt").writeText(buildString {
                (0..<maxY * 3).forEach { y ->
                    (0..<maxX * 3).forEach { x ->
                        append(this@printOut[Pos(x, y)].let { if (it == 'o') ' ' else it } ?: '.')
                    }
                    append("\n")
                }
            })
        }

        fun Map<Pos, Char>.floodFill(): Int {
            val outerTiles = filterKeys { it.x == 0 || it.y == 0 || it.x == maxX * 3 - 1 || it.y == maxY * 3 - 1}
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
                val b = (0..2).none { x ->
                    val b1 = (0..2).any { y ->
                        val pos3x3 = pos1x1.to3x3() + Pos(x, y)

                        val c = allTiles[pos3x3]
                        //println("Checking $pos3x3 -> $c")
                        c == '0' || c == 'x'
                    }
                    //println("-----> $b1")
                    b1
                }
                //println("--> $b")
                b
            }
        }

        /* Map each tile to 3x3 */
        val mappedTiles = buildMap {
            grid.forEach { (pos1x1, tile) ->
                when (tile) {
                    '|' -> """
                    oxo
                    oxo
                    oxo
                """.trimIndent()

                    '-' -> """
                    ooo
                    xxx
                    ooo
                """.trimIndent()

                    'J' -> """
                    oxo
                    xxo
                    ooo
                """.trimIndent()

                    '7' -> """
                    ooo
                    xxo
                    oxo
                """.trimIndent()

                    'F' -> """
                    ooo
                    oxx
                    oxo
                """.trimIndent()

                    'L' -> """
                    oxo
                    oxx
                    ooo
                """.trimIndent()

                    'S' -> """
                    oxo
                    oxo
                    oxo
                """.trimIndent() // FIXME

                    '.' -> """
                    ooo
                    ooo
                    ooo
                """.trimIndent()

                    else -> panic("Unknown tile $tile")
                }.lines().readGrid().forEach { (innerPos, innerChar) ->
                    put(pos1x1.to3x3() + innerPos, innerChar)
                }
            }
        }

        println("SIZE: ${bigLoop.size}")
        mappedTiles.mapValues {
            if (it.key.to1x1() !in bigLoop) 'o' else it.value
        }.floodFill()
    }

    expectPart2 = 10
}