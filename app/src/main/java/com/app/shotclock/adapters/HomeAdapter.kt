package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsHomesBinding
import com.app.shotclock.models.HomeResponseModel
import com.app.shotclock.utils.Constants
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.bumptech.glide.Glide

class HomeAdapter(
    private var ctx: Context, private var homeList: ArrayList<HomeResponseModel.HomeBody>
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(itemView: ItemsHomesBinding) : RecyclerView.ViewHolder(itemView.root) {
        val itemBinding: ItemsHomesBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsHomesBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        Glide.with(ctx).load(ApiConstants.IMAGE_URL + homeList[position].profileImage)
            .into(holder.itemBinding.rivUser)
        holder.itemBinding.tvUserName.text = homeList[position].username
        holder.itemBinding.tvDetails.text = homeList[position].bio
        holder.itemBinding.tvAddress.text = homeList[position].address

        if (homeList[position].isOnline == "1")
            holder.itemBinding.ivGreenDot.isVisible()
        else
            holder.itemBinding.ivGreenDot.isGone()

        if (Constants.isPlus)
            holder.itemBinding.ivTick.isVisible()
        else
            holder.itemBinding.ivTick.isGone()

        holder.itemBinding.ivTick.isChecked = homeList[position].isSelect

        holder.itemView.setOnClickListener {
            homeList[position].isSelect = !homeList[position].isSelect
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return homeList.size
    }

}