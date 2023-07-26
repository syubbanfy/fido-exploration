package com.digiventure.fido_exploration.repository

import android.app.PendingIntent
import com.digiventure.fido_exploration.model.RegisterCredentialBody
import com.digiventure.fido_exploration.model.RegisterCredentialResponse
import com.digiventure.fido_exploration.service.AuthService
import com.digiventure.fido_exploration.util.parsePublicKeyCredentialCreationOptions
import com.google.android.gms.fido.fido2.Fido2ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepository @Inject constructor(
    private val service: AuthService,
    private val fido2ApiClient: Fido2ApiClient
) {
    private companion object {
        const val TAG = "AuthRepository"
    }

    suspend fun registerRequest(token: String, apiKeyHash: String): Flow<Result<PendingIntent>> =
        service.registerRequest(token, apiKeyHash).map {
            if (it.isSuccess) {
                val parsedCredential = it.getOrNull()
                    ?.let { it1 -> parsePublicKeyCredentialCreationOptions(it1) }

                val pendingIntent = suspendCoroutine<PendingIntent> { continuation ->
                    parsedCredential?.let { it1 ->
                        fido2ApiClient.getRegisterPendingIntent(
                            it1
                        )
                    }?.addOnCompleteListener { completedTask ->
                        if (completedTask.isSuccessful) {
                            continuation.resume(completedTask.result)
                        } else {
                            continuation.resumeWithException(Exception("loading"))
                        }
                    }
                }

                Result.success(pendingIntent)
            } else
                Result.failure(it.exceptionOrNull()!!)
        }

    suspend fun registerResponse(
        token: String,
        apiKeyHash: String,
        body: RegisterCredentialBody
    ): Flow<Result<RegisterCredentialResponse>> = service.registerResponse(token, apiKeyHash, body).map {
        if (it.isSuccess) {
            Result.success(it.getOrThrow())
        } else {
            Result.failure(it.exceptionOrNull() ?: Exception("Something went wrong"))
        }
    }
}