package io.dongxi.page.panel.form

import kotlinx.serialization.Serializable

/**
 * Data representing authentication credentials in the form of username and password.
 */
@Serializable
data class PasswordConfirmation(val password: String, val confirmPassword: String) {
    fun isMatch(): Boolean {
        return password.isNotEmpty() && confirmPassword == password
    }
}
