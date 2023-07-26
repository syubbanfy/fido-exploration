package com.digiventure.fido_exploration.service

import com.digiventure.fido_exploration.api.AuthAPI
import com.digiventure.fido_exploration.model.Option
import com.digiventure.fido_exploration.model.RegisterCredentialBody
import com.digiventure.fido_exploration.model.RegisterCredentialResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthService @Inject constructor(
    private val authAPI: AuthAPI
) {
    private companion object {
        const val TAG = "AuthService"
    }

    suspend fun registerRequest(token: String, apiKeyHash: String): Flow<Result<Option>> =
        flow {
            emit(Result.success(authAPI.getOption(token, apiKeyHash)))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }

    suspend fun registerResponse(
        token: String,
        apiKeyHash: String,
        body: RegisterCredentialBody
    ): Flow<Result<RegisterCredentialResponse>> =
        flow {
            emit(Result.success(authAPI.registerCredential(token, apiKeyHash, body)))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
}