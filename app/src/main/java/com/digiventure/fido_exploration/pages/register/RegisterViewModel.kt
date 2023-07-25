package com.digiventure.fido_exploration.pages.register

import android.app.PendingIntent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digiventure.fido_exploration.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val loader = MutableLiveData<Boolean>()

    suspend fun register(token: String, apiKeyHash: String): Result<PendingIntent> =
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
}