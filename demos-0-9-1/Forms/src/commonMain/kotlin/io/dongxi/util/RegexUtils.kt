package io.dongxi.util

object RegexUtils {

    val twoDigitNumber = Regex("\\d{2}")
    val threeDigitNumber = Regex("\\d{3}")

    fun isMatch(input: String, pattern: Regex): Boolean {
        return pattern.matches(input)
    }
}
