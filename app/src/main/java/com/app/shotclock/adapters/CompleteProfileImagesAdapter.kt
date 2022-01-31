package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsCompleteProfileImagesBinding
import com.bumptech.glide.Glide

class CompleteProfileImagesAdapter(val imageList: ArrayList<String>) :
    RecyclerView.Adapter<CompleteProfileImagesAdapter.ImageHolder>() {


    var onItemCLickListener: ((pos: Int) -> Unit)? = null

    inner class ImageHolder(val binding: ItemsCompleteProfileImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pos: Int) {

            if (pos != imageList.size)
                Glide.with(binding.rivUser).load(imageList[pos]).into(binding.rivUser)

            itemView.setOnClickListener {
                onItemCLickListener?.invoke(pos)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsCompleteProfileImagesBinding.inflate(inflater, parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(position)

    }

    override fun getItemCount(): Int {
        return imageList.size + 1
    }
}