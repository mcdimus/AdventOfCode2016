package ee.mcdimus.aoc2016.common

import ee.mcdimus.aoc2016.common.Direction.E
import ee.mcdimus.aoc2016.common.Direction.N
import ee.mcdimus.aoc2016.common.Direction.S
import ee.mcdimus.aoc2016.common.Direction.W

/**
 * @author Dmitri Maksimov
 */
data class Coord(val x: Int, val y: Int) {

  fun move(direction: Direction, distance: Int): Coord {
    return when (direction) {
      N -> Coord(this.x + distance, this.y)
      E -> Coord(this.x, this.y + distance)
      S -> Coord(this.x - distance, this.y)
      W -> Coord(this.x, this.y - distance)
    }
  }

  fun distanceTo(other: Coord) = Math.abs(other.x - this.x) + Math.abs(other.y - this.y)

  override fun toString() = "($x, $y)"

}
