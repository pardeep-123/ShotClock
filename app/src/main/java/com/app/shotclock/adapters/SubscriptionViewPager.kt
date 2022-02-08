package com.app.shotclock.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.app.shotclock.R
import com.app.shotclock.models.SubscriptionModel


class SubscriptionViewPager(private val ctx: Context, private val list: ArrayList<SubscriptionModel>) :
    PagerAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val myLayout: View = LayoutInflater.from(ctx)
            .inflate(R.layout.items_subscription_viewpager, container, false)
//        container.setBackgroundColor(randomColor())
        val tvPrice: TextView = myLayout.findViewById(R.id.tvPrice)
        val tvDays: TextView = myLayout.findViewById(R.id.tvDay)
        val tvDescriptions: TextView = myLayout.findViewById(R.id.tvDescription)
        tvPrice.text = list[position].price
        tvDays.text = list[position].days
        tvDescriptions.text = list[position].text
        container.addView(myLayout, 0)
        return myLayout
    }

    override fun getPageWidth(position: Int): Float {
        return 0.5f
    }

//    var rnd: Random = Random()
//    private fun randomColor(): Int {
//        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
//    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}