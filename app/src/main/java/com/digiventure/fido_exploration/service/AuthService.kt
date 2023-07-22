package com.digiventure.fido_exploration.service

import com.digiventure.fido_exploration.api.AuthAPI
import javax.inject.Inject

class AuthService @Inject constructor(
    private val authAPI: AuthAPI
) {
    suspend fun registerRequest(sessionId: String): String {
        return "yeay"
    }
}