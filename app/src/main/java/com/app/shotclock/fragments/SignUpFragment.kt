package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.cache.getToken
import com.app.shotclock.databinding.FragmentSignUpBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.Body
import com.app.shotclock.models.SignUpResponseModel
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class SignUpFragment : ImagePickerUtility1<FragmentSignUpBinding>(),Observer<Resource<SignUpResponseModel>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel
    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private var imageResultPath =""

    override fun getViewBinding(): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()
        configureViewModel()
    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btNext.setOnClickListener {
            this.findNavController().navigate(R.id.action_signUpFragment_to_completeProfileFragment)

//            getSigUpData()
        }

        binding.tvSignIn.setOnClickListener {
            this.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.civUser.setOnClickListener {
            getImage(requireActivity(), 0, false)
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
            Status.SUCCESS->{
                binding.pb.clLoading.isGone()
                this.findNavController().navigate(R.id.action_signUpFragment_to_completeProfileFragment)
            }
            Status.ERROR->{
                binding.pb.clLoading.isGone()
                showToast(t.message!!)
            }
            Status.LOADING->{
                binding.pb.clLoading.isVisible()
            }
        }
    }

    private fun getSigUpData() {
        val map: HashMap<String, RequestBody> = HashMap()
        map["firstname"+"lastname"] = createRequestBody(binding.etName.text.trim().toString())
//        map["lastname"] = createRequestBody(etLastNameSignUp.text.toString())
        map["email"] = createRequestBody(binding.etEmail.text.toString())
        map["password"] = createRequestBody(binding.etPassword.text.trim().toString())
        map["country_code"] = createRequestBody(binding.ccp.selectedCountryCodeWithPlus.toString())
        map["device_type"] = createRequestBody("2")
        map["device_token"] = createRequestBody(getToken(requireContext())!!)
        map["phone"] = createRequestBody(binding.etMobile.text.trim().toString())

//        var imagePerfil: MultipartBody.Part?=null
//        if (imageResultPath!=null){
//            //create RequestBody instance from file
//            val requestFile: RequestBody= imageResultPath.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//            // MultipartBody.Part is used to send also the actual file name
//            imagePerfil = MultipartBody.Part.createFormData("image",imageResultPath?.name,requestFile)
//        }



    }

}