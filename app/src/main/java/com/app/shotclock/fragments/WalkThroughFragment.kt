package com.app.shotclock.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import com.app.shotclock.R
import com.app.shotclock.adapters.WalkThroughAdapter
import com.app.shotclock.base.BaseFragment
import com.app.shotclock.databinding.FragmentWalkThroughBinding
import com.app.shotclock.models.WalkThroughModel
import info.jeovani.viewpagerindicator.constants.PagerItemType

class WalkThroughFragment : BaseFragment<FragmentWalkThroughBinding>() {
   private var walkList : ArrayList<WalkThroughModel>? = ArrayList()
    private var selectedArrayList = ArrayList<Int>()
    private var unSelectedArrayList = ArrayList<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
//        initViewPager()
    }

    private fun initViewPager() {
        selectedArrayList.add(Color.parseColor("#10C7DF"))
        unSelectedArrayList.add(Color.parseColor("#FFFFFF"))

        binding.viewPagerIndicator.itemsCount = 3
        binding.viewPagerIndicator.itemType = PagerItemType.OVAL
        binding.viewPagerIndicator.itemSelectedColors = selectedArrayList
        binding.viewPagerIndicator.itemsUnselectedColors = unSelectedArrayList
        binding.viewPagerIndicator.itemElevation = 1
        binding.viewPagerIndicator.itemWidth = 9
        binding.viewPagerIndicator.itemHeight = 9
        binding.viewPagerIndicator.itemMargin = 6
        binding.viewPagerIndicator.setBackgroundColor(Color.TRANSPARENT)

//        binding.rvWalkThrough.adapter = WalkThroughAdapter(walkList!!)
    }

    private fun setAdapter() {
//        searchList = arguments?.getSerializable("searchList") as ArrayList<SearchListResponse.Body.SearchUser>
//        selectedPos = arguments?.getInt("selectedPos")

        selectedArrayList.add(Color.parseColor("#10C7DF"))
        unSelectedArrayList.add(Color.parseColor("#FFFFFF"))

        binding.viewPagerIndicator.itemsCount = 3
        binding.viewPagerIndicator.itemType = PagerItemType.OVAL
        binding.viewPagerIndicator.itemSelectedColors = selectedArrayList
        binding.viewPagerIndicator.itemsUnselectedColors = unSelectedArrayList
        binding.viewPagerIndicator.itemElevation = 1
        binding.viewPagerIndicator.itemWidth = 9
        binding.viewPagerIndicator.itemHeight = 9
        binding.viewPagerIndicator.itemMargin = 6
        binding.viewPagerIndicator.setBackgroundColor(Color.TRANSPARENT)

        walkList?.add(WalkThroughModel(R.drawable.img_three))
        walkList?.add(WalkThroughModel(R.drawable.img_four))
        walkList?.add(WalkThroughModel(R.drawable.img_two))

        binding.rvWalkThrough.adapter = WalkThroughAdapter(walkList!!)
//        adapter.searchInterface = this

        binding.rvWalkThrough.set3DItem(true)
        binding.rvWalkThrough.setInfinite(true)
        binding.rvWalkThrough.setAlpha(true)
        binding.rvWalkThrough.setFlat(false)
        binding.rvWalkThrough.setIntervalRatio(0.6F)
//        binding.rvWalkThrough.scrollToPosition(selectedPos!!)
//        val carouselLayoutManager = binding.rvWalkThrough.getCarouselLayoutManager()
//        val currentlyCenterPosition = binding.rvWalkThrough.getSelectedPosition()

//        val pagerSnapHelper = PagerSnapHelper()
//
//
//        pagerSnapHelper.attachToRecyclerView(binding.rvWalkThrough)

// chooseCircleIndicator.attachToRecyclerView(carouselRecyclerview, pagerSnapHelper)

    }

    override fun getViewBinding(): FragmentWalkThroughBinding {
        return FragmentWalkThroughBinding.inflate(layoutInflater)
    }


}