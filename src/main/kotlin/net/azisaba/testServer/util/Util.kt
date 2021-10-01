package net.azisaba.testServer.util

import java.security.SecureRandom

object Util {
    private val secureRandom = SecureRandom()
    val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val charPoolWithSomeSpecialChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '$' + '-' + '_' + ',' + '!' + '(' + ')' + '+'

    fun generateRandomCharacters(charPool: List<Char>, length: Int) =
        (1..length)
            .map { secureRandom.nextInt(charPool.size) }
            .joinToString("") { charPool[it].toString() }
}
