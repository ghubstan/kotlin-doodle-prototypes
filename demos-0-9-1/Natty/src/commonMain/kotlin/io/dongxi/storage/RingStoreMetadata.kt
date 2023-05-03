package io.dongxi.storage

object RingStoreMetadata {

    private const val SMALL_RINGS_PATH = "assets/images/rings/mock/small/"
    private const val LARGE_RINGS_PATH = "assets/images/rings/mock/medium/"

    val allSmallRings: List<Pair<String, String>> = listOf(
        "A" to pathToSmallRing("a.png"),
        "B" to pathToSmallRing("b.png"),
        "C" to pathToSmallRing("c.png"),
        "D" to pathToSmallRing("d.png"),
        "E" to pathToSmallRing("e.png"),
        "F" to pathToSmallRing("f.png")
    ).sortedBy { it.first }

    fun getSmallRingMetadata(ringName: String): Pair<String, String> {
        val filename = "${ringName.lowercase()}.png"
        return ringName to pathToSmallRing(filename)
    }

    fun getLargeRingMetadata(ringName: String): Pair<String, String> {
        val filename = "${ringName.lowercase()}.png"
        return ringName to pathToLargeRing(filename)
    }

    private fun pathToSmallRing(imageFilename: String): String {
        return SMALL_RINGS_PATH + imageFilename
    }

    private fun pathToLargeRing(imageFilename: String): String {
        return LARGE_RINGS_PATH + imageFilename
    }
}