package com.app.shotclock.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.app.shotclock.R
import com.app.shotclock.adapters.WalkThroughAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.getUser
import com.app.shotclock.databinding.FragmentWalkThroughBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.SocialLoginRequestModel
import com.app.shotclock.models.WalkThroughModel
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.json.JSONObject
import java.net.URL
import javax.inject.Inject


class WalkThroughFragment : BaseFragment<FragmentWalkThroughBinding>(),
    Observer<Resource<BaseResponseModel>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var walkList: ArrayList<WalkThroughModel>? = ArrayList()
    private var mCallbackManager: CallbackManager? = null
    private var googleSignInClient:GoogleSignInClient?=null
    private lateinit var googleHelper: GoogleHelper
    private var facebookHelper: FacebookHelper? = null

    private var socialLoginType = ""
    var firstName = ""
    var lastName = ""
    var socialEmail = ""
    var socialId = ""
    var socialImage = ""
    var socialtype = ""

    private val googleLoginActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }
    override fun getViewBinding(): FragmentWalkThroughBinding {
        return FragmentWalkThroughBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCallbackManager = CallbackManager.Factory.create()

        setAdapter()
        configureViewModel()
        handleClicks()

    }
    // for google
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

// Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
// The ApiException status code indicates the detailed failure reason.
// Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleLogin", "signInResult:failed code=" + e.statusCode)
        }
    }

    // for google
    private fun updateUI(account: GoogleSignInAccount?) {

        showToast("success")
        if (account != null) {

            val personName = account.displayName
            val email = account.email
            val familyName = account.familyName
            val idToken = account.idToken
            val id = account.id
            val givenName = account.givenName

            googleSignInClient?.signOut()

            if (personName != null && email != null) {
               // socialLoginApiHit("1", id.toString(), personName, email)
            } else {
               showToast("Google error")
            }

        } else {
            showToast("Google error")
        }
    }
    private fun configureViewModel() {
        loginSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
    }

    private fun handleClicks() {

        binding.ivEmail.setOnClickListener {

            this.findNavController().navigate(R.id.action_walkThroughFragment_to_loginFragment)
        }

        binding.ivFacebook.setOnClickListener {
            socialLoginType = "Fb"
            fbLogin()

        }

        binding.ivGoogle.setOnClickListener {
//            if (socialtype == "2")
                signIn()
        }

    }

    private fun setAdapter() {
        walkList?.clear()
        walkList?.add(WalkThroughModel(R.drawable.img_walkthree))
        walkList?.add(WalkThroughModel(R.drawable.img_walktwo))
        walkList?.add(WalkThroughModel(R.drawable.img_walkone))

        binding.rvWalkThrough.adapter = WalkThroughAdapter(walkList!!)

        binding.rvWalkThrough.set3DItem(false)
        binding.rvWalkThrough.setInfinite(false)
        binding.rvWalkThrough.setAlpha(false)
        binding.rvWalkThrough.setFlat(false)
        binding.rvWalkThrough.setIntervalRatio(0.6F)
//        binding.rvWalkThrough.scrollToPosition(selectedPos!!)
//        val carouselLayoutManager = binding.rvWalkThrough.getCarouselLayoutManager()
//        val currentlyCenterPosition = binding.rvWalkThrough.getSelectedPosition()

        val pagerSnapHelper = PagerSnapHelper()

        pagerSnapHelper.attachToRecyclerView(binding.rvWalkThrough)
        binding.viewPagerIndicator.attachToRecyclerView(binding.rvWalkThrough, pagerSnapHelper)

    }

    private fun setSocialLoginData() {
        val body = SocialLoginRequestModel(
            getUser(requireContext())?.countryCode!!,
            Prefs.with(view?.context).getString("token", "0"),
            socialtype.toInt(),
            socialEmail,
            firstName + lastName,
            getUser(requireContext())?.phone!!,
            socialImage,
            socialId,
            socialtype.toInt()
        )

        loginSignUpViewModel.socialLogin(body).observe(viewLifecycleOwner, this)
    }

    override fun onChanged(t: Resource<BaseResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                 findNavController().navigate(R.id.action_walkThroughFragment_to_homeActivity)
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showError(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

    private fun signIn() {
        socialLoginType = "Google"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val intentGoogle = googleSignInClient?.signInIntent
        googleLoginActivityResult.launch(intentGoogle)
       // googleHelper.signIn()

    }


    // for facebook
    private fun fbLogin() {
        val loginButton = LoginButton(requireContext())
        loginButton.setPermissions(listOf("public_profile, email"))
        loginButton.performClick()
        LoginManager.getInstance()
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult?>,
                GraphRequest.GraphJSONObjectCallback {
                override fun onCancel() {
                    Log.e("FacebookLogin", "Cancel Facebook Login")
                }

                override fun onError(error: FacebookException) {
                    Log.e("FacebookLogin", error.message.toString())
                }

                override fun onSuccess(result: LoginResult?) {
                    val request = GraphRequest.newMeRequest(
                        result!!.accessToken,
                        this
                    )
                    val parameters = Bundle()
                    parameters.putString("fields", "id, first_name, last_name, email")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
                    showToast("Success")
                    Log.e("FacebookLogin", response.toString())

// Application code
                    if (obj != null) {
                        var email = ""
                        var name = ""

                        val id: String = obj.getString("id")

                        val profilePic = URL("https://graph.facebook.com/$id/picture?width=200&height=150")
                        Log.i("profilePic", profilePic.toString() + "")

                        if (obj.has("email")) {
                            email = obj.getString("email")
                        }

                        if (obj.has("birthday")) {
                            val birthday: String = obj.getString("birthday") // 01/31/1980 format
                        }

                        if (obj.has("first_name")) {
                            name = obj.getString("first_name")
                        }

                        if (obj.has("last_name")) {
                            val lastName = obj.getString("last_name")
                        }

                      //  socialLoginApiHit("2", id, name, email)


                    }
                }


            })
    }



/*
  socialtype = "1"
                firstName = firstNames
                lastName = fatchName[fatchName.size - 1]
                socialEmail = account.email!!
                socialId = account.id!!
                socialImage = photo

// hit api here

                val data = SocialLoginRequestModel("",Prefs.with(requireContext()).getString("token",""), 2,socialEmail, firstName + lastName, "",
                    socialImage,socialId,1)

                loginSignUpViewModel.socialLogin(data).observe(viewLifecycleOwner,this)

 */
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    mCallbackManager!!.onActivityResult(requestCode, resultCode, data)

}

}