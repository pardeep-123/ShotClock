package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsTextlistBottomsheetBinding

class HeightBottomSheetAdapter(private var heightList: ArrayList<String>) :
    RecyclerView.Adapter<HeightBottomSheetAdapter.BottomHolder>() {

    var itemClickListener: ((pos: Int) -> Unit)? = null

    class BottomHolder(itemsView: ItemsTextlistBottomsheetBinding) :
        RecyclerView.ViewHolder(itemsView.root) {
        val itemBinding: ItemsTextlistBottomsheetBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsTextlistBottomsheetBinding.inflate(inflater, parent, false)
        return BottomHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomHolder, position: Int) {
        holder.itemBinding.tvHeightBottomSheet.text = heightList[position]
        holder.itemBinding.tvHeightBottomSheet.setOnClickListener {
            itemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return heightList.size
    }
}