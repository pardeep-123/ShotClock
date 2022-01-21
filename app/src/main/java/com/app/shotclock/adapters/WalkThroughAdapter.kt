package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsWalkthroughBinding
import com.app.shotclock.models.WalkThroughModel
import com.bumptech.glide.Glide

class WalkThroughAdapter(private var list : ArrayList<WalkThroughModel>) : RecyclerView.Adapter<WalkThroughAdapter.WalkHolder>() {

    class WalkHolder(itemView: ItemsWalkthroughBinding) : RecyclerView.ViewHolder(itemView.root) {
        val itemBinding: ItemsWalkthroughBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsWalkthroughBinding.inflate(inflater, parent, false)
        return WalkHolder(binding)
    }

    override fun onBindViewHolder(holder: WalkHolder, position: Int) {
      holder.itemBinding.rivUser.setImageResource(list[position].image)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}