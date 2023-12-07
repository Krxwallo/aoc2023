
fun panic(message: Any?): Nothing = throw RuntimeException(message.toString())

fun List<String>.readGrid() = buildMap {
    this@readGrid.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { charIndex, char ->
            put(Pos(charIndex, lineIndex), char)
        }
    }
}

fun <T> List<List<T>>.transpose(): List<List<T>> =
    if (isEmpty() || this.any { it.isEmpty() }) {
        emptyList()
    } else {
        (0 until first().size).map { col -> map { it[col] } }
    }


fun String.parseInts() = Regex("\\b\\d+\\b").findAll(this).map { it.value.toInt() }.toList()