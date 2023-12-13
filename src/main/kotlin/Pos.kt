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

    operator fun plus(other: Pos) = Pos(x + other.x, y + other.y)

    operator fun minus(other: Pos) = Pos(x - other.x, y - other.y)

    operator fun times(other: Int) = Pos(x * other, y * other)

    operator fun div(other: Int) = Pos(x / other, y / other)

    operator fun unaryMinus() = Pos(-x, -y)

    override fun toString(): String = "Pos($x/$y)"
}