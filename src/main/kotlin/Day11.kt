import kotlin.math.abs

fun main() = day(11) {
    val ints = inputLines.map { it.parseSignedInts() }
    val flatInts = inputLines.flatMap { it.parseSignedInts() }

    var lines = inputLines.toMutableList()
    lines.toList().forEachIndexed { index, line ->
        if (line.all { it == '.' }) {
            // empty line
            lines.add(index, line)
        }
    }

    var rotatedLines = lines.map { it.toCharArray().toList() }.transpose().toMutableList()
    rotatedLines.toList().forEachIndexed { index, line ->
        if (line.all { it == '.' }) {
            // empty column
            rotatedLines.add(index, line)
        }
    }

    if (rotatedLines.first().size != 12) panic(rotatedLines.first().size)
    if (rotatedLines.size != 13) panic("")

    val universe = rotatedLines.transpose().map { it.toString() }.readGrid()
    println("Width: ${rotatedLines.first().size}")
    println("Height: ${rotatedLines.size}")

    val galaxies = universe.filterValues { it == '#' }

    fun Pair<Pos, Pos>.distance(): Int {
        // Find the shortest path between the two galaxies

        val (x, y) = first - second

        return abs(x) + abs(y)
    }

    part1 {
        val galaxyPairs = galaxies.keys.pairs()

        println(galaxyPairs)
        println(galaxyPairs.size)

        val galaxyOne = Pos(4, 0)
        val galaxy7 = Pos(9, 10)

        print("Distance: ")
        println((galaxyOne to galaxy7).distance())

        galaxyPairs.sumOf {
            it.distance()
        }
    }

    part2 {

    }

    expectPart1 = 374
    expectPart2 = null
}