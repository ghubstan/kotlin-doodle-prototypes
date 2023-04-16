package io.dongxi.storage

object PendantStoreMetadata {

    private const val PENDANTS_PATH = "pendants/mock/"

    fun getPendants(necklaceName: String): List<Pair<String, String>> {
        if (necklaceName.equals("A", ignoreCase = true)) {
            return necklaceAPendants
        } else if (necklaceName.equals("B", ignoreCase = true)) {
            return necklaceBPendants
        } else if (necklaceName.equals("C", ignoreCase = true)) {
            return necklaceCPendants
        } else if (necklaceName.equals("D", ignoreCase = true)) {
            return necklaceDPendants
        } else if (necklaceName.equals("E", ignoreCase = true)) {
            return necklaceEPendants
        } else if (necklaceName.equals("F", ignoreCase = true)) {
            return necklaceFPendants
        } else {
            // TODO throw exception?
            return necklaceAPendants
        }
    }

    private val necklaceAPendants: List<Pair<String, String>> = listOf(
        "Agate" to pathTo("agate.png"),
        "Amethyst" to pathTo("amethyst.png"),
        "Beryl" to pathTo("beryl.png"),
        "Carnelian" to pathTo("carnelian.png"),
        "Bloodstone" to pathTo("bloodstone.png"),
        "Emerald" to pathTo("emerald.png"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png")
    )

    private val necklaceBPendants: List<Pair<String, String>> = listOf(
        "Agate" to pathTo("agate.png"),
        "Carnelian" to pathTo("carnelian.png"),
        "Emerald" to pathTo("emerald.png"),
        "Garnet" to pathTo("garnet.png"),
        "Onyx" to pathTo("onyx.png"),
        "Ruby" to pathTo("ruby.png"),
        "Sapphire" to pathTo("sapphire.png")
    )

    private val necklaceCPendants: List<Pair<String, String>> = listOf(
        "Agate" to pathTo("agate.png"),
        "Carnelian" to pathTo("carnelian.png"),
        "Emerald" to pathTo("emerald.png"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png")
    )

    private val necklaceDPendants: List<Pair<String, String>> = listOf(
        "Agate" to pathTo("agate.png"),
        "Beryl" to pathTo("beryl.png"),
        "Bloodstone" to pathTo("bloodstone.png"),
        "Emerald" to pathTo("emerald.png"),
        "Garnet" to pathTo("garnet.png"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Ruby" to pathTo("ruby.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png")
    )

    private val necklaceEPendants: List<Pair<String, String>> = listOf(
        "Agate" to pathTo("agate.png"),
        "Emerald" to pathTo("emerald.png"),
        "Garnet" to pathTo("garnet.png"),
        "Onyx" to pathTo("onyx.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png")
    )

    private val necklaceFPendants: List<Pair<String, String>> = listOf(
        "Agate" to pathTo("agate.png"),
        "Beryl" to pathTo("beryl.png"),
        "Bloodstone" to pathTo("bloodstone.png"),
        "Carnelian" to pathTo("carnelian.png"),
        "Emerald" to pathTo("emerald.png"),
        "Garnet" to pathTo("garnet.png"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Ruby" to pathTo("ruby.png"),
        "Sapphire" to pathTo("sapphire.png")
    )

    private fun pathTo(imageFilename: String): String {
        return PENDANTS_PATH + imageFilename
    }

}