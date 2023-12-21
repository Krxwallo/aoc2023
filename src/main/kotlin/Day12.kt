import java.lang.Math.pow
import kotlin.math.pow

fun main() = day(12) {

    fun String.arrangements(): List<Int> {
        var currentCount = 0
        return buildList {
            this@arrangements.forEach { c ->
                if (c == '?') panic("? in arrangements() function")
                else if (c == '#') currentCount++
                else if (c == '.') {
                    if (currentCount != 0) this.add(currentCount)
                    currentCount = 0
                }
                else panic("Unknown char $c")
            }
            if (currentCount != 0) this.add(currentCount)
        }
    }

    part1 {
        inputLines.sumOf { line ->
            val (springs, arrangementsStr) = line.split(' ')
            val arrangements = arrangementsStr.parseInts()
            val replacements = springs.getReplacements('?', '#', '.')
            replacements.count {
                it.arrangements() == arrangements
            }
        }
    }

    part2 {
        inputLines.sumOf { line ->
            println("Parsing $line...")
            val (foldedSprings, arrangementsStr) = line.split(' ')
            val springs = buildString {
                repeat(5) {
                    append("$foldedSprings?")
                }
            }.dropLast(1)
            println("Springs: $springs")
            val arrangements = buildList {
                val ints = arrangementsStr.parseInts()
                repeat(5) {
                    addAll(ints)
                }
            }
            println("Arrangements: $arrangements")
            val replacements = springs.getReplacements('?', '#', '.')
            replacements.count {
                it.arrangements() == arrangements
            }
        }
    }

    expectPart1 = 21
    expectPart2 = 525152
}