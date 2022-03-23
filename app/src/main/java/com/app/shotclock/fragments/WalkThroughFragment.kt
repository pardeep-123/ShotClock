package com.app.shotclock.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.facebook.FacebookException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject


class WalkThroughFragment : BaseFragment<FragmentWalkThroughBinding>(),
    Observer<Resource<BaseResponseModel>>, FacebookHelper.FacebookHelperCallback,
    GoogleHelper.GoogleHelperCallback {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var walkList: ArrayList<WalkThroughModel>? = ArrayList()

    private lateinit var googleHelper: GoogleHelper
    private var facebookHelper: FacebookHelper? = null

    private var socialLoginType = ""
    var firstName = ""
    var lastName = ""
    var socialEmail = ""
    var socialId = ""
    var socialImage = ""
    var socialtype = ""


    override fun getViewBinding(): FragmentWalkThroughBinding {
        return FragmentWalkThroughBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        configureViewModel()
        handleClicks()

        facebookHelper = FacebookHelper(requireContext(), this)

        googleHelper = GoogleHelper(requireActivity(), this)
    }

    private fun configureViewModel() {
        loginSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
    }

    private fun handleClicks() {
        /*       binding.ivPhone.setOnClickListener {
           this.findNavController().navigate(R.id.action_walkThroughFragment_to_loginPhoneFragment)
       }*/

        binding.ivEmail.setOnClickListener {
            this.findNavController().navigate(R.id.action_walkThroughFragment_to_loginFragment)
        }

        binding.ivFacebook.setOnClickListener {
//            if (socialtype == "1") {
                socialLoginType = "Fb"
                facebookHelper!!.login(requireContext())
//                setSocialLoginData()
//            }

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
        googleHelper.signIn()

    }

    override fun onSuccessFacebook(bundle: Bundle?) {

        firstName = bundle!!.getString(FacebookHelper.FIRST_NAME)!!
        lastName = bundle.getString(FacebookHelper.LAST_NAME)!!

        socialEmail = if (bundle.getString(FacebookHelper.EMAIL) != null) {

            bundle.getString(FacebookHelper.EMAIL)!!
        } else {
            ""
        }
        socialId = bundle.getString(FacebookHelper.FACEBOOK_ID)!!
        socialImage = bundle.getString(FacebookHelper.PROFILE_PIC)!!
        socialtype = "1"
        val data = SocialLoginRequestModel("",Prefs.with(requireContext()).getString("token",""), 2,socialEmail, firstName + lastName, "",
            socialImage,socialId,2)

        loginSignUpViewModel.socialLogin(data).observe(viewLifecycleOwner,this)


    }

    override fun onCancelFacebook() {

    }

    override fun onErrorFacebook(ex: FacebookException?) {

    }

    override fun onSuccessGoogle(account: GoogleSignInAccount?) {

        try {
            var photo = ""
            if (account?.photoUrl != null) {
                photo = account.photoUrl.toString()
            }

            googleHelper.signOut()
            val fatchName = account?.displayName!!.split(" ")

            try {
                var firstNames = ""
                for (i in 0 until fatchName.size - 1) {
                    firstNames = if (firstNames.isEmpty()) {
                        fatchName[i]
                    } else {
                        firstNames + " " + fatchName[i]
                    }
                }

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


            } catch (e: Exception) {
            }
        } catch (ex: Exception) {
            ex.localizedMessage
        }

    }

    override fun onErrorGoogle() {
        Log.d("===jdj","dkshf")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//      If signIn with google
        if (socialLoginType == "Fb")
            facebookHelper!!.onResult(requestCode, resultCode, data)
        else if (socialLoginType == "Google")
            googleHelper.onResult(requestCode, resultCode, data)
    }

}