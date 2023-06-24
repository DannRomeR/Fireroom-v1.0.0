package com.example.fireroomv100.model.service

import com.example.fireroomv100.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>

    suspend fun authenticate()
    suspend fun authenticate(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}