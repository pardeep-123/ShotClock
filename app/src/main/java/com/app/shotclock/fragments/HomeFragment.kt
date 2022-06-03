package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.HeightBottomSheetAdapter
import com.app.shotclock.adapters.HomeAdapter
import com.app.shotclock.adapters.SexualOrientationAdapter
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentHomeBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.*
import com.app.shotclock.models.sockets.VideoCallResponse
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

open class HomeFragment : LocationUpdateUtility<FragmentHomeBinding>(),
    Observer<Resource<HomeResponseModel>>, SocketManager.Observer {

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var isBottom = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetBehavior2: BottomSheetBehavior<ConstraintLayout>
    private var rsAge: RangeSlider? = null
    private var rsDistance: RangeSlider? = null
    private var adapter: HomeAdapter? = null
    private var itemHeightBottomAdapter: HeightBottomSheetAdapter? = null
    private var orientationList = ArrayList<EduSexualOrientationModel>()
    private var astrologicalList = ArrayList<EduSexualOrientationModel>()
    private var tvHeightSelect: TextView? = null
    private var tvClear: TextView? = null

    //    private var educationList = ArrayList<String>()
    private var educationList = ArrayList<EduSexualOrientationModel>()
    private var rgSmoke: RadioGroup? = null
    private var rgDrink: RadioGroup? = null
    private var rgPets: RadioGroup? = null
    private var rgGender: RadioGroup? = null
    private var rvHeightBottomSheet: RecyclerView? = null
    private var latitude = ""
    private var longitude = ""
    private var education = ""
    private var sexualOrientation = ""
    private var astrologicalSign = ""
    private var lowerAge = ""
    private var upperAge = ""
    private var lowerDistance = 0
    private var upperDistance = 0
    private var gender = 0
    private var smoke = ""
    private var pets = ""
    private var drink = ""
    private val list = ArrayList<String>()
    private var idList = ArrayList<SelectionDoneRequestModel.SelectionDoneUser>()
    private var homeList = ArrayList<HomeResponseModel.HomeBody>()

    private lateinit var socketManager: SocketManager
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private var orientationAdapter: SexualOrientationAdapter? = null
    private var educationAdapter: SexualOrientationAdapter? = null
    private var astrologicalAdapter: SexualOrientationAdapter? = null


    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CacheConstants.Current = "home"

        rvHeightBottomSheet = view.findViewById(R.id.rvHeightBottomSheet)
//        initializeSocket()
//        activateReceiverListenerSocket()

        configureViewModel()
        handleClickListeners()
        filterBottomSheet(view)
        itemHeightBottomSheet(view)
        rangeSliders()
        setAdapter()
        getLiveLocation(requireActivity())
        Constants.isPlus = false
    }

    private fun initializeSocket() {
        socketManager = App.mInstance.getSocketManager()!!
        if (!socketManager.isConnected() || socketManager.getmSocket() == null) {
            socketManager.init()
        }
        socketManager.unRegister(this)
        socketManager.onRegister(this)
    }

//    private fun activateReceiverListenerSocket() {
//        socketManager.callToUserActivate()
//    }

    private fun setAdapter() {
        adapter = HomeAdapter(requireContext(), homeList)
        binding.rvHome.adapter = adapter
    }

    private fun configureViewModel() {
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

    }

    private fun itemHeightBottomSheet(view: View) {
        val heightBottomSheetLayout: ConstraintLayout = view.findViewById(R.id.heightBottomSheet)
        bottomSheetBehavior2 = BottomSheetBehavior.from(heightBottomSheetLayout)

        val heightList = resources.getStringArray(R.array.heights)
        list.addAll(heightList)
        itemHeightBottomAdapter = HeightBottomSheetAdapter(list)
        rvHeightBottomSheet?.adapter = itemHeightBottomAdapter
        itemHeightBottomAdapter?.itemClickListener = { pos ->
            bottomOpen(bottomSheetBehavior2)
            tvHeightSelect?.text = list[pos]
        }

        bottomBehave(bottomSheetBehavior2)

    }

    // method open filter bottom sheet
    private fun filterBottomSheet(view: View) {
        val bottomSheet: ConstraintLayout = view.findViewById(R.id.bottom_sheet)
        val ivClose: ImageView = view.findViewById(R.id.ivClose)
        val btApply: MaterialButton = view.findViewById(R.id.btApply)
        val rvEducation: RecyclerView = view.findViewById(R.id.rvEducation)
        val rvSexualOrientation: RecyclerView = view.findViewById(R.id.rvSexualOrientation)
        val rvAstrologicalSign: RecyclerView = view.findViewById(R.id.rvAstrologicalSign)
        rgGender = view.findViewById(R.id.rgGender)
        rgSmoke  = view.findViewById(R.id.rgSmoke)
        tvClear  = view.findViewById(R.id.tvClear)
        rgDrink = view.findViewById(R.id.rgDrink)
        rgPets = view.findViewById(R.id.rgPets)
        rsAge = view.findViewById(R.id.rsAge)
        rsDistance = view.findViewById(R.id.rsDistance)
        tvHeightSelect = view.findViewById(R.id.tvHeightSelect)
        // add list education adapter

        // radio group gender
        rgGender?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbMale)
                gender = 1
            else if (checkedId == R.id.rbFemale)
                gender = 2
        }

        // radio group smoke
        rgSmoke?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbSmokeYes)
                smoke = "Yes"
            else if (checkedId == R.id.rbSmokeNo)
                smoke = "No"
        }
        // radio group drink
        rgDrink?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbDrinkYes)
                drink = "Yes"
            else if (checkedId == R.id.rbDrinkNo)
                drink = "No"
        }
        // radio group pets
        rgPets?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbPetsYes)
                pets = "Yes"
            else if (checkedId == R.id.rbPetsNo)
                pets = "No"
        }

        // add list education adapter
        educationList.clear()
        educationList.add(EduSexualOrientationModel("High School", false))
        educationList.add(EduSexualOrientationModel("Bachelor's Degree", false))
        educationList.add(EduSexualOrientationModel("Master's Degree", false))
        educationList.add(EduSexualOrientationModel("Doctorate Degree", false))
        educationList.add(EduSexualOrientationModel("Other", false))

        educationAdapter = SexualOrientationAdapter(requireContext(), educationList)
        rvEducation.adapter = educationAdapter

        // add list astrologicalSign adapter
        astrologicalList.clear()
        astrologicalList.add(EduSexualOrientationModel("Aquarius", false))
        astrologicalList.add(EduSexualOrientationModel("Pisces", false))
        astrologicalList.add(EduSexualOrientationModel("Aries", false))
        astrologicalList.add(EduSexualOrientationModel("Taurus", false))
        astrologicalList.add(EduSexualOrientationModel("Gemini", false))
        astrologicalList.add(EduSexualOrientationModel("Cancer", false))
        astrologicalList.add(EduSexualOrientationModel("Leo", false))
        astrologicalList.add(EduSexualOrientationModel("Virgo", false))
        astrologicalList.add(EduSexualOrientationModel("Libra", false))
        astrologicalList.add(EduSexualOrientationModel("Scorpio", false))
        astrologicalList.add(EduSexualOrientationModel("Sagittarius", false))
        astrologicalList.add(EduSexualOrientationModel("Capricorn", false))

        astrologicalAdapter = SexualOrientationAdapter(requireContext(), astrologicalList)
        rvAstrologicalSign.adapter = astrologicalAdapter

        // add list sexualOrientation adapter
        orientationList.clear()
        orientationList.add(EduSexualOrientationModel("Straight", false))
        orientationList.add(EduSexualOrientationModel("Gay", false))
        orientationList.add(EduSexualOrientationModel("Lesbian", false))
        astrologicalList.add(EduSexualOrientationModel("Bisexual", false))
        orientationList.add(EduSexualOrientationModel("Demisexual", false))
        orientationList.add(EduSexualOrientationModel("Pansexual", false))

        orientationAdapter = SexualOrientationAdapter(requireContext(), orientationList)
        rvSexualOrientation.adapter = orientationAdapter

//        educationAdapter.onItemClickListener = { pos ->
//            education = pos
//        }

//        astrologicalAdapter.onItemClickListener = { pos ->
//            astrologicalSign = pos
//        }

//        orientationAdapter.onItemClickListener = { pos ->
//            sexualOrientation = pos
//        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomBehave(bottomSheetBehavior)
        // bottom sheet press close icon
        ivClose.setOnClickListener {
            bottomOpen(bottomSheetBehavior)
        }

        // hit filter api
        btApply.setOnClickListener {
            setFilterData()
        }

        tvHeightSelect?.setOnClickListener {
            Constants.BottomSheet = false
            bottomOpen(bottomSheetBehavior2)
        }

        tvClear?.setOnClickListener {
            Constants.isPlus = false
            rgSmoke?.clearCheck()
            rgPets?.clearCheck()
            rgDrink?.clearCheck()
            rgGender?.clearCheck()
            rsAge?.values = mutableListOf(0F, 100F)
            rsDistance?.values = mutableListOf(0F, 100F)
            astrologicalSign = ""
            education = ""
            sexualOrientation=""

            educationList.forEach {
                it.isSelected = false
            }
            orientationList.forEach {
                it.isSelected = false
            }
            astrologicalList.forEach {
                it.isSelected = false
            }

            educationAdapter?.notifyDataSetChanged()
            orientationAdapter?.notifyDataSetChanged()
            astrologicalAdapter?.notifyDataSetChanged()

            /*val educationAdapter = SexualOrientationAdapter(requireContext(), educationList)
            rvEducation.adapter = educationAdapter
            val orientationAdapter = SexualOrientationAdapter(requireContext(), orientationList)
            rvSexualOrientation.adapter = orientationAdapter
            val astrologicalAdapter = SexualOrientationAdapter(requireContext(), astrologicalList)
            rvAstrologicalSign.adapter = astrologicalAdapter*/
            //    astrologicalList.clear()
            //  orientationList.clear()
            tvHeightSelect?.text = "Select"
            bottomOpen(bottomSheetBehavior)
            homeViewModel.homeApi(latitude, longitude).observe(viewLifecycleOwner, this)
        }

    }

    // for Age range set
    private fun rangeSliders() {
        rsAge?.addOnChangeListener(RangeSlider.OnChangeListener { _, _, _ ->
            try {

                val startValue = rsAge?.values?.get(0)
                val endValue = rsAge?.values?.get(1)

                lowerAge = startValue?.toInt().toString()
                upperAge = endValue?.toInt().toString()

            }catch (e:Exception){

            }
        })

        rsDistance?.addOnChangeListener(RangeSlider.OnChangeListener { _, _, _ ->
            try {
                val startValue = rsDistance?.values?.get(0)
                val endValue = rsDistance?.values?.get(1)

                lowerDistance = startValue?.toInt()!!
                upperDistance = endValue?.toInt()!!
            } catch (e: Exception) {

            }
        })
        // for  label Age range set
        rsAge?.setLabelFormatter { value: Float ->
            value.toInt().toString()

        }

        rsAge?.isTickVisible = true
        rsDistance?.setLabelFormatter { value: Float ->
            value.toInt().toString()
        }

    }

    private fun handleClickListeners() {
        binding.tb.ivMenu.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivFilter.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
            hideKeyboard(it, requireActivity())
        }

        // for open bottom sheet
        binding.tb.ivFilter.setOnClickListener {
            Constants.BottomSheet = true
            bottomOpen(bottomSheetBehavior)
        }

        binding.ivPlus.setOnClickListener {
            Constants.isPlus = true
            if (homeList.size == 0){
                showErrorAlert(requireActivity(),getString(R.string.please_select_5_users))
            }else {
                binding.ivPlus.isGone()
                binding.clBottomBtn.isVisible()
                adapter?.notifyDataSetChanged()
            }
        }

        binding.btCancel.setOnClickListener {
            Constants.isPlus = false
            homeList.forEach {
                it.isSelect = false
            }
            binding.clBottomBtn.isGone()
            binding.ivPlus.isVisible()
            adapter?.notifyDataSetChanged()
        }

        binding.btDone.setOnClickListener {
            idList.clear()
            homeList.forEach {
                if (it.isSelect) {
                    idList.add(SelectionDoneRequestModel.SelectionDoneUser(it.id.toString()))
                }
            }

            when {
                idList.size == 0 -> {
                    showErrorAlert(requireActivity(),getString(R.string.please_select_5_users))
                }
                idList.size < 5 -> {
                    showErrorAlert(requireActivity(),getString(R.string.please_select_maximum_5_users))
                }
                idList.size > 5 -> {
                    showErrorAlert(requireActivity(),getString(R.string.please_select_5_users))
                }
                else -> {
                    Constants.isPlus = false
                    val data = SelectionDoneRequestModel(idList)
                    homeViewModel.selectionDone(data).observe(viewLifecycleOwner, selectionDoneObserver)
                }
            }
        }

    }

    // home api
    override fun onChanged(t: Resource<HomeResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                homeList.clear()
                homeList.addAll(t.data?.body!!)
                adapter?.notifyDataSetChanged()
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                if (t.message!="Invalid Authorization Key" || t.message!= "Invalid authorization key")
                showToast(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

    private fun setFilterData() {
        var astrologicalSignSelected = ""
        var sexualOrientationSelected = ""
        var educationSelected = ""
        astrologicalList.forEach {
            if (it.isSelected == true) {
                astrologicalSign = astrologicalSign + it.name + ","
            }
        }

        orientationList.forEach {
            if (it.isSelected == true) {
                sexualOrientation = sexualOrientation + it.name + ","
            }
        }

        educationList.forEach {
            if (it.isSelected == true) {
                education = education + it.name + ","
            }
        }


        if (astrologicalSign.isNotEmpty()) {
            astrologicalSignSelected = astrologicalSign.substring(0, (astrologicalSign.length - 1))
        } else {
            astrologicalSignSelected = ""
        }


        if (sexualOrientation.isNotEmpty()) {
            sexualOrientationSelected =
                sexualOrientation.substring(0, (sexualOrientation.length - 1))
        }
        else {
            sexualOrientationSelected = ""
        }

        if (education.isNotEmpty()) {
            educationSelected =
                education.substring(0, (education.length - 1))
        } else {
            educationSelected = ""
        }


        val body = FilterRequestModel(
            astrologicalSignSelected,
            drink,
            gender,
            tvHeightSelect?.text.toString().trim(),
            latitude,
            longitude,
            lowerAge,
            lowerDistance,
            pets,
            educationSelected,
            sexualOrientationSelected,
            smoke,
            upperAge,
            upperDistance
        )
        homeViewModel.filterApi(body).observe(viewLifecycleOwner, filterApiObserver)

    }

    // filter api
    private val filterApiObserver = Observer<Resource<HomeResponseModel>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                bottomOpen(bottomSheetBehavior)
                homeList.clear()
                homeList.addAll(it.data?.body!!)
                adapter?.notifyDataSetChanged()

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

    // selection done api
    private val selectionDoneObserver = Observer<Resource<SelectionDoneResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                val dialog = Dialog(requireContext())
                with(dialog) {
                    setCancelable(false)
                    setContentView(R.layout.alert_dialog_home)
                    val btDone: MaterialButton = findViewById(R.id.btDone)
                    btDone.setOnClickListener {
                        findNavController().navigate(R.id.action_homeFragment_to_myRequestsFragment)
                        dismiss()
                    }
                    show()
                }
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

    // set lat lng and hit home api
    override fun updatedLatLng(lat: Double, lng: Double) {
        latitude = lat.toString()
        longitude = lng.toString()
        try {
            if (homeViewModel != null && viewLifecycleOwner != null)
                homeViewModel.homeApi(latitude, longitude).observe(viewLifecycleOwner, this)


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        stopLocationUpdates()
    }

    private fun bottomOpen(bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>) {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            isBottom = true
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            isBottom = false
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    private fun bottomBehave(bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>) {
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

    override fun onResponseArray(event: String, args: JSONArray) {

    }

    override fun onResponse(event: String, args: JSONObject) {
        when (event) {
            SocketManager.call_to_user_listener -> {
                activityScope.launch {
                    val mObject = args as JSONObject
                    val gson = GsonBuilder().create()
                    val userToCallList = gson.fromJson(mObject.toString(), VideoCallResponse::class.java)

                    /*              val options = NavOptions.Builder().setPopUpTo(R.id.incomingCallActivity,true).build()
                                  val bundle = Bundle()
                                  bundle.putString("channelName",userToCallList.channelName)
                                  findNavController(R.id.fragment).navigate(R.id.incomingCallActivity,bundle,options)*/


                    //// latest
//                    val intent = Intent(requireContext(),IncomingCallActivity::class.java)
//                    intent.putExtra("channelName",userToCallList.channelName)
//                    startActivity(intent)

                }
            }
        }
    }

    override fun onError(event: String, vararg args: Array<*>) {

    }

}