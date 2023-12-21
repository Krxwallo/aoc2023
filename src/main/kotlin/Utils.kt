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

val Day.gridWidth get() = inputLines.maxOf { it.length }
val Day.gridHeight get() = inputLines.size
val Day.gridPositions get() = (0 until gridWidth).flatMap { x -> (0 until gridHeight).map { y -> Pos(x, y) } }

fun <T> List<List<T>>.transpose(): List<List<T>> =
    if (isEmpty() || this.any { it.isEmpty() }) {
        emptyList()
    } else {
        (0 until first().size).map { col -> map { it[col] } }
    }

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

/**
 * Get all possible combinations when replacing [toReplace] with either [replacement1] or [replacement2] in the string.
 */
fun String.getReplacements(toReplace: Char, replacement1: Char, replacement2: Char): List<String> {
    val firstIndex = indexOf(toReplace).takeIf { it != -1 } ?: return listOf(this)

    val prefix = substring(0, firstIndex)
    val suffix = substring(firstIndex + 1)
    val combinations1 = "$prefix$replacement1$suffix".getReplacements(toReplace, replacement1, replacement2)
    val combinations2 = "$prefix$replacement2${suffix}".getReplacements(toReplace, replacement1, replacement2)

    return combinations1 + combinations2
}