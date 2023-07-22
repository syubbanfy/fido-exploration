package com.digiventure.fido_exploration.model

data class Option (
    val challenge: String,
    val rp: Rp,
    val user: User,
    val pubKeyCredParams: List<PubKeyCredParam>,
    val timeout: Long,
    val attestation: String,
    val excludeCredentials: List<Any?>,
    val authenticatorSelection: AuthenticatorSelection,
    val extensions: Extensions
)

data class AuthenticatorSelection (
    val userVerification: String
)

data class Extensions (
    val credProps: Boolean
)

data class PubKeyCredParam (
    val alg: Long,
    val type: String
)

data class Rp (
    val name: String,
    val id: String
)

data class User (
    val id: String,
    val name: String,
    val displayName: String
)