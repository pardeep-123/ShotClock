package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsMessagesBinding

class MessagesAdapter(private var ctx: Context) : RecyclerView.Adapter<MessagesAdapter.MessageHolder>(){

    var onItemClickListener : ((pos:Int)->Unit)? =null

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
                 onItemClickListener?.invoke(position)
             }
    }

    override fun getItemCount(): Int {
        return 5
    }

}