package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import com.app.shotclock.adapters.ChatAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentChatBinding

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    override fun getViewBinding(): FragmentChatBinding {
        return FragmentChatBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.rvChat.adapter = ChatAdapter()

    }

}