package com.app.shotclock.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsCompleteProfileImagesBinding

class CompleteProfileImagesAdapter :
    RecyclerView.Adapter<CompleteProfileImagesAdapter.ImageHolder>() {

    class ImageHolder(itemView: ItemsCompleteProfileImagesBinding) : RecyclerView.ViewHolder(itemView.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsCompleteProfileImagesBinding.inflate(inflater, parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }
}