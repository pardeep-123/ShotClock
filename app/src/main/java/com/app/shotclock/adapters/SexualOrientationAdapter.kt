package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsSexualorientationBinding

class SexualOrientationAdapter: RecyclerView.Adapter<SexualOrientationAdapter.OrientationHolder>() {

    class OrientationHolder(itemViews:ItemsSexualorientationBinding): RecyclerView.ViewHolder(itemViews.root){
        val binding : ItemsSexualorientationBinding = itemViews
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrientationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsSexualorientationBinding.inflate(inflater,parent,false)
        return OrientationHolder(binding)
    }

    override fun onBindViewHolder(holder: OrientationHolder, position: Int) {

    }

    override fun getItemCount(): Int {
       return 2
    }
}