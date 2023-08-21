package com.example.fireroomv100.model.service

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser:FirebaseUser
//    val currentUser: Flow<User>
    fun authenticateWithEmail(email: String, password: String)
    /**
     * Last modification by Daniel Mendoza
     * @date July 19th
     * @param authCredential: The Authorization credential obtained from the Account Provider
     * @return Task<AuthResult>: The asynchronous Task which will indicate whether the auth operation succeed or fails
     **/
    fun authenticateWithCredential(authCredential: AuthCredential): Task<AuthResult>
    fun linkAccount(email: String, password: String)
    fun deleteAccount()
    fun signOut()
    fun getPhoneOptions(
        activity: AppCompatActivity,
        phone: String,
        callbacks: OnVerificationStateChangedCallbacks
    )
}