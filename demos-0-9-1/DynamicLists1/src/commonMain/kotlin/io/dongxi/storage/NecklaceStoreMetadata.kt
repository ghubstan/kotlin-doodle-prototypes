package io.dongxi.storage

object NecklaceStoreMetadata {

    private const val LARGE_NECKLACES_PATH = "necklaces/mock/large/"
    private const val SMALL_NECKLACES_PATH = "necklaces/mock/small/"

    val allLargeNecklaces: List<Pair<String, String>> = listOf(
        "A" to pathToLargeNecklaces("a.svg"),
        "B" to pathToLargeNecklaces("b.svg"),
        "C" to pathToLargeNecklaces("c.svg"),
        "D" to pathToLargeNecklaces("d.svg"),
        "E" to pathToLargeNecklaces("e.svg"),
        "F" to pathToLargeNecklaces("f.svg")
    )


    val allSmallNecklaces: List<Pair<String, String>> = listOf(
        "A" to pathToSmallNecklaces("a.svg"),
        "B" to pathToSmallNecklaces("b.svg"),
        "C" to pathToSmallNecklaces("c.svg"),
        "D" to pathToSmallNecklaces("d.svg"),
        "E" to pathToSmallNecklaces("e.svg"),
        "F" to pathToSmallNecklaces("f.svg")
    )

    fun getLargeNecklaceMetadata(necklaceName: String): Pair<String, String> {
        val filename = "${necklaceName.lowercase()}.svg"
        return necklaceName to pathToLargeNecklaces(filename)
    }

    fun getSmallNecklaceMetadata(necklaceName: String): Pair<String, String> {
        val filename = "${necklaceName.lowercase()}.svg"
        return necklaceName to pathToSmallNecklaces(filename)
    }

    private fun pathToLargeNecklaces(imageFilename: String): String {
        return LARGE_NECKLACES_PATH + imageFilename
    }

    private fun pathToSmallNecklaces(imageFilename: String): String {
        return SMALL_NECKLACES_PATH + imageFilename
    }

}
