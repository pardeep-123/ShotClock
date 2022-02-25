package com.app.shotclock.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.cache.clearAllData
import com.app.shotclock.cache.clearData
import com.app.shotclock.cache.getUser
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ActivityHomeBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.utils.App
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.myAlert
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var drawableLayout: DrawerLayout
    private lateinit var listener: NavController.OnDestinationChangedListener

    lateinit var loginSignUpViewModel: LoginSignUpViewModel
    @Inject
    lateinit var viewModelProvider : ViewModelProvider.Factory

    private var headerView : View? =null
    private var civUser : CircleImageView?= null
    private var tvUserName : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureViewModel()
        manageHeaderView()

        navController = findNavController(R.id.fragment)
        drawableLayout = findViewById(R.id.drawerLayout)
        binding.navigationView.setupWithNavController(navController)

        // logout click
        binding.navigationView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            //write your implementation here
            //to close the navigation drawer
            myAlert(this, getString(R.string.are_you_sure_you_want_to_log_out), { clickLogout() }, "Cancel", "Log Out")

            binding.navigationView.setCheckedItem(R.id.homeFragment)
            true
        }

//        listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
//                if (destination.id == R.id.logout) {
//                }
//            }

    }

    // manage home side header view
     fun manageHeaderView(){
        headerView  = binding.navigationView.getHeaderView(0)
        civUser = headerView?.findViewById(R.id.civUser)
        tvUserName  = headerView?.findViewById(R.id.tvUserName)

        civUser?.setOnClickListener {
            val options = NavOptions.Builder().setPopUpTo(R.id.fragment,false).build()
            findNavController(R.id.fragment).navigate(R.id.profileFragment,null,options)
            openClose()
        }

        Glide.with(this).load(ApiConstants.IMAGE_URL+ getUser(this)?.profileImage).into(civUser!!)
        tvUserName?.text = getUser(this)?.username

    }

    private fun configureViewModel() {
        loginSignUpViewModel = ViewModelProviders.of(this,viewModelProvider).get(LoginSignUpViewModel::class.java)
    }

    private var userLogoutObserver = Observer<Resource<BaseResponseModel>>{
        when(it.status){
            Status.SUCCESS->{
                 binding.pb.clLoading.isGone()
                clearAllData(this)
                clearData(this,"token")
                App.app?.clearShared()
                val intent = Intent(this@HomeActivity, InitialActivity::class.java)
                intent.putExtra("logout", true)
                startActivity(intent)
                finishAffinity()
            }
            Status.ERROR->{
                binding.pb.clLoading.isGone()
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
            Status.LOADING->{
                binding.pb.clLoading.isVisible()
            }
        }
    }
    private fun clickLogout() {
        loginSignUpViewModel.userLogout().observe(this,userLogoutObserver)
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
