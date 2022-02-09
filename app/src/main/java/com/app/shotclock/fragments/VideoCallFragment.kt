package com.app.shotclock.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.constants.CacheConstants
import com.app.shotclock.databinding.FragmentVideoCallBinding
import com.app.shotclock.utils.isVisible
import com.google.android.material.button.MaterialButton


class VideoCallFragment : BaseFragment<FragmentVideoCallBinding>() {

    override fun getViewBinding(): FragmentVideoCallBinding {
        return FragmentVideoCallBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClicks()

    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvIcebreaker.setOnClickListener {
            this.findNavController()
                .navigate(R.id.action_videoCallFragment_to_icebreakerQuestionsFragment)
        }

        binding.tvCancel.setOnClickListener {
            val dialog = Dialog(requireContext())
            with(dialog) {
                setCancelable(false)
                setContentView(R.layout.alert_dialog_cancel_call)

                val btOk: MaterialButton = findViewById(R.id.btYes)
                val btNo: MaterialButton = findViewById(R.id.btNo)

                btOk.setOnClickListener {
                    dismiss()
                }
                btNo.setOnClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

}