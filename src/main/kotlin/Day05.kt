data class Mapping(
    val destRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength: Long,
) {
    fun map(value: Long): Long? =
        (destRangeStart + (value - sourceRangeStart)).takeIf { value in sourceRangeStart..<sourceRangeStart + rangeLength }

    override fun toString() = "[$destRangeStart $sourceRangeStart $rangeLength]"
}

fun Mapping(data: String): Mapping {
    val (dest, source, length) = data.split(' ', limit = 3).map { it.toLong() }
    return Mapping(
        dest,
        source,
        length,
    )
}

fun main() = day(5) {
    var sections = inputString.split("\r\n\r\n")
    val sourceNumbers = sections.first().substringAfter(":")
        .split(' ')
        .mapNotNull { it.toLongOrNull() }

    sections = sections.drop(1)
    val mappings = sections.map { section ->
        section.lines().drop(1).map { mappingData ->
            Mapping(mappingData)
        }
    }

    fun computeLocation(number: Long): Long {
        var mappedNumber = number
        mappings.forEach { sectionMappings ->
            for (mapping in sectionMappings) {
                mapping.map(mappedNumber)?.let {
                    mappedNumber = it
                    break
                }
            }
        }
        return mappedNumber
    }

    part1 {
        sourceNumbers.minOf(::computeLocation)
    }

    part2 {
        // needs rewrite
        sourceNumbers.chunked(2)
            .map { it.first()..<it.first() + it.last() }
            .minOf {
                it.minOf(::computeLocation)
            }
    }

    expectPart1 = 35L
    expectPart2 = 46L
}