package com.example.fireroomv100.model.service

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
//    val currentUser: Flow<User>
    fun authenticateWithEmail(email: String, password: String)
    fun authenticateWithCredential(token: String)
    fun linkAccount(email: String, password: String)
    fun deleteAccount()
    fun signOut()
}