package io.dongxi.storage

object NecklaceStoreMetadata {

    private const val LARGE_NECKLACES_PATH = "assets/images/necklaces/mock/large/"
    private const val SMALL_NECKLACES_PATH = "assets/images/necklaces/mock/small/"

    val allSmallNecklaces: List<Pair<String, String>> = listOf(
        "A" to pathToSmallNecklaces("a.svg"),
        "B" to pathToSmallNecklaces("b.svg"),
        "C" to pathToSmallNecklaces("c.svg"),
        "D" to pathToSmallNecklaces("d.svg"),
        "E" to pathToSmallNecklaces("e.svg"),
        "F" to pathToSmallNecklaces("f.svg")
    ).sortedBy { it.first }

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
