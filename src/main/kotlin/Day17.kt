import java.io.File
import kotlin.math.min

fun main() = day(17) {
    val grid = inputLines.readGrid().mapValues { it.value.digitToInt() }

    data class Crucible(
        var currentPos: Pos = Pos(0, 0),
        var currentDirection: Pos = Pos(1, 0),
        val history: List<Triple<Pos, Pos, Long>> = emptyList(),
        val heatLoss: Long = 0,
    ) {
        fun getNextPositions(): List<Pos> = currentPos.neighbours.filter { n ->
            (history.none { n == it.first }) && n in grid && history.takeIf { it.size >= 3 }?.takeLast(3)?.let { (prePreLast, preLast, last) ->
                val direction = (prePreLast.second)
                direction != (preLast.second) || direction != (last.second)
            } ?: true
        }

        fun moveTo(nextPos: Pos): Crucible = Crucible(nextPos, nextPos - currentPos, history + listOf(Triple(currentPos, currentDirection, heatLoss)), heatLoss + grid[nextPos]!!)

        fun visualize() {
            val myGrid = grid.filterKeys { pos -> history.none { it.first == pos } }

            File("Day17_vis.txt").writeText(buildString {
                (0 until gridHeight).forEach { y ->
                    (0 until gridWidth).forEach { x ->
                        append(myGrid[Pos(x, y)]?.toString() ?: '=')
                    }
                    append("\n")
                }
            })
        }
    }

    part1 {
        val path = mutableListOf<Pos>()
        val endPos = Pos(gridWidth - 1, gridHeight - 1)

        val crucibles = mutableListOf(Crucible())
        var minCrucible = Crucible(heatLoss = 104L)

        while (crucibles.any { it.currentPos != endPos }) {
            crucibles.toList().also { crucibles.clear() }.forEach { c ->
                c.getNextPositions().filter { pos ->
                    crucibles.none { it.history.any { (hPos, hDir, heatLoss) ->
                        hPos == pos && hDir == c.history.firstOrNull { it.first == hPos }?.second && heatLoss < c.heatLoss
                    }}
                }.forEach { nextPos ->
                    val newCrucible = c.moveTo(nextPos)
                    if (newCrucible.currentPos == endPos) {
                        println("Crucible finished: $newCrucible")
                        if (newCrucible.heatLoss < minCrucible.heatLoss) minCrucible = newCrucible.copy()
                    }
                    else if (newCrucible.heatLoss < minCrucible.heatLoss) crucibles.add(newCrucible)
                }
            }
            print("Crucibles: ")
            println(crucibles.size)
        }

        println(minCrucible)
        minCrucible.visualize()
        minCrucible.heatLoss
    }

    part2 {

    }

    expectPart2 = null
}