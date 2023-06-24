package com.example.fireroomv100.ui

import com.example.fireroomv100.model.service.AccountService
import com.example.fireroomv100.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : MyApplicationViewModel(logService)