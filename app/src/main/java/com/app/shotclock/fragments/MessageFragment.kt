package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.app.shotclock.R
import com.app.shotclock.adapters.MessagesAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentMessageBinding


class MessageFragment : BaseFragment<FragmentMessageBinding>(),MessagesAdapter.ClickMessage {

    override fun getViewBinding(): FragmentMessageBinding {
        return FragmentMessageBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMessages.adapter = MessagesAdapter(this@MessageFragment)
    }

    // message interface
    override fun onClick() {
        this.findNavController().navigate(R.id.action_messageFragment_to_chatFragment)
    }

}