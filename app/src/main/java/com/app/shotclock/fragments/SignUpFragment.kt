package com.app.shotclock.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.cache.saveToken
import com.app.shotclock.cache.saveUser
import com.app.shotclock.databinding.FragmentSignUpBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.FileUploadResponse
import com.app.shotclock.models.SignUpResponseModel
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import com.bumptech.glide.Glide
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class SignUpFragment : ImagePickerUtility1<FragmentSignUpBinding>(),Observer<Resource<SignUpResponseModel>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel
    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private var imageResultPath = ""

    override fun getViewBinding(): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()
        configureViewModel()

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                // Prevent CheckBox state from being toggled when link is clicked
                widget.cancelPendingInputEvents()
                // Do action for link text...
                this@SignUpFragment.findNavController().navigate(R.id.termsConditionsFragment2)
            }
        }

        val linkText = SpannableString(getString(R.string.i_accept_terms_conditions))
        linkText.setSpan(clickableSpan, 0, linkText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val cs = TextUtils.expandTemplate(" ^1 ", linkText)

        binding.cbTermsConditions.text = cs
// Finally, make links clickable
        binding.cbTermsConditions.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btNext.setOnClickListener {

//            this.findNavController().navigate(R.id.action_signUpFragment_to_completeProfileFragment)
            if (Validation().signUpValidation(
                    requireActivity(),
                    imageResultPath,
                    binding.etName.text.toString().trim(),
                    binding.etEmail.text.toString().trim(),
                    binding.etMobile.text.toString().trim(),
                    binding.etPassword.text.toString().trim(),
                    binding.etConfirmPassword.text.toString().trim()
                )
            ) {
                getSigUpData()
            }
        }

        binding.tvSignIn.setOnClickListener {
            this.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.civUser.setOnClickListener {
            getImage(requireActivity(), 0, false)
        }

        // set checkbox terms and conditions
        binding.cbTermsConditions.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){

            }
        }
    }

    override fun selectedImage(imagePath: String?, code: Int) {
        if (!imagePath.isNullOrEmpty()) {
            imageResultPath = imagePath
            Glide.with(context!!).load(imageResultPath).into(binding.civUser)
        }
    }

    private fun configureViewModel(){
        loginSignUpViewModel = ViewModelProviders.of(this,viewModelFactory).get(LoginSignUpViewModel::class.java)
    }

    override fun onChanged(t: Resource<SignUpResponseModel>) {
        when(t.status){
            Status.SUCCESS-> {
                saveUser(requireContext(), t.data?.body!!)
                saveToken(requireContext(), t.data.body.authKey)
                binding.pb.clLoading.isGone()
                this.findNavController()
                    .navigate(R.id.action_signUpFragment_to_completeProfileFragment)
            }
            Status.ERROR->{
                binding.pb.clLoading.isGone()
                showToast(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

    private fun getSigUpData() {
        val list = ArrayList<MultipartBody.Part>()
        list.add(prepareMultiPart("image", File(imageResultPath)))
        val hashMap = HashMap<String,RequestBody>()
        hashMap["type"] = createRequestBody("image")
        hashMap["folder"] = createRequestBody("profile")
        loginSignUpViewModel.fileUpload(list,hashMap).observe(viewLifecycleOwner, fileUploadObserver)

    }

    private val fileUploadObserver = Observer<Resource<FileUploadResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                //
                val map: HashMap<String, RequestBody> = HashMap()
                map["name"] = createRequestBody(binding.etName.text.toString().trim())
                map["email"] = createRequestBody(binding.etEmail.text.toString().trim())
                map["password"] = createRequestBody(binding.etPassword.text.toString().trim())
                map["confirm_password"] = createRequestBody(binding.etConfirmPassword.text.toString().trim())
                map["countryCode"] = createRequestBody(binding.ccp.selectedCountryCodeWithPlus.toString())
                map["device_type"] = createRequestBody("2")
                map["device_token"] = createRequestBody(Prefs.with(requireContext()).getString("token","0"))
                map["phone"] = createRequestBody(binding.etMobile.text.toString().trim())
                map["profile_image"] = createRequestBody(it.data?.body!![0].image)
                //
                loginSignUpViewModel.userSignUp(map).observe(viewLifecycleOwner, this)
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showToast(it.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}