package ee.mcdimus.aoc2016.common

import ee.mcdimus.aoc2016.day01.TurnDirection
import ee.mcdimus.aoc2016.day01.TurnDirection.L
import ee.mcdimus.aoc2016.day01.TurnDirection.R

/**
 * @author Dmitri Maksimov
 */
enum class Direction {

  N, E, S, W;

  fun turn(turnDirection: TurnDirection): Direction {
    return when (turnDirection) {
      R -> values()[(this.ordinal + 1) % 4]
      L -> values()[if ((this.ordinal - 1) < 0) 3 else (this.ordinal - 1)]
    }
  }

}
