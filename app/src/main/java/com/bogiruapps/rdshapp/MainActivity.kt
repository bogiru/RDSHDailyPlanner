package com.bogiruapps.rdshapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bogiruapps.rdshapp.databinding.ActivityMainBinding
import com.bogiruapps.rdshapp.databinding.DrawerHeaderBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView

    private lateinit var mainViewModel: MainActivityViewModel

    private lateinit var authFirebase: FirebaseAuth
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    private lateinit var db: FirebaseFirestore
    private lateinit var userRepository: UserRepository
    private lateinit var userDataSource: UserRemoteDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureViewModel()
        configureBinding()
        setupObserverViewModel()
        configureFirebase()
        configureToolbar()
        configureNavigationView()
        setupLogoutButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            RC_SIGN_IN -> mainViewModel.handleSignInActivityResult(resultCode, data, authFirebase.currentUser)
        }
    }

    private fun configureViewModel() {
        db = FirebaseFirestore.getInstance()
        userDataSource = UserRemoteDataSource.getInstance(db)
        userRepository = UserRepositoryImpl.getInstance(userDataSource)
        val viewModelFactory = MainActivityViewModelFactory(userRepository)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    private fun configureBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this
    }

    private fun setupObserverViewModel() {
        mainViewModel.openSignInActivityEvent.observe(this, EventObserver {
            openSignInActivity()
        })

        mainViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            openNoticeFragment()
        })
    }

    private fun configureFirebase() {
        authFirebase = FirebaseAuth.getInstance()
        mainViewModel.checkUserIsConnected(authFirebase.currentUser)
    }

    private fun configureToolbar() {
        drawerLayout = binding.drawerLayout
        toolbar = findViewById(R.id.toolbar)
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

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        navView.setupWithNavController(navController)
        toolbar.setupWithNavController(navController, appBarConfiguration)



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
       // navView.menu.findItem(R.id.eventFragment).isEnabled = value
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
        navController.navigate(R.id.splashFragment)
    }
}
