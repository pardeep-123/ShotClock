package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.adapters.ChatAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentChatBinding
import com.app.shotclock.utils.isVisible

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private var chatAdapter : ChatAdapter? = null

    override fun getViewBinding(): FragmentChatBinding {
        return FragmentChatBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickHandle()
        setAdapter()

    }

    private fun setAdapter() {
        chatAdapter = ChatAdapter()
        binding.rvChat.adapter = chatAdapter
    }

    private fun clickHandle() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()
        binding.tb.ivVideoCall.isVisible()
        binding.tb.ivBack.setOnClickListener {
           activity?.onBackPressed()
        }

        binding.tb.ivVideoCall.setOnClickListener {
            this.findNavController().navigate(R.id.action_chatFragment_to_videoCallFragment)
        }
    }

}