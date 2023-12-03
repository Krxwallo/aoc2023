
private data class CubeSet(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    infix fun isPossibleWith(other: CubeSet): Boolean {
        return other.red >= red && other.green >= green && other.blue >= blue
    }

    fun power() = red * green * blue
}

private val cubesInBag = CubeSet(
    12, 13, 14
)

private fun CubeSet(
    data: String
): CubeSet {
    fun countFor(type: String): Int {
        val index = data.indexOf(type).takeIf { it != -1 } ?: return 0
        return data.substring(index - 3, index - 1).removePrefix(" ").toInt() ?: 0
    }

    return CubeSet(
        countFor("red"),
        countFor("green"),
        countFor("blue"),
    )
}

fun main() = day(2) {

    val input = inputString.split("\n")

    part1 {
        input.withIndex().sumOf {
            val isPossible = it.value.split(":").last().split(";").map {
                CubeSet(it)
            }.all {
                it isPossibleWith cubesInBag
            }
            if (isPossible) it.index + 1 else 0
        }
    }

    part2 {
        input.sumOf {
            val cubes = it.split(":").last().split(";").map {
                CubeSet(it)
            }
            val minCubes = CubeSet(
                cubes.maxOf { it.red },
                cubes.maxOf { it.green},
                cubes.maxOf { it.blue },
            )
            minCubes.power()
        }
    }

    expectPart1 = 8
    expectPart2 = 2286
}