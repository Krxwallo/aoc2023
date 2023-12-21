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

val Day.bigGridWidth get() = inputLines.maxOf { it.length }
val Day.bigGridHeight get() = inputLines.size
val Day.gridPositions get() = (0 until bigGridWidth).flatMap { x -> (0 until bigGridHeight).map { y -> Pos(x, y) } }

val Map<Pos, Char>.width get() = maxOf { it.key.x } + 1
val Map<Pos, Char>.height get() = maxOf { it.key.y } + 1

fun Map<Pos, Char>.getColumn(x: Int): List<Char> {
    return filter { it.key.x == x }.map { it.value }
}

fun Map<Pos, Char>.getRow(y: Int): List<Char> {
    return filter { it.key.y == y }.map { it.value }
}

fun <T> List<List<T>>.transpose(): List<List<T>> =
    if (isEmpty() || this.any { it.isEmpty() }) {
        emptyList()
    } else {
        (0 until first().size).map { col -> map { it[col] } }
    }

@JvmName("stringListTranspose")
fun List<String>.transpose() = map { it.toList() }.transpose()

fun String.counts() = groupingBy { it }.eachCount()


fun String.parseInts() = Regex("\\b\\d+\\b").findAll(this).map { it.value.toInt() }.toList()

fun String.parseSignedInts() = Regex("-?\\b\\d+\\b").findAll(this).map { it.value.toInt() }.toList()

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

/**
 * Return a list with -1 the original size containing the differences of every two neighbours.
 */
fun List<Int>.diffs(): List<Int> = let { ints ->
    buildList {
        ints.forEachIndexed { index, i ->
            val i2 = ints.getOrNull(index + 1)
            if (i2 != null) {
                add(i2 - i)
            }
        }
    }
}

fun <T: Any> Collection<T>.pairs(): List<Pair<T, T>> {
    val pairs = mutableListOf<Pair<T, T>>()
    forEachIndexed { index, item ->
        this@pairs.filterIndexed { i, _ -> i != index }.forEach { other ->
            if ((item to other) !in pairs && (other to item) !in pairs) pairs.add(item to other)
        }
    }
    return pairs
}

fun <T> Collection<T>.splitAt(item: T): List<List<T>> {
    val result = mutableListOf<List<T>>()
    val currentList = mutableListOf<T>()

    for (element in this) {
        if (element == item) {
            result.add(currentList.toList())
            currentList.clear()
        } else {
            currentList.add(element)
        }
    }

    result.add(currentList.toList())

    return result
}