package com.digiventure.fido_exploration.repository

import android.app.PendingIntent
import android.util.Log
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

    suspend fun registerRequest(token: String): Flow<Result<PendingIntent>> = service.registerRequest(token).map {
        if (it.isSuccess) {
            val parsedCredential = it.getOrNull()
                ?.let { it1 -> parsePublicKeyCredentialCreationOptions(it1) }

            val pendingIntent = suspendCoroutine<PendingIntent> { continuation ->
                val task = fido2ApiClient.getRegisterPendingIntent(parsedCredential!!)
                task.addOnCompleteListener { completedTask ->
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
}