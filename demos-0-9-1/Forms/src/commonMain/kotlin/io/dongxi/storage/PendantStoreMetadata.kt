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
        "Amethyst" to pathTo("amethyst.png"),
        "Beryl" to pathTo("beryl.png"),
        "Bloodstone" to pathTo("bloodstone.png"),
        "Carnelian" to pathTo("carnelian.png"),
        "Bloodstone" to pathTo("bloodstone.png"),
        "Earth" to pathTo("earth.svg"),
        "Emerald" to pathTo("emerald.png"),
        "Fleur de Lis" to pathTo("fleur-de-lis.svg"),
        "Green Diamond" to pathTo("green-diamond.svg"),
        "Jewel Beetle" to pathTo("jewel-beetle.svg"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Rat King" to pathTo("rat-king.svg"),
        "Sapphire" to pathTo("sapphire.png"),
        "Snake" to pathTo("snake.svg"),
        "Topaz" to pathTo("topaz.png")
    ).sortedBy { it.first }

    private val necklaceBPendants: List<Pair<String, String>> = listOf(
        "Carnelian" to pathTo("carnelian.png"),
        "Earth" to pathTo("earth.svg"),
        "Emerald" to pathTo("emerald.png"),
        "Garnet" to pathTo("garnet.png"),
        "Onyx" to pathTo("onyx.png"),
        "Ruby" to pathTo("ruby.png"),
        "Sapphire" to pathTo("sapphire.png")
    ).sortedBy { it.first }

    private val necklaceCPendants: List<Pair<String, String>> = listOf(
        "Agate" to pathTo("agate.png"),
        "Carnelian" to pathTo("carnelian.png"),
        "Emerald" to pathTo("emerald.png"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png")
    ).sortedBy { it.first }

    private val necklaceDPendants: List<Pair<String, String>> = listOf(
        "Beryl" to pathTo("beryl.png"),
        "Bloodstone" to pathTo("bloodstone.png"),
        "Emerald" to pathTo("emerald.png"),
        "Fleur de Lis" to pathTo("fleur-de-lis.svg"),
        "Garnet" to pathTo("garnet.png"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Ruby" to pathTo("ruby.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png")
    ).sortedBy { it.first }

    private val necklaceEPendants: List<Pair<String, String>> = listOf(
        "Bloodstone" to pathTo("bloodstone.png"),
        "Emerald" to pathTo("emerald.png"),
        "Garnet" to pathTo("garnet.png"),
        "Jewel Beetle" to pathTo("jewel-beetle.svg"),
        "Onyx" to pathTo("onyx.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png")
    ).sortedBy { it.first }

    private val necklaceFPendants: List<Pair<String, String>> = listOf(
        "Bloodstone" to pathTo("bloodstone.png"),
        "Flamengo" to pathTo("flamengo.svg"),
        "Fleur de Lis" to pathTo("fleur-de-lis.svg"),
        "Green Diamond" to pathTo("green-diamond.svg"),
        "Orange Diamond" to pathTo("orange-diamond.svg"),
        "Jewel Beetle" to pathTo("jewel-beetle.svg"),
        "Bozo of Hate" to pathTo("the-bozo.svg"),
        "Buddhist Peace" to pathTo("buddhist-swastika.svg"),
        "Earth" to pathTo("earth.svg"),
        "Red Cross" to pathTo("red-cross.svg"),
        "Jesus" to pathTo("black-cross.svg"),
        "Another Jesus" to pathTo("brown-cross.svg"),
        "Rat King" to pathTo("rat-king.svg"),
        "Russian Jesus" to pathTo("russian-orthodox-cross.svg"),
        "Satan" to pathTo("satan.svg"),
        "Snake" to pathTo("snake.svg"),
        "Tiger" to pathTo("tiger.svg")

        // Problems:
        //  "Red Chrystal" to pathTo("red-chrystal.svg"),       // Can't size properly to fit center panel.
        //  "Yinyang" to pathTo("yinyang.png")                  // Background not transparent.
        //  "Lotus" to pathTo("lotus.png"),                    // Background not transparent.
        //  blue-diamond.svg  can't re-size
        //  nassak-diamond.svg  can't re-size
    ).sortedBy { it.first }

    private fun pathTo(imageFilename: String): String {
        return PENDANTS_PATH + imageFilename
    }

}