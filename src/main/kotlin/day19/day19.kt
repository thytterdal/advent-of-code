package day19


import utils.readInput
import utils.splitOnEmpty

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
    val sum = 0

    println("Second ⭐: $sum")
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
                val condition = if (step.comparator == WorkFlowComparator.LT) {
                    value < step.value
                } else {
                    value > step.value
                }

                if (condition) {
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
                    val (property, value) = condition.split("<", ">")
                    WorkFlowStepWithCondition(
                        property = PartProperty.valueOf(property.uppercase()),
                        value = value.toLong(),
                        comparator = if (condition.contains("<")) WorkFlowComparator.LT else WorkFlowComparator.GT,
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

private sealed interface WorkFlowStep
data class WorkFlowStepWithCondition(
    val property: PartProperty,
    val value: Long,
    val comparator: WorkFlowComparator,
    val destination: String
) : WorkFlowStep

data class Destination(
    val destination: String
) : WorkFlowStep


enum class WorkFlowComparator {
    LT, GT
}

enum class PartProperty {
    X, M, A, S
}