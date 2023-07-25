package com.digiventure.fido_exploration.pages.register

import android.app.PendingIntent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digiventure.fido_exploration.model.RegisterCredentialBody
import com.digiventure.fido_exploration.model.RegisterCredentialResponse
import com.digiventure.fido_exploration.model.Response
import com.digiventure.fido_exploration.repository.AuthRepository
import com.google.android.gms.fido.fido2.api.common.AuthenticatorAttestationResponse
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredential
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialType
import com.google.android.gms.identity.sample.fido2.toBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val loader = MutableLiveData<Boolean>()

    suspend fun generateCredential(token: String, apiKeyHash: String): Result<PendingIntent> =
        withContext(Dispatchers.IO) {
            loader.postValue(true)
            try {
                authRepository.registerRequest(token, apiKeyHash).onEach {
                    loader.postValue(false)
                }.last()
            } catch (e: Exception) {
                loader.postValue(false)
                Result.failure(e)
            }
        }

    suspend fun registerCredential(
        token: String, apiKeyHash: String,
        body: PublicKeyCredential
    ): Result<RegisterCredentialResponse> = withContext(Dispatchers.IO) {
        loader.postValue(true)
        try {
            val rawId = body.rawId.toBase64()
            val response = body.response as AuthenticatorAttestationResponse

            val requestBody = RegisterCredentialBody(
                id = rawId,
                rawID = rawId,
                type = PublicKeyCredentialType.PUBLIC_KEY.toString(),
                response = Response(
                    clientDataJSON = response.clientDataJSON.toBase64(),
                    attestationObject = response.attestationObject.toBase64(),
                )
            )

            authRepository.registerResponse(token, apiKeyHash, requestBody).onEach {
                loader.postValue(false)
            }.last()
        } catch (e: Exception) {
            loader.postValue(false)
            Result.failure(e)
        }
    }
}