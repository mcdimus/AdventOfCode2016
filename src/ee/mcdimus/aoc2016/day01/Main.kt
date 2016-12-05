package ee.mcdimus.aoc2016.day01

import ee.mcdimus.aoc2016.day01.Facing.*
import ee.mcdimus.aoc2016.day01.TurnDirection.*
import java.util.*

/**
 * --- Day 1: No Time for a Taxicab ---
 *
 * Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is regulated by stars.
 * Unfortunately, the stars have been stolen... by the Easter Bunny.
 * To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the advent calendar;
 * the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * You're airdropped near Easter Bunny Headquarters in a city somewhere.
 * "Near", unfortunately, is as close as you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here,
 * and nobody had time to work them out further.
 *
 * The Document indicates that you should start at the given coordinates (where you just landed) and face North.
 * Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward the given number of blocks,
 * ending at a new intersection.
 *
 * There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work out the destination.
 * Given that you can only walk on the street grid of the city, how far is the shortest path to the destination?
 *
 * For example:
 *
 * Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 * R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 * R5, L5, R5, R3 leaves you 12 blocks away.
 *
 * How many blocks away is Easter Bunny HQ?
 *
 * --- Part Two ---
 *
 * Then, you notice the instructions continue on the back of the Recruiting Document.
 * Easter Bunny HQ is actually at the first location you visit twice.
 *
 * For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East.
 *
 * How many blocks away is the first location you visit twice?
 *
 * @author Dmitri Maksimov
 * @see <a href="http://adventofcode.com/2016/day/1">http://adventofcode.com/2016/day/1</a>
 */
fun main(vararg args: String) {
  val input = "R1, L3, R5, R5, R5, L4, R5, R1, R2, L1, L1, R5, R1, L3, L5, L2, R4, L1, R4, R5, L3, R5, L1, R3, L5, " +
      "R1, L2, R1, L5, L1, R1, R4, R1, L1, L3, R3, R5, L3, R4, L4, R5, L5, L1, L2, R4, R3, R3, L185, R3, R4, L5, L4, " +
      "R48, R1, R2, L1, R1, L4, L4, R77, R5, L2, R192, R2, R5, L4, L5, L3, R2, L4, R1, L5, R5, R4, R1, R2, L3, R4, R4, " +
      "L2, L4, L3, R5, R4, L2, L1, L3, R1, R5, R5, R2, L5, L2, L3, L4, R2, R1, L4, L1, R1, R5, R3, R3, R4, L1, L4, R1, " +
      "L2, R3, L3, L2, L1, L2, L2, L1, L2, R3, R1, L4, R1, L1, L4, R1, L2, L5, R3, L5, L2, L2, L3, R1, L4, R1, R1, R2, " +
      "L1, L4, L4, R2, R2, R2, R2, R5, R1, L1, L4, L5, R2, R4, L3, L5, R2, R3, L4, L1, R2, R3, R5, L2, L3, R3, R1, R3"

  val movements = input.split(", ").map(::Movement)

  val start = Coord(0, 0)
  val startFacing = Facing.N

  var firstLocationToBeVisitedTwice: Coord? = null
  val visitedLocations = HashSet<Coord>()
  visitedLocations.add(start)

  var currentCoord = start
  var currentFacing = startFacing

  movements.forEach {
    currentFacing = currentFacing.turn(it.turnDirection)

    if (firstLocationToBeVisitedTwice == null) {
      (1..it.distance)
          .map { currentCoord.move(currentFacing, it) }
          .filterNot { visitedLocations.add(it) }
          .forEach { firstLocationToBeVisitedTwice = it }
    }

    currentCoord = currentCoord.move(currentFacing, it.distance)
  }

  val end = currentCoord

  println("------------------------------------")
  println("Easter Bunny HQ is ${start.distanceTo(end)} blocks away at $end.")
  println("------------------------------------")
  if (firstLocationToBeVisitedTwice != null) {
    println("The first location visited twice is " +
        "${start.distanceTo(firstLocationToBeVisitedTwice as Coord)} blocks away at $firstLocationToBeVisitedTwice")
    println("------------------------------------")
  }
}

class Movement(movement: String) {

  val turnDirection: TurnDirection
  val distance: Int

  init {
    turnDirection = TurnDirection.valueOf(movement[0].toString())
    distance = movement.substring(1).toInt()
  }

}

enum class TurnDirection { L, R }

enum class Facing {

  N, E, S, W;

  fun turn(turnDirection: TurnDirection): Facing {
    return when (turnDirection) {
      R -> Facing.values()[(this.ordinal + 1) % 4]
      L -> Facing.values()[if ((this.ordinal - 1) < 0) 3 else (this.ordinal - 1)]
    }
  }

}

data class Coord(val x: Int, val y: Int) {

  fun move(facing: Facing, distance: Int): Coord {
    return when (facing) {
      N -> Coord(this.x + distance, this.y)
      E -> Coord(this.x, this.y + distance)
      S -> Coord(this.x - distance, this.y)
      W -> Coord(this.x, this.y - distance)
    }
  }

  fun distanceTo(other: Coord) = Math.abs(other.x - this.x) + Math.abs(other.y - this.y)

  override fun toString() = "($x, $y)"

}
