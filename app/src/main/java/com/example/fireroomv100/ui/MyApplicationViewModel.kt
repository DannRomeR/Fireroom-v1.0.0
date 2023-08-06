package com.example.fireroomv100.ui

import androidx.lifecycle.ViewModel
import com.example.fireroomv100.model.service.LogService


open class MyApplicationViewModel(
    private val logService: LogService
) : ViewModel()