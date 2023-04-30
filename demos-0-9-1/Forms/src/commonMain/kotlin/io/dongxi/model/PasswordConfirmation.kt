package io.dongxi.model

import kotlinx.serialization.Serializable

/**
 * Data representing a candidate password with matching confirmation password.
 */
@Serializable
data class PasswordConfirmation(val password: String, val confirmPassword: String) {

    private var errorMessage: String? = null

    fun isMatch(): Boolean {
        return password.isNotEmpty() && confirmPassword == password
    }

    fun isValid(): Boolean {
        // TODO Check for required character mix.
        return true
    }

    fun getErrorMessage(): String {
        return errorMessage ?: ""
    }
}
