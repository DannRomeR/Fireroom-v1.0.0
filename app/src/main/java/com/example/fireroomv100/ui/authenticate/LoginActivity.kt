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
import androidx.lifecycle.lifecycleScope
import com.example.fireroomv100.NavigationActivity
import com.example.fireroomv100.R
import com.example.fireroomv100.databinding.LoginBinding
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: SignInClient
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        googleSignInResultHandler(result.data)
    }

    /**
     * Entry point from the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginBinding = LoginBinding.inflate(layoutInflater)
        googleSignInClient = Identity.getSignInClient(this)
        setContentView(loginBinding.root)
        loginBinding.googleLoginButton.setOnClickListener { googleSignIn() }
    }

    private fun checkForUser() {
        val viewModel: LoginViewModel by viewModels()
        if (!viewModel.isUserExists()) return
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
    private fun googleSignInResultHandler(data: Intent?) {
        val viewModel: LoginViewModel by viewModels()

        try {
            val credential = googleSignInClient.getSignInCredentialFromIntent(data)
            val idToken: String = credential.googleIdToken.orEmpty()
            if (idToken.isNotEmpty()) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                lifecycleScope.launch {
                    viewModel.authenticateWithGoogle(idToken)
                    checkForUser()
                }
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
        }
    }

    /**
     * Companinon object to log purposes
     * @author Ricardo Taboada
     * @date 30/05/2023
     */
    companion object {
        private const val TAG = "GoogleFragmentKt"
    }
}