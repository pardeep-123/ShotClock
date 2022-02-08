package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.shotclock.R
import com.app.shotclock.activities.HomeActivity
import com.app.shotclock.adapters.HomeAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentHomeBinding
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var isbottom = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CacheConstants.Current = "home"
        handleClickListeners()

        val bottomSheet: ConstraintLayout = view.findViewById(R.id.bottom_sheet)
        val ivClose: ImageView = view.findViewById(R.id.ivClose)
        val btApply: MaterialButton = view.findViewById(R.id.btApply)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomBehave()

        binding.rvHome.adapter = HomeAdapter()

        // bottom sheet press close icon
        ivClose.setOnClickListener {
            bottomOpen()
        }

        btApply.setOnClickListener {
            bottomOpen()
        }


    }

/*    // for Age range set
    private fun ageSlider() {
        binding.sliderAge.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
            }

        })
    }*/


    private fun handleClickListeners() {

        binding.tb.ivMenu.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivFilter.isVisible()

        binding.tb.ivMenu.setOnClickListener {
            (activity as HomeActivity).openClose()
        }

        // for open bottom sheet
//        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetDialog.bottomSheet)

        binding.tb.ivFilter.setOnClickListener {
            bottomOpen()
        }

        binding.ivPlus.setOnClickListener {
//            if (binding.ivPlus.visibility == View.VISIBLE) {
            binding.clBottomBtn.isVisible()
            binding.ivPlus.isGone()

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