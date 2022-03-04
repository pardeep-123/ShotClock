package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.cache.getUser
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsChatBinding
import com.app.shotclock.models.sockets.ChatHistoryResponse
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.bumptech.glide.Glide

class ChatAdapter(private var ctx: Context,private var list: ArrayList<ChatHistoryResponse.ChatHistoryResponseItem>) : RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    class ChatHolder(itemsView: ItemsChatBinding) : RecyclerView.ViewHolder(itemsView.root) {
        val itemsBinding: ItemsChatBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsChatBinding.inflate(inflater, parent, false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {

        if (getUser(ctx)?.id.toString() == list[position].senderID.toString()){

            holder.itemsBinding.constraintLayoutReceiver.isGone()
            holder.itemsBinding.constraintLayoutSender.isVisible()
        Glide.with(ctx).load(ApiConstants.SOCKET_URL + list[position].senderImage).into(holder.itemsBinding.civSender)
         holder.itemsBinding.tvSender.text = list[position].message
            }else {
                holder.itemsBinding.constraintLayoutSender.isGone()
            holder.itemsBinding.constraintLayoutReceiver.isVisible()
            Glide.with(ctx).load(ApiConstants.SOCKET_URL + list[position].receiverImage)
                .into(holder.itemsBinding.civReceiver)
            holder.itemsBinding.tvReceiver.text = list[position].message
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}