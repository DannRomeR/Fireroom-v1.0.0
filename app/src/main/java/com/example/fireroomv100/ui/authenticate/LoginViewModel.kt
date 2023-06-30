package com.example.fireroomv100.ui.authenticate

import com.example.fireroomv100.model.service.AccountService
import com.example.fireroomv100.model.service.LogService
import com.example.fireroomv100.ui.MyApplicationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : MyApplicationViewModel(logService) {

    fun isUserExists() = accountService.hasUser

//    private var uiState = LoginUiState()
//    fun onEmailChange(email: String) {
//        uiState = uiState.copy(email = email)
//    }
//    fun onPasswordChange(password: String) {
//        uiState = uiState.copy(password = password)
//    }
    suspend fun authenticateWithGoogle(idToken: String) {
         accountService.authenticateWithCredential(idToken)
    }
}