package day20

import utils.readInput

fun main() {
    val lines = readInput("day20/test_input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val modules = lines.parseModules()

    val broadcaster = modules.first { it is Module.Broadcaster }
    val output = modules.first { it is Module.Output } as Module.Output

    //repeat(1000) {
    broadcaster("button", Pulse.LOW)
    //}
    val result = output.reportAndClear()
    println("low: ${result.low}")
    println("high: ${result.high}")

    val sum = 0

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val sum = 0

    println("Second ⭐: $sum")
}

private fun List<String>.parseModules(): List<Module> {
    val successorMap = mutableMapOf<String, List<String>>()
    val modules = listOf(Module.Output) + map { line ->
        val (id, successors) = line.split("->").let {
            it.first().trim() to it.last().split(",").map(String::trim)
        }
        successorMap[id.dropWhile { it in "%&" }] = successors
        when {
            id == "broadcaster" -> Module.Broadcaster(id = id)
            id.startsWith("%") -> Module.FlipFlop(id = id.drop(1))
            id.startsWith("&") -> Module.Conjunction(id = id.drop(1))
            else -> throw Exception("Unexpected input $id")
        }
    }
    modules.forEach { module ->
        val successors = successorMap[module.id] ?: emptyList()
        successors.forEach { successorId ->
            val successor =
                modules.firstOrNull { it.id == successorId } ?: throw Exception("no match found for $successorId")
            module.registerSuccessor(successor)
            if (successor is Module.Conjunction) {
                successor.registerPredecessor(module.id)
            }
        }
    }

    return modules
}

private sealed class Module {

    protected val successors: MutableList<Module> = mutableListOf()
    abstract val id: String

    abstract operator fun invoke(sender: String, pulse: Pulse)
    abstract operator fun invoke()

    fun registerSuccessor(module: Module) {
        successors.add(module)
    }

    data class FlipFlop(
        override val id: String
    ) : Module() {
        var currentState: Boolean = false
            private set

        override operator fun invoke(sender: String, pulse: Pulse) {
            println("$sender -$pulse-> FlipFlop $id")
            when (pulse) {
                Pulse.HIGH -> Unit
                Pulse.LOW -> currentState = !currentState
            }
        }

        override fun invoke() {
            if (currentState) {
                successors.forEach { it(id, Pulse.HIGH) }
                successors.forEach { it() }
            }
        }
    }

    data class Conjunction(
        override val id: String
    ) : Module() {
        private val predecessors = mutableMapOf<String, Pulse>()

        fun registerPredecessor(predecessorId: String) {
            predecessors[predecessorId] = Pulse.LOW
        }

        override fun invoke(sender: String, pulse: Pulse) {
            predecessors[sender] = pulse
            println("$sender -$pulse-> Conjunction $id")
        }

        override fun invoke() {
            val pulseToSend = if (predecessors.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
            successors.forEach { it(id, pulseToSend) }
            successors.forEach { it() }
        }
    }

    data class Broadcaster(
        override val id: String
    ) : Module() {
        override fun invoke(sender: String, pulse: Pulse) {
            println("$sender -$pulse-> Broadcaster $id")
            successors.forEach { it(id, pulse) }
            successors.forEach { it() }
        }
        override fun invoke() = Unit
    }

    data object Output : Module() {
        override val id: String = "output"
        private var lowCount: Long = 0L
        private var highCount: Long = 0L

        fun reportAndClear(): PulseCount {
            val pulseCount = PulseCount(low = lowCount, high = highCount)
            lowCount = 0L
            highCount = 0L
            return pulseCount
        }

        override fun invoke(sender: String, pulse: Pulse) {
            println("$sender -$pulse-> Output $id")
            when (pulse) {
                Pulse.HIGH -> highCount++
                Pulse.LOW -> lowCount++
            }
        }
        override fun invoke() = Unit
    }
}

private enum class Pulse {
    HIGH, LOW
}

private data class PulseCount(
    val low: Long,
    val high: Long
)