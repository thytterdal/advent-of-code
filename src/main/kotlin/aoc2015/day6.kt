package aoc2015

import common.Challenge
import utils.Point

fun main() = aoc2015day6.run()

private val aoc2015day6 = object : Challenge(year = 2015, day = 6) {
    override fun silverStar(lines: List<String>): Long {
        val instructions = lines.parseInstructions()
        val lights = Array(1000) {
            BooleanArray(1000)
        }

        instructions.forEach { lightingInstruction ->
            for (x in lightingInstruction.start.x..lightingInstruction.end.x) {
                for (y in lightingInstruction.start.y..lightingInstruction.end.y) {
                    lights[y][x] = when (lightingInstruction.instruction) {
                        Instruction.TURN_ON -> true
                        Instruction.TURN_OFF -> false
                        Instruction.TOGGLE -> !lights[y][x]
                    }
                }
            }
        }

        return lights.sumOf { line -> line.count { it } }.toLong()
    }

    override fun goldStar(lines: List<String>): Long {
        val instructions = lines.parseInstructions()
        val lights = Array(1000) {
            LongArray(1000)
        }

        instructions.forEach { lightingInstruction ->
            for (x in lightingInstruction.start.x..lightingInstruction.end.x) {
                for (y in lightingInstruction.start.y..lightingInstruction.end.y) {
                    lights[y][x] = when (lightingInstruction.instruction) {
                        Instruction.TURN_ON -> lights[y][x] + 1
                        Instruction.TURN_OFF -> (lights[y][x] - 1).coerceAtLeast(0)
                        Instruction.TOGGLE -> lights[y][x] + 2
                    }
                }
            }
        }

        return lights.sumOf(LongArray::sum)
    }


    private fun List<String>.parseInstructions(): List<LightingInstruction> {
        val digitsRegex = """\d+""".toRegex()
        val instructionRegex = """^\D+""".toRegex()
        return map { line ->
            val digits = digitsRegex.findAll(line).map { it.value.toInt() }.toList()
            val instruction = when (instructionRegex.find(line)!!.value) {
                "turn off " -> Instruction.TURN_OFF
                "turn on " -> Instruction.TURN_ON
                else -> Instruction.TOGGLE
            }
            LightingInstruction(
                start = Point(digits[0], digits[1]),
                end = Point(digits[2], digits[3]),
                instruction = instruction
            )
        }
    }
}

private data class LightingInstruction(
    val start: Point,
    val end: Point,
    val instruction: Instruction
)

private enum class Instruction {
    TURN_ON,
    TURN_OFF,
    TOGGLE
}