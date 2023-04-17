package io.dongxi.storage


object RingStoneStoreMetadata {

    private const val STONES_PATH = "stones/mock/"

    // Unused, but do not delete.
    // TODO "List" all files in directory, and load the map instead of like this, by hand.
    private val stonesByColorName = mapOf(
        "Agate" to pathTo("agate.png"),
        "Amethyst" to pathTo("amethyst.png"),
        "Beryl" to pathTo("beryl.png"),
        "Bloodstone" to pathTo("bloodstone.png"),
        "Carnelian" to pathTo("carnelian.png"),
        "Cyan" to pathTo("cyan.png"),
        "Dark Blue" to pathTo("dark-blue.png"),
        "Dark Coral" to pathTo("dark-coral.png"),
        "Dark Orchid" to pathTo("dark-orchid.png"),
        "Deep Jungle Green" to pathTo("deep-jungle-green.png"),
        "Dogwood Rose" to pathTo("dogwood-rose.png"),
        "Emerald" to pathTo("emerald.png"),
        "Garnet" to pathTo("garnet.png"),
        "Green" to pathTo("green.png"),
        "Harvest Gold" to pathTo("harvest-gold.png"),
        "Indigo" to pathTo("indigo.png"),
        "Lava" to pathTo("lava.png"),
        "Light Sky Blue" to pathTo("light-sky-blue.png"),
        "Magenta" to pathTo("magenta.png"),
        "Medium Persian Blue" to pathTo("medium-persian-blue.png"),
        "Midnight Blue" to pathTo("midnight-blue.png"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Ruby" to pathTo("ruby.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png"),
        "Violet" to pathTo("violet.png"),
        "Yellow" to pathTo("yellow.png")
    )

    fun getStones(ringName: String): List<Pair<String, String>> {
        if (ringName.equals("A", ignoreCase = true)) {
            return ringAStones
        } else if (ringName.equals("B", ignoreCase = true)) {
            return ringBStones
        } else if (ringName.equals("C", ignoreCase = true)) {
            return ringCStones
        } else if (ringName.equals("D", ignoreCase = true)) {
            return ringDStones
        } else if (ringName.equals("E", ignoreCase = true)) {
            return ringEStones
        } else if (ringName.equals("F", ignoreCase = true)) {
            return ringFStones
        } else {
            // TODO throw exception?
            return ringAStones
        }
    }

    // Map each ring (a.png, b.png, etc..) to a map of stonesByColorName.

    private val ringAStones: List<Pair<String, String>> = listOf(
        "Amethyst" to pathTo("amethyst.png"),
        "Cyan" to pathTo("cyan.png"),
        "Dark Blue" to pathTo("dark-blue.png"),
        "Dark Coral" to pathTo("dark-coral.png"),
        "Dark Orchid" to pathTo("dark-orchid.png"),
        "Dogwood Rose" to pathTo("dogwood-rose.png"),
        "Emerald" to pathTo("emerald.png"),
        "Green" to pathTo("green.png"),
        "Harvest Gold" to pathTo("harvest-gold.png"),
        "Indigo" to pathTo("indigo.png"),
        "Lava" to pathTo("lava.png"),
        "Light Sky Blue" to pathTo("light-sky-blue.png"),
        "Magenta" to pathTo("magenta.png"),
        "Medium Persian Blue" to pathTo("medium-persian-blue.png"),
        "Midnight Blue" to pathTo("midnight-blue.png"),
        "Onyx" to pathTo("onyx.png"),
        "Sapphire" to pathTo("sapphire.png")
    )

    private val ringBStones: List<Pair<String, String>> = listOf(
        "Beryl" to pathTo("beryl.png"),
        "Deep Jungle Green" to pathTo("deep-jungle-green.png"),
        "Dogwood Rose" to pathTo("dogwood-rose.png"),
        "Emerald" to pathTo("emerald.png"),
        "Green" to pathTo("green.png"),
        "Harvest Gold" to pathTo("harvest-gold.png"),
        "Indigo" to pathTo("indigo.png"),
        "Lava" to pathTo("lava.png"),
        "Sapphire" to pathTo("sapphire.png")
    )

    private val ringCStones: List<Pair<String, String>> = listOf(
        "Amethyst" to pathTo("amethyst.png"),
        "Garnet" to pathTo("garnet.png"),
        "Lava" to pathTo("lava.png"),
        "Magenta" to pathTo("magenta.png"),
        "Medium Persian Blue" to pathTo("medium-persian-blue.png"),
        "Midnight Blue" to pathTo("midnight-blue.png"),
        "Onyx" to pathTo("onyx.png"),
        "Peridot" to pathTo("peridot.png"),
        "Ruby" to pathTo("ruby.png"),
        "Violet" to pathTo("violet.png"),
        "Yellow" to pathTo("yellow.png"),
        "Sapphire" to pathTo("sapphire.png"),
        "Topaz" to pathTo("topaz.png")
    )

    private val ringDStones: List<Pair<String, String>> = listOf(
        "Beryl" to pathTo("beryl.png"),
        "Bloodstone" to pathTo("bloodstone.png"),
        "Carnelian" to pathTo("carnelian.png"),
        "Dark Blue" to pathTo("dark-blue.png"),
        "Dogwood Rose" to pathTo("dogwood-rose.png"),
        "Emerald" to pathTo("emerald.png"),
        "Green" to pathTo("green.png"),
        "Harvest Gold" to pathTo("harvest-gold.png"),
        "Indigo" to pathTo("indigo.png"),
        "Light Sky Blue" to pathTo("light-sky-blue.png"),
        "Magenta" to pathTo("magenta.png"),
        "Medium Persian Blue" to pathTo("medium-persian-blue.png"),
        "Midnight Blue" to pathTo("midnight-blue.png"),
        "Onyx" to pathTo("onyx.png"),
        "Topaz" to pathTo("topaz.png"),
        "Violet" to pathTo("violet.png"),
        "Yellow" to pathTo("yellow.png")
    )

    private val ringEStones: List<Pair<String, String>> = listOf(
        "Amethyst" to pathTo("amethyst.png"),
        "Dark Coral" to pathTo("dark-coral.png"),
        "Dark Orchid" to pathTo("dark-orchid.png"),
        "Deep Jungle Green" to pathTo("deep-jungle-green.png"),
        "Dogwood Rose" to pathTo("dogwood-rose.png"),
        "Emerald" to pathTo("emerald.png"),
        "Green" to pathTo("green.png"),
        "Magenta" to pathTo("magenta.png"),
        "Medium Persian Blue" to pathTo("medium-persian-blue.png"),
        "Midnight Blue" to pathTo("midnight-blue.png"),
        "Topaz" to pathTo("topaz.png"),
        "Violet" to pathTo("violet.png"),
        "Yellow" to pathTo("yellow.png")
    )

    private val ringFStones: List<Pair<String, String>> = listOf(
        "Emerald" to pathTo("emerald.png"),
        "Green" to pathTo("green.png"),
        "Harvest Gold" to pathTo("harvest-gold.png"),
        "Indigo" to pathTo("indigo.png"),
        "Light Sky Blue" to pathTo("light-sky-blue.png"),
        "Magenta" to pathTo("magenta.png"),
        "Medium Persian Blue" to pathTo("medium-persian-blue.png"),
        "Midnight Blue" to pathTo("midnight-blue.png"),
        "Onyx" to pathTo("onyx.png"),
        "Topaz" to pathTo("topaz.png"),
        "Violet" to pathTo("violet.png"),
        "Yellow" to pathTo("yellow.png")
    )


    private fun pathTo(imageFilename: String): String {
        return STONES_PATH + imageFilename
    }
}
