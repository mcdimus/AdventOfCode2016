package ee.mcdimus.aoc2016.day05

import java.security.MessageDigest

/**
 * @author Dmitri Maksimov
 */
fun main(vararg args: String) {
  val input = "cxdnnyjw"

  println("First door password is ${SimplePasswordFinder(input).find()}")
  println("Second door password is ${AdvancedPasswordFinder(input).find()}")
}

val hexArray = "0123456789ABCDEF".toCharArray()
fun bytesToHex(bytes: ByteArray): String {
  val hexChars = CharArray(bytes.size * 2)
  for (j in bytes.indices) {
    val v = bytes[j].toInt() and 0xFF
    hexChars[j * 2] = hexArray[v.ushr(4)]
    hexChars[j * 2 + 1] = hexArray[v and 0x0F]
  }
  return String(hexChars)
}

class SimplePasswordFinder(val input: String) {

  fun find(): String {
    val md5 = MessageDigest.getInstance("MD5")

    var index = 0
    val pass = StringBuilder()
    while (pass.length != 8) {
      md5.update((input + index).toByteArray())
      val digest = md5.digest()
      val hash = bytesToHex(digest)
      if (hash.startsWith("00000")) {
        pass.append(hash[5])
      }
      index++
    }
    return pass.toString()
  }

}

class AdvancedPasswordFinder(val input: String) {

  fun find(): String {
    val md5 = MessageDigest.getInstance("MD5")

    var count = 0
    var index = 0
    val pass = CharArray(8)
    while (count != 8) {
      md5.update((input + index).toByteArray())
      val digest = md5.digest()
      val hash = bytesToHex(digest)
      if (hash.startsWith("00000")) {
        val position = hash[5]
        if (position.isDigit()
            && position.toString().toInt() in 0..7
            && pass[position.toString().toInt()] == '\u0000') {
          pass[position.toString().toInt()] = hash[6]
          count++
        }
      }
      index++
    }
    return pass.joinToString("")
  }

}
