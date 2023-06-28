package com.example.fireroomv100.model.service

import com.example.fireroomv100.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>
    suspend fun authenticateWithEmail(email: String, password: String)
    suspend fun authenticateWithCredential(token: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}