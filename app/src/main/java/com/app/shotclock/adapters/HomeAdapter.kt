package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsHomesBinding
import com.app.shotclock.utils.Constants
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible

class HomeAdapter(private var showTick: ShowTick) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    var selectedPosition = -1
    interface ShowTick{
      fun showClick(pos :Int){}
    }
    class HomeViewHolder(itemView: ItemsHomesBinding) : RecyclerView.ViewHolder(itemView.root) {
        val itemBinding : ItemsHomesBinding = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsHomesBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            selectedPosition = position
            if (selectedPosition == position) {

                holder.itemBinding.ivTick.setOnCheckedChangeListener { _, _ ->
                    showTick.showClick(position)
//                    Toast.makeText(cts,isChecked.toString(),Toast.LENGTH_SHORT).show()

                }

//                if (!holder.itemBinding.ivTick.isVisible){
//                    holder.itemBinding.ivTick.isVisible()
//                }else{
//                    holder.itemBinding.ivTick.isGone()
//                }
            }
        }

        if (Constants.isPlus){
            holder.itemBinding.ivTick.isVisible()
        }else
            holder.itemBinding.ivTick.isGone()
    }

    override fun getItemCount(): Int {
        return 5
    }
}