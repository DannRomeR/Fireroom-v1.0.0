package com.example.fireroomv100.ui.authenticate;

import android.content.Intent
import android.credentials.Credential
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fireroomv100.NavigationActivity
import com.example.fireroomv100.ui.authenticate.RegisterActivity_data
import com.example.fireroomv100.R
import android.widget.Toast
import com.example.fireroomv100.databinding.ActivityNavigationBinding
import com.example.fireroomv100.ui.authenticate.LoginActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.fireroomv100.databinding.RegisterBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity_data : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: RegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val drawerLayout: DrawerLayout = binding.drawerLayout
        //val navView: NavigationView = binding.navView
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_navigation) as NavHostFragment
        //val navController = navHostFragment.navController

        binding.submitRegisterBTN.setOnClickListener {
            val nombre = binding.nameRegisterTextView.text.toString()
            val email = binding.emailRegisterTextView.text.toString()
            val password = binding.passwordRegisterTextView.text.toString()
            val telefono = binding.phoneRegisterTextView.text.toString()
            val edad = binding.ageRegisterTextView.text.toString()
            val pais = binding.countryRegisterTextView.text.toString()

            if (nombre.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && telefono.isNotEmpty() && edad.isNotEmpty() && pais.isNotEmpty()) {
                registrarUsuario(
                    nombre,
                    email,
                    password,
                    telefono,
                    edad,
                    pais)
            } else {
                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        auth = Firebase.auth
    }
    /**
     * Starting the user credentials
     * @author Jesus Rodriguez
     * @date 11/08/2023
     */
    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        } else {
        }
    }
    /**
     * Fields to be completed by the user to store their data
     * @author Jesus Rodriguez
     * @date 09/08/2023
     */
    private fun registrarUsuario(
        nombre: String,
        email: String,
        password: String,
        telefono: String,
        edad: String,
        pais: String,
    ) {
                val user = auth.currentUser

                val uid = user!!.uid
                val map = hashMapOf(
                    "nombre" to nombre,
                    "email" to email,
                    "password" to password,
                    "telefono" to telefono,
                    "edad" to edad,
                    "pais" to pais,
                )

                val db = Firebase.firestore
                db.collection("users").document(uid).set(map).addOnSuccessListener {
                    Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show()
                    val infoUserIntent = Intent(this, NavigationActivity::class.java)
                    startActivity(infoUserIntent)
                }/*
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Fallo al guardar la informacion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }*/
            } /*else {
                Toast.makeText(
                    this, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        }
        */
    /**
     * Redirecting Activity
     * @author Jesus Rodriguez
     * @date 09/08/2023
     */
    private fun infoUser() {
        val infoUserIntent = Intent(this, NavigationActivity::class.java)
        startActivity(infoUserIntent)
    }

    private fun reload() {
    }
}
