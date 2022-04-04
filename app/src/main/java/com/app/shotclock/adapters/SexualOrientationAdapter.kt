package com.app.shotclock.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.R
import com.app.shotclock.databinding.ItemsSexualorientationBinding
import com.app.shotclock.models.EduSexualOrientationModel

class SexualOrientationAdapter(
    private var ctx: Context,
    private var list: ArrayList<EduSexualOrientationModel>
) : RecyclerView.Adapter<SexualOrientationAdapter.OrientationHolder>() {
    var onItemClickListener: ((pos: String) -> Unit)? = null

    class OrientationHolder(itemViews: ItemsSexualorientationBinding) :
        RecyclerView.ViewHolder(itemViews.root) {
        val binding: ItemsSexualorientationBinding = itemViews
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrientationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsSexualorientationBinding.inflate(inflater, parent, false)
        return OrientationHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrientationHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.binding.tvSexualList.text = list[position].name

        if (list[position].isSelected!!) {
            holder.binding.tvSexualList.setTextColor(ContextCompat.getColor(ctx, R.color.black))
            holder.binding.tvSexualList.background =
                ContextCompat.getDrawable(ctx, R.drawable.bg_white_corners)
        } else {
            holder.binding.tvSexualList.setTextColor(ContextCompat.getColor(ctx, R.color.white))
            holder.binding.tvSexualList.background =
                ContextCompat.getDrawable(ctx, R.drawable.bg_grey)
        }

        holder.itemView.setOnClickListener {
            list[position].isSelected = !list[position].isSelected!!
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