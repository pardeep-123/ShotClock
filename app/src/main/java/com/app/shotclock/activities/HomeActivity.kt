package com.app.shotclock.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

//    NavigationView.OnNavigationItemSelectedListener

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawableLayout: DrawerLayout
    private var toolbar: Toolbar? = null
    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.fragment)
        drawableLayout = findViewById(R.id.drawerLayout)
        binding.navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawableLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        listener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id == R.id.homeFragment) {

                }
            }


//        val navHostFragment = (supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment)
//        val inflater = navHostFragment.navController.navInflater
//        //
//        val navController = navHostFragment.navController
//        findViewById<NavigationView>(R.id.navigationView)
//            .setupWithNavController(navController)
//        findViewById<NavigationView>(R.id.navigationView).setupWithNavController(navController)
//        //
//
//        navController.addOnDestinationChangedListener()
//
//        val graph = inflater.inflate(R.navigation.nav_graph_home)
//
//        navHostFragment.navController.graph = graph


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration)
                || return super.onSupportNavigateUp()
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(R.id.nav_graph_home)
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.navigation_menu, menu)
//        return true
//    }
//
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//        return true
//    }
//}
//
//private fun NavController.addOnDestinationChangedListener() {
//    if (R.id.homeFragment == R.id.home_fragment){
//
//    }
}
