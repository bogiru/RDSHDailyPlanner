package com.bogiruapps.rdshapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bogiruapps.rdshapp.databinding.ActivityMainBinding
import com.bogiruapps.rdshapp.login.LoginViewModel
import com.firebase.ui.auth.AuthUI

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var  navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        drawerLayout = binding.drawerLayout
        navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        binding.lifecycleOwner = this
        observeState()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun observeState() {
        loginViewModel.authenticationState.observe(this, Observer { state ->
            when (state) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.navView.menu.findItem(R.id.btn_sign_out).setOnMenuItemClickListener {
                        signOut()
                        true
                    }
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
                else -> {
                    navController.navigate(R.id.loginFragment)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)

                }
            }
        })
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(this)
    }
}
