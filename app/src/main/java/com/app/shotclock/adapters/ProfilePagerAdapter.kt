package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.shotclock.R
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.models.ProfileImagesModel
import com.app.shotclock.models.ProfileUserImage
import com.app.shotclock.models.SubscriptionModel
import com.app.shotclock.utils.Constants
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class ProfilePagerAdapter(private val ctx: Context, private val list: ArrayList<ProfileUserImage>):PagerAdapter() {

    override fun getCount(): Int {
      return list.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val myLayout : View = LayoutInflater.from(ctx).inflate(R.layout.items_images_viewpager,container,false)

        val rivUser : RoundedImageView = myLayout.findViewById(R.id.rivUserViewPager)
        Glide.with(ctx).load(ApiConstants.IMAGE_URL + list[position].image_path).into(rivUser)
        container.addView(myLayout,0)
        return myLayout

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}