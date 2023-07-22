package com.digiventure.fido_exploration.pages.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.digiventure.fido_exploration.repository.AuthRepository
import javax.inject.Inject

class RegisterViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}