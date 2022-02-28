package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsSpeedDateSessionBinding
import com.app.shotclock.models.RequestListResponseModel
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

        holder.itemBinding.tvStatus.text = list[position].isOnline

    }

    override fun getItemCount(): Int {
       return list.size
    }
}