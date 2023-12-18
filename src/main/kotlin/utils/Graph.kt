package utils

import java.util.PriorityQueue

object Graph {
    class Vertex<T>(
        val position: T,
        val weight: Int,
        val parent: Vertex<T>? = null,
        val heuristic: Int? = null
    ) : Comparable<Vertex<T>> {

        private val f: Int
            get() = heuristic?.let { weight + it } ?: weight

        override fun compareTo(other: Vertex<T>): Int =
            if (heuristic != null && other.heuristic != null) {
                when {
                    f - other.f > 0 -> 1
                    f - other.f < 0 -> -1
                    else -> (heuristic - other.heuristic).let { if (it < 0) -1 else 1 }
                }
            } else {
                weight.compareTo(other.weight)
            }


        fun path(): List<Vertex<T>> {
            val returnPath = mutableListOf(this)
            var parent = parent
            while (parent != null) {
                returnPath.add(parent)
                parent = parent.parent
            }
            returnPath.reverse()

            return returnPath
        }

    }

    data class Edge<T>(
        val vertexPosition: T,
        val weight: Int
    ) {
        fun toVertex(parent: Vertex<T>): Vertex<T> =
            Vertex(position = vertexPosition, parent.weight + weight, parent)

        fun toVertex(parent: Vertex<T>, heuristic: (T) -> Int): Vertex<T> =
            Vertex(
                position = vertexPosition,
                weight = parent.weight + weight,
                parent = parent,
                heuristic = heuristic(vertexPosition)
            )

    }

    fun <T> dijkstra(
        startPosition: T,
        neighbors: (T) -> List<Edge<T>>,
        endCondition: (T) -> Boolean
    ): List<Vertex<T>> {
        val startVertex = Vertex(startPosition, 0)
        val vertices = mutableMapOf(startPosition to startVertex)

        val queue = PriorityQueue<Vertex<T>>()
        queue.add(startVertex)

        val knownVertexes = mutableSetOf<T>()

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.position !in knownVertexes) {
                knownVertexes.add(current.position)
                if (endCondition(current.position)) {
                    return current.path()
                }
                val currentNeighbors = neighbors(current.position)
                for (neighbor in currentNeighbors) {
                    val alternative = neighbor.toVertex(current)
                    val vertex = vertices.getOrPut(neighbor.vertexPosition) {
                        Vertex(
                            position = neighbor.vertexPosition,
                            weight = Int.MAX_VALUE
                        )
                    }
                    if (alternative.weight < vertex.weight) {
                        vertices[neighbor.vertexPosition] = alternative
                        queue.add(alternative)
                    }
                }
            }
        }

        return emptyList()
    }

    fun <T> aStar(
        startPosition: T,
        neighbors: (T) -> List<Edge<T>>,
        endCondition: (T) -> Boolean,
        heuristic: (T) -> Int
    ): List<Vertex<T>> {
        val startVertex = Vertex(startPosition, 0, heuristic = heuristic(startPosition))

        val queue = PriorityQueue<Vertex<T>>()
        queue.add(startVertex)

        val visited = mutableSetOf<T>()

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if(current.position !in visited) {
                visited.add(current.position)
                if (endCondition(current.position)) {
                    return current.path()
                }
                for (neighbor in neighbors(current.position)) {
                    if (!visited.contains(neighbor.vertexPosition)) {
                        queue.add(neighbor.toVertex(current, heuristic))
                    }
                }
            }
        }
        return emptyList()
    }
}