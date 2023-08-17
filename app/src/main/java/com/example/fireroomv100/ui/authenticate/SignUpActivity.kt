package com.example.fireroomv100.ui.authenticate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fireroomv100.R
import com.example.fireroomv100.databinding.LoginBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var loginBinding : LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)

        loginBinding = LoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)




    }
}