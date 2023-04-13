package io.dongxi.storage

import io.dongxi.model.Ring
import kotlin.random.Random


object RingStoneStoreMetadata {

    private const val STONES_PATH = "stones/mock/"

    private val AVAILABLE_STONES: List<String> = listOf(
        pathTo("cyan.png"),
        pathTo("dark-blue.png"),
        pathTo("dark-coral.png"),
        pathTo("dark-orchid.png"),
        pathTo("deep-jungle-green.png"),
        pathTo("dogwood-rose.png"),
        pathTo("emerald.png"),
        pathTo("green.png"),
        pathTo("harvest-gold.png"),
        pathTo("indigo.png"),
        pathTo("lava.png"),
        pathTo("light-sky-blue.png"),
        pathTo("magenta.png"),
        pathTo("medium-persian-blue.png"),
        pathTo("midnight-blue.png"),
        pathTo("violet.png"),
        pathTo("yellow.png")
    )

    fun stoneImagePathForColorName(colorName: String): String {
        return stonesByColorName[colorName]!!
    }

    private val stonesByColorName = mapOf(
        "Cyan" to pathTo("cyan.png"),
        "Dark Blue" to pathTo("dark-blue.png"),
        "Dark Coral" to pathTo("dark-coral.png"),
        "Dark Orchid" to pathTo("dark-orchid.png"),
        "Deep Jungle Green" to pathTo("deep-jungle-green.png"),
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
        "Midnight Blue" to pathTo("midnight-blue.png")
    )

    private val ringBStones: List<Pair<String, String>> = listOf(
        "Deep Jungle Green" to pathTo("deep-jungle-green.png"),
        "Dogwood Rose" to pathTo("dogwood-rose.png"),
        "Emerald" to pathTo("emerald.png"),
        "Green" to pathTo("green.png"),
        "Harvest Gold" to pathTo("harvest-gold.png"),
        "Indigo" to pathTo("indigo.png"),
        "Lava" to pathTo("lava.png")
    )

    private val ringCStones: List<Pair<String, String>> = listOf(
        "Lava" to pathTo("lava.png"),
        "Magenta" to pathTo("magenta.png"),
        "Medium Persian Blue" to pathTo("medium-persian-blue.png"),
        "Midnight Blue" to pathTo("midnight-blue.png"),
        "Violet" to pathTo("violet.png"),
        "Yellow" to pathTo("yellow.png")
    )

    private val ringDStones: List<Pair<String, String>> = listOf(
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
        "Violet" to pathTo("violet.png"),
        "Yellow" to pathTo("yellow.png")
    )

    private val ringEStones: List<Pair<String, String>> = listOf(
        "Dark Coral" to pathTo("dark-coral.png"),
        "Dark Orchid" to pathTo("dark-orchid.png"),
        "Deep Jungle Green" to pathTo("deep-jungle-green.png"),
        "Dogwood Rose" to pathTo("dogwood-rose.png"),
        "Emerald" to pathTo("emerald.png"),
        "Green" to pathTo("green.png"),
        "Magenta" to pathTo("magenta.png"),
        "Medium Persian Blue" to pathTo("medium-persian-blue.png"),
        "Midnight Blue" to pathTo("midnight-blue.png"),
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
        "Violet" to pathTo("violet.png"),
        "Yellow" to pathTo("yellow.png")
    )


    private fun pathTo(imageFilename: String): String {
        return STONES_PATH + imageFilename
    }

    private fun getStones(ring: Ring): List<String> {
        val randomIndex = Random.nextInt(1, AVAILABLE_STONES.size - 1)
        return AVAILABLE_STONES.subList(randomIndex, AVAILABLE_STONES.size - 1)
    }
}
