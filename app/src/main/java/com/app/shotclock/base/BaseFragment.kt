package com.app.shotclock.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    var baseView: View? = null
    private var _binding: VB? = null
    val binding get() = _binding!!
    var isLoaded = false
//    lateinit var binding: VB

//    @LayoutRes
//    protected abstract fun getLayoutRes(): Int

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        //   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
//        return binding.root
//        _binding = inflate.invoke(inflater, container, false)
//        return binding.root

        _binding = getViewBinding()
        return binding.root
//        binding = DataBindingUtil.inflate.invoke(inflater, getLayoutRes(),container, false)
//        return binding.root


    }

    override fun onResume() {
        super.onResume()
        isLoaded = true
    }

    abstract fun getViewBinding(): VB

//    fun showError(message: String) {
//        showToast(message)
//
//    }
override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}


}