package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.shotclock.R
import com.app.shotclock.models.ProfileImagesModel
import com.app.shotclock.models.SubscriptionModel
import com.makeramen.roundedimageview.RoundedImageView

class ProfilePagerAdapter(private val ctx: Context, private val list: ArrayList<ProfileImagesModel>):PagerAdapter() {

    override fun getCount(): Int {
      return list.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val myLayout : View = LayoutInflater.from(ctx).inflate(R.layout.items_images_viewpager,container,false)

        val rivUser : RoundedImageView = myLayout.findViewById(R.id.rivUserViewPager)
        rivUser.setImageResource(list[position].images)
        container.addView(myLayout,0)
        return myLayout

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}