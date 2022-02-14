package com.app.shotclock.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.databinding.ActivityHomeBinding
import com.app.shotclock.utils.myAlert
import de.hdodenhof.circleimageview.CircleImageView


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
        val headerView : View = binding.navigationView.getHeaderView(0)
        val civUser : CircleImageView = headerView.findViewById(R.id.civUser)
        val tvUserName : TextView = headerView.findViewById(R.id.tvUserName)

        civUser.setOnClickListener {
          val options = NavOptions.Builder().setPopUpTo(R.id.fragment,false).build()
            findNavController(R.id.fragment).navigate(R.id.profileFragment,null,options)
            openClose()
        }
        // logout click
        binding.navigationView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            //write your implementation here
            //to close the navigation drawer
            myAlert(
                this,
                getString(R.string.are_you_sure_you_want_to_log_out),
                { clickLogout() },
                "Cancel",
                "Log Out"
            )

            binding.navigationView.setCheckedItem(R.id.homeFragment)
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

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> binding.drawerLayout.closeDrawer(Gravity.LEFT)
            else -> {
                super.onBackPressed()
//                when (CacheConstants.Current) {
//                    "home" -> {
//                        finishAffinity()
//                    }
//                    else -> {
//                        super.onBackPressed()
//                    }
//                }
            }
        }
    }

}
