package com.example.fireroomv100.ui.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fireroomv100.NavigationActivity
import com.example.fireroomv100.R
//import com.example.fireroomv100.databinding.ActivityEmailCreateAccount
import com.example.fireroomv100.databinding.EmailCreateAccountBinding
import com.example.fireroomv100.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailCreateAccountActivity : AppCompatActivity() {
    lateinit var emailBinding: EmailCreateAccountBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        emailBinding = EmailCreateAccountBinding.inflate(layoutInflater)
        setContentView(emailBinding.root)
//        setContentView(R.layout.activity_email_create_account)

        auth = Firebase.auth
        val createAccountBtn = emailBinding.CreateAccountBtn

        createAccountBtn.setOnClickListener {
           verifyCreationEmailAccount()
        }

    }
    /**
     * This verifies that the email, password and confirmPassword are not empty before calling the CreateUserWithEmailAndPassword,
     * Also verifies that the password and confirmPassword are equal to avoid issues later
     * @author Alan Cruz
     * @date 16/08/2023
     */
    fun verifyCreationEmailAccount (){
        val email = emailBinding.userEmailTextView.text.toString()
        val password = emailBinding.userPasswordTextView.text.toString()
        val confirmPassword = emailBinding.userPasswordConfirmTextView.text.toString()

        if (email.length !=0 && password.length !=0 && confirmPassword.length != 0){
            if (password == confirmPassword){
                createEmailAccount(email,password)
            } else {
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this, "Fill all the required information", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * This method creates the User & password account with the Firebase Auth method

     * @author Alan Cruz
     * @date 16/08/2023
     */
    fun createEmailAccount(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { result ->
                val user = result.user
                if (user != null) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginActivity.TAG, "signInWithEmail:success")

                    Toast.makeText(baseContext, "Login successful", Toast.LENGTH_SHORT).show()
                    navigateToMain(user)

//                    updateUI(user)
                }
            }.addOnFailureListener {
                // If sign in fails, display a message to the user.
                Log.w(LoginActivity.TAG, "signInWithEmail:failure", it )
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
//                    updateUI(null)
            }
    }

     fun navigateToMain(user: FirebaseUser) {
         Toast.makeText(this,"Welcome ${user.displayName}", Toast.LENGTH_SHORT).show()
         val intent = Intent(this, RegisterActivity_data::class.java)
         startActivity(intent)
         finish()
    }
}