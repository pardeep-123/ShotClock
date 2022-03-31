package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsMyrequestsBinding
import com.app.shotclock.models.AllRequestResponseModel
import com.bumptech.glide.Glide

class AllSubRequestAdapter(
    private var ctx: Context,
    private var list: ArrayList<AllRequestResponseModel.AllRequestBody>
) : RecyclerView.Adapter<AllSubRequestAdapter.RequestListHolder>() {

    class RequestListHolder(itemsView: ItemsMyrequestsBinding) :
        RecyclerView.ViewHolder(itemsView.root) {
        val itemBinding: ItemsMyrequestsBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsMyrequestsBinding.inflate(inflater, parent, false)
        return RequestListHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestListHolder, position: Int) {
        Glide.with(ctx).load(ApiConstants.IMAGE_URL + list[position].requestTo.profileImage)
            .into(holder.itemBinding.civUser)
        holder.itemBinding.tvUserName.text = list[position].requestTo.username

    }

    override fun getItemCount(): Int {
        return list.size
    }
}