package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsNotificationsBinding

class NotificationsAdapter :RecyclerView.Adapter<NotificationsAdapter.NotificationHolder>(){

    class NotificationHolder (itemsView : ItemsNotificationsBinding) : RecyclerView.ViewHolder(itemsView.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsNotificationsBinding.inflate(inflater,parent,false)
        return NotificationHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 3
    }
}