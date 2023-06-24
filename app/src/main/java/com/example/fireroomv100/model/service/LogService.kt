package com.example.fireroomv100.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}