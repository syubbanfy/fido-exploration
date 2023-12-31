package com.digiventure.fido_exploration.model

data class RegisterCredentialBody (
    val id: String,
    val type: String,
    val rawId: String,
    val response: Response
)

data class Response (
    val clientDataJSON: String,
    val attestationObject: String
)