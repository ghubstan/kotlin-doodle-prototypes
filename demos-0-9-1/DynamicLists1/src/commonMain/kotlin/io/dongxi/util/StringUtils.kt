package io.dongxi.util

object StringUtils {

    fun getFilename(path: String): String {
        val parts: List<String> = path.split("/")
        return parts[parts.size - 1]
    }

    fun getFilenameBase(path: String): String {
        val filename = getFilename(path)
        val parts: List<String> = filename.split(".")
        return parts[0]
    }

    fun capitalize(s: String): String {
        return s.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
