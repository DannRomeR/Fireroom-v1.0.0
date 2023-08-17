package com.example.fireroomv100.ui.authenticate

import com.example.fireroomv100.model.service.AccountService
import com.example.fireroomv100.model.service.LogService
import com.example.fireroomv100.ui.MyApplicationViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
) : MyApplicationViewModel(logService) {

    var navigator:Navigator?=null

    fun isUserExists() = accountService.hasUser
    fun getCurrentUser()=accountService.currentUser

//    private var uiState = LoginUiState()
//    fun onEmailChange(email: String) {
//        uiState = uiState.copy(email = email)
//    }
//    fun onPasswordChange(password: String) {
//        uiState = uiState.copy(password = password)
//    }

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