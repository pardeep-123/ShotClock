package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsMessagesBinding

class MessagesAdapter(private var clickMessage: ClickMessage) : RecyclerView.Adapter<MessagesAdapter.MessageHolder>(){

    interface ClickMessage {
        fun onClick()
    }

    class MessageHolder (itemsView : ItemsMessagesBinding) : RecyclerView.ViewHolder(itemsView.root){
         val itemBinding : ItemsMessagesBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsMessagesBinding.inflate(inflater,parent, false)
        return MessageHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {

             holder.itemView.setOnClickListener {
                 clickMessage.onClick()
             }
    }

    override fun getItemCount(): Int {
        return 5
    }

}