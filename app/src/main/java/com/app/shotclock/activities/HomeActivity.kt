package com.app.shotclock.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseActivity
import com.app.shotclock.cache.clearAllData
import com.app.shotclock.cache.clearData
import com.app.shotclock.cache.getIsSocialLoginString
import com.app.shotclock.cache.getUser
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.ActivityHomeBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class HomeActivity : BaseActivity() , SocketManager.Observer,NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private var senderId = ""
    private var senderName = ""
    private lateinit var socketManager: SocketManager
    private val activityScope = CoroutineScope(Dispatchers.Main)

    lateinit var loginSignUpViewModel: LoginSignUpViewModel
    @Inject
    lateinit var viewModelProvider : ViewModelProvider.Factory
    private var headerView : View? =null
    private var civUser : CircleImageView?= null
    private var tvUserName : TextView? = null
    private var fromVideo =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        fromVideo = intent.getStringExtra("fromVideo").toString()

        setContentView(binding.root)
        initializeSocket()
//        activateReceiverListenerSocket()
        configureViewModel()
        manageHeaderView()

        navController = findNavController(R.id.fragment)

        binding.navigationView.setNavigationItemSelectedListener(this)

        /*    binding.navigationView.setupWithNavController(navController)     */

//         logout click
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

        if (fromVideo == "fromVideo"){
            val options = NavOptions.Builder().setPopUpTo(R.id.icebreakerQuestionsFragment,false).build()
            findNavController(R.id.fragment).navigate(R.id.icebreakerQuestionsFragment,null,options)
        }

        loadData(intent)

        if (getIsSocialLoginString(this)=="true"){


            try {
                binding.navigationView.getMenu().getItem(5).setVisible(false)
            } catch (e: Exception) {
            }


        }
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
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> binding.drawerLayout.closeDrawer(
                Gravity.LEFT
            )

            else -> {
                when (CacheConstants.Current) {
                    "home" -> {
                        finishAffinity()
                    }
                    "profile", "myRequest", "notification", "message", "changePassword", "cookie",
                    "copyRight", "privacy", "safeDating", "terms" -> {
                        val options = NavOptions.Builder().setPopUpTo(R.id.fragment, false).build()
                        findNavController(R.id.fragment).navigate(R.id.homeFragment, null, options)
                    }
                    else -> {
                        super.onBackPressed()
                    }

                }

            }
        }
    }
    private fun initializeSocket() {
        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
        socketManager.unRegister(this)
        socketManager.onRegister(this)
    }

    override fun onResume() {
        super.onResume()
        socketManager.unRegister(this)
        socketManager.onRegister(this)

    }

    private fun activateReceiverListenerSocket() {
        socketManager.callToUserActivate()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_Fragment -> {
                val options = NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                findNavController(R.id.fragment).navigate(R.id.homeFragment, null, options)
                openClose()
            }
            R.id.profile_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.profileFragment)
                openClose()
            }
            R.id.myRequests_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.myRequestsFragment)
                openClose()
            }
            R.id.notification_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.notificationFragment)
                openClose()
            }
            R.id.message_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.messageFragment)
                openClose()
            }
            R.id.changePassword_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.changePasswordFragment)
                openClose()
            }
            R.id.cookiePolicy_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.cookiePolicyFragment)
                openClose()
            }
            R.id.privacyPolicy_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.privacyPolicyFragment)
                openClose()
            }
            R.id.copyrightPolicy_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.copyrightPolicyFragment)
                openClose()
            }
            R.id.safeDatingPolicy_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.safeDatingPolicyFragment)
                openClose()
            }
            R.id.termsConditions_Fragment -> {
                findNavController(R.id.fragment).navigate(R.id.termsConditionsFragment)
                openClose()
            }
        }
        return true
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        loadData(intent)
    }

    // notification_code = 1 = request, 3 = send message
    private fun loadData(intent: Intent?) {
        if (intent!!.getStringExtra("notification_code") != null) {
            if (intent.getStringExtra("notification_code").equals("3")) {
                senderId = intent.getStringExtra("sender_id").toString()
                senderName = intent.getStringExtra("sender_name").toString()
                val bundle = Bundle()
                bundle.putInt("user2Id", senderId.toInt())
                bundle.putString("username", senderName)
                val options = NavOptions.Builder().setPopUpTo(R.id.chatFragment, true).build()
                findNavController(R.id.fragment).navigate(R.id.chatFragment, bundle, options)
            } else {
                val options = NavOptions.Builder().setPopUpTo(R.id.notificationFragment,true).build()
                findNavController(R.id.fragment).navigate(R.id.notificationFragment, null, options)

            }
        }
    }
    override fun onResponseArray(event: String, args: JSONArray) {

    }

    override fun onResponse(event: String, args: JSONObject) {
        when (event) {
//            SocketManager.call_to_user_listener -> {
//                activityScope.launch {
//                    val mObject = args as JSONObject
//                    val gson = GsonBuilder().create()
//                    val userToCallList = gson.fromJson(mObject.toString(), VideoCallResponse::class.java)
//
//                    // latest
//                     val intent = Intent(this@HomeActivity,IncomingCallActivity::class.java)
//                    intent.putExtra("channelName",userToCallList.channelName)
//                    intent.putExtra("receiverName", userToCallList.senderName)
//                    startActivity(intent)
//
//                }
//            }
        }
    }

    override fun onError(event: String, vararg args: Array<*>) {
    }


}