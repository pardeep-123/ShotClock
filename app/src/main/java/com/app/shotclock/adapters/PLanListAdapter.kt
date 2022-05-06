package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.activities.PlanListActivity
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsHomesBinding
import com.app.shotclock.databinding.ItemsSelectUserBinding
import com.app.shotclock.databinding.RowPlanListBinding
import com.app.shotclock.models.HomeResponseModel
import com.app.shotclock.models.RequestListResponseModel
import com.app.shotclock.utils.Constants
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.bumptech.glide.Glide

class PLanListAdapter(private var ctx: Context, var list: ArrayList<PlanListActivity.Model>) : RecyclerView.Adapter<PLanListAdapter.HomeViewHolder>() {

    class HomeViewHolder(itemView: RowPlanListBinding) : RecyclerView.ViewHolder(itemView.root) {
        val itemBinding: RowPlanListBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowPlanListBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        holder.itemBinding.tvPrice.text=list[position].price
        holder.itemBinding.tvTitle.text=list[position].title
        holder.itemBinding.tvDescription.text=list[position].description


    }

    override fun getItemCount(): Int {
        return list.size
    }

}