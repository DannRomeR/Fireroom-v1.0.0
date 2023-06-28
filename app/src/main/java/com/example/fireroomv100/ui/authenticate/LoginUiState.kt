package com.example.fireroomv100.ui.authenticate

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val googleLogin: Boolean = false,
    val phoneLogin: Boolean = false
)
