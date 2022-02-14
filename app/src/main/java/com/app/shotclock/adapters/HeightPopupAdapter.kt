package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.app.shotclock.databinding.ItemsHeightPopupBinding

class HeightPopupAdapter(private val list: ArrayList<String>,private var textClick: TextClick) : Adapter<HeightPopupAdapter.HeightHolder>(){

     interface TextClick{
         fun clickText(pos:Int)
     }

    class HeightHolder(itemsView: ItemsHeightPopupBinding): RecyclerView.ViewHolder(itemsView.root){
        val itemsBinding: ItemsHeightPopupBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeightHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsHeightPopupBinding.inflate(inflater,parent,false)
        return HeightHolder(binding)
    }

    override fun onBindViewHolder(holder: HeightHolder, position: Int) {
        holder.itemsBinding.tvHeightList.text = list[position]

        holder.itemsBinding.tvHeightList.setOnClickListener {
            textClick.clickText(position)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}