package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.adapters.ChatAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentChatBinding
import com.app.shotclock.utils.isVisible

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    override fun getViewBinding(): FragmentChatBinding {
        return FragmentChatBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickHandle()
       binding.rvChat.adapter = ChatAdapter()

    }

    private fun clickHandle() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivVideoCall.isVisible()
        binding.tb.ivBack.setOnClickListener {
           activity?.onBackPressed()
        }

    }

}