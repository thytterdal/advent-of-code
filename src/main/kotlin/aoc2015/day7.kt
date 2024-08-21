package aoc2015

import common.Challenge
import utils.isNumeric

private val aoc2015day7 = object : Challenge(year = 2015, day = 7) {
    override fun silverStar(lines: List<String>): Long {
        val (knownValues, operations) = lines.parseLogicGates()
        return findValueOfA(operations, knownValues)
    }

    override fun goldStar(lines: List<String>): Long {
        val a = silverStar(lines)

        val (knownValues, operations) = lines.parseLogicGates()
        knownValues["b"] = a.toUShort()

        return findValueOfA(operations, knownValues)
    }

    private fun findValueOfA(
        operations: MutableList<LogicOperation>,
        knownValues: HashMap<String, UShort>
    ): Long {
        val toRemove = mutableListOf<LogicOperation>()

        while (operations.isNotEmpty()) {
            toRemove.clear()
            operations.forEach { operation ->
                operation.performOperation(knownValues)?.let {
                    knownValues[operation.target] = it
                    toRemove.add(operation)
                }
            }
            operations.removeAll(toRemove)
        }
        return knownValues["a"].toString().toLong()
    }
}

private fun List<String>.parseLogicGates(): Pair<HashMap<String, UShort>, MutableList<LogicOperation>> {
    val knownValues = hashMapOf<String, UShort>()
    val operations = mutableListOf<LogicOperation>()
    forEach { line ->
        val (logic, target) = line.split(" -> ")
        if (logic.isNumeric()) {
            val value = logic.toUShort()
            knownValues[target] = value
            return@forEach
        }

        val split = logic.split(" ")
        when (split.size) {
            1 -> operations.add(LogicOperation.Direct(split[0], target))
            2 -> operations.add(LogicOperation.Not(split[1], target))
            3 -> operations.add(
                when (split[1]) {
                    "AND" -> LogicOperation.And(split[0], split[2], target)
                    "OR" -> LogicOperation.Or(split[0], split[2], target)
                    "RSHIFT" -> LogicOperation.Rshift(split[0], split[2].toInt(), target)
                    "LSHIFT" -> LogicOperation.Lshift(split[0], split[2].toInt(), target)
                    else -> throw IllegalArgumentException("Unknown operation type $split")
                }

            )
        }
    }

    return Pair(knownValues, operations)
}

private sealed interface LogicOperation {
    val target: String
    fun performOperation(knownValues: HashMap<String, UShort>): UShort?

    data class And(val wire1: String, val wire2: String, override val target: String) :
        LogicOperation {
        override fun performOperation(knownValues: HashMap<String, UShort>): UShort? {
            val value1 = knownValues[wire1] ?: wire1.toUShortOrNull()
            val value2 = knownValues[wire2] ?: wire2.toUShortOrNull()
            return if (value1 != null && value2 != null) {
                value1 and value2
            } else {
                null
            }
        }
    }

    data class Or(val wire1: String, val wire2: String, override val target: String) :
        LogicOperation {
        override fun performOperation(knownValues: HashMap<String, UShort>): UShort? {
            val value1 = knownValues[wire1] ?: wire1.toUShortOrNull()
            val value2 = knownValues[wire2] ?: wire2.toUShortOrNull()
            return if (value1 != null && value2 != null) {
                value1 or value2
            } else {
                null
            }
        }
    }

    data class Not(val wire1: String, override val target: String) :
        LogicOperation {
        override fun performOperation(knownValues: HashMap<String, UShort>): UShort? {
            return knownValues[wire1]?.inv()
        }
    }

    data class Lshift(val wire1: String, val shiftBy: Int, override val target: String) :
        LogicOperation {
        override fun performOperation(knownValues: HashMap<String, UShort>): UShort? {
            val value1 = knownValues[wire1] ?: wire1.toUShortOrNull()

            return if (value1 != null) {
                (value1.toInt() shl shiftBy).toUShort()
            } else {
                null
            }
        }
    }

    data class Rshift(val wire1: String, val shiftBy: Int, override val target: String) :
        LogicOperation {
        override fun performOperation(knownValues: HashMap<String, UShort>): UShort? {
            val value1 = knownValues[wire1] ?: wire1.toUShortOrNull()

            return if (value1 != null) {
                (value1.toInt() shr shiftBy).toUShort()
            } else {
                null
            }
        }
    }

    class Direct(val wire1: String, override val target: String) : LogicOperation {
        override fun performOperation(knownValues: HashMap<String, UShort>): UShort? {
            return knownValues[wire1] ?: wire1.toUShortOrNull()
        }
    }
}


fun main() = aoc2015day7.run()