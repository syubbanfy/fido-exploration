package com.digiventure.fido_exploration.pages.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.digiventure.fido_exploration.repository.AuthRepository
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    suspend fun calculate() {
        Log.d("tes", authRepository.registerRequest(""))
    }
}