package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.app.shotclock.R
import com.app.shotclock.adapters.HomeAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentHomeBinding
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.RangeSlider

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbarHome.ivFilter.isVisible()

        binding.toolbarHome.ivFilter.setOnClickListener {


        }


        binding.rvHome.adapter = HomeAdapter()

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

/*    // for Age range set
    private fun ageSlider() {
        binding.sliderAge.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
            }

        })
    }*/


}