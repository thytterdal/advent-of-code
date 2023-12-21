package day20

import utils.readInput

fun main() {
    val lines = readInput("day20/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val modules = lines.parseModules()

    val broadcaster = modules.first { it is Module.Broadcaster } as Module.Broadcaster

    repeat(1000) {
        broadcaster.simulate()
    }
    val low = modules.sumOf { it.lowCount }
    val high = modules.sumOf { it.highCount }
    println("low: $low")
    println("high: $high")

    val sum = low * high

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val sum = 0

    println("Second ⭐: $sum")
}

private fun Module.Broadcaster.simulate() {
    val queue = mutableListOf(Triple("button", listOf<Module>(this), Pulse.LOW))
    while (queue.isNotEmpty()) {
        val (source, destinations, pulse) = queue.removeFirst()
        for (destination in destinations) {
            destination(sender = source, pulse = pulse)?.let { output ->
                queue.add(Triple(destination.id, destination.successors, output))
            }
        }
    }
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
            val successor = modules.firstOrNull { it.id == successorId } ?: Module.Output
            module.registerSuccessor(successor)
            if (successor is Module.Conjunction) {
                successor.registerPredecessor(module.id)
            }

        }
    }

    return modules
}

private sealed class Module {
    var lowCount: Long = 0L
    var highCount: Long = 0L
    private val _successors: MutableList<Module> = mutableListOf()
    val successors: List<Module> = _successors
    abstract val id: String

    abstract operator fun invoke(sender: String, pulse: Pulse): Pulse?

    protected fun incrementCount(pulse: Pulse) = when (pulse) {
        Pulse.HIGH -> highCount++
        Pulse.LOW -> lowCount++
    }

    fun registerSuccessor(module: Module) {
        _successors.add(module)
    }

    data class FlipFlop(
        override val id: String
    ) : Module() {
        var currentState: Boolean = false
            private set

        override operator fun invoke(sender: String, pulse: Pulse): Pulse? {
            //println("$sender -$pulse-> FlipFlop $id")

            incrementCount(pulse)
            return if (pulse == Pulse.LOW) {
                currentState = !currentState
                if (currentState) Pulse.HIGH else Pulse.LOW
            } else {
                null
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

        override fun invoke(sender: String, pulse: Pulse): Pulse {
            //println("$sender -$pulse-> Conjunction $id")

            incrementCount(pulse)
            predecessors[sender] = pulse

            return if (predecessors.all { it.value == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
        }
    }

    data class Broadcaster(
        override val id: String
    ) : Module() {
        override fun invoke(sender: String, pulse: Pulse): Pulse {
            //println("$sender -$pulse-> Broadcaster $id")

            incrementCount(pulse)

            return pulse
        }
    }

    data object Output : Module() {
        override val id: String = "output"

        override fun invoke(sender: String, pulse: Pulse): Pulse? {
            //println("$sender -$pulse-> Output $id")

            incrementCount(pulse)

            return null
        }
    }
}

private enum class Pulse {
    HIGH, LOW
}