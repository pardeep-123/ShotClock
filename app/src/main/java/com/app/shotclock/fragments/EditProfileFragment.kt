package com.app.shotclock.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.EditProfileImagesAdapter
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
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class EditProfileFragment : ImagePickerUtility1<FragmentEditProfileBinding>(),
    Observer<Resource<EditProfileResponse>> {

    lateinit var profileViewModels: ProfileViewModels

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val myCalendar = Calendar.getInstance()
    lateinit var date: DatePickerDialog.OnDateSetListener
    private var imageResultPath = ""
    private var imageList = ArrayList<String>()
    private var imagesAdapter: EditProfileImagesAdapter? = null
    private var latitude = ""
    private var longitude = ""
    private var gender = 1      // 1 = male, 2= female
    private var interested = "" // 1 = men, 2= women, 3 = both
    private var imageFileList = ArrayList<EditProfileRequestModel.EditProfileImage>()
    private var profileData : ProfileBody?= null

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

    private fun setEditProfileData(){
        Glide.with(requireContext()).load(ApiConstants.IMAGE_URL+profileData?.profileImage).into(binding.rivUser)
        binding.etName.setText( profileData?.username)
        binding.etEmail.setText(profileData?.email)
        binding.ccp.registerCarrierNumberEditText(binding.etMobile)
        binding.ccp.fullNumber = profileData?.countryCode
        binding.etMobile.setText(profileData?.phone)
        binding.tvDOBSelect.text = profileData?.dateofbirth
        var genderValue =""
        genderValue = if (profileData?.gender==1)
            "Male"
        else
            "Female"
        binding.tvGenderSelect.text = genderValue

        binding.tvHeightSelect.text = profileData?.height
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
            2 -> binding.tvInterestSelect.text="Women"
            else -> binding.tvInterestSelect.text = "Others"
        }

        for (i in 0 until profileData?.user_images!!.size ){
            imageList.add(profileData?.user_images!![i].image_path)
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
            getImage(requireActivity(), 1, false)
        }

        binding.ivCamera.setOnClickListener {
            getImage(requireActivity(), 0, false)
        }

        binding.tvGenderSelect.setOnClickListener {
            val list = ArrayList<String>()
            list.add("Male")
            list.add("Female")
            setPopUpWindow(binding.tvGenderSelect, requireContext(), list)
        }
        binding.tvSmokingSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindow(binding.tvSmokingSelect, requireContext(), list)
        }
        binding.tvDrinkingSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindow(binding.tvDrinkingSelect, requireContext(), list)
        }
        binding.tvPetsSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList = resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindow(binding.tvPetsSelect, requireContext(), list)
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
            setPopUpWindow(binding.tvInterestSelect, requireContext(), list)
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
            if (Validation().editProfileValidation(
                    requireActivity(),
                    imageResultPath,
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
                    imageList
                )
            )
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
                imageResultPath = imagePath
                Glide.with(context!!).load(imageResultPath).into(binding.rivUser)
            } else {
                imageList.add(imagePath.toString())
                imagesAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun getEditProfile() {
        val imageList = ArrayList<MultipartBody.Part>()
        imageList.add(prepareMultiPart("image", File(imageResultPath)))
        profileViewModels.fileUpload(imageList).observe(viewLifecycleOwner, imageUploadObserver)

    }

    // for upload images
    private val imageUploadObserver = Observer<Resource<FileUploadResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()

                val data = it.data!!.body
                for (i in 0 until data.size) {
                    imageFileList.add(EditProfileRequestModel.EditProfileImage(data[i].image))
                }

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
                    imageResultPath,
                    binding.tvQualificatSelect.text.toString(),
                    binding.tvSexualOrientationSelect.text.toString(),
                    binding.tvSmokingSelect.text.toString()
                )

                profileViewModels.editProfile(requestData).observe(viewLifecycleOwner, this)

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

    // edit profile api response
    override fun onChanged(t: Resource<EditProfileResponse>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                (activity as HomeActivity).manageHeaderView()
                activity?.onBackPressed()

            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showToast(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}