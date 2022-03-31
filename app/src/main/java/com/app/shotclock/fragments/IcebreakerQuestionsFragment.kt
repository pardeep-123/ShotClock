package com.app.shotclock.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.shotclock.adapters.IcebreakerAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentIcebrakerQuestionsBinding
import com.app.shotclock.genericdatacontainer.Resource
import com.app.shotclock.genericdatacontainer.Status
import com.app.shotclock.models.IceBreakerQuestionResponse
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.viewmodels.HomeViewModel
import javax.inject.Inject


class IcebreakerQuestionsFragment : BaseFragment<FragmentIcebrakerQuestionsBinding>(),
    Observer<Resource<IceBreakerQuestionResponse>> {

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var iceBreakerAdapter: IcebreakerAdapter? = null
    private var iceBreakerList = ArrayList<IceBreakerQuestionResponse.IceBreakerBody>()

    override fun getViewBinding(): FragmentIcebrakerQuestionsBinding {
        return FragmentIcebrakerQuestionsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewModel()
        handleClicks()

        iceBreakerAdapter = IcebreakerAdapter(iceBreakerList)
        binding.rvIcebreaker.adapter = iceBreakerAdapter

    }

    private fun configureViewModel() {
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.iceBreakerQuestions().observe(viewLifecycleOwner, this)
    }

    private fun handleClicks() {
        binding.tb.ivBack.isVisible()
        binding.tb.ivAppLogo.isVisible()

        binding.tb.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    override fun onChanged(t: Resource<IceBreakerQuestionResponse>) {
        when (t.status) {
            Status.SUCCESS -> {
                binding.pb.clLoading.isGone()
                if (t.data?.body!!.size > 0) {
                    iceBreakerList.addAll(t.data.body)
                    iceBreakerAdapter?.notifyDataSetChanged()
                } else {
                    binding.tvNoResultFound.isVisible()
                }
            }
            Status.ERROR -> {
                binding.pb.clLoading.isGone()
                showError(t.message!!)
            }
            Status.LOADING -> {
                binding.pb.clLoading.isVisible()
            }
        }
    }

}