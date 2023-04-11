package io.dongxi.util

object ClassUtils {

    fun simpleClassName(obj: Any): String {
        return obj::class.simpleName.toString()
    }
}
