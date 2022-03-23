package com.app.shotclock.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.EditProfileImagesAdapter
import com.app.shotclock.cache.saveUser
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.FragmentEditProfileBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.*
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.ProfileViewModels
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class EditProfileFragment : ImagePickerUtility1<FragmentEditProfileBinding>(),
    Observer<Resource<LoginResponseModel>> {

    lateinit var profileViewModels: ProfileViewModels

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val myCalendar = Calendar.getInstance()
    lateinit var date: DatePickerDialog.OnDateSetListener
    private var imageResultPath = ""
    private var imageList = ArrayList<EditProfileModel>()
    private var imagesAdapter: EditProfileImagesAdapter? = null
    private var latitude = ""
    private var longitude = ""
    private var imageType = 0
    private var profileImage = ""
    private var gender = 1      // 1 = male, 2= female
    private var interested = "" // 1 = men, 2= women, 3 = both
    private var imageFileList = ArrayList<EditProfileRequestModel.EditProfileImage>()
    private var profileData: ProfileBody? = null
    private val oneFeet = 0.0328  //Don't change this value
    private val imageArrayList = ArrayList<MultipartBody.Part>()
    private val userImages = ArrayList<EditProfileModel>()

    override fun getViewBinding(): FragmentEditProfileBinding {
        return FragmentEditProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Places.initialize(requireContext(), getString(R.string.api_key_map))
        val bundle = arguments
        profileData = (bundle?.getSerializable("data") as ProfileBody?)!!
        configureViewModel()
        handleClicks()
        setEditProfileData()
    }

    private fun setEditProfileData() {
        profileImage = profileData?.profileImage!!
        Glide.with(requireContext()).load(ApiConstants.IMAGE_URL + profileData?.profileImage)
            .into(binding.rivUser)
        binding.etName.setText(profileData?.username)
        binding.etEmail.setText(profileData?.email)
        binding.ccp.registerCarrierNumberEditText(binding.etMobile)
        binding.ccp.fullNumber = profileData?.countryCode
        binding.etMobile.setText(profileData?.phone)
        binding.tvDOBSelect.text = profileData?.dateofbirth
        var genderValue = ""
        genderValue = if (profileData?.gender == 1)
            "Male"
        else
            "Female"
        binding.tvGenderSelect.text = genderValue

        binding.tvHeightSelect.text = profileData?.height
        /*       if (profileData?.height!!.isNotEmpty()) {
                   val feetHeight = (oneFeet * profileData?.height!!.toDouble()).toFloat()
                   binding.tvHeightSelect.text  =  String.format("%.1f", feetHeight)
               }*/
        //        binding.tvHeightSelect.text = profileData?.height

        binding.tvQualificatSelect.text = profileData?.qualification
        binding.etLocation.setText(profileData?.address)
        binding.tvSexualOrientationSelect.text = profileData?.sexualOrientation
        binding.tvAstrologicalSignSelect.text = profileData?.astrologicalSign
        binding.tvSmokingSelect.text = profileData?.smoking
        binding.tvDrinkingSelect.text = profileData?.drinking
        binding.tvPetsSelect.text = profileData?.pets
        binding.etAddBio.setText(profileData?.bio)

        when (profileData?.interested) {
            1 -> binding.tvInterestSelect.text = "Men"
            2 -> binding.tvInterestSelect.text = "Women"
            else -> binding.tvInterestSelect.text = "Others"
        }

        for (i in 0 until profileData?.user_images!!.size) {
            imageFileList.add(EditProfileRequestModel.EditProfileImage(profileData?.user_images!![i].image_path))
            imageList.add(EditProfileModel(profileData?.user_images!![i].image_path, 1))
        }
        imagesAdapter?.notifyDataSetChanged()

    }

    private fun configureViewModel() {
        profileViewModels =
            ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModels::class.java)
    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvDOBSelect.setOnClickListener {
            date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.tvDOBSelect.text = longToDate(myCalendar.timeInMillis, "dd/MM/yy")
//                    edtIssuedOnDate.setText(AppUtils.longToDate(myCalendar.timeInMillis))
            }
            datePicker(requireContext())
        }

        // edit profile images adapter
        imagesAdapter = EditProfileImagesAdapter(requireContext(), imageList)
        binding.rvEditProfile.adapter = imagesAdapter
        imagesAdapter?.onItemClickListener = {
            if (imageList.size == 5) {
                Toast.makeText(requireContext(), "You can only select 5 images", Toast.LENGTH_SHORT)
                    .show()

            } else {
                imageType = 2
                getImage(requireActivity(), 1, false)
            }

        }

        binding.ivCamera.setOnClickListener {
            imageType = 1
            getImage(requireActivity(), 0, false)
        }

        binding.tvGenderSelect.setOnClickListener {
            val list = ArrayList<String>()
            list.add("Male")
            list.add("Female")
            setPopUpWindowTwo(binding.tvGenderSelect, requireContext(), list)
        }
        binding.tvSmokingSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindowTwo(binding.tvSmokingSelect, requireContext(), list)
        }
        binding.tvDrinkingSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindowTwo(binding.tvDrinkingSelect, requireContext(), list)
        }
        binding.tvPetsSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindowTwo(binding.tvPetsSelect, requireContext(), list)
        }
        binding.tvHeightSelect.setOnClickListener {
            val list = ArrayList<String>()
            val heights = resources.getStringArray(R.array.heights)
            list.addAll(heights)
            setPopUpWindow(binding.tvHeightSelect, requireContext(), list)
        }

        binding.tvQualificatSelect.setOnClickListener {
            val list = ArrayList<String>()
            list.add("High School")
            list.add("Bachelor's Degree")
            list.add("Master's Degree")
            list.add("Doctorate Degree")
            list.add("Other")
            setPopUpWindow(binding.tvQualificatSelect, requireContext(), list)
        }

        binding.tvInterestSelect.setOnClickListener {
            val list = ArrayList<String>()
            val interests = resources.getStringArray(R.array.interests)
            list.addAll(interests)
            setPopUpWindowTwo(binding.tvInterestSelect, requireContext(), list)
        }

        binding.tvSexualOrientationSelect.setOnClickListener {
            val list = ArrayList<String>()
            val sexualOrientation = resources.getStringArray(R.array.sexualOrientation)
            list.addAll(sexualOrientation)
            setPopUpWindow(binding.tvSexualOrientationSelect, requireContext(), list)
        }

        binding.tvAstrologicalSignSelect.setOnClickListener {
            val list = ArrayList<String>()
            val astrological = resources.getStringArray(R.array.astrologicalSign)
            list.addAll(astrological)
            setPopUpWindow(binding.tvAstrologicalSignSelect, requireContext(), list)
        }

        binding.etLocation.setOnClickListener {
            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.ADDRESS
            )
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            locationLauncher.launch(intent)
        }

        binding.btUpdate.setOnClickListener {
            val imagearray = ArrayList<String>()
            for(i in 0 until imageList.size){
                imagearray.add(imageList[i].image)
            }
            if (Validation().editProfileValidation(
                    requireActivity(),
                    binding.etName.text.toString().trim(),
                    binding.etEmail.text.toString().trim(),
                    binding.etMobile.text.toString().trim(),
                    binding.tvDOBSelect.text.toString().trim(),
                    binding.tvGenderSelect.text.toString().trim(),
                    binding.tvHeightSelect.text.toString().trim(),
                    binding.tvQualificatSelect.text.toString().trim(),
                    binding.etLocation.text.toString().trim(),
                    binding.tvInterestSelect.text.toString().trim(),
                    binding.tvSexualOrientationSelect.text.toString().trim(),
                    binding.tvAstrologicalSignSelect.text.toString().trim(),
                    binding.tvSmokingSelect.text.toString().trim(),
                    binding.tvDrinkingSelect.text.toString().trim(),
                    binding.tvPetsSelect.text.toString().trim(),
                    binding.etAddBio.text.toString().trim(),
                    imagearray
                )
            )
                if(imageResultPath.isEmpty() && profileImage.isEmpty()){
                    Toast.makeText(context, "Please select profile image", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            getEditProfile()


        }
    }

    private val locationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            Log.d("Place: ", place.name.toString() + " " + place.address)

            latitude = place.latLng!!.latitude.toString()
            longitude = place.latLng!!.longitude.toString()

            binding.etLocation.setText(place.address)
        }
    }

    private fun datePicker(context: Context) {
        DatePickerDialog(
            context, R.style.DialogTheme,
            date,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONDAY],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    override fun selectedImage(imagePath: String?, code: Int) {
        if (!imagePath.isNullOrEmpty()) {
            if (code == 0) {
                if (imageType == 1) {
                    imageResultPath = imagePath
                    Glide.with(context!!).load(imageResultPath).into(binding.rivUser)
                    val hasMap = HashMap<String, RequestBody>()
                    hasMap["type"] = createRequestBody("image")
                    hasMap["folder"] = createRequestBody("profile")

                    imageArrayList.add(prepareMultiPart("image", File(imageResultPath)))
                    profileViewModels.fileUpload(imageArrayList, hasMap)
                        .observe(viewLifecycleOwner, imageUploadObserver)

                }
            }
            else {
                imageList.add(EditProfileModel(imagePath.toString(),0))
                imagesAdapter?.notifyDataSetChanged()
            }

        }
    }

    private fun getEditProfile() {
        /*  val imagesList= ArrayList<MultipartBody.Part>()
          for(i in 0 until imageList.size){

              imagesList.add(prepareMultiPart("image", File(imageList[i].image)))

          }
          val hasMap = HashMap<String, RequestBody>()
          hasMap["type"] = createRequestBody("image")
          hasMap["folder"] = createRequestBody("user_images")

          profileViewModels.fileUpload(imagesList, hasMap).observe(viewLifecycleOwner, imageUploadObserver)
  */
        val arrStringMultipleImagesUploadable = ArrayList<String>()
        for (i in 0 until imageList.size) {
            if (imageList[i].image.contains("/uploads")) {
                arrStringMultipleImagesUploadable.remove(imageList[i].image)
            } else {
                arrStringMultipleImagesUploadable.add(imageList[i].image)
            }
        }
        if (arrStringMultipleImagesUploadable.size > 0) {
            val imagesList = ArrayList<MultipartBody.Part>()
            for (i in 0 until arrStringMultipleImagesUploadable.size) {

                imagesList.add(
                    prepareMultiPart(
                        "image",
                        File(arrStringMultipleImagesUploadable[i])
                    )
                )

            }
            val hasMap = HashMap<String, RequestBody>()
            hasMap["type"] = createRequestBody("image")
            hasMap["folder"] = createRequestBody("user_images")

            profileViewModels.fileUpload(imagesList, hasMap)
                .observe(viewLifecycleOwner, imageUploadObserver)
        }
        else{
            updateProfile()
        }

    }

    // for upload images
    private val imageUploadObserver = Observer<Resource<FileUploadResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()

                if (imageType == 2) {
                    val data = it.data!!.body
                    for (i in 0 until data.size) {
                        imageFileList.add(EditProfileRequestModel.EditProfileImage(data[i].image))
                    }
                    updateProfile()

                } else {
                    val data = it.data!!.body
                    profileImage = data[0].image
                }

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

    private fun updateProfile() {
        // for gender selects
        gender = if (binding.tvGenderSelect.text.toString() == "Male")
            1
        else
            2

        // for interested select
        interested = when {
            binding.tvInterestSelect.text.toString() == "Men" -> "1"
            binding.tvInterestSelect.text.toString() == "Women" -> "2"
            else -> "3"
        }

        val requestData = EditProfileRequestModel(
            binding.tvAstrologicalSignSelect.text.toString(),
            binding.etAddBio.text.toString(),
            binding.ccp.selectedCountryCodeWithPlus.toString(),
            binding.tvDOBSelect.text.toString(),
            gender,
            binding.tvDrinkingSelect.text.toString(),
            binding.etEmail.text.toString(),
            binding.tvHeightSelect.text.toString(),
            imageFileList,
            interested,
            binding.etLocation.text.toString(),
            binding.etName.text.toString(),
            binding.tvPetsSelect.text.toString(),
            binding.etMobile.text.toString(),
            profileImage,
            binding.tvQualificatSelect.text.toString(),
            binding.tvSexualOrientationSelect.text.toString(),
            binding.tvSmokingSelect.text.toString()
        )

        profileViewModels.editProfile(requestData).observe(viewLifecycleOwner, this)

    }

    // edit profile api response
    override fun onChanged(t: Resource<LoginResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                saveUser(requireContext(), t.data?.body!!)
                (activity as HomeActivity).manageHeaderView()
                activity?.onBackPressed()
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                if (t.message != "Invalid Authorization Key" || t.message != "Invalid authorization key")
                    showToast(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}