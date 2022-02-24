package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.databinding.ItemsHomesBinding
import com.app.shotclock.models.HomeResponseModel
import com.app.shotclock.utils.Constants
import com.app.shotclock.utils.isGone
import com.app.shotclock.utils.isVisible
import com.bumptech.glide.Glide

class HomeAdapter(
    private var ctx: Context,
    private var showTick: ShowTick,
    private var homeList: ArrayList<HomeResponseModel.HomeBody>
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    var selectedPosition = -1

    interface ShowTick {
        fun showClick(pos: Int, id: String)
    }

    class HomeViewHolder(itemView: ItemsHomesBinding) : RecyclerView.ViewHolder(itemView.root) {
        val itemBinding: ItemsHomesBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsHomesBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        Glide.with(ctx).load(ApiConstants.IMAGE_URL + homeList[position].profileImage)
            .into(holder.itemBinding.rivUser)
        holder.itemBinding.tvUserName.text = homeList[position].username
        val num = homeList[position].distance
        val sum = (String.format("%.2f", num))
        holder.itemBinding.tvAddress.text = "$sum miles away"
        holder.itemBinding.tvDetails.text = homeList[position].bio

        if (homeList[position].isOnline == "1")
            holder.itemBinding.ivGreenDot.isVisible()
        else
            holder.itemBinding.ivGreenDot.isGone()

        holder.itemView.setOnClickListener {
            selectedPosition = position
            if (selectedPosition == position) {
                holder.itemBinding.ivTick.isChecked=true
                showTick.showClick(position,homeList[position].id.toString())
                /*holder.itemBinding.ivTick.setOnCheckedChangeListener { _, _ ->
                    showTick.showClick(position,homeList[position].id.toString())
//                    Toast.makeText(cts,isChecked.toString(),Toast.LENGTH_SHORT).show()

                }*/

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
        return homeList.size
    }
}