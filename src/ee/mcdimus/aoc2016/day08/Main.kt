package ee.mcdimus.aoc2016.day08


/**
 * --- Day 8: Two-Factor Authentication ---
 *
 * You come across a door implementing what you can only assume is an implementation of two-factor authentication
 * after a long game of requirements telephone.
 *
 * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk).
 * Then, it displays a code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.
 *
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured out how it works.
 * Now you just have to work out what the screen would have displayed.
 *
 * The magnetic strip on the card you swiped encodes a series of instructions for the screen;
 * these instructions are your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off,
 * and is capable of three somewhat peculiar operations:
 *
 * - rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
 * - rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels.
 * Pixels that would fall off the right end appear at the left end of the row.
 * - rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels.
 * Pixels that would fall off the bottom appear at the top of the column.
 *
 * For example, here is a simple sequence on a smaller screen:
 *
 * rect 3x2 creates a small rectangle in the top-left corner:
 *
 * ###....
 * ###....
 * .......
 *
 * rotate column x=1 by 1 rotates the second column down by one pixel:
 *
 * #.#....
 * ###....
 * .#.....
 *
 * rotate row y=0 by 4 rotates the top row right by four pixels:
 *
 * ....#.#
 * ###....
 * .#.....
 *
 * rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel to wrap back to the top:
 *
 * .#..#.#
 * #.#....
 * .#.....
 *
 * As you can see, this display technology is extremely powerful, and will soon dominate the tiny-code-displaying-screen market.
 * That's what the advertisement on the back of the display tries to convince you, anyway.
 *
 * There seems to be an intermediate check of the voltage used by the display:
 * after you swipe your card, if the screen did work, how many pixels should be lit?
 *
 * --- Part Two ---
 *
 * You notice that the screen is only capable of displaying capital letters;
 * in the font it uses, each letter is 5 pixels wide and 6 tall.
 *
 * After you swipe your card, what code is the screen trying to display?
 *
 * @author Dmitri Maksimov
 * @see <a href="http://adventofcode.com/2016/day/8">Day 8: Two-Factor Authentication</a>
 */
class Main {

  companion object {
    @JvmStatic
    fun main(vararg args: String) {
      val input = Main::class.java.getResourceAsStream("input.txt").bufferedReader().lines()
          .filter(String::isNotBlank).collect(java.util.stream.Collectors.toList<String>())

      val commands = input.map { Screen.Command.create(it) }

      val screen = Screen(50, 6).draw()

      commands.forEach {
        for (i in 0..11) {
          println()
        } // a la clear for terminal

        screen.apply(it).draw()
        Thread.sleep(50)
      }
      println("${screen.countPixels(active = true)} pixels are lit")
    }
  }

}

class Screen(val width: Int, val height: Int) {

  private val matrix = Array(height, { BooleanArray(width) })

  fun apply(command: Command): Screen {
    command.apply(this)
    return this
  }

  fun draw(): Screen {
    matrix.forEach {
      it.forEach { print(if (it) "@" else ".") }
      println()
    }
    return this
  }

  fun getColumnAt(columnIndex: Int) = matrix.map { it[columnIndex] }.toBooleanArray()

  fun replaceColumnAt(columnIndex: Int, newColumn: BooleanArray) {
    matrix.forEachIndexed { i, booleans ->
      booleans[columnIndex] = newColumn[i]
    }
  }

  fun getRowAt(rowIndex: Int) = matrix[rowIndex]

  fun replaceRowAt(rowIndex: Int, newRow: BooleanArray) {
    matrix[rowIndex] = newRow
  }

  fun countPixels(active: Boolean): Int {
    return matrix.map { it.toList() }.flatMap { it }.count { it == active }
  }

  abstract class Command {

    abstract fun apply(screen: Screen)

    companion object {
      fun create(value: String): Command {
        val commandParts = value.split(" ")

        return if (commandParts[0] == "rect") {
          RectCommand(commandParts.takeLast(commandParts.size - 1))
        } else if (commandParts[0] == "rotate" && commandParts[1] == "column") {
          RotateColumnCommand(commandParts.takeLast(commandParts.size - 2))
        } else if (commandParts[0] == "rotate" && commandParts[1] == "row") {
          RotateRowCommand(commandParts.takeLast(commandParts.size - 2))
        } else {
          throw UnsupportedOperationException(value)
        }
      }
    }

  }

  class RectCommand(args: List<String>) : Command() {

    private val w: Int
    private val h: Int

    init {
      val dimension = args.first().split("x")
      w = dimension[0].toInt()
      h = dimension[1].toInt()
    }

    override fun apply(screen: Screen) {
      for (i in 0..h - 1) {
        for (j in 0..w - 1) {
          screen.matrix[i][j] = true
        }
      }
    }

  }

  class RotateColumnCommand(args: List<String>) : Command() {

    private val index = args.first().split("=")[1].toInt()
    private val distance = args.last().toInt()

    override fun apply(screen: Screen) {
      val optimizedDistance = distance % screen.height
      if (optimizedDistance == 0) return

      val column = screen.getColumnAt(index)
      val rotatedColumn = column.rotate(distance)

      screen.replaceColumnAt(index, rotatedColumn)
    }

  }

  class RotateRowCommand(args: List<String>) : Command() {

    private val index = args.first().split("=")[1].toInt()
    private val distance = args.last().toInt()

    override fun apply(screen: Screen) {
      val optimizedDistance = distance % screen.width
      if (optimizedDistance == 0) return

      val row = screen.getRowAt(index)
      val rotatedRow = row.rotate(distance)

      screen.replaceRowAt(index, rotatedRow)
    }

  }

}

fun BooleanArray.rotate(distance: Int): BooleanArray {
  val rotated = BooleanArray(this.size)

  this.forEachIndexed { i, b ->
    val newIndex = (i + distance) % this.size
    rotated[newIndex] = b
  }

  return rotated
}
