package io.dongxi.util

// See https://rextester.com/BPFLLU52006

// Special pwd chars:
//      # % & @ ^ ` ~ * - + ! ? < > = . ; : ,
//

// I must support all Portuguese letters (diacritics) without the help
// of the JVM based Normalizer.
// Here they are.
//      Lower Case:     ã á à â ç é ê í õ ó ô ú ü
//      Upper Case:     Ã Á À Â Ç É Ê Í Õ Ó Ô Ú Ü
// See https://portuguese.typeit.org

object RegexUtils {

    // Support portuguese diacritics
    const val LOWERCASE_PT_CHARACTERS = "ã á à â ç é ê í õ ó ô ú ü"

    // Support portuguese diacritics
    const val UPPERCASE_PT_CHARACTERS = "Ã Á À Â Ç É Ê Í Õ Ó Ô Ú Ü"

    const val SPECIAL_CHARACTERS = "# % & @ ^ ` ~ * - + ! ? < > = . ; : ,"

    val digit = Regex("[0-9]")

    val twoDigitNumber = Regex("\\d{2}")

    val threeDigitNumber = Regex("\\d{3}")

    val lowercaseLetter = Regex("[a-zãáàâçéêíõóôúü]")

    val uppercaseLetter = Regex("[A-ZÃÁÀÂÇÉÊÍÕÓÔÚÜ]")

    val specialCharacter = Regex("[#%&@^`~*-+!?<>=.;:,]")

    val cpfPattern = Regex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")

    fun containsMatchIn(input: String, pattern: Regex): Boolean {
        return pattern.containsMatchIn(input)
    }

    fun isMatch(input: String, pattern: Regex): Boolean {
        return pattern.matches(input)
    }
}
