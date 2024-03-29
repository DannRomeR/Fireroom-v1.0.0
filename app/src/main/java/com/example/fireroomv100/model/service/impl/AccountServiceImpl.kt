package com.example.fireroomv100.model.service.impl

import com.example.fireroomv100.model.service.AccountService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {
    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null

    /**
     * @author Daniel Mendoza
     * @date July 19th
     * simple getter for the current user
     * @throws Exception if the current user is null
     * */
    override val currentUser: FirebaseUser
        get() = auth.currentUser?:throw Exception("User is null")

    override  fun authenticateWithCredential(credential:AuthCredential) :Task<AuthResult>{
        return auth.signInWithCredential(credential)
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