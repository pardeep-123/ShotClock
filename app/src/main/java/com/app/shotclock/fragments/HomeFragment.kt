package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.HomeAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentHomeBinding
import com.app.shotclock.utils.hideKeyboard
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var isbottom = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var rsHeight: RangeSlider? = null

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CacheConstants.Current = "home"
        handleClickListeners()

        // for back press in fragment
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //Handle back event from any fragment
                    activity?.finishAffinity()
                }
            })

        val bottomSheet: ConstraintLayout = view.findViewById(R.id.bottom_sheet)
        val ivClose: ImageView = view.findViewById(R.id.ivClose)
        val btApply: MaterialButton = view.findViewById(R.id.btApply)
        rsHeight = view.findViewById(R.id.rsHeight)
        val tvSixFeet: TextView = view.findViewById(R.id.tvSixFeet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomBehave()

//        rsHeight?.addOnChangeListener { _, value, _ ->
//            tvSixFeet.text = value.toInt().toString() + " ft"
//            // for api
////            distance = value.toInt().toString()
//
//            // Responds to when slider's value is changed
//        }

        binding.rvHome.adapter = HomeAdapter()

        // bottom sheet press close icon
        ivClose.setOnClickListener {
            bottomOpen()
        }

        btApply.setOnClickListener {
            bottomOpen()
        }


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
            bottomOpen()
        }

        binding.ivPlus.setOnClickListener {
//            if (binding.ivPlus.visibility == View.VISIBLE) {
            binding.ivPlus.isGone()
            binding.clBottomBtn.isVisible()

            binding.btCancel.setOnClickListener {
                binding.clBottomBtn.isGone()
                binding.ivPlus.isVisible()
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
    }

    private fun bottomOpen() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            isbottom = true
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            isbottom = false
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    private fun bottomBehave() {
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

}