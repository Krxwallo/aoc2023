import kotlin.math.max
import kotlin.math.min

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

fun String.parseLetterGroups(line: String): List<String> {
    val regex = Regex("""([A-Z]+)\s*=\s*\(([A-Z]+),\s*([A-Z]+)\)""")
    val matchResult = regex.find(line)
    return matchResult?.destructured?.toList() ?: emptyList()
}

val Day.intLines get() = inputLines.map {
    it.toInt()
}

fun Double.isNaturalInt() = toInt().toDouble() == this

/**
 * Get the least common multiple of two numbers.
 */
fun lcm(n1: Long, n2: Long): Long {
    val bigger = max(n1, n2)
    val smaller = min(n1, n2)
    var current = bigger
    while (!(current.toDouble() / smaller).isNaturalInt()) {
        current += bigger
    }
    return current
}

//fun println(vararg args: Any?) = kotlin.io.println(args.joinToString(" "))

/**
 * Get the least common multiple of multiple numbers.
 */
fun lcm(vararg numbers: Long): Long = numbers.fold(1, ::lcm)

fun lcm(numbers: Collection<Long>): Long = numbers.fold(1, ::lcm)
