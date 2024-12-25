package aoc2024

import common.Challenge
import utils.splitOnEmptyLine
import java.lang.IllegalArgumentException

private data class Gate(val firstInput: String, val secondInput: String, val gate: String, var output: String)

private val aoc2024day24 = object : Challenge(year = 2024, day = 24) {
    override fun silverStar(lines: List<String>): Long {
        val (values, instructions) = lines.parse()

        return values.applyGates(instructions).getLongValue('z')
    }

    override fun goldStar(lines: List<String>): Long {
        val (values, gates) = lines.parse()

        val x = values.getLongValue('x')
        val y = values.getLongValue('y')
        val sum = x + y

        val erronousEndgates = gates.filter {
            it.output.startsWith("z") &&
                    it.output != "z45" &&
                    it.gate != "XOR"
        }

        val erronousIntermediateGates = gates.filter {
            !it.output.startsWith("z") &&
                    it.firstInput.first() !in "xy" &&
                    it.secondInput.first() !in "xy" &&
                    it.gate == "XOR"
        }

        erronousIntermediateGates.forEach { gate ->
            val swapWith = erronousEndgates.first { it.output == gates.firstZaffectedBy(gate.output) }
            val temp = gate.output
            gate.output = swapWith.output
            swapWith.output = temp
        }

        val falseCarry = (sum xor values.applyGates(gates).getLongValue('z'))
            .countTrailingZeroBits()
            .toString()
            .padStart(2, '0')

        val gatesToSwap = (
                erronousEndgates + erronousIntermediateGates + gates.filter {
                    it.firstInput.endsWith(falseCarry) && it.secondInput.endsWith(falseCarry)
                })
            .map { it.output }
            .sorted().joinToString(",")

        println(gatesToSwap)
        return 0L
    }

    private fun List<Gate>.firstZaffectedBy(gateOutput: String): String {
        val x = filter { it.firstInput == gateOutput || it.secondInput == gateOutput }
        x.firstOrNull { it.output.startsWith('z') }
            ?.let { return "z" + (it.output.drop(1).toInt() - 1).toString().padStart(2, '0') }
        return x.firstNotNullOf { firstZaffectedBy(it.output) }
    }


    fun Map<String, Int>.applyGates(
        gates: List<Gate>,
    ): Map<String, Int> {
        var temp = gates
        val mutableValues = toMutableMap()
        var idx = 0
        while (temp.isNotEmpty() && idx < 100_000) {
            temp = temp.mapNotNull { gate ->
                if (gate.firstInput in mutableValues && gate.secondInput in mutableValues) {
                    val value = applyGate(
                        mutableValues.getValue(gate.firstInput),
                        mutableValues.getValue(gate.secondInput),
                        gate.gate
                    )
                    mutableValues[gate.output] = value
                    null
                } else {
                    gate
                }
            }
            idx++
        }

        return mutableValues
    }

    fun applyGate(from: Int, to: Int, gate: String) = when (gate) {
        "AND" -> from and to
        "OR" -> from or to
        "XOR" -> from xor to
        else -> throw IllegalArgumentException()
    }

    fun List<String>.parse(): Pair<Map<String, Int>, List<Gate>> {
        val (rawValues, rawGates) = splitOnEmptyLine()
        val values = rawValues.associate {
            val (input, value) = it.split(": ")
            input to value.toInt()
        }
        val gates = rawGates.map { instruction ->
            val (raw, target) = instruction.split(" -> ")
            val (from, gate, to) = raw.split(" ")
            Gate(
                firstInput = from,
                secondInput = to,
                gate = gate,
                output = target
            )
        }

        return values to gates
    }

    fun Map<String, Int>.getLongValue(letter: Char) =
        filterKeys { it.startsWith(letter) }.toSortedMap().values.reversed().joinToString("").toLong(2)
}

fun main() = aoc2024day24.run()