package com.digiventure.fido_exploration.service

import com.digiventure.fido_exploration.api.AuthAPI
import com.digiventure.fido_exploration.model.Option
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

    suspend fun registerRequest(token: String): Flow<Result<Option>> =
        flow {
            emit(Result.success(authAPI.getOption(token)))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
}