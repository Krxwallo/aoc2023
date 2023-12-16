fun main() = day(15) {
    fun String.hash() = (listOf(0) + map { it.code }).reduce { acc, c ->
        (acc + c) * 17 % 256
    }

    data class Step(
        val lensLabel: String,
        val focalLength: Int? = null
    ) {
        val box: Int get() = lensLabel.hash()
        val operation: Char get() = if (focalLength != null) '=' else '-'
    }

    data class Lens(
        val label: String,
        val focalLength: Int
    )

    part1 {
        inputString
            .split(",")
            .sumOf { it.hash() }
    }

    val boxes = Array(256) {
        mutableListOf<Lens>()
    }

    part2 {
        inputString
            .split(",")
            .map {
                val focalLength = it.takeLastWhile { c -> c.isDigit() }.toIntOrNull()
                if (focalLength == null) Step(it.substringBefore("-"))
                else Step(it.substringBefore("="), focalLength)
            }.forEach {
                when (it.operation) {
                    '=' -> {
                        val box = boxes[it.box]
                        val lens = Lens(it.lensLabel, it.focalLength!!)
                        box.indexOfFirst { l -> l.label == it.lensLabel }.takeIf { i -> i != -1 }?.let { replaceIndex ->
                            // replace
                            box.removeAt(replaceIndex)
                            box.add(replaceIndex, lens)
                        } ?: run {
                            // add
                            box += lens
                        }
                    }
                    '-' -> {
                        boxes[it.box].removeIf { l -> l.label == it.lensLabel }
                    }
                }
            }

        boxes.withIndex().sumOf { (boxN, lenses) ->
            lenses.withIndex().sumOf { (index, lens) ->
                (boxN + 1) * (index + 1) * lens.focalLength
            }
        }
    }

    expectPart1 = 1320
    expectPart2 = 145
}