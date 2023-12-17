import kotlin.math.max
import kotlin.math.min

fun main() = day(11) {
    val columns = inputLines.map { it.toList() }.transpose()

    val universe = inputLines.readGrid()
    val galaxies = universe.filterValues { it == '#' }

    /**
     * Find the shortest path between two galaxies.
     *
     * @param expansion how many times larger a row/column should be when it contains no galaxy
     */
    fun Pair<Pos, Pos>.distance(expansion: Long = 1) =
        (min(first.x, second.x) until max(first.x, second.x)).sumOf { x ->
            if (columns[x].all { it == '.' }) expansion
            else 1
        } + (min(first.y, second.y) until max(first.y, second.y)).sumOf { y ->
            if (inputLines[y].all { it == '.' }) expansion
            else 1
        }

    val galaxyPairs = galaxies.keys.pairs()

    part1 {
        galaxyPairs.sumOf {
            it.distance(2)
        }
    }

    part2 {
        galaxyPairs.sumOf {
            it.distance(1000000)
        }
    }

    expectPart1 = 374L
    expectPart2 = 82000210L
}