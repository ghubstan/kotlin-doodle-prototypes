package io.dongxi.page.panel.form

import kotlinx.serialization.Serializable

/**
 * Data representing a persisted user profile.
 *
 * TODO This is not to be used until registration functions are implemented, and registrations are persisted.
 */
@Serializable
data class UserProfile(

    // Field label: Nome Completo*
    // Error Msg: É necessário informar seu nome completo.
    val fullName: String,

    // Field label: CPF*
    // Error Msg: É necessário informar um CPF.
    val cpf: String,            // ___.___.___-__   -> DDD.DDD.DDD-DD

    // Field label: Data de Nascimento
    // Error Msg: É necessário informar sua data de nascimento completa, no formato DD/MM/AAAA.
    val birthDate: String,      // __/__/____       -> DD/MM/AAAA

    // Field label: Telefone Celular*
    // Error Msg: É necessário informar o número completo no formato (DDD) 99999-9999.
    val cellPhone: String,      // (__) _ ____-____

    // Field label: E-mail*
    // Error Msg: É necessário informar um E-mail válido.
    // This is the username used in the LoginForm.
    val email: String,

    // Field label: Crie Sua Senha*
    // Error Msg: É necessário informar uma senha. (TODO how many chars, a #, a capital letter, a control char)
    val password: String,

    // Field label: Confirme Sua Senha*
    // Error Msg: Password confirmation does not match original. (TODO translate to BR_pt)
    val passwordConfirm: String
) {
    fun isValid(): Boolean {
        return true
    }
}
