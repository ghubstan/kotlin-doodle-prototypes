package io.dongxi.util

import io.dongxi.model.IProduct
import io.dongxi.model.IProductAccessory

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

    fun capitalizeWord(s: String): String {
        return capitalize(s.lowercase())
    }

    fun productLabelText(product: IProduct): String {
        return "${StringUtils.capitalizeWord(product.productCategory.name)}: ${product.name}"
    }

    fun accessoryLabelText(accessory: IProductAccessory): String {
        return "${StringUtils.capitalizeWord(accessory.accessoryCategory.name)}: ${accessory.name}"
    }
}
