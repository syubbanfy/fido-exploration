package com.digiventure.fido_exploration.model

data class GlitchCredentialBody (
    val attestation: String,
    val authenticatorSelection: AuthenticatorSelection
)
