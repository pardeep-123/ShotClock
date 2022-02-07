package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.adapters.IcebreakerAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentIcebrakerQuestionsBinding
import com.app.shotclock.utils.isVisible


class IcebreakerQuestionsFragment : BaseFragment<FragmentIcebrakerQuestionsBinding>() {

    override fun getViewBinding(): FragmentIcebrakerQuestionsBinding {
        return FragmentIcebrakerQuestionsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()

        binding.rvIcebreaker.adapter = IcebreakerAdapter()

    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }


    }

}