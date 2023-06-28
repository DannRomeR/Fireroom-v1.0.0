package com.example.fireroomv100.ui.authenticate

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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
    private lateinit var signInClient: SignInClient
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        handleSignInResult(result.data)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginBinding = LoginBinding.inflate(layoutInflater)
        signInClient = Identity.getSignInClient(this)
        setContentView(loginBinding.root)
        loginBinding.googleLoginButton.setOnClickListener { signInOnGoogle() }
    }

    private fun signInOnGoogle() {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()
        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                Log.d(TAG, "Execute callback with intent: $pendingIntent")
                launchingGoogleSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Google Sign-in failed", e)
            }
        Log.w(TAG, signInClient.toString())
    }
    private fun launchingGoogleSignIn(pendingIntent: PendingIntent?) {
        Log.i(TAG, pendingIntent.toString())
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent!!)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start Sign In: ${e.localizedMessage}")
        }
    }
    private fun handleSignInResult(data: Intent?) {
        val viewModel: LoginViewModel by viewModels()

        try {
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken: String = credential.googleIdToken.orEmpty()
            if (idToken.isNotEmpty()) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                lifecycleScope.launch {
                    viewModel.authenticateWithGoogle(idToken)
                }

            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
        }
    }

    companion object {
        private const val TAG = "GoogleFragmentKt"
    }
}