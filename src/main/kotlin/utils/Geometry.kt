package utils

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(
    val x: Int,
    val y: Int
)


fun Array<Point>.pointInPolygon(point: Point): Boolean {
    val eps = 0.000001
    var crossings = 0

    repeat(this.size) {
        val minX = min(this[it].x, this[(it + 1) % this.size].x)
        val maxX = max(this[it].x, this[(it + 1) % this.size].x)

        if (point.x in minX + 1 ..maxX) {
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