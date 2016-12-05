package ee.mcdimus.aoc2016.day02

import ee.mcdimus.aoc2016.common.Coord
import ee.mcdimus.aoc2016.common.Direction.E
import ee.mcdimus.aoc2016.common.Direction.N
import ee.mcdimus.aoc2016.common.Direction.S
import ee.mcdimus.aoc2016.common.Direction.W

/**
 * --- Day 2: Bathroom Security ---
 *
 * You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush that you forgot to use the bathroom!
 * Fancy office buildings like this one usually have keypad locks on their bathrooms, so you search the front desk for the code.
 *
 * "In order to improve security," the document you find says, "bathroom codes will no longer be written down.
 * Instead, please memorize and follow the procedure below to access the bathrooms."
 *
 * The document goes on to explain that each button to be pressed can be found by starting on the previous button
 * and moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves right.
 * Each line of instructions corresponds to one button, starting at the previous button (or, for the first line, the "5" button);
 * press whatever button you're on at the end of each line. If a move doesn't lead to a button, ignore it.
 *
 * You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom. You picture a keypad like this:
 *
 * 1 2 3
 * 4 5 6
 * 7 8 9
 *
 * Suppose your instructions are:
 *
 * ULL
 * RRDDD
 * LURDL
 * UUUUD
 *
 * You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the first button is 1.
 * Starting from the previous button ("1"), you move right twice (to "3")
 * and then down three times (stopping at "9" after two moves and ignoring the third), ending up with 9.
 * Continuing from "9", you move left, up, right, down, and left, ending with 8.
 * Finally, you move up four times (stopping at "2"), then down once, ending with 5.
 *
 * So, in this example, the bathroom code is 1985.
 *
 * Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?
 *
 * --- Part Two ---
 *
 * You finally arrive at the bathroom (it's a several minute walk from the lobby so visitors can behold the many fancy conference rooms
 * and water coolers on this floor) and go to punch in the code. Much to your bladder's dismay, the keypad is not at all like you imagined it.
 * Instead, you are confronted with the result of hundreds of man-hours of bathroom-keypad-design meetings:
 *
 *     1
 *   2 3 4
 * 5 6 7 8 9
 *   A B C
 *     D
 *
 * You still start at "5" and stop when you're at an edge, but given the same instructions as above, the outcome is very different:
 *
 * You start at "5" and don't move at all (up and left are both edges), ending at 5.
 * Continuing from "5", you move right twice and down three times (through "6", "7", "B", "D", "D"), ending at D.
 * Then, from "D", you move five more times (through "D", "B", "C", "C", "B"), ending at B.
 * Finally, after five more moves, you end at 3.
 *
 * So, given the actual keypad layout, the code would be 5DB3.
 *
 * Using the same instructions in your puzzle input, what is the correct bathroom code?
 *
 * @author Dmitri Maksimov
 * @see <a href="http://adventofcode.com/2016/day/2">Day 2: Bathroom Security</a>
 */
fun main(vararg args: String) {
  val input = "UUURRRRULRDLRDRRDURDDDLLDLLLULDUDDLDLULUURULRLDLRRLLLRRDRRLDDLLULUDUDDLRDRDUURDLURUURLRULLDDURULRRURDUURLULUUUURDDDDUUDLULRULLLRLLRRRURDLLRLLRRRUURULRDRUUDDDDDLLLRURRURRUURDUURDDRDLULRRLLLDRRRLURRLLURLDRRDDLDLRRLLRDRLLLLDLULDLRRDRRLDDURLULLUDLUDRRDRRLRLULURDRLRLUUUDLRLDLLLURDUDULULDDRUUURLLLDLLDDUDDRURURUDDLUULRDRRRRLDRDDURLUDURDULLDLUDLULDRLRLLRLLLLRURDURLLDRRDRLRUUUUULLLRUDURUDLLLUDLLLLRDLDRDUDRURLUDDUDDURLUUUUDDLLUDLULLLLLDUDLLRLRRDDDULULRLDRLLULDLUDLLURULRDDUURULRDLDLDLRL\n" +
      "URUUURDULUDLUUUUDDRRRDRRRLDUDLRDRRDRDDLRUULDLLDUDULLLRLDRDRRLDLDLUUDRUULDUDULDUDURURDDURULDLURULRLULDUDDUULDLLLDDURDDRDDURUULUUDRLDDULDRRRRDURRUDLLLURDDDLRULLRDDRDDDDLUUDRDUULRRRRURULDDDLDDRDRRUDRRURUDRDDLDRRRLLURURUULUUDRDULLDRLRDRRDDURDUDLDRLUDRURDURURULDUUURDUULRRRRRUDLLULDDDRLULDDULUDRRRDDRUDRRDLDLRUULLLLRRDRRLUDRUULRDUDRDRRRDDRLLRUUDRLLLUDUDLULUUDULDRRRRDDRURULDULLURDLLLDUUDLLUDRLDURRRLDDDURUDUDURRULDD\n" +
      "LRUDDULLLULRLUDUDUDRLLUUUULLUDLUUUUDULLUURDLLRDUDLRUDRUDDURURRURUDLLLRLDLUDRRRRRRDLUURLRDDDULRRUDRULRDRDDUULRDDLDULDRRRDDLURRURLLLRURDULLRUUUDDUDUURLRLDDUURLRDRRLURLDRLLUUURDRUUDUUUDRLURUUUDLDRRLRLLRRUURULLLRLLDLLLDULDDLDULDLDDRUDURDDURDUDURDLLLRRDDLULLLUDURLUDDLDLUUDRDRUDUUDLLDDLLLLDRDULRDLDULLRUDDUULDUDLDDDRUURLDRRLURRDDRUUDRUDLLDLULLULUDUDURDDRLRDLRLDRLDDRULLLRUDULDRLRLRULLRLLRRRLLRRRDDRULRUURRLLLRULDLUDRRDDLLLUDDUDDDLURLUDRDLURUUDLLDLULURRLLDURUDDDDRLULRDDLRLDDLRLLDDRRLRDUDUUULRRLRULUDURDUDRLRLRUDUDLLRRRRLRRUDUL\n" +
      "RULLLLUUUDLLDLLRULLRURRULDDRDLUULDRLLRUDLLRRLRDURLLDUUUUURUUURDLUURRLDDDLRRRRLRULDUDDLURDRRUUDLRRRDLDDUDUDDRUDURURLDULLDLULDLLUDLULRDRLLURRLLDURLDLRDLULUDDULDLDDDDDUURRDRURLDLDULLURDLLDDLLUDLDLDRLRLDLRDRLDLRRUUDRURLUUUDLURUULDUDRDULLDURUDLUUURRRLLDUDUDDUUULLLRUULDLURUDDRLUDRDDLDLLUDUDRRRDDUUULUULLLRLLUURDUUDRUUULULLDLDRUUDURLLUULRLDLUURLLUUDRURDDRLURULDUDUUDRRUUURDULRLDUUDDRURURDRRULDDDRLUDLLUUDURRRLDLRLRDRURLURLLLRLDDLRRLDLDDURDUUDRDRRLDRLULDRLURUUUDDRLLLDDLDURLLLLDRDLDRRUDULURRLULRDRLLUULLRLRDRLLULUURRUDRUDDDLLDURURLURRRDLLDRDLUDRULULULRLDLRRRUUDLULDURLRDRLULRUUURRDDLRUURUDRURUDURURDD\n" +
      "DURRDLLLDDLLDLLRLULULLRDLDRRDDRDLRULURRDUUDDRLLDDLDRRLRDUDRULDLRURDUUDRDDLLDRRDRUDUDULLDDDDLDRRRLRLRDRDLURRDDLDDDUUDRDRLLLDLUDDDLUULRDRLLLRLLUULUDDDRLDUUUURULRDDURRDRLUURLUDRLRLLLDDLRDDUULRRRRURDLDDDRLDLDRRLLDRDDUDDUURDLDUUDRDLDLDDULULUDDLRDDRLRLDDLUDLLDRLUDUDDRULLRLDLLRULRUURDDRDRDRURDRRLRDLLUDDRRDRRLDDULLLDLUDRRUDLDULDRURRDURLURRLDLRDLRUDLULUDDRULRLLDUURULURULURRLURRUULRULRRRLRDLULRLRLUDURDDRUUURDRLLRRRDDLDRRRULLDLRDRULDRRLRRDLUDDRDDDUUURRLULLDRRUULULLRRRRLDDRDDLUURLLUDLLDUDLULUULUDLLUUURRRUDDDRLLLRDRUUDUUURDRULURRLRDLLUURLRDURULDRRUDURRDDLDRLDRUUDRLLUDLRRU\n"

  val instructionsSets = input.split("\n").filterNot(String::isBlank)

  val keyPadController = SimpleKeyPadController('5')
  instructionsSets.forEach {
    it.forEach {
      keyPadController.move(it)
    }
    print(keyPadController.getCurrentKey())
  }
  println("\n--------------------------------")

  val crazyKeyPadController = CrazyKeyPadController('5')
  instructionsSets.forEach {
    it.forEach {
      crazyKeyPadController.move(it)
    }
    print(crazyKeyPadController.getCurrentKey())
  }
  println("\n--------------------------------")
}

abstract class KeyPadController {

  protected lateinit var keyPointer: Coord
  protected lateinit var matrix: Array<CharArray>

  fun move(direction: Char) {
    when (direction) {
      'U' -> {
        if (keyPointer.x > 0) {
          val newCoord = keyPointer.move(N, -1)
          if (matrix[newCoord.x][newCoord.y] != '#') {
            keyPointer = newCoord
          }
        }
      }
      'D' -> {
        if (keyPointer.x < matrix.size - 1) {
          val newCoord = keyPointer.move(S, -1)
          if (matrix[newCoord.x][newCoord.y] != '#') {
            keyPointer = newCoord
          }
        }
      }
      'L' -> {
        if (keyPointer.y > 0) {
          val newCoord = keyPointer.move(W, 1)
          if (matrix[newCoord.x][newCoord.y] != '#') {
            keyPointer = newCoord
          }
        }
      }
      'R' -> {
        if (keyPointer.y < matrix[keyPointer.x].size - 1) {
          val newCoord = keyPointer.move(E, 1)
          if (matrix[newCoord.x][newCoord.y] != '#') {
            keyPointer = newCoord
          }
        }
      }
    }
  }

  fun getCurrentKey(): String {
    return matrix[keyPointer.x][keyPointer.y].toString()
  }

  fun getKeyCoord(key: Char): Coord {
    var x = -1
    var y = -1
    matrix.forEachIndexed { i, chars ->
      chars.forEachIndexed { j, char ->
        if (char == key) {
          x = i; y = j
        }
      }
    }
    return Coord(x, y)
  }

}

class SimpleKeyPadController(startKey: Char) : KeyPadController() {

  init {
    val line1 = charArrayOf('1', '2', '3')
    val line2 = charArrayOf('4', '5', '6')
    val line3 = charArrayOf('7', '8', '9')

    matrix = arrayOf(line1, line2, line3)
    keyPointer = getKeyCoord(startKey)
  }

}

class CrazyKeyPadController(startKey: Char) : KeyPadController() {

  init {
    val line1 = charArrayOf('#', '#', '1', '#', '#')
    val line2 = charArrayOf('#', '2', '3', '4', '#')
    val line3 = charArrayOf('5', '6', '7', '8', '9')
    val line4 = charArrayOf('#', 'A', 'B', 'C', '#')
    val line5 = charArrayOf('#', '#', 'D', '#', '#')

    matrix = arrayOf(line1, line2, line3, line4, line5)
    keyPointer = getKeyCoord(startKey)
  }

}
