fun main() = day(16) {
    val grid = inputLines.readGrid()

    fun tryConfiguration(startingPosition: Pos, startingDirection: Direction): Int {
        val history = mutableSetOf<Pair<Pos, Direction>>()
        val beams = mutableSetOf(startingPosition to startingDirection)

        while (beams.isNotEmpty()) {
            for (beam in beams.toList()) {
                history += beam
                // remove old beam
                beams -= beam

                var (currentPosition, currentDirection) = beam

                if (currentPosition !in grid) continue

                when (grid[currentPosition]) {
                    '.' -> {}
                    '/' -> currentDirection = when (currentDirection) {
                        Direction.DOWN -> Direction.LEFT
                        Direction.UP -> Direction.RIGHT
                        Direction.RIGHT -> Direction.UP
                        Direction.LEFT -> Direction.DOWN
                    }

                    '\\' -> currentDirection = when (currentDirection) {
                        Direction.DOWN -> Direction.RIGHT
                        Direction.UP -> Direction.LEFT
                        Direction.RIGHT -> Direction.DOWN
                        Direction.LEFT -> Direction.UP
                    }

                    '|' -> {
                        if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
                            // split

                            val beam1 = currentPosition to Direction.UP
                            if (beam1 !in history) beams.add(beam1)

                            val beam2 = currentPosition to Direction.DOWN
                            if (beam2 !in history) beams.add(beam2)

                            continue
                        }
                    }

                    '-' -> {
                        if (currentDirection == Direction.UP || currentDirection == Direction.DOWN) {
                            // split

                            val beam1 = currentPosition to Direction.RIGHT
                            if (beam1 !in history) beams.add(beam1)

                            val beam2 = currentPosition to Direction.LEFT
                            if (beam2 !in history) beams.add(beam2)

                            continue
                        }
                    }
                }
                // update position
                currentPosition = currentDirection.translation(currentPosition)

                // set new beam when it wasn't split
                val newBeam = currentPosition to currentDirection
                if (newBeam !in history) beams.add(newBeam)
            }
        }

        return history.distinctBy { it.first }.count { it.first in grid }
    }


    part1 {
        tryConfiguration(Pos(0, 0), Direction.RIGHT)
    }

    part2 {
        val configurations = buildList {
            grid.keys.forEach { pos ->
                if (pos.x == 0) add(pos to Direction.RIGHT)
                if (pos.x == bigGridWidth - 1) add(pos to Direction.LEFT)
                if (pos.y == 0) add(pos to Direction.DOWN)
                if (pos.y == bigGridHeight - 1) add(pos to Direction.UP)
            }
        }
        configurations.maxOf { (pos, dir) -> tryConfiguration(pos, dir) }
    }

    expectPart2 = 51
}