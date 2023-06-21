package com.example.fireroomv100

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fireroomv100.databinding.LoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginBinding=LoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        loginBinding.phoneLoginButton.setOnClickListener {
            //handle phone login
        }
    }
}