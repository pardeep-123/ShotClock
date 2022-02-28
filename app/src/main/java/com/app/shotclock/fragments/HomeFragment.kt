package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentHomeBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.BaseResponseModel
import com.app.shotclock.models.HomeResponseModel
import com.app.shotclock.models.SelectionDoneRequestModel
import com.app.shotclock.models.SelectionDoneResponse
import com.app.shotclock.utils.*
import com.app.shotclock.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider
import javax.inject.Inject


open class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeAdapter.ShowTick,
    Observer<Resource<HomeResponseModel>> {

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var isBottom = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetBehavior2: BottomSheetBehavior<ConstraintLayout>
    private var rsAge: RangeSlider? = null
    private var rsDistance: RangeSlider? = null
    private var adapter: HomeAdapter? = null
    private var selectHeightText = ""
    private var astrologicalAdapter: SexualOrientationAdapter? = null
    private var itemHeightBottomAdapter: HeightBottomSheetAdapter? = null
    private var orientationList = ArrayList<String>()
    private var astrologicalList = ArrayList<String>()
    private var tvHeightSelect: TextView? = null
    private var educationList = ArrayList<String>()
    private var rgSmoke: RadioGroup?=null
    private var rgDrink: RadioGroup?=null
    private var rgPets: RadioGroup?=null
    private var latitude = "30.4370"
    private var longitude = "76.7179"
    private var education = ""
    private var sexualOrientation = ""
    private var astrologicalSign = ""
    private var lowerAge = ""
    private var upperAge = ""
    private var lowerDistance = ""
    private var upperDistance = ""
    private var gender = 0
    private var smoke =""
    private var pets =""
    private var drink =""
    private var idList = ArrayList<SelectionDoneRequestModel.SelectionDoneUser>()

    private var homeList = ArrayList<HomeResponseModel.HomeBody>()

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CacheConstants.Current = "home"
        configureViewModel()
        handleClickListeners()
        filterBottomSheet(view)
        itemHeightBottomSheet(view)
        handleHomeFragmentBackPress()
        rangeSliders()


        adapter = HomeAdapter(requireContext(), this, homeList)
        binding.rvHome.adapter = adapter





    }

    private fun configureViewModel() {
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.homeApi("30.7046", "76.7179").observe(viewLifecycleOwner, this)
    }

    private fun itemHeightBottomSheet(view: View) {
        val heightBottomSheetLayout: ConstraintLayout = view.findViewById(R.id.heightBottomSheet)
        bottomSheetBehavior2 = BottomSheetBehavior.from(heightBottomSheetLayout)
        val rvHeightBottomSheet: RecyclerView = view.findViewById(R.id.rvHeightBottomSheet)
        val list = ArrayList<String>()
        val heightList = resources.getStringArray(R.array.heights)
        list.addAll(heightList)
        itemHeightBottomAdapter = HeightBottomSheetAdapter(list)
        rvHeightBottomSheet.adapter = itemHeightBottomAdapter
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
        val rgGender: RadioGroup = view.findViewById(R.id.rgGender)
        rgSmoke  = view.findViewById(R.id.rgSmoke)
        rgDrink = view.findViewById(R.id.rgDrink)
        rgPets = view.findViewById(R.id.rgPets)
        rsAge = view.findViewById(R.id.rsAge)
        rsDistance = view.findViewById(R.id.rsDistance)

        val rbMale: RadioButton = view.findViewById(R.id.rbMale)
        val rbFemale: RadioButton = view.findViewById(R.id.rbFemale)
        val rbSmokeYes: RadioButton = view.findViewById(R.id.rbSmokeYes)
        val rbSmokeNo: RadioButton = view.findViewById(R.id.rbSmokeNo)
        val rbDrinkYes: RadioButton = view.findViewById(R.id.rbDrinkYes)
        val rbDrinkNo: RadioButton = view.findViewById(R.id.rbDrinkNo)
        val rbPetsYes: RadioButton = view.findViewById(R.id.rbPetsYes)
        val rbPetsNo: RadioButton = view.findViewById(R.id.rbPetsNo)

        tvHeightSelect = view.findViewById(R.id.tvHeightSelect)
        // add list education adapter

        // radio group gender
        rgGender.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbMale) {
                gender = 1
            } else if (checkedId == R.id.rbFemale) {
                gender = 2
            }
        }

        // radio group smoke
        rgSmoke?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbSmokeYes) {
                smoke = "Yes"
            } else if (checkedId == R.id.rbSmokeNo) {
                smoke = "No"
            }
        }

        rgDrink?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbDrinkYes){
                drink = "Yes"
            }else if (checkedId ==R.id.rbDrinkNo)
                drink ="No"
        }

        rgPets?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbPetsYes){
                pets = "Yes"
            }else if (checkedId == R.id.rbPetsNo){
                pets = "No"
            }
        }

        // add list education adapter
        educationList.add("High School")
        educationList.add("Bachelor's Degree")
        educationList.add("Master's Degree")
        educationList.add("Doctorate Degree")
        educationList.add("Other")
        val educationAdapter = SexualOrientationAdapter(requireContext(), educationList,"education")
        rvEducation.adapter = educationAdapter

        // add list astrologicalSign adapter
        val astrologicalSignList = resources.getStringArray(R.array.astrologicalSign)
        astrologicalList.addAll(astrologicalSignList)
        val astrologicalAdapter = SexualOrientationAdapter(requireContext(), astrologicalList,"astro")
        rvAstrologicalSign.adapter = astrologicalAdapter

        // add list sexualOrientation adapter
        val orientation = resources.getStringArray(R.array.sexualOrientation)
        orientationList.addAll(orientation)
        val orientationAdapter = SexualOrientationAdapter(requireContext(), orientationList,"sexual")
        rvSexualOrientation.adapter = orientationAdapter


        educationAdapter.onItemClickListener = {pos ->
            education = pos
        }

        astrologicalAdapter.onItemClickListener = {pos ->
            astrologicalSign = pos
        }

        orientationAdapter.onItemClickListener = {pos ->
            sexualOrientation = pos
        }

//        rsHeight = view.findViewById(R.id.rsHeight)
//        val tvSixFeet: TextView = view.findViewById(R.id.tvSixFeet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomBehave(bottomSheetBehavior)
        // bottom sheet press close icon
        ivClose.setOnClickListener {
            bottomOpen(bottomSheetBehavior)
        }

        // hit filter api
        btApply.setOnClickListener {
            bottomOpen(bottomSheetBehavior)
        }

        tvHeightSelect?.setOnClickListener {
            Constants.BottomSheet = false
            bottomOpen(bottomSheetBehavior2)
        }
//        selectHeightText = tvHeightSelect.text.toString()

    }

    // for Age range set
    private fun rangeSliders() {
        rsAge?.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            try {
                val startValue = rsAge?.values?.get(0)
                val endValue = rsAge?.values?.get(1)

                lowerAge = startValue?.toInt().toString()
                upperAge = endValue?.toInt().toString()

            }catch (e:Exception){

            }
        })

        rsDistance?.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            try {
                val startValue = rsDistance?.values?.get(0)
                val endValue = rsDistance?.values?.get(1)

                lowerDistance = startValue?.toInt().toString()
                upperDistance = endValue?.toInt().toString()
            }catch (e:Exception){

            }
        })
        // for Age range set
        rsAge?.setLabelFormatter { value: Float ->

            value.toInt().toString()
        }
        rsDistance?.setLabelFormatter { value: Float ->

            value.toInt().toString()
        }

    }

/*    rsAge?.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
        try {
            val start_values = rangeSlider.values[0]
            val end_values = rangeSlider.values[1]
            tvValues.text = start_values.toInt().toString() + "-" + end_values.toInt().toString()
            minAge = start_values.toInt().toString()
            maxAge = end_values.toInt().toString()
        } catch (e: Exception) {
        }

//            tvValues.text = values.
    })*/

    private fun handleClickListeners() {
        binding.tb.ivMenu.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivFilter.isVisible()
        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
            hideKeyboard(it, requireActivity())
        }

        // for open bottom sheet
//        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetDialog.bottomSheet)

        binding.tb.ivFilter.setOnClickListener {
            Constants.BottomSheet = true
            bottomOpen(bottomSheetBehavior)

//            findNavController().navigate(R.id.action_homeFragment_to_subscriptionFragment)
        }

        binding.ivPlus.setOnClickListener {
//            if (binding.ivPlus.visibility == View.VISIBLE) {
            Constants.isPlus = true
            binding.ivPlus.isGone()
            binding.clBottomBtn.isVisible()
            adapter?.notifyDataSetChanged()
        }

        binding.btCancel.setOnClickListener {
            Constants.isPlus = false
            binding.clBottomBtn.isGone()
            binding.ivPlus.isVisible()
            adapter?.notifyDataSetChanged()
        }
//                binding.btDone.isGone()
//                binding.btCancel.isGone()
//                binding.ivPlus.isVisible()
//            }else{
//
//            }

//            binding.bottomSheetDialog.btApply.setOnClickListener {
//                activity?.onBackPressed()
//            }

        binding.btDone.setOnClickListener {
            Log.e("=====", idList.size.toString())
            Constants.isPlus = false
            val data = SelectionDoneRequestModel(idList)
            homeViewModel.selectionDone(data).observe(viewLifecycleOwner, selectionDoneObserver)

        }
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

    //        for back press in fragment
    private fun handleHomeFragmentBackPress() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //Handle back event from any fragment
                    activity?.finishAffinity()
                }
            })
    }

    // home api
    override fun onChanged(t: Resource<HomeResponseModel>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                homeList.addAll(t.data?.body!!)
                adapter?.notifyDataSetChanged()

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

//    protected fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        setSliderTooltipAlwaysVisible(this)
//    }

    private fun setFilterData() {
        if (gender == 1){

        }
//        val body = FilterRequestModel(tvHeightSelect?.text.toString().trim())

    }

    // filter api
    private val filterApiObserver = Observer<Resource<BaseResponseModel>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()

            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showError(it.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

    override fun showClick(pos: Int, id: String) {
        idList.add(SelectionDoneRequestModel.SelectionDoneUser(id))
        Log.e("====size", idList.size.toString())

        if(idList.size>5){
            showToast("You cannot add more than 5 persons.")
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
                showError(it.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }


}