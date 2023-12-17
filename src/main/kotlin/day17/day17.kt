package day17

import utils.*

fun main() {
    val lines = readInput("day17/input")

    val city = lines.toIntMatrix()
    val start = CityBlock(
        position = Point(x = 0, y = 0),
        direction = Direction.Right,
        straightBlocks = 0
    )
    val end = Point(
        x = city.first().lastIndex,
        y = city.lastIndex
    )

    firstStar(city, start, end)
    secondStar(city, start, end)
}

private fun firstStar(city: List<List<Int>>, start: CityBlock, end: Point) {
    val path = Graph.dijkstra(
        startPosition = start,
        neighbors = { cityBlock ->
            city.neighbors(cityBlock) { direction ->
                direction != cityBlock.direction || cityBlock.straightBlocks < 3
            }
        },
        endCondition = { cityBlock ->
            cityBlock.position.distanceTo(end) == 0
        }
    )

    val heatLoss = path.last().weight

    println("First  ⭐: $heatLoss")
}

private fun secondStar(city: List<List<Int>>, start: CityBlock, end: Point) {
    val path = Graph.dijkstra(
        startPosition = start,
        neighbors = { cityBlock ->
            city.neighbors(cityBlock) { direction ->
                when(cityBlock.straightBlocks) {
                    in 0..3 -> direction == cityBlock.direction
                    in 4..10 -> true
                    else -> false
                }
            }
        },
        endCondition = { cityBlock ->
            cityBlock.position.distanceTo(end) == 0 && cityBlock.straightBlocks >= 4
        }
    )

    val heatLoss = path.last().weight

    println("Second ⭐: $heatLoss")
}

private fun List<List<Int>>.neighbors(cityBlock: CityBlock, excludeCondition: (Direction) -> Boolean) =
    listOf(Direction.Right, Direction.Left, Direction.Up, Direction.Down)
        .filter { !it.isOpposite(cityBlock.direction) && excludeCondition(it) }
        .map { direction -> cityBlock.position + direction to direction }
        .filter { this.positionIsValid(it.first) }
        .map { (position, direction) ->
            val straightBlocks = if (cityBlock.direction == direction) cityBlock.straightBlocks + 1 else 1
            val nextCityBlock = CityBlock(
                position = position,
                direction = direction,
                straightBlocks = straightBlocks
            )
            Graph.Edge(vertexPosition = nextCityBlock, weight = this[position.y][position.x])
        }

private data class CityBlock(
    val position: Point,
    val direction: Direction,
    val straightBlocks: Int
)
