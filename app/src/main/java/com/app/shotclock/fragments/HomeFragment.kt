package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavOptions
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
import com.app.shotclock.utils.Constants
import com.app.shotclock.utils.hideKeyboard
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeAdapter.ShowTick {
    private var isbottom = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetBehavior2: BottomSheetBehavior<ConstraintLayout>
    private var rsHeight: RangeSlider? = null
    private var adapter: HomeAdapter? = null
    private var selectHeightText = ""
    private var astrologicalAdapter: SexualOrientationAdapter? = null
    private var itemHeightBottomAdapter: HeightBottomSheetAdapter? = null
    private var orientationList = ArrayList<String>()
    private var astrologicalList = ArrayList<String>()

    private var tvHeightSelect: TextView? = null

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CacheConstants.Current = "home"
        handleClickListeners()
        filterBottomSheet(view)
        itemHeightBottomSheet(view)
        handleHomeFragmentBackPress()

        adapter = HomeAdapter(this)
        binding.rvHome.adapter = adapter


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
         tvHeightSelect = view.findViewById(R.id.tvHeightSelect)
        // add list education adapter
        val educationList = ArrayList<String>()
        educationList.add("High School")
        educationList.add("Bachelor's Degree")
        educationList.add("Master's Degree")
        educationList.add("Doctorate Degree")
        educationList.add("Other")
        astrologicalAdapter = SexualOrientationAdapter(requireContext(),educationList)
        rvEducation.adapter = astrologicalAdapter

        // add list astrologicalSign adapter
        val astrologicalSignList = resources.getStringArray(R.array.astrologicalSign)
        astrologicalList.addAll(astrologicalSignList)
        rvAstrologicalSign.adapter = SexualOrientationAdapter(requireContext(),astrologicalList)
        // add list sexualOrientation adapter
        val orientation = resources.getStringArray(R.array.sexualOrientation)
        orientationList.addAll(orientation)
        rvSexualOrientation.adapter = SexualOrientationAdapter(requireContext(),orientationList)


//        rsHeight = view.findViewById(R.id.rsHeight)
//        val tvSixFeet: TextView = view.findViewById(R.id.tvSixFeet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomBehave(bottomSheetBehavior)
        // bottom sheet press close icon
        ivClose.setOnClickListener {
            bottomOpen(bottomSheetBehavior)
        }

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
    private fun ageSlider() {
        rsHeight?.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }
            override fun onStopTrackingTouch(slider: RangeSlider) {
            }
        })

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
//        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetDialog.bottomSheet)

        binding.tb.ivFilter.setOnClickListener {
            Constants.BottomSheet = true
            bottomOpen(bottomSheetBehavior)
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
                Constants.isPlus = false
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

    }

    private fun bottomOpen(bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>) {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            isbottom = true
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            isbottom = false
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

}