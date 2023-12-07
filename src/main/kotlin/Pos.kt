data class Pos(val x: Int, val y: Int) {

    val neighbours
        get() = listOf(
            Pos(x + 1, y),
            Pos(x - 1, y),
            Pos(x, y + 1),
            Pos(x, y - 1),
        )

    val adjacent
        get() = (-1..1).flatMap { dx ->
            (-1..1).map { dy ->
                Pos(x + dx, y + dy)
            }
        }
}