package com.example.fireroomv100.ui.authenticate

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fireroomv100.NavigationActivity
import com.example.fireroomv100.R
import com.example.fireroomv100.databinding.LoginBinding
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.example.fireroomv100.databinding.PhoneNumberInputBinding
import com.example.fireroomv100.databinding.VerificationCodeInputBinding



import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(),LoginViewModel.Navigator {
    private lateinit var googleSignInClient: SignInClient
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var loginBinding : LoginBinding
    private lateinit var  auth: FirebaseAuth
    private val googleSignInLauncher = registerForActivityResult( ActivityResultContracts.StartIntentSenderForResult()) { result ->
        Log.d(TAG, "Called GoogleSignInLauncher")
        googleSignInResultHandler(result.data)
    }

    /**
     * Entry point from the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Check whether the user exists before loading the UI to reduce the startup time and CPU usage
        if(viewModel.isUserExists()){
            navigateToMain(viewModel.getCurrentUser())
        }
        viewModel.navigator=this
        auth = Firebase.auth
        loginBinding = LoginBinding.inflate(layoutInflater)
        googleSignInClient = Identity.getSignInClient(this)
        setContentView(loginBinding.root)
        // Adding to the UI the GoogleSignIn Flow
        loginBinding.googleLoginButton.setOnClickListener { googleSignIn() }
        // Adding Phone signing flow
        loginBinding.phoneLoginButton.setOnClickListener { phoneSignIn() }
        // Adding email flow
        loginBinding.emailLoginButton.setOnClickListener { emailSignIn() }

        loginBinding.createAccountOptionTextView.setOnClickListener {
            val intent = Intent( this, EmailCreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    @Deprecated("Directly check the user availability with the the ViewModel and use NavigateToMain instead")
    private fun verifyIfUserExists() {
        if (!viewModel.isUserExists()) {
            Log.d(TAG, viewModel.isUserExists().toString())
            return
        }
        Toast.makeText(this,"User exists, yey!!!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Start the signing flow by building a new request for the Google Signing launcher with our
     * Project credentials.
     * I take the sample [here](https://github.com/firebase/quickstart-android/blob/master/auth/README.md)
     * @author Ricardo Taboada
     * @date 30/05/2023
     */
    private fun googleSignIn() {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()
        googleSignInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                Log.d(TAG, "Execute callback with intent: $pendingIntent")
                googleSignInLaunchActivity(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Google Sign-in failed", e)
            }
        Log.w(TAG, googleSignInClient.toString())
    }

    /**
     * Method where the Google signing activity is launched with an intent
     * I take the sample [here](https://github.com/firebase/quickstart-android/blob/master/auth/README.md)
     * @author Ricardo Taboada
     * @date 30/05/2023
     */
    private fun googleSignInLaunchActivity(pendingIntent: PendingIntent) {
        Log.i(TAG, pendingIntent.toString())
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            googleSignInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start Sign In: ${e.localizedMessage}")
        }
    }

    /**
     * Handler method that is trigger as soon as the launcher notice if the user is already signed with Google
     * Authentication. It obtains the Google Credential to pass to our Firebase Auth project
     * I take the sample [here](https://github.com/firebase/quickstart-android/blob/master/auth/README.md)
     * @author Ricardo Taboada
     * @date 30/05/2023
     */
    private  fun googleSignInResultHandler(data: Intent?) {
        try {
            val credential = googleSignInClient.getSignInCredentialFromIntent(data)
            val idToken: String = credential.googleIdToken.orEmpty()
            if (idToken.isNotEmpty()) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                viewModel.authenticateWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)

        }
    }

    private fun emailSignIn(){
        val email = loginBinding.emailInputEdittext.text.toString()
        val password = loginBinding.passwordInputEditText.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill the required fields", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { result ->
                val user = result.user
                if (user != null) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")

                    Toast.makeText(baseContext, "Login successful", Toast.LENGTH_SHORT).show()
                    navigateToMain(user)

//                    updateUI(user)
                }
            }.addOnFailureListener {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", it )
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
//                    updateUI(null)
            }

    }

    private fun createDialog(dataBinding: ViewDataBinding): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dataBinding.root)
        return dialogBuilder.create()
    }

    /**
     * Phone Authentication
     * There's still a beta version, only happy path without any control flow for bad numbers and codes
     * @author Ricardo Taboada
     */
    private fun phoneSignIn() {
        val phoneNumberInputBinding = PhoneNumberInputBinding.inflate(layoutInflater)
        val verificationCodeInputBinding = VerificationCodeInputBinding.inflate(layoutInflater)
        val phoneDialog = createDialog(phoneNumberInputBinding)
        val verificationDialog = createDialog(verificationCodeInputBinding)

        phoneNumberInputBinding.confirmPhoneNumberButton.setOnClickListener {
            val inputNumber = phoneNumberInputBinding.verificationCodeEditText.text.toString()
            viewModel.startPhoneAuthentication(this, verificationDialog, inputNumber)
            phoneDialog.dismiss()
        }
        verificationCodeInputBinding.confirmPhoneCodeButton.setOnClickListener {
            val inputCode = verificationCodeInputBinding.verificationCodeEditText.text.toString()
            viewModel.completePhoneAuthentication(verificationDialog, inputCode)
        }
        phoneNumberInputBinding.cancelPhoneNumberButton.setOnClickListener { viewModel.cancelPhoneAuth(phoneDialog) }
        verificationCodeInputBinding.cancelPhoneCodeButton.setOnClickListener { viewModel.cancelPhoneAuth(verificationDialog) }

        //Start phone flow by showing the first alert dialog
        phoneDialog.show()
    }
    /**
     * @author Daniel Mendoza
     * @date July 19th
     * Takes the user to the NavigationActivity
     * @param: A Firebase user
     * */
    override fun navigateToMain(user: FirebaseUser) {
        Toast.makeText(this,"Welcome ${user.displayName}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * @author Daniel Mendoza
     * @date July 19th
     * Callback: Reports an exception if a login operation fails
     * @param loginException: The exception occurred during the login process
     * */
    override fun onAuthException(loginException: Exception) {
        TODO("Not yet implemented")
    }

    /**
     * Companinon object to log purposes
     * @author Ricardo Taboada
     * @date 30/05/2023
     */
    companion object {
        const val TAG = "GoogleFragmentKt"
    }


}