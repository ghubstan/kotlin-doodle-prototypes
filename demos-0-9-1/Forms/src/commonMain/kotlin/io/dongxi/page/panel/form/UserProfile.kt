package io.dongxi.page.panel.form

import kotlinx.serialization.Serializable

/**
 * Data representing authentication credentials in the form of username and password.
 */
@Serializable
data class UserProfile(val name: String, val file: String) {
    fun isValid(): Boolean {
        return true
    }
}
