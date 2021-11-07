package com.dx.star

import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dx.star.base.BaseActivity
import com.dx.star.base.KeepStateNavigator
import com.dx.star.base.inflate
import com.dx.star.databinding.ActivityMainBinding

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by inflate()

    override fun initView() {
        //        setTheme(R.style.startupTheme)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        //添加自定义的KeepStateNavigator
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navigator =
            KeepStateNavigator(this, navHostFragment.childFragmentManager, navHostFragment.id)
        navController.navigatorProvider.addNavigator(navigator)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.setGraph(R.navigation.mobile_navigation)
        binding.navView.setupWithNavController(navController)
    }

    override fun initData() {

    }
}