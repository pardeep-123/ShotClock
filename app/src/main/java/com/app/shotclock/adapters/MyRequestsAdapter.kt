package com.app.shotclock.adapters

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsMyrequestsBinding
import com.app.shotclock.models.RequestListResponseModel
import com.bumptech.glide.Glide

class MyRequestsAdapter(private var ctx: Context,private var list: ArrayList<RequestListResponseModel.RequestListResponseBody>) : RecyclerView.Adapter<MyRequestsAdapter.MyRequestHolder>() {

    var onItemClickListener : ((pos: Int)->Unit)? = null

    class MyRequestHolder(itemsView: ItemsMyrequestsBinding) : RecyclerView.ViewHolder(itemsView.root) {
         val itemBinding: ItemsMyrequestsBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRequestHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsMyrequestsBinding.inflate(inflater, parent, false)
        return MyRequestHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRequestHolder, position: Int) {
        Glide.with(ctx).load(ApiConstants.IMAGE_URL + list[position].profileImage).into(holder.itemBinding.civUser)
          holder.itemBinding.tvUserName.text = list[position].username

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
//        val itemsRecyclerView

    }

    override fun getItemCount(): Int {
        return list.size
    }
}