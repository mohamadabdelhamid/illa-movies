package com.mabdelhamid.illamovies.ui.activity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mabdelhamid.illamovies.R
import com.mabdelhamid.illamovies.base.BaseActivity
import com.mabdelhamid.illamovies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * App is based on single-activity-architecture, and this the main activity of the app,
 * all screens of the app will be represented as a fragment, and be replaced or added using
 * navigation component.
 */

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getNavController()
        setupBottomNavWithNavController()
    }

    override fun initUi() = Unit

    override fun initObservers() {
        viewModel.viewState.observe(this) { state ->
            with(binding.bottomNavigation.getOrCreateBadge(R.id.favouritesFragment)) {
                isVisible = !state.isEmptyFavourites
                number = state.favouritesCount
            }
        }
    }

    private fun getNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostMain) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomNavWithNavController() {
        with(binding.bottomNavigation) {
            setupWithNavController(navController)
            setOnItemReselectedListener {
                // do nothing here, it will prevent recreating same fragment
            }
        }
    }
}