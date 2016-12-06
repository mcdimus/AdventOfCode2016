package ee.mcdimus.aoc2016.day04

import java.util.*
import java.util.stream.Collectors.toList

/**
 * --- Day 4: Security Through Obscurity ---
 *
 * Finally, you come across an information kiosk with a list of rooms.
 * Of course, the list is encrypted and full of decoy data, but the instructions to decode the list are barely hidden nearby.
 * Better remove the decoy data first.
 *
 * Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID,
 * and a checksum in square brackets.
 *
 * A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order,
 * with ties broken by alphabetization. For example:
 *
 * aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
 * a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each), the first five are listed alphabetically.
 * not-a-real-room-404[oarel] is a real room.
 * totally-real-room-200[decoy] is not.
 *
 * Of the real rooms from the list above, the sum of their sector IDs is 1514.
 *
 * What is the sum of the sector IDs of the real rooms?
 *
 * --- Part Two ---
 *
 * With all the decoy data out of the way, it's time to decrypt this list and get moving.
 *
 * The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without the right software.
 * However, the information kiosk designers at Easter Bunny HQ were not expecting to deal with a master cryptographer like yourself.
 *
 * To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's sector ID.
 * A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.
 *
 * For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.
 *
 * What is the sector ID of the room where North Pole objects are stored?
 *
 * @author Dmitri Maksimov
 * @see <a href="http://adventofcode.com/2016/day/4">Day 4: Security Through Obscurity</a>
 */
class Main {

  companion object {
    @JvmStatic
    fun main(vararg args: String) {
      val input = Main::class.java.getResourceAsStream("input.txt").bufferedReader().lines()
          .filter(String::isNotBlank).collect(toList<String>())

      val validRooms = input.map(::Room).filter(Room::isValid)

      println("Sum of the sector IDs of the real rooms: ${validRooms.sumBy(Room::number)}")
      println("------------------------------------------------")
      println("The sector ID of the room where North Pole objects are stored: ${validRooms.single { it.name == "northpole object storage" }.number}")
    }
  }

}

class Room(signature: String) {

  val cypher: String
  val number: Int
  val checksum: String
  val name: String

  init {
    val matchResult = Regex("([a-zA-Z\\-]+)(\\d+)\\[(\\w+)\\]").find(signature)
    cypher = matchResult?.groupValues?.get(1)?.trim('-')!!
    number = matchResult?.groupValues?.get(2)?.toInt()!!
    checksum = matchResult?.groupValues?.get(3)!!

    name = decypher(cypher, number)
  }

  private fun decypher(cypher: String, number: Int): String {
    return cypher.map { if (it == '-') ' ' else ((it.toInt() - 97 + number) % 26 + 97).toChar() }.joinToString("")
  }

  private fun calculateChecksum(): String {
    val charCounts = HashMap<Char, Int>()

    cypher.replace("-", "").forEach {
      charCounts.compute(it, { k, v -> if (v == null) 1 else v + 1 })
    }

    return charCounts.entries.sortedWith(Comparator { o1, o2 ->
      val compareByValue = -1 * o1.value.compareTo(o2.value)
      if (compareByValue == 0) {
        o1.key.toString().compareTo(o2.key.toString())
      } else {
        compareByValue
      }
    }).map { it.key }.take(5).joinToString("")
  }

  fun isValid(): Boolean {
    return checksum == calculateChecksum()
  }

  override fun toString(): String {
    return "Room(cypher='$cypher', number=$number, checksum='$checksum', name='$name') ${calculateChecksum()}"
  }

}

