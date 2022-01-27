package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsChatBinding

class ChatAdapter :RecyclerView.Adapter<ChatAdapter.ChatHolder>(){

 class ChatHolder(itemsView : ItemsChatBinding) : RecyclerView.ViewHolder(itemsView.root){

 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsChatBinding.inflate(inflater,parent,false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 3
    }

}