val digitStrings = listOf(
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
)

val reversedDigitStrings = buildList {
    digitStrings.forEach {
        add(it.reversed())
    }
}

fun main() = day(1) {

    val input = inputString.split("\n")

    part1 {
        input.sumOf {
            (it.first { f -> f.isDigit() }.toString() + it.last { f -> f.isDigit() }).toInt()
        }
    }

    part2 {
        input.sumOf {
            // yes I know. At least it works.
            fun compute(input: String, reversed: Boolean): Int {
                val myDigitStrings = if (reversed) reversedDigitStrings else digitStrings

                val firstStringDigitPair = input.findAnyOf(myDigitStrings)
                val firstStringDigitIndex = firstStringDigitPair?.first
                val firstStringDigitString = firstStringDigitPair?.second
                val firstStringDigit = myDigitStrings.indexOf(firstStringDigitString).takeIf { it != -1 }?.plus(1)

                val firstRealDigit = input.firstOrNull { f -> f.isDigit() }
                val firstRealDigitIndex = input.indexOf(firstRealDigit ?: '_').takeIf { i -> i != -1 }
                return if (firstStringDigit == null) firstRealDigit!!.digitToInt()
                    else if (firstRealDigit == null) firstStringDigit
                    else if (firstStringDigitIndex!! < firstRealDigitIndex!!) firstStringDigit
                    else firstRealDigit.digitToInt()
            }

            val firstDigit = compute(it, false)
            val lastDigit = compute(it.reversed(), true)

            (firstDigit.toString() + lastDigit).toInt()
        }
    }

    expectPart1 = 142
    expectPart2 = 281
}