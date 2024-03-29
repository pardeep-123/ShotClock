package com.app.shotclock.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.adapters.CompleteProfileImagesAdapter
import com.app.shotclock.cache.saveIsLoginString
import com.app.shotclock.cache.saveString
import com.app.shotclock.databinding.FragmentCompleteProfileBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.CompleteProfileRequestModel
import com.app.shotclock.models.CompleteProfileResponse
import com.app.shotclock.models.FileUploadResponse
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.LoginSignUpViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CompleteProfileFragment : ImagePickerUtility1<FragmentCompleteProfileBinding>(),
    Observer<Resource<CompleteProfileResponse>> {

    lateinit var loginSignUpViewModel: LoginSignUpViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val myCalendar = Calendar.getInstance()
    lateinit var date: DatePickerDialog.OnDateSetListener
    private var imageList = ArrayList<String>()
    private var completeProfileImagesAdapter: CompleteProfileImagesAdapter? = null
    private var latitude = ""
    private var longitude = ""
    private var gender = 1      // 1 = male, 2= female
    private var interested ="" // 1 = men, 2= women, 3 = both
    private var imageFileList = ArrayList<CompleteProfileRequestModel.Image>()

    override fun selectedImage(imagePath: String?, code: Int) {
        if (!imagePath.isNullOrEmpty()) {
            imageList.add(imagePath.toString())
            completeProfileImagesAdapter?.notifyDataSetChanged()
        }
    }

    override fun getViewBinding(): FragmentCompleteProfileBinding {
        return FragmentCompleteProfileBinding.inflate(layoutInflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Places.initialize(requireContext(), getString(R.string.api_key_map))

        configureViewModel()
        clicksHandle(view)
        completeProfileImageAdapter()

    }

    // complete profile images adapter
    private fun completeProfileImageAdapter() {
        completeProfileImagesAdapter = CompleteProfileImagesAdapter(requireContext(), imageList)
        binding.rvUserImages.adapter = completeProfileImagesAdapter

        completeProfileImagesAdapter?.onItemCLickListener = {
            if(imageList.size==5){
                Toast.makeText(requireContext(), "You can only select 5 images", Toast.LENGTH_SHORT).show()
            }else{
                getImage(requireActivity(), 0, false)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clicksHandle(view: View) {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvDOBSelect.setOnClickListener {
            date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.tvDOBSelect.text = longToDate(myCalendar.timeInMillis, "yyyy-MM-dd")
//                    edtIssuedOnDate.setText(AppUtils.longToDate(myCalendar.timeInMillis))
            }
            datePicker(requireContext())
        }

        binding.etAddBio.setOnClickListener {
            view.setOnTouchListener(View.OnTouchListener { v, event ->
                if (view.hasFocus()) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                            return@OnTouchListener true
                        }
                    }
                }
                false
            })
        }

        binding.btSubmit.setOnClickListener {
            if (Validation().completeProfileValidation(requireActivity(),
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
            ) {

                completeProfileData()
            }

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
            val yesNoList =resources.getStringArray(R.array.yesNoList)
            list.addAll(yesNoList)
            setPopUpWindowTwo(binding.tvDrinkingSelect, requireContext(), list)
        }
        binding.tvPetsSelect.setOnClickListener {
            val list = ArrayList<String>()
            val yesNoList =resources.getStringArray(R.array.yesNoList)
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

    private val locationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            Log.d("Place: ", place.name.toString() + " " + place.address)

            latitude = place.latLng!!.latitude.toString()
            longitude = place.latLng!!.longitude.toString()

            binding.etLocation.setText(place.address)
        }
    }

    private fun completeProfileData() {
        val list = ArrayList<MultipartBody.Part>()
        if (imageList.size > 0) {
            for (i in imageList) {
                val file = File(i)
                list.add(prepareMultiPart("image", file))
            }
            val hashMap = HashMap<String,RequestBody>()
            hashMap["type"] = createRequestBody("image")
            hashMap["folder"] = createRequestBody("user_images")
            loginSignUpViewModel.fileUpload(list,hashMap).observe(viewLifecycleOwner, fileUploadObserver)

        }
    }

    private fun configureViewModel() {
        loginSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginSignUpViewModel::class.java)
    }

    // complete profile api hit
    override fun onChanged(t: Resource<CompleteProfileResponse>) {
        when (t.status) {
            Status.SUCCESS -> {
                saveIsLoginString(requireActivity(),"true")
                binding.pb.clLoading.isGone()
                val dialog = Dialog(requireContext())
                with(dialog) {
                    setCancelable(false)
                    setContentView(R.layout.alert_dialog_submitted)
                    val btDone: MaterialButton = findViewById(R.id.btDone)
                    btDone.setOnClickListener {
                        saveString(requireContext(),t.data?.body?.authKey!!)
                        findNavController().navigate(R.id.action_completeProfileFragment_to_homeActivity)
                        dismiss()
                    }
                    show()
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

    private val fileUploadObserver = Observer<Resource<FileUploadResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()

                val data = it.data!!.body
                for (i in 0 until data.size) {
                    imageFileList.add(CompleteProfileRequestModel.Image(data[i].image))
                }

                // for gender select
                gender = if (binding.tvGenderSelect.text.toString() == "Male")
                    1
                else
                    2

                // for interested select
                interested = when {
                    binding.tvInterestSelect.text.toString() =="Men" -> "1"
                    binding.tvInterestSelect.text.toString() =="Women" -> "2"
                    else -> "3"
                }

                val requestData = CompleteProfileRequestModel(
                    binding.tvAstrologicalSignSelect.text.toString(),
                    binding.etAddBio.text.toString(),
                    binding.tvDOBSelect.text.toString(),
                    binding.tvDrinkingSelect.text.toString(),
                    gender,
                    binding.tvHeightSelect.text.toString().toFloat().toString(),
                    imageFileList,
                    interested,
                    latitude,
                    binding.etLocation.text.toString(),
                    longitude,
                    binding.tvPetsSelect.text.toString(),
                    binding.tvQualificatSelect.text.toString(),
                    binding.tvSexualOrientationSelect.text.toString(),
                    binding.tvSmokingSelect.text.toString())

                loginSignUpViewModel.completeProfile(requestData).observe(viewLifecycleOwner,this)

            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showErrorAlert(requireActivity(),it.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}