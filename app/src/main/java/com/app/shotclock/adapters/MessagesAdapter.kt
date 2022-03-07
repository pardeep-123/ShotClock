package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsMessagesBinding
import com.app.shotclock.fragments.MessageFragment
import com.app.shotclock.models.sockets.GetChatListModel
import com.app.shotclock.utils.getChatListTime
import com.app.shotclock.utils.getNotificationTime
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class MessagesAdapter(
    private var ctx: Context,private var list: ArrayList<GetChatListModel.GetChatListModelItem>) :
    RecyclerView.Adapter<MessagesAdapter.MessageHolder>(),Filterable {

    var arrayList= ArrayList<GetChatListModel.GetChatListModelItem>()

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

        Glide.with(ctx).load(ApiConstants.SOCKET_URL + arrayList[position].userImage)
            .into(holder.itemBinding.civUser)
        holder.itemBinding.tvUserName.text = arrayList[position].userName
        holder.itemBinding.tvMessages.text = arrayList[position].lastMessage
        if (arrayList[position].unreadcount == 0)
            holder.itemBinding.tvMsgCount.isGone()
        else {
            holder.itemBinding.tvMsgCount.isVisible()
            holder.itemBinding.tvMsgCount.text = arrayList[position].unreadcount.toString()
        }

        if (arrayList[position].createdAt != null)
            holder.itemBinding.tvTime.text =
                getNotificationTime(getChatListTime(arrayList[position].createdAt))

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)

        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charSearch = constraint.toString()
                arrayList = if (charSearch.isEmpty()) {
                    ArrayList(list)
                }else{
                    val resultList = ArrayList<GetChatListModel.GetChatListModelItem>()
                    for (row in list){
                        if (row.userName.uppercase(Locale.US).contains(constraint.toString().uppercase(Locale.US))
                            || row.userName.lowercase(Locale.US).contains(constraint.toString().lowercase(
                                Locale.US))){
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResult = FilterResults()
                filterResult.values = arrayList
                return filterResult

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                arrayList = results?.values as ArrayList<GetChatListModel.GetChatListModelItem>
                notifyDataSetChanged()
                if (arrayList.size > 0)
                    MessageFragment.txtMessage.isGone()
                else
                    MessageFragment.txtMessage.isVisible()

            }
        }
    }
}