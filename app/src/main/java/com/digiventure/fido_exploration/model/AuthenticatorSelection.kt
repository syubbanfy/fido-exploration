package com.digiventure.fido_exploration.model

data class AuthenticatorSelection (
    val authenticatorAttachment: String? = null,
    val userVerification: String
)