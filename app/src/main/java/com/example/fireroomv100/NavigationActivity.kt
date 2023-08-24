package com.example.fireroomv100

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fireroomv100.databinding.ActivityNavigationBinding
import com.example.fireroomv100.ui.authenticate.LoginActivity
import com.example.fireroomv100.ui.authenticate.LoginViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationBinding

    /**
     * Method that check the current user from Firebase and returns to the LoginActivity if
     * there's no user.
     * @author Ricardo Taboada 30/06/2023
     */
    private fun checkToJumpToLoginActivity() {
        val loginViewModel: LoginViewModel by viewModels()
        if (loginViewModel.isUserExists()) return
        Toast.makeText(this, "No account, redirect to Login", Toast.LENGTH_SHORT).show()
        val loginActivityIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginActivityIntent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkToJumpToLoginActivity()
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarNavigation.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_navigation) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_posts, R.id.nav_shifts, R.id.nav_onboarding
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)

        return true
    }

    /**
     * Listeners actions for the menu. I've added click listener to
     * the sign-out action.
     * @author Ricardo Taboada 30/06/23
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        val viewModel: LoginViewModel by viewModels()
        if (id  == R.id.action_settings_logout) {
            viewModel.signOut()
            checkToJumpToLoginActivity()
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}