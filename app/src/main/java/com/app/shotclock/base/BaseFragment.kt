package com.app.shotclock.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.app.shotclock.utils.showToast
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    var isLoaded = false

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        //   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = getViewBinding()
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        isLoaded = true
    }

    abstract fun getViewBinding(): VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showError(message: String) {
        showToast(message)
    }

}