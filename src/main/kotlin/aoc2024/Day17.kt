package aoc2024

import common.Challenge
import utils.longs
import utils.splitOnEmptyLine
import java.lang.IllegalArgumentException
import kotlin.math.pow

private val aoc2024day17 = object : Challenge(year = 2024, day = 17) {
    override fun silverStar(lines: List<String>): Long {
        val (rawRegisters, rawInstructions) = lines.splitOnEmptyLine()
        val registers = rawRegisters.map { it.substringAfter(": ").toLong() }
        val instructions = rawInstructions.first().longs()

        val output = findOutput(registers, instructions)

        println(output.joinToString(","))

        return 0L
    }

    override fun goldStar(lines: List<String>): Long {
        val (rawRegisters, rawInstructions) = lines.splitOnEmptyLine()
        val registers = rawRegisters.map { it.substringAfter(": ").toLong() }
        val instructions = rawInstructions.first().longs()

        var registerA = instructions.drop(1).fold(1L) { acc, _ ->
            acc shl 3
        }

        while (true) {
            val correctedOutput = findOutput(registers, instructions, registerA)
            if (correctedOutput == instructions) {
                break
            }

            if (correctedOutput.size > instructions.size) {
                throw IllegalArgumentException()
            }

            var step = 1L
            repeat(
                instructions.size - instructions.zip(correctedOutput).reversed()
                    .takeWhile { it.first == it.second }.size - 1
            ) {
                step = step shl 3
            }

            registerA += step
        }

        return registerA
    }

    fun findOutput(registers: List<Long>, instructions: List<Long>, overrideA: Long? = null): List<Long> {
        var pointer = 0
        var (registerA, registerB, registerC) = registers
        overrideA?.let {
            registerA = it
        }
        val output = mutableListOf<Long>()

        while (pointer in instructions.indices) {
            val instruction = instructions[pointer]
            val operand = instructions[pointer + 1]
            val operandValue = when (operand) {
                in 0L..3L -> operand
                4L -> registerA
                5L -> registerB
                6L -> registerC
                else -> throw IllegalArgumentException()
            }
            when (instruction) {
                0L -> registerA = (registerA / 2.0.pow(operandValue.toInt())).toLong()
                1L -> registerB = registerB xor operand
                2L -> registerB = operandValue % 8
                3L -> if (registerA != 0L) {
                    pointer = operand.toInt()
                    continue
                }

                4L -> registerB = registerB xor registerC
                5L -> output.add(operandValue % 8)
                6L -> registerB = (registerA / 2.0.pow(operandValue.toInt())).toLong()
                7L -> registerC = (registerA / 2.0.pow(operandValue.toInt())).toLong()
                else -> throw IllegalArgumentException()
            }

            pointer += 2
        }

        return output
    }
}

fun main() = aoc2024day17.run()