package com.bogiruapps.rdshapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bogiruapps.rdshapp.databinding.ActivityMainBinding
import com.bogiruapps.rdshapp.databinding.DrawerHeaderBinding
import com.bogiruapps.rdshapp.utils.ConnectionLiveData
import com.bogiruapps.rdshapp.utils.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView

    private val mainViewModel: MainActivityViewModel by viewModel()

    private lateinit var authFirebase: FirebaseAuth
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    private lateinit var isConnected: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureBinding()
        checkConnectivity()
        setupObserverViewModel()
        configureFirebase()
        configureToolbar()
        configureNavigationView()
        setupLogoutButton()
        setupObserverDestination()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }

        when(requestCode) {
            RC_SIGN_IN ->
                mainViewModel.handleSignInActivityResult(resultCode, data, authFirebase.currentUser)
        }
    }
    private fun configureBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this
    }

    private fun checkConnectivity() {
        isConnected = ConnectionLiveData(applicationContext)
    }

    private fun setupObserverViewModel() {
        mainViewModel.openSignInActivityEvent.observe(this, EventObserver {
            openSignInActivity()
        })

        mainViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            openNoticeFragment()
        })

        isConnected.observe(this, Observer { isConnected ->
            if (isConnected) connected() else disconnected()
        })

        mainViewModel.setVisibilityEmailUnverifiedLayoutEvent.observe(this, EventObserver {
            if (it) {
                binding.emailUnverifiedLayout.visibility = View.VISIBLE
                setEnabledMenuItem(false)
            } else {
                binding.emailUnverifiedLayout.visibility = View.INVISIBLE
                setEnabledMenuItem(true)
            }
        })

    }

    private fun connected() {
        binding.noInthernetLayout.visibility = View.INVISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun disconnected() {
        binding.noInthernetLayout.visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun configureFirebase() {
        authFirebase = FirebaseAuth.getInstance()
        mainViewModel.checkUserIsConnected(authFirebase.currentUser)
    }

    private fun configureToolbar() {
        drawerLayout = binding.drawerLayout
        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
    }

    private fun configureNavigationView() {
        navView = binding.navView
        val navViewBinding = DataBindingUtil.inflate<DrawerHeaderBinding>(
            layoutInflater, R.layout.drawer_header, navView, false
        )
        navViewBinding.viewModel = mainViewModel
        navViewBinding.lifecycleOwner = this
        binding.navView.addHeaderView(navViewBinding.root)

        navController = findNavController(R.id.content_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        navView.setupWithNavController(navController)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.content_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupLogoutButton() {
        navView.menu.findItem(R.id.btn_sign_out).setOnMenuItemClickListener {
            mainViewModel.logoutUser(this)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupObserverDestination() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.choseSchoolFragment) {
                setEnabledMenuItem(false)
            } else {
                setEnabledMenuItem(true)
            }
        }
    }

    private fun setEnabledMenuItem(value: Boolean) {
        navView.menu.findItem(R.id.userFragment).isEnabled = value
        navView.menu.findItem(R.id.schoolEventsFragment).isEnabled = value
        navView.menu.findItem(R.id.noticeFragment).isEnabled = value
        navView.menu.findItem(R.id.chatsFragment).isEnabled = value
        navView.menu.findItem(R.id.ratingFragment).isEnabled = value
        navView.menu.findItem(R.id.infoFragment).isEnabled = value
    }

    private fun openSignInActivity() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    private fun openNoticeFragment() {
        navController.navigate(R.id.noticeFragment)
    }
}
