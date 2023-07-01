package com.example.fireroomv100.model.service.impl

import com.example.fireroomv100.model.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {
    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null

    override  fun authenticateWithCredential(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
    }

    override fun authenticateWithEmail(email: String, password: String) {
        TODO("Not yet implemented")
    }
    override fun linkAccount(email: String, password: String) {}

    override  fun deleteAccount() {
        auth.currentUser!!.delete()
    }

    override  fun signOut() {
        if (auth.currentUser == null) return
        auth.currentUser!!.delete()
        auth.signOut()
    }
}