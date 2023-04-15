package io.dongxi.util

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object SVGUtils {

    // The variations of the replace extension function in Koltin
    // will return a new string instead of changing it in place.
    // This is because strings are immutable in Kotlin.

    private const val TEXT_PLACE_HOLDER = "[TEXT]"

    private const val PAGE_NAME_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<svg version=\"1.1\"\n" +
            "     width=\"85\" height=\"30\"\n" +
            "     xmlns=\"http://www.w3.org/2000/svg\">\n" +
            "    <rect width=\"100%\" height=\"100%\" fill=\"transparent\"/>\n" +
            "    <text x=\"42\" y=\"23\"\n" +
            "          font-family=\"Helvetica, sans-serif\" font-size=\"20\" text-anchor=\"middle\"\n" +
            "          fill=\"#000000\">\n" +
            "        " + TEXT_PLACE_HOLDER + "\n" +
            "    </text>\n" +
            "</svg>\n"

    fun genPageNameSVG(pageName: String): String {
        val template = PAGE_NAME_TEMPLATE
        val asciiSvg = template.replace(TEXT_PLACE_HOLDER, pageName)
        return ecodeBase64(asciiSvg)
    }

    // TODO This does work because it results in attempted GET from server, i.e., URL = base64-string
    private fun ecodeBase64(asciiSvg: String): String {
        // return  = Base64.getEncoder().encodeToString(asciiSvg.toByteArray())
        val byteArray = asciiSvg.encodeToByteArray()
        @OptIn(ExperimentalEncodingApi::class)
        return Base64.encode(byteArray)
    }
}

