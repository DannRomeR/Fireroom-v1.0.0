package com.example.fireroomv100.ui.authenticate

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fireroomv100.model.service.AccountService
import com.example.fireroomv100.model.service.LogService
import com.example.fireroomv100.ui.MyApplicationViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
) : MyApplicationViewModel(logService) {

    private var phoneRefreshToken: PhoneAuthProvider.ForceResendingToken? = null
    private var phoneVerificationId: String? = null
    var navigator:Navigator?=null

    fun isUserExists() = accountService.hasUser
    fun getCurrentUser()=accountService.currentUser

    /**
     * Last modification by Daniel Mendoza
     * @date July 19th
     * The credential should be created here as Account Service is a General implementation
     * */
    fun authenticateWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
         accountService.authenticateWithCredential(credential)
             .addOnSuccessListener {
                 onLoginSuccessful(it)
             }
             .addOnFailureListener{
                 onLoginFailed(it)

             }
    }

    /**
     * @author Daniel Mendoza
     * @param authResult: The result from the Auth operation
     * Notifies the Navigator callback when the authentication succeeds
     * */
    private fun onLoginSuccessful(authResult: AuthResult){
        val user=authResult.user
        if(user!=null)navigator?.navigateToMain(user)
    }
    /**
     * @author Daniel Mendoza
     * @date July 19th
     * @param exception: Auth exception
     * Records the error in the logs and notifies the Navigator callback when the authentication fails
     * */
    private fun onLoginFailed(exception:java.lang.Exception){
        val throwable=Throwable(exception)
        logService.logNonFatalCrash(throwable)
        navigator?.onAuthException(exception)
    }

    fun signOut() {
       accountService.signOut()
    }

    fun authenticateWithCredential(credential: PhoneAuthCredential) {
        accountService.authenticateWithCredential(credential)
            .addOnSuccessListener {
                onLoginSuccessful(it)
            }
            .addOnFailureListener{
                onLoginFailed(it)
            }
    }

    fun cancelPhoneAuth(dialog: AlertDialog) {
        phoneRefreshToken = null
        phoneVerificationId = null
        dialog.dismiss()
    }

    fun startPhoneAuthentication(activity: AppCompatActivity, dialog: AlertDialog, number: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                authenticateWithCredential(phoneAuthCredential)
                dialog.dismiss()
            }
            override fun onVerificationFailed(firebaseException: FirebaseException) {
                dialog.dismiss()
            }

            override fun onCodeSent(code: String, token: PhoneAuthProvider.ForceResendingToken) {
                phoneVerificationId = code
                phoneRefreshToken = token
            }
        }
        accountService.getPhoneOptions(activity, number, callbacks)
        dialog.show()
    }

    fun completePhoneAuthentication(
        dialog: AlertDialog,
        code: String
    ) {
        val credential = PhoneAuthProvider.getCredential(phoneVerificationId!!, code)
        authenticateWithCredential(credential)
        dialog.dismiss()
    }

    /**
     * @author Daniel Mendoza
     * @date July 19th
     * Interface to work as callback with the LoginActivity
     * */
    interface Navigator{
        fun navigateToMain(user: FirebaseUser)
        fun onAuthException(loginException:Exception)
//        fun emailSignIn()
    }
}