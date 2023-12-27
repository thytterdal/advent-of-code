package day24

import utils.PointD
import utils.readInput
import kotlin.math.sign

val validRange = 200000000000000.0..400000000000000.0
//val validRange = 7.0..27.0

fun main() {
    val lines = readInput("day24/input")

    firstStar(lines)
}

private fun firstStar(lines: List<String>) {
    val hail = lines.parseHail()
    var sum = 0

    hail.forEachIndexed { index, hailStone ->
        if (index != hail.lastIndex) {
            hail.subList(index + 1, hail.lastIndex).forEach { other ->
                val intersectionPoint = hailStone.intersectionPoint(other) ?: return@forEach
                if (intersectionPoint.x in validRange && intersectionPoint.y in validRange) {
                    sum++
                }
            }
        }
    }

    println("First  ⭐: $sum")
}

private fun List<String>.parseHail() = this.map { line ->
    val (position, velocity) = line.split("@")
    val (x, y, z) = position.split(",").map { it.trim().toDouble() }
    val (xVelocity, yVelocity, zVelocity) = velocity.split(",").map { it.trim().toDouble() }
    Hail(
        x = x,
        y = y,
        z = z,
        xVelocity = xVelocity,
        yVelocity = yVelocity,
        zVelocity = zVelocity
    )
}


private data class Hail(
    val x: Double,
    val y: Double,
    val z: Double,
    val xVelocity: Double,
    val yVelocity: Double,
    val zVelocity: Double
) {
    val xySlope = yVelocity / xVelocity

    fun intersectionPoint(other: Hail): PointD? {
        if (this.xySlope == other.xySlope) return null
        val xIntersection =
            (this.y - other.y - this.x * this.xySlope + other.x * other.xySlope) / (other.xySlope - this.xySlope)
        val yIntersection = this.y + this.xySlope * (xIntersection - this.x)
        val onCollisionPath = (xIntersection - this.x).sign == this.xVelocity.sign
        val otherOnCollisionPath = (xIntersection - other.x).sign == other.xVelocity.sign

        return if (onCollisionPath && otherOnCollisionPath) PointD(xIntersection, yIntersection) else null
    }
}