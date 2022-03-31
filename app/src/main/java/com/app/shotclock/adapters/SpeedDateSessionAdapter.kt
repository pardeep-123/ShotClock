package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.R
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsSpeedDateSessionBinding
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.bumptech.glide.Glide

class SpeedDateSessionAdapter(private var ctx: Context,private var list: ArrayList<RequestListResponseModel.RequestListResponseBody>) :RecyclerView.Adapter<SpeedDateSessionAdapter.SpeedDateHolder>(){

    class SpeedDateHolder(itemsView: ItemsSpeedDateSessionBinding) : RecyclerView.ViewHolder(itemsView.root) {
        val itemBinding: ItemsSpeedDateSessionBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeedDateHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsSpeedDateSessionBinding.inflate(inflater,parent,false)
        return SpeedDateHolder(binding)
    }

    override fun onBindViewHolder(holder: SpeedDateHolder, position: Int) {
      Glide.with(ctx).load(ApiConstants.IMAGE_URL + list[position].profileImage).into(holder.itemBinding.rivUser)
        holder.itemBinding.tvUserName.text = list[position].username
        holder.itemBinding.tvBio.text = list[position].bio

        when (list[position].status) {
            1 -> {
                holder.itemBinding.tvStatus.text = "Pending"
            }
            2 -> {
                holder.itemBinding.tvStatus.text = "Accepted"
                holder.itemBinding.tvStatus.setTextColor(ContextCompat.getColor(ctx,R.color.green))
            }
            else -> holder.itemBinding.tvStatus.text = "Cancelled"
        }

        if (list[position].isOnline == "1")
            holder.itemBinding.ivGreenDot.isVisible()
        else
            holder.itemBinding.ivGreenDot.isGone()

    }

    override fun getItemCount(): Int {
       return list.size
    }
}