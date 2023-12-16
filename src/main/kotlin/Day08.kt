private data class Step(val start: String, val left: String, val right: String)

private fun Step(data: String): Step {
    return Step(data.take(3), data.drop(7).take(3), data.dropLast(1).takeLast(3))
}

fun main() = day(8) {
    val instructions = inputLines.first().toCharArray()

    val nodes = inputLines.drop(2)
        .map {
            Step(it)
        }

    fun findNextNode(input: String): Int {
        return nodes.indexOfFirst { it.start == input }
    }

    fun getStepCount(startIndex: Int, part2: Boolean = false): Int {
        var currentNode = startIndex
        var stepCount = 0
        while (nodes[currentNode].start != "ZZZ" && (!part2 || !nodes[currentNode].start.endsWith('Z'))) {
            val instruction = instructions[stepCount % instructions.size]
            val currentStep = nodes[currentNode]
            currentNode = if (instruction == 'L') {
                findNextNode(currentStep.left)
            } else if (instruction == 'R') findNextNode(currentStep.right)
            else panic(instruction)

            stepCount++
        }
        return stepCount
    }

    part1 {
        val startNode = nodes.indexOfFirst { it.start == "AAA" }
        getStepCount(startNode)
    }

    part2 {
        buildList {
            nodes.forEachIndexed { index, node ->
                if (node.start.endsWith('A')) add(index)
            }
        }.map {
            getStepCount(it, true)
        }.map { it.toLong() }.let(::lcm)
    }


    expectPart1 = 6
    expectPart2 = 6
}