package com.app.shotclock.fragments

import android.app.Activity
import android.content.Context
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
import com.app.shotclock.activities.InitialActivity
import com.app.shotclock.adapters.WalkThroughAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.cache.saveString
import com.app.shotclock.cache.saveToken
import com.app.shotclock.cache.saveUser
import com.app.shotclock.databinding.FragmentWalkThroughBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.LoginResponseModel
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
    Observer<Resource<LoginResponseModel>>, GraphRequest.GraphJSONObjectCallback {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var walkList: ArrayList<WalkThroughModel>? = ArrayList()
    private var mCallbackManager: CallbackManager? = null
    private var googleSignInClient: GoogleSignInClient? = null

    private var socialLoginType = ""
    var firstName = ""
    var lastName = ""
    var socialEmail = ""
    var socialId = ""
    private var socialImage = ""
    var ctx: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
        (ctx as InitialActivity).setData(this)
    }

    override fun getViewBinding(): FragmentWalkThroughBinding {
        return FragmentWalkThroughBinding.inflate(layoutInflater)
    }

    private val googleLoginActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCallbackManager = CallbackManager.Factory.create()

        setAdapter()
        configureViewModel()
        handleClicks()

    }

    private fun handleClicks() {

        binding.ivEmail.setOnClickListener {

            this.findNavController().navigate(R.id.action_walkThroughFragment_to_loginFragment)
        }

        binding.ivFacebook.setOnClickListener {
            LoginManager.getInstance().logOut()
            fbLogin()
        }

        binding.ivGoogle.setOnClickListener {
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

        val pagerSnapHelper = PagerSnapHelper()

        pagerSnapHelper.attachToRecyclerView(binding.rvWalkThrough)
        binding.viewPagerIndicator.attachToRecyclerView(binding.rvWalkThrough, pagerSnapHelper)

    }

    override fun onChanged(t: Resource<LoginResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                binding.pb.clLoading.isVisible()
                saveUser(requireContext(), t.data?.body!!)
                saveString(requireContext(), t.data.body.authKey)
                if (t.data.body.isComplete == 1) {
                    this.findNavController()
                        .navigate(R.id.action_walkThroughFragment_to_homeActivity)
                    saveToken(requireContext(), t.data.body.authKey)
                } else {
                    saveToken(requireContext(), t.data.body.authKey)


                    this.findNavController()
                        .navigate(R.id.action_walkThroughFragment_to_completeProfileFragment)
                }
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showErrorAlert(requireActivity(),t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
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
        if (account != null) {

            firstName = account.displayName.toString()
            socialEmail = account.email.toString()
            val familyName = account.familyName
            val idToken = account.idToken
            socialId = account.id.toString()
            socialImage = account.photoUrl.toString()
            val givenName = account.givenName

            googleSignInClient?.signOut()

            // api hit
            if (firstName != null && socialEmail != null) {
                val data = SocialLoginRequestModel(
                    "",
                    Prefs.with(requireContext()).getString("token", ""),
                    2,
                    socialEmail,
                    firstName,
                    "",
                    socialImage,
                    socialId,
                    1
                )
                loginSignUpViewModel.socialLogin(data).observe(viewLifecycleOwner, this)

            } else {
                showErrorAlert(requireActivity(),"Google error")
            }

        } else {
            showErrorAlert(requireActivity(),"Google error")
        }
    }

    private fun configureViewModel() {
        loginSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
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
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult?> {
                override fun onCancel() {
                    Log.e("FacebookLogin", "Cancel Facebook Login")
                }

                override fun onError(error: FacebookException) {
                    Log.e("FacebookLogin", error.message.toString())
                }

                override fun onSuccess(result: LoginResult?) {
                    val request = GraphRequest.newMeRequest(
                        result!!.accessToken,
                        this@WalkThroughFragment
                    )
                    val parameters = Bundle()
                    parameters.putString("fields", "id, first_name, last_name, email")
                    request.parameters = parameters
                    request.executeAsync()

                }
            })
    }

    fun setDataFb(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    // facebook completion
    override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
        Log.e("FacebookLogin", response.toString())

// Application code
        if (obj != null) {
            socialId = obj.getString("id")

            socialImage =
                URL("https://graph.facebook.com/$id/picture?width=200&height=150").toString()
            Log.i("profile_pic", socialImage.toString() + "")

            if (obj.has("email")) {
                socialEmail = obj.getString("email")
            }

            if (obj.has("birthday")) {
                val birthday: String = obj.getString("birthday") // 01/31/1980 format
            }

            if (obj.has("first_name")) {
                firstName = obj.getString("first_name")
            }

            if (obj.has("last_name")) {
                lastName = obj.getString("last_name")
            }

            // api hit
            val data = SocialLoginRequestModel(
                "",
                Prefs.with(requireContext()).getString("token", ""),
                2,
                socialEmail,
                firstName + lastName,
                "",
                socialImage,
                socialId,
                2
            )
            loginSignUpViewModel.socialLogin(data).observe(viewLifecycleOwner, this)

        }
    }

}