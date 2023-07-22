package com.digiventure.fido_exploration.repository

import com.digiventure.fido_exploration.service.AuthService
import com.google.android.gms.fido.fido2.Fido2ApiClient
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val service: AuthService,
    private val fido2ApiClient: Fido2ApiClient
) {
    private companion object {
        const val TAG = "AuthRepository"
    }

    suspend fun registerRequest(sessionId: String): String {
        return service.registerRequest(sessionId)
    }
}