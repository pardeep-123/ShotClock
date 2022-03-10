package com.app.shotclock.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.cache.getUser
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsChatBinding
import com.app.shotclock.models.sockets.ChatHistoryResponse
import com.app.shotclock.models.sockets.GetChatListModel
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.app.shotclock.utils.openImagePopUp
import com.bumptech.glide.Glide

class ChatAdapter(
    private var ctx: Context,
    private var list: ArrayList<ChatHistoryResponse.ChatHistoryResponseItem>
) : RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    class ChatHolder(itemsView: ItemsChatBinding) : RecyclerView.ViewHolder(itemsView.root) {
        val itemsBinding: ItemsChatBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsChatBinding.inflate(inflater, parent, false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {

            if (getUser(ctx)?.id == list[position].senderID) {
                holder.itemsBinding.constraintLayoutSender.isVisible()
                holder.itemsBinding.constraintLayoutReceiver.isGone()

                Glide.with(ctx).load(ApiConstants.SOCKET_URL + list[position].senderImage).into(holder.itemsBinding.civSender)

                if (list[position].messageType == 0) {
                    holder.itemsBinding.ivSendPic.isGone()
                    holder.itemsBinding.tvSender.isVisible()
                    holder.itemsBinding.tvSender.text = list[position].message

                } else {
                    holder.itemsBinding.tvSender.isGone()
                    holder.itemsBinding.ivSendPic.isVisible()
                    Glide.with(ctx).load(ApiConstants.SOCKET_URL + list[position].message)
                        .into(holder.itemsBinding.ivSendPic)

                }

                holder.itemsBinding.ivSendPic.setOnClickListener {
                    openImagePopUp(list[position].message, ctx)
                }

            } else {
                holder.itemsBinding.constraintLayoutSender.isGone()
                holder.itemsBinding.constraintLayoutReceiver.isVisible()
                Glide.with(ctx).load(ApiConstants.SOCKET_URL + list[position].senderImage).into(holder.itemsBinding.civReceiver)

                if (list[position].messageType == 0) {
                    holder.itemsBinding.ivReceivePic.isGone()
                    holder.itemsBinding.tvReceiver.isVisible()
                    holder.itemsBinding.tvReceiver.text = list[position].message
                } else {
                    holder.itemsBinding.tvReceiver.isGone()
                    holder.itemsBinding.ivReceivePic.isVisible()
                    Glide.with(ctx).load(ApiConstants.SOCKET_URL + list[position].message)
                        .into(holder.itemsBinding.ivReceivePic)
                }

                holder.itemsBinding.ivReceivePic.setOnClickListener {
                    openImagePopUp(list[position].message, ctx)
                }
            }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}