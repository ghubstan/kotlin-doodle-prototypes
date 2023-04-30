package io.dongxi.util

object RegexUtils {

    val twoDigitNumber = Regex("\\d{2}")
    val threeDigitNumber = Regex("\\d{3}")

    val cpfPattern = Regex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")

    fun isMatch(input: String, pattern: Regex): Boolean {
        return pattern.matches(input)
    }
}
