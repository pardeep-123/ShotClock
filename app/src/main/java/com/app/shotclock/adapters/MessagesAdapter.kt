package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsMessagesBinding
import com.app.shotclock.models.sockets.GetChatListModel
import com.app.shotclock.utils.getChatListTime
import com.app.shotclock.utils.getNotificationTime
import com.bumptech.glide.Glide

class MessagesAdapter(
    private var ctx: Context,
    private var list: ArrayList<GetChatListModel.GetChatListModelItem>
) :
    RecyclerView.Adapter<MessagesAdapter.MessageHolder>() {

    var onItemClickListener: ((pos: Int) -> Unit)? = null

    class MessageHolder(itemsView: ItemsMessagesBinding) : RecyclerView.ViewHolder(itemsView.root) {
        val itemBinding: ItemsMessagesBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsMessagesBinding.inflate(inflater, parent, false)
        return MessageHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {

        Glide.with(ctx).load(ApiConstants.SOCKET_URL + list[position].userImage)
            .into(holder.itemBinding.civUser)
        holder.itemBinding.tvUserName.text = list[position].userName
        holder.itemBinding.tvMessages.text = list[position].lastMessage
        holder.itemBinding.tvMsgCount.text = list[position].unreadcount.toString()
        holder.itemBinding.tvTime.text =
            getNotificationTime(getChatListTime(list[position].createdAt))

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}