package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsCompleteProfileImagesBinding
import com.app.shotclock.models.EditProfileModel
import com.bumptech.glide.Glide

class EditProfileImagesAdapter(private var ctx: Context,private val imageList: ArrayList<EditProfileModel>) :
    RecyclerView.Adapter<EditProfileImagesAdapter.EditHolder>() {

    var onItemClickListener: ((pos: Int) -> Unit)? = null

    class EditHolder(itemsView: ItemsCompleteProfileImagesBinding) :
        RecyclerView.ViewHolder(itemsView.root) {
        val itemBinding: ItemsCompleteProfileImagesBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsCompleteProfileImagesBinding.inflate(inflater, parent, false)
        return EditHolder(binding)
    }

    override fun onBindViewHolder(holder: EditHolder, position: Int) {
        if (position != 0) {
            if(imageList[position-1].type==0){
                Glide.with(ctx).load(imageList[position - 1].image).into(holder.itemBinding.rivUser)

            }else{
                Glide.with(ctx).load(imageList[position - 1].image).into(holder.itemBinding.rivUser)

            }
        }
        holder.itemView.setOnClickListener {
            if (position == 0)
                onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size + 1
    }
}