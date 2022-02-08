package com.app.shotclock.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.ActivityHomeBinding
import com.app.shotclock.utils.myAlert


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var drawableLayout: DrawerLayout
    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.fragment)
        drawableLayout = findViewById(R.id.drawerLayout)
        binding.navigationView.setupWithNavController(navController)


        // logout click
        binding.navigationView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            //write your implementation here
            //to close the navigation drawer
            myAlert(
                this,
                getString(R.string.are_you_sure_you_want_to_log_out),
                { clickLogout() },
                "Ok",
                "Cancel"
            )
//            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//                drawer_layout.closeDrawer(GravityCompat.START)
//            }
//            Toast.makeText(applicationContext, "single item click listener implemented", Toast.LENGTH_SHORT).show()
            true
        }

//        listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
//                if (destination.id == R.id.logout) {
//                }
//            }


    }

    private fun clickLogout() {
        val intent = Intent(this@HomeActivity, InitialActivity::class.java)
        intent.putExtra("logout", true)
        startActivity(intent)
        finishAffinity()

    }

    fun openClose() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
        } else {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }
    }

//    override fun onBackPressed() {
//    drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(Gravity.LEFT)
//            if(CacheConstants.Current == "home") {
//                finishAffinity()
//            }else {
//                super.onBackPressed()
//            }
//
//    }


}
