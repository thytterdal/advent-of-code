package day19

import utils.readInput
import utils.splitOnEmpty
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = readInput("day19/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {

    val (parts, workflows) = lines.parseInput()

    val conditions = parts.associateWith { part ->
        workflows.checkCondition("in", part)
    }

    val sum = conditions.filter { it.value == "A" }.keys.sumOf { it.sum() }

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val (_, workflows) = lines.parseInput()

    val sum = workflows.acceptedWorkFlows().map { steps ->
        steps.groupBy(
            keySelector = { it.property }
        ) {
            it.validRange
        }.mapValues { (_, value) -> value.reduce { acc, validRange -> acc.intersect(validRange) } }
            .withDefault { 1L..4000L }
    }.sumOf {
        val x = it.getValue(PartProperty.X).length()
        val m = it.getValue(PartProperty.M).length()
        val a = it.getValue(PartProperty.A).length()
        val s = it.getValue(PartProperty.S).length()
        x * m * a * s
    }

    println("Second ⭐: $sum")
}


private fun Map<String, List<WorkFlowStep>>.acceptedWorkFlows(
    stepName: String = "in",
    stepsSoFar: List<WorkFlowStep> = emptyList()
): List<List<WorkFlowStepWithCondition>> = buildList {
    val steps = this@acceptedWorkFlows[stepName] ?: throw Exception("Unknown step $stepName")
    val previous = mutableListOf<WorkFlowStepWithCondition>()
    for (step in steps) {
        when (step.destination) {
            "A" -> add((stepsSoFar + previous + step).filterIsInstance<WorkFlowStepWithCondition>())
            "R" -> Unit
            else -> addAll(
                this@acceptedWorkFlows.acceptedWorkFlows(
                    stepName = step.destination,
                    stepsSoFar = (stepsSoFar + previous + step)
                )
            )
        }

        if (step is WorkFlowStepWithCondition) {
            previous += step.invert()
        }
    }
}

private fun Map<String, List<WorkFlowStep>>.checkCondition(stepName: String, part: Part): String {
    val steps = this[stepName] ?: throw Exception("Unknown step $stepName")

    for (step in steps) {
        when (step) {
            is Destination -> return when (step.destination) {
                "A" -> "A"
                "R" -> "R"
                else -> this.checkCondition(step.destination, part)
            }

            is WorkFlowStepWithCondition -> {
                val value = when (step.property) {
                    PartProperty.X -> part.x
                    PartProperty.M -> part.m
                    PartProperty.A -> part.a
                    PartProperty.S -> part.s
                }

                if (value in step.validRange) {
                    return when (step.destination) {
                        "A" -> "A"
                        "R" -> "R"
                        else -> this.checkCondition(step.destination, part)
                    }
                }
            }
        }
    }
    throw Exception("No path found")
}


private fun List<String>.parseInput(): Pair<List<Part>, Map<String, List<WorkFlowStep>>> {
    val (workFlowInstructions, partDescriptions) = this.splitOnEmpty()

    val parts = partDescriptions.map { part ->
        part
            .substringAfter("{")
            .substringBefore("}")
            .split(",")
            .let {
                Part(
                    x = it[0].substringAfter("x=").toLong(),
                    m = it[1].substringAfter("m=").toLong(),
                    a = it[2].substringAfter("a=").toLong(),
                    s = it[3].substringAfter("s=").toLong()
                )
            }
    }

    val workFlows = workFlowInstructions.associate { workFlowInstruction ->
        val key = workFlowInstruction.substringBefore("{")
        val steps = workFlowInstruction
            .substringAfter("{")
            .substringBefore("}")
            .split(",")
            .map { step ->
                if (step.contains(":")) {
                    val (condition, destination) = step.split(":")
                    val (property, value) = condition.split("<", ">").let { it.first() to it.last().toLong() }
                    WorkFlowStepWithCondition(
                        property = PartProperty.valueOf(property.uppercase()),
                        value = value,
                        validRange = if (condition.contains("<")) {
                            1L..<value
                        } else {
                            (value + 1)..4000L
                        },
                        destination = destination
                    )
                } else {
                    Destination(
                        destination = step
                    )
                }
            }

        key to steps
    }

    return parts to workFlows
}

private data class Part(
    val x: Long,
    val m: Long,
    val a: Long,
    val s: Long
) {
    fun sum(): Long {
        return x + m + a + s
    }
}

private fun LongRange.invert() = if (first == 1L) (last + 1)..4000L else 1L..<first
private fun LongRange.intersect(other: LongRange) = max(first, other.first)..min(last, other.last)
private fun LongRange.length() = last - first + 1

private sealed interface WorkFlowStep {
    val destination: String
}

data class WorkFlowStepWithCondition(
    val property: PartProperty,
    val value: Long,
    val validRange: LongRange,
    override val destination: String
) : WorkFlowStep {
    fun invert() = this.copy(
        validRange = validRange.invert()
    )
}

data class Destination(
    override val destination: String
) : WorkFlowStep


enum class PartProperty {
    X, M, A, S
}