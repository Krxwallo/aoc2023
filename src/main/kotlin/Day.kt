@file:Suppress("MemberVisibilityCanBePrivate")

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.awt.Toolkit
import java.io.File
import java.time.Instant
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

const val YEAR = "2023"

fun day(number: Int, scope: Day.() -> Unit) {
    Day(number, scope).scrape().run()
}

// needs refactoring
class Day(private val number: Int, val scope: Day.() -> Unit) {
    private fun inputFileName(extra: String = "") = "Day${number.toString().padStart(2, '0')}${extra}.txt"

    var inputString: String = ""
        private set(value) {
            field = value
            inputLines = value.lines()
        }
    var inputLines: List<String> = emptyList()

    var currentPart = 1
    var isTestRun = false

    private var part1Block: (() -> Any?)? = null
    private var part2Block: (() -> Any?)? = null

    var expectPart1: Any? = null
    var expectPart2: Any? = null

    @OptIn(com.github.ajalt.mordant.terminal.ExperimentalTerminalApi::class)
    private val terminal = Terminal()

    fun part1(block: () -> Any?) {
        part1Block = block
    }

    fun part2(block: () -> Any?) {
        part2Block = block
    }

    val passingParts = mutableSetOf<Int>()

    private fun testPart(partN: Int, part: (() -> Any?)?, expected: Any?) {
        if (part != null && expected != null) {
            val actual = part.invoke().toString()
            if (actual != expected.toString()) {
                terminal.println("${TextColors.red("Failed")} test for part $partN! Expected '$expected' but got '$actual' instead")
                passingParts -= partN
            } else {
                terminal.println("The result of part $partN is ${TextColors.green("correct")}.")
                passingParts += partN
            }
        }
    }

    private fun runPart(partName: String, part: (() -> Any?)?) {
        if (part != null) {
            val result: Any?
            val time = measureNanoTime {
                result = part.invoke()
            }
            val copy = result != Unit && result != null
            if (copy) {
                // Copy to clipboard
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(java.awt.datatransfer.StringSelection(result.toString()), null)
            }
            val msTime = TextColors.brightMagenta("(${time.nanoseconds})")
            terminal.println("The result of $partName is ${TextStyles.bold(TextColors.brightCyan(result.toString()))} $msTime ${if (copy) TextColors.gray("(copied)") else ""}. ${
                if (partName.last().digitToInt() in passingParts) " (${TextColors.green("test passed")})"
                else " (${TextColors.red("test failed")})"
            }")
        }
    }

    val client = HttpClient(CIO)

    private val testInputFile = File("input/Day${number.toString().padStart(2, '0')}_test.txt")
    private val testInput2File = File("input/Day${number.toString().padStart(2, '0')}_test_02.txt")
    private val inputFile = File("input/Day${number.toString().padStart(2, '0')}.txt")
    private val testOutputFile = File("input/Day${number.toString().padStart(2, '0')}_test_result.txt")
    private val testOutput2File = File("input/Day${number.toString().padStart(2, '0')}_test_02_result.txt")

    fun scrape(): Day = apply {
        val now = Instant.now()
        if (testInputFile.exists() && testOutputFile.exists() && inputFile.exists()) {
            println("Input/output files already exist, skipping scraping")
            println(testInputFile.absolutePath)
            return@apply
        }
        runBlocking {
            runCatching {
                val mainPage = client.get("https://adventofcode.com/$YEAR/day/$number") {
                    cookie("session", File(".session").readText())
                }
                val codeBlocks = mainPage.bodyAsText().split("<code>").drop(1).map { it.substringAfter("::before").substringBefore("</code>") }
                val testInput = codeBlocks.first { it.lines().size > 1 }.removeSuffix("\n")
                val testOutput = codeBlocks.first { it.lines().size == 1 && it.contains("<em>") }.substringAfter("<em>").substringBefore("</em>")

                testInputFile.writeText(testInput)
                testOutputFile.writeText(testOutput)

                val input = client.get("https://adventofcode.com/$YEAR/day/$number/input") {
                    cookie("session", File(".session").readText())
                }.bodyAsText().removeSuffix("\n")
                inputFile.writeText(input)

                println("Scraped inputs and outputs in ${Instant.now().minusMillis(now.toEpochMilli()).toEpochMilli()}ms")
            }.onFailure {
                terminal.println(TextColors.red("Failed to scrape inputs and outputs!"))
            }
        }
    }

    fun run() {
        println("Running ${TextColors.brightMagenta("Day $number")}")

        println()
        terminal.println(TextColors.gray("Running against tests..."))
        println()

        isTestRun = true
        inputString = testInputFile.readText()
        currentPart = 1
        this.scope()
        testPart(1, part1Block, expectPart1 ?: testOutputFile.let {
            if (it.exists()) it.readText() else ""
        })
        testInput2File.takeIf { it.exists() }?.let { inputString = it.readText() }
        currentPart = 2
        this.scope()
        testPart(2, part2Block, expectPart2 ?: testOutput2File.let {
            if (it.exists()) it.readText() else ""
        })
        isTestRun = false

        println()
        terminal.println(TextColors.gray("Running against real input..."))
        println()

        inputString = inputFile.readText()
        currentPart = 1
        this.scope()
        runPart("part1", part1Block)
        currentPart = 2
        this.scope()
        runPart("part2", part2Block)
    }
}
