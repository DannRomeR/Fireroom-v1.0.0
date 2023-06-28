package com.example.fireroomv100.ui.authenticate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fireroomv100.model.service.AccountService
import com.example.fireroomv100.model.service.LogService
import com.example.fireroomv100.ui.MyApplicationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : MyApplicationViewModel(logService) {

    private var uiState = LoginUiState()
    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email)
    }
    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password)
    }
    suspend fun authenticateWithGoogle(idToken: String) {
        accountService.authenticateWithCredential(idToken)
    }
}