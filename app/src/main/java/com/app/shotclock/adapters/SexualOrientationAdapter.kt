package com.app.shotclock.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.R
import com.app.shotclock.databinding.ItemsSexualorientationBinding

class SexualOrientationAdapter(private var ctx: Context,private var list : ArrayList<String>, private var from : String): RecyclerView.Adapter<SexualOrientationAdapter.OrientationHolder>() {
    private var selectedPosition = -1

   var onItemClickListener : ((pos: String)->Unit)?= null

    class OrientationHolder(itemViews:ItemsSexualorientationBinding): RecyclerView.ViewHolder(itemViews.root){
        val binding : ItemsSexualorientationBinding = itemViews
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrientationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsSexualorientationBinding.inflate(inflater,parent,false)
        return OrientationHolder(binding)
    }

    override fun onBindViewHolder(holder: OrientationHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.tvSexualList.text = list[position]

        if (selectedPosition == position){
//               selectedPosition = position

            holder.binding.tvSexualList.setTextColor(ContextCompat.getColor(ctx,R.color.black))
            holder.binding.tvSexualList.background = ContextCompat.getDrawable(ctx,R.drawable.bg_white_corners)
        }else{
            holder.binding.tvSexualList.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            holder.binding.tvSexualList.background = ContextCompat.getDrawable(ctx,R.drawable.bg_grey)
        }

        holder.itemView.setOnClickListener {
                selectedPosition = position
                onItemClickListener?.invoke(list[position])
                notifyDataSetChanged()

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
       return list.size
    }
}