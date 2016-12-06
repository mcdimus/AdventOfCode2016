package ee.mcdimus.aoc2016.day03

import java.util.*
import java.util.stream.Collectors.toList

/**
 * --- Day 3: Squares With Three Sides ---
 *
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up this part of Easter Bunny HQ.
 * This must be a graphic design department; the walls are covered in specifications for triangles.
 *
 * Or are they?
 *
 * The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't triangles.
 * You can't help but mark the impossible ones.
 *
 * In a valid triangle, the sum of any two sides must be larger than the remaining side.
 * For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 *
 * In your puzzle input, how many of the listed triangles are possible?
 *
 * --- Part Two ---
 *
 * Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified in groups of three vertically.
 * Each set of three numbers in a column specifies a triangle. Rows are unrelated.
 *
 * For example, given the following specification, numbers with the same hundreds digit would be part of the same triangle:
 *
 * 101 301 501
 * 102 302 502
 * 103 303 503
 * 201 401 601
 * 202 402 602
 * 203 403 603
 *
 * In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
 *
 * @author Dmitri Maksimov
 * @see <a href="http://adventofcode.com/2016/day/3">Day 3: Squares With Three Sides</a>
 */
class Main {

  companion object {
    @JvmStatic
    fun main(vararg args: String) {
      val input = Main::class.java.getResourceAsStream("input.txt").bufferedReader().lines()
          .filter(String::isNotBlank).collect(toList<String>())

      val validTrianglesCount = input.map(::Triangle).filter(Triangle::isValid).count()
      println("Valid Triangles Count = $validTrianglesCount")

      val transformedInput = ArrayList<String>()

      for (i in 0..input.size - 1 step 3) {
        val tokens1 = input[i].split(Regex("\\s+")).filter(String::isNotBlank)
        val tokens2 = input[i + 1].split(Regex("\\s+")).filter(String::isNotBlank)
        val tokens3 = input[i + 2].split(Regex("\\s+")).filter(String::isNotBlank)

        tokens1.forEachIndexed { j, token ->
          transformedInput.add("${tokens1[j]} ${tokens2[j]} ${tokens3[j]}")
        }
      }

      val validTrianglesCount2 = transformedInput.map(::Triangle).filter(Triangle::isValid).count()
      println("Valid Triangles Count 2 = $validTrianglesCount2")
    }
  }

}

class Triangle(string: String) {

  val a: Int
  val b: Int
  val c: Int

  init {
    val sides = string.split(Regex("\\s+")).filter(String::isNotBlank).map(String::toInt)
    a = sides[0]
    b = sides[1]
    c = sides[2]
  }

  fun isValid(): Boolean {
    // use The Law of Cosines
    return Math.abs((a * a + b * b - c * c) / (2 * a * b)) < 1
  }

}
