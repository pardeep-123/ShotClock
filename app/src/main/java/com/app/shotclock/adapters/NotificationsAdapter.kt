package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsNotificationsBinding
import com.app.shotclock.models.GetNotificationResponse
import com.app.shotclock.utils.getNotificationTime
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.bumptech.glide.Glide

class NotificationsAdapter(private var ctx: Context,private var list: ArrayList<GetNotificationResponse.GetNotificationBody>) :RecyclerView.Adapter<NotificationsAdapter.NotificationHolder>(){

    class NotificationHolder (itemsView : ItemsNotificationsBinding) : RecyclerView.ViewHolder(itemsView.root){
      val itemBinding : ItemsNotificationsBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsNotificationsBinding.inflate(inflater,parent,false)
        return NotificationHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        Glide.with(ctx).load(ApiConstants.IMAGE_URL + list[position].request.requestBy.profileImage).into(holder.itemBinding.civUser)
     holder.itemBinding.tvMessage.text = list[position].data
        holder.itemBinding.tvTime.text = getNotificationTime(list[position].createdAt)

        if (list[position].type ==1){
            holder.itemBinding.tvAccept.isVisible()
            holder.itemBinding.tvDecline.isVisible()
        }else{
            holder.itemBinding.tvAccept.isGone()
            holder.itemBinding.tvDecline.isGone()
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}