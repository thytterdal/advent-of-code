package aoc2023.day22

import utils.Point
import utils.Point3D
import utils.get
import utils.readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = readInput("aoc2023/day22/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val bricks = lines.mapBricks()

    val droppedBricks = bricks.drop()

    val sum = droppedBricks.count { brick ->
        brick.supports.all { supports -> droppedBricks.first { it.id == supports }.supportedBy.size > 1 }
    }

    println("First  ⭐: $sum")
}

private fun secondStar(lines: List<String>) {
    val bricks = lines.mapBricks()

    val droppedBricks = bricks.drop()

    val sum = droppedBricks.sumOf { brick ->
        val fallenBricks = mutableSetOf(brick.id)

        droppedBricks
            .filter { it.id != brick.id }
            .sortedBy { it.minZ }.forEach {
                if (it.supportedBy.all { x -> x in fallenBricks } && it.supportedBy.isNotEmpty()) {
                    fallenBricks.add(it.id)
                }
            }
        fallenBricks.size - 1
    }

    println("Second ⭐: $sum")
}

private fun List<String>.mapBricks() = mapIndexed { index, line ->
    val (start, end) = line.split("~")
    val (x1, y1, z1) = start.split(",").map { it.toInt() }
    val (x2, y2, z2) = end.split(",").map { it.toInt() }
    Brick(
        id = index + 1,
        cubes = if (x1 != x2) {
            (min(x1, x2)..max(x1, x2)).map { x -> Point3D(x, y1, z1) }
        } else if (y1 != y2) {
            (min(y1, y2)..max(y1, y2)).map { y -> Point3D(x1, y, z1) }
        } else {
            (min(z1, z2)..max(z1, z2)).map { z -> Point3D(x1, y1, z) }
        }
    )
}


private fun List<Brick>.drop(): List<Brick> {
    val height = this.maxOf { it.maxZ } - 1
    val xWidth = this.maxOf { it.maxX } + 1
    val yWidth = this.maxOf { it.maxY } + 1

    val cube = (1..height).associateWith {
        List(yWidth) {
            MutableList(xWidth) {
                0
            }
        }
    }

    return this.sortedBy { it.minZ }.map { brick ->
        val droppedToZ = (height downTo 1).takeWhile { z ->
            brick.surface().all { (cube[z]?.get(it) ?: -1) == 0 }
        }.min()

        if (brick.hasMoreThanOneLevel) {
            (brick.minZ..brick.maxZ).forEachIndexed { index, z ->
                brick.surface(z).forEach {
                    cube[droppedToZ + index]?.get(it.y)?.set(it.x, brick.id)
                }
            }
        } else {
            brick.surface().forEach {
                cube[droppedToZ]?.get(it.y)?.set(it.x, brick.id) ?: throw Exception("?")
            }
        }

        if (droppedToZ > 1) {
            brick.surface().forEach {
                val beneath = cube[droppedToZ - 1]?.get(it) ?: 0
                if (beneath > 0) {
                    brick.supportedBy.add(beneath)
                    this.first { brickBeneath -> brickBeneath.id == beneath }.supports.add(brick.id)
                }
            }
        }

        brick.dropTo(droppedToZ)
    }

}

private data class Brick(
    val id: Int,
    val cubes: List<Point3D>,
    val supportedBy: MutableSet<Int> = mutableSetOf(),
    val supports: MutableSet<Int> = mutableSetOf()
) {
    val maxX
        get() = cubes.maxBy { it.x }.x

    val maxY
        get() = cubes.maxBy { it.y }.y

    val minZ
        get() = cubes.minBy { it.z }.z
    val maxZ
        get() = cubes.maxBy { it.z }.z

    val hasMoreThanOneLevel
        get() = cubes.distinctBy { it.z }.count() > 1


    fun surface() = cubes.map { Point(x = it.x, y = it.y) }.distinct()
    fun surface(z: Int) = cubes.filter { it.z == z }.map { Point(x = it.x, y = it.y) }.distinct()

    fun dropTo(z: Int): Brick {
        val levels = minZ - z
        return this.copy(
            cubes = this.cubes.map { point3D -> point3D.copy(z = point3D.z - levels) }
        )
    }

}