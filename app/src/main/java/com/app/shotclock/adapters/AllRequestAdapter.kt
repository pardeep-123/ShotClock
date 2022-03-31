package com.app.shotclock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsBaseBinding
import com.app.shotclock.models.AllRequestResponseModel

class AllRequestAdapter(
    private var ctx: Context,
    private var list: ArrayList<ArrayList<AllRequestResponseModel.AllRequestBody>>
) : RecyclerView.Adapter<AllRequestAdapter.RequestListHolder>() {

    class RequestListHolder(var itemsView: ItemsBaseBinding) :
        RecyclerView.ViewHolder(itemsView.root) {
        val itemBinding: ItemsBaseBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsBaseBinding.inflate(inflater, parent, false)
        return RequestListHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestListHolder, position: Int) {
        val adapter = AllSubRequestAdapter(ctx, list[position])
        holder.itemBinding.rvBase.adapter = adapter
    }

    override fun getItemCount(): Int {
      return list.size
    }
}