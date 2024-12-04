package utils

import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class PointL(
    val x: Long,
    val y: Long
) {
    infix operator fun plus(direction: Direction): PointL {
        return PointL(
            x = direction.x + this.x,
            y = direction.y + this.y
        )
    }

    fun move(times: Long, direction: Direction): PointL {
        return PointL(
            x = (direction.x * times) + this.x,
            y = (direction.y * times) + this.y
        )
    }

    fun distanceTo(other: PointL): Long {
        return abs(other.x - this.x) + abs(other.y - this.y)
    }
}

data class Point(
    val x: Int,
    val y: Int
) {
    infix operator fun plus(direction: Direction): Point {
        return Point(
            x = direction.x + this.x,
            y = direction.y + this.y
        )
    }

    fun move(times: Int, direction: Direction): Point {
        return Point(
            x = (direction.x * times) + this.x,
            y = (direction.y * times) + this.y
        )
    }

    fun distanceTo(other: Point): Int {
        return abs(other.x - this.x) + abs(other.y - this.y)
    }

    fun neighbors() = listOf(Direction.Up, Direction.Right, Direction.Down, Direction.Left).map { this + it }

}

data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int
)

data class PointD(
    val x: Double,
    val y: Double
)

fun Array<PointL>.pointInPolygon(point: PointL): Boolean {
    val eps = 0.000001
    var crossings = 0

    repeat(this.size) {
        val minX = min(this[it].x, this[(it + 1) % this.size].x)
        val maxX = max(this[it].x, this[(it + 1) % this.size].x)

        if (point.x in minX + 1..maxX) {
            val dx = (this[(it + 1) % this.size].x - this[it].x).toFloat()
            val dy = (this[(it + 1) % this.size].y - this[it].y).toFloat()

            val k = if (abs(dx) < eps) {
                (9999999999).toFloat()
            } else {
                dy / dx
            }

            val m = this[it].y - k * this[it].x
            val y2 = k * point.x + m

            if (point.y <= y2) {
                crossings += 1
            }
        }
    }

    return crossings % 2 == 1
}

fun List<Point>.pointInPolygon(point: Point): Boolean {
    val eps = 0.000001
    var crossings = 0

    repeat(this.size) {
        val minX = min(this[it].x, this[(it + 1) % this.size].x)
        val maxX = max(this[it].x, this[(it + 1) % this.size].x)

        if (point.x in minX + 1..maxX) {
            val dx = (this[(it + 1) % this.size].x - this[it].x).toFloat()
            val dy = (this[(it + 1) % this.size].y - this[it].y).toFloat()

            val k = if (abs(dx) < eps) {
                (9999999999).toFloat()
            } else {
                dy / dx
            }

            val m = this[it].y - k * this[it].x
            val y2 = k * point.x + m

            if (point.y <= y2) {
                crossings += 1
            }
        }
    }

    return crossings % 2 == 1
}

sealed interface Direction {
    val x: Int
    val y: Int

    data object Up : Direction {
        override val x = 0
        override val y = -1
    }

    data object UpLeft : Direction {
        override val x = -1
        override val y = -1
    }

    data object UpRight : Direction {
        override val x = 1
        override val y = -1
    }

    data object Left : Direction {
        override val x = -1
        override val y = 0
    }

    data object Right : Direction {
        override val x = 1
        override val y = 0
    }

    data object Down : Direction {
        override val x = 0
        override val y = 1
    }

    data object DownLeft : Direction {
        override val x = -1
        override val y = 1
    }

    data object DownRight : Direction {
        override val x = 1
        override val y = 1
    }

    data object None : Direction {
        override val x = 0
        override val y = 0
    }

    fun isOpposite(other: Direction): Boolean {
        return when (other) {
            Down -> this == Up
            Left -> this == Right
            Right -> this == Left
            Up -> this == Down
            DownLeft -> this == UpRight
            DownRight -> this == UpLeft
            UpLeft -> this == DownRight
            UpRight -> this == DownLeft
            None -> false
        }
    }
}

val AllDirections = listOf(
    Direction.Up,
    Direction.UpLeft,
    Direction.UpRight,
    Direction.Left,
    Direction.Right,
    Direction.Down,
    Direction.DownLeft,
    Direction.DownRight
)